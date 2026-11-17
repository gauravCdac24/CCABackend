package in.lms.cca.esign;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import in.lms.cca.util.golbal.RealPath;

public class PdfEmbedder {

    /** Prefix for each signature field. A second sign on the same PDF needs a new name. */
    private static final String FIELD_PREFIX = "CDAC_ESIGN_SIG";
    private static final float SIG_WIDTH = 200f;
    private static final float SIG_HEIGHT = 50f;
    private static final float SIG_GAP = 6f;

    private static final class SignatureSlot {
        final String fieldName;
        final int existingSignatures;

        SignatureSlot(String fieldName, int existingSignatures) {
            this.fieldName = fieldName;
            this.existingSignatures = existingSignatures;
        }
    }

    /**
     * Two-pass approach, pass 1:
     * Prepares the PDF with a blank signature placeholder and returns the SHA-256
     * hex digest of the PDF byte range.
     *
     * This digest MUST be sent to CDAC as the InputHash so that the PKCS7 blob
     * CDAC returns is cryptographically tied to the exact bytes that iText will
     * later verify — fixing the "signature invalid / document modified" error.
     *
     * @param src        path to the uploaded (original) PDF
     * @param dest       path to write the prepared PDF (may be the same as src)
     * @param signerName display name shown in the signature appearance
     * @return lowercase hex SHA-256 of the PDF byte range
     */
    public static String prepareForSigning(String src, String dest, String signerName) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        ByteArrayOutputStream preparedStream = new ByteArrayOutputStream();

        // Each signature on one PDF must use a new field. Reusing CDAC_ESIGN_SIG
        // throws "Field has been already signed" on the second auditor.
        SignatureSlot slot = nextSignatureSlot(src);

        // setUnethicalReading(true) allows iText to open PDFs with corrupted tag
        // structures or non-standard internal layouts without throwing an exception.
        PdfReader reader = new PdfReader(src);
        reader.setUnethicalReading(true);

        StampingProperties props = new StampingProperties().useAppendMode();
        PdfSigner signer = new PdfSigner(reader, preparedStream, props);

        // Stack each new signature above the previous one, bottom-right of page 1.
        PdfDocument tmpDoc = signer.getDocument();
        PdfPage page = tmpDoc.getPage(1);
        Rectangle pageSize = page.getPageSize();
        Rectangle sigRect = signatureRectangle(pageSize, slot.existingSignatures);

        SignatureFieldAppearance appearance = new SignatureFieldAppearance(slot.fieldName);
        appearance.setContent("Digitally signed by " + (signerName != null ? signerName : ""));
        appearance.setFontSize(8);

        signer.setSignatureAppearance(appearance);
        signer.setFieldName(slot.fieldName);
        signer.setPageNumber(1);
        signer.setPageRect(sigRect);
        signer.setSignDate(Calendar.getInstance());

        // Reserve placeholder — this sets /ByteRange and fills /Contents with zeroes
        ExternalBlankSignatureContainer blank = new ExternalBlankSignatureContainer(
                PdfName.Adobe_PPKLite, PdfName.Adbe_pkcs7_detached);
        signer.signExternalContainer(blank, 16384);

        byte[] preparedBytes = preparedStream.toByteArray();

        // Persist the prepared PDF to disk
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(preparedBytes);
        }

        // Extract /ByteRange written by iText.
        // PdfAcroForm.getAcroForm is the correct iText 8 API (PdfFormCreator does not exist).
        PdfReader preparedReader = new PdfReader(new ByteArrayInputStream(preparedBytes));
        preparedReader.setUnethicalReading(true);
        PdfDocument preparedDoc = new PdfDocument(preparedReader);
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(preparedDoc, false);
        PdfFormField sigField = acroForm == null ? null : acroForm.getField(slot.fieldName);
        if (sigField == null) {
            preparedDoc.close();
            throw new IllegalStateException("Signature field '" + slot.fieldName + "' not found after preparation");
        }
        PdfDictionary valueDict = sigField.getPdfObject().getAsDictionary(PdfName.V);
        if (valueDict == null) {
            preparedDoc.close();
            throw new IllegalStateException("Signature value dictionary not found");
        }
        PdfArray byteRange = valueDict.getAsArray(PdfName.ByteRange);
        if (byteRange == null) {
            preparedDoc.close();
            throw new IllegalStateException("ByteRange not found in signature dictionary");
        }
        int start1 = byteRange.getAsNumber(0).intValue();
        int len1   = byteRange.getAsNumber(1).intValue();
        int start2 = byteRange.getAsNumber(2).intValue();
        int len2   = byteRange.getAsNumber(3).intValue();
        preparedDoc.close();

        // Hash exactly the bytes that a PDF validator will hash during verification
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(preparedBytes, start1, len1);
        md.update(preparedBytes, start2, len2);
        byte[] hashBytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /**
     * Two-pass approach, pass 2:
     * Injects the CDAC PKCS7 blob into the pre-prepared PDF's signature placeholder.
     *
     * Uses PdfSigner.signDeferred so that iText simply writes the PKCS7 bytes into
     * the reserved /Contents field without touching any other bytes in the PDF —
     * keeping the byte range intact and the signature valid.
     *
     * @param src            path to the prepared PDF (output of prepareForSigning)
     * @param dest           path for the final signed PDF
     * @param signatureValue Base64-encoded PKCS7 blob from CDAC DocSignature
     * @param certificateString (unused in signing, retained for API compatibility)
     */
    public String pdfSigner(String src, String dest, String signatureValue, String certificateString) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        signatureValue = signatureValue.trim().replaceAll("[^A-Za-z0-9+/=]", "");
        final byte[] pkcs7Bytes = Base64.getDecoder().decode(signatureValue);

        PdfReader reader = new PdfReader(src);
        reader.setUnethicalReading(true);
        PdfDocument preparedDoc = new PdfDocument(reader);
        String fieldName = findPendingSignatureField(preparedDoc);

        RealPath.ensureParentDirectory(dest);
        try (OutputStream out = new FileOutputStream(dest)) {
            PdfSigner.signDeferred(preparedDoc, fieldName, out,
                    new IExternalSignatureContainer() {
                        @Override
                        public byte[] sign(InputStream data) throws GeneralSecurityException {
                            // Return the PKCS7 blob CDAC signed over SHA-256(byte_range).
                            // Because prepareForSigning sent exactly that byte-range hash
                            // to CDAC, this blob is cryptographically valid for this PDF.
                            return pkcs7Bytes;
                        }

                        @Override
                        public void modifySigningDictionary(PdfDictionary signDic) {
                            // SubFilter was already set to Adbe_pkcs7_detached during
                            // the prepare phase — nothing to change here.
                        }
                    });
        }
        preparedDoc.close();

        return "Document signed successfully.";
    }

    public static String findSignerName(String certificateString) throws CertificateException {
        certificateString = certificateString.trim()
                .replaceAll("-----BEGIN CERTIFICATE-----|-----END CERTIFICATE-----", "")
                .replaceAll("\\s+", "");
        byte[] decodedCertificate = Base64.getDecoder().decode(certificateString);

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory
                .generateCertificate(new ByteArrayInputStream(decodedCertificate));

        X500Principal principal = certificate.getSubjectX500Principal();
        String dn = principal.getName();
        for (String part : dn.split(",")) {
            if (part.trim().startsWith("CN=")) {
                return part.trim().substring(3);
            }
        }
        return null;
    }

    public static String getSignerName(X509Certificate certificate) {
        X500Principal principal = certificate.getSubjectX500Principal();
        String dn = principal.getName();
        for (String part : dn.split(",")) {
            if (part.trim().startsWith("CN=")) {
                return part.trim().substring(3);
            }
        }
        return "Unknown";
    }

    /**
     * Next unused signature field, plus how many signature fields already exist
     * so the new appearance can be placed above them.
     */
    private static SignatureSlot nextSignatureSlot(String src) throws IOException {
        PdfReader reader = new PdfReader(src);
        reader.setUnethicalReading(true);
        try (PdfDocument doc = new PdfDocument(reader)) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(doc, false);
            int existing = 0;
            if (form != null) {
                for (PdfFormField field : form.getAllFormFields().values()) {
                    if (isSignatureField(field)) {
                        existing++;
                    }
                }
            }
            String fieldName = FIELD_PREFIX;
            int suffix = 2;
            while (form != null && form.getField(fieldName) != null) {
                fieldName = FIELD_PREFIX + "_" + suffix;
                suffix++;
            }
            return new SignatureSlot(fieldName, existing);
        }
    }

    /** Placeholder written by prepareForSigning: /Contents is still all zeroes. */
    private static String findPendingSignatureField(PdfDocument doc) {
        PdfAcroForm form = PdfAcroForm.getAcroForm(doc, false);
        if (form == null) {
            throw new IllegalStateException("No signature field found to complete");
        }
        String pending = null;
        for (Map.Entry<String, PdfFormField> entry : form.getAllFormFields().entrySet()) {
            if (isBlankSignature(entry.getValue())) {
                pending = entry.getKey();
            }
        }
        if (pending == null) {
            throw new IllegalStateException("No unsigned signature field found");
        }
        return pending;
    }

    private static boolean isSignatureField(PdfFormField field) {
        return field != null && PdfName.Sig.equals(field.getFormType());
    }

    private static boolean isBlankSignature(PdfFormField field) {
        if (!isSignatureField(field)) {
            return false;
        }
        PdfDictionary valueDict = field.getPdfObject().getAsDictionary(PdfName.V);
        if (valueDict == null) {
            return false;
        }
        PdfString contents = valueDict.getAsString(PdfName.Contents);
        if (contents == null) {
            return false;
        }
        byte[] bytes = contents.getValueBytes();
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        for (byte b : bytes) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    private static Rectangle signatureRectangle(Rectangle pageSize, int existingSignatures) {
        float margin = 20f;
        float baseY = 10f;
        float rowHeight = SIG_HEIGHT + SIG_GAP;
        float maxY = pageSize.getHeight() - SIG_HEIGHT - margin;
        int perColumn = Math.max(1, (int) ((maxY - baseY) / rowHeight) + 1);
        int column = existingSignatures / perColumn;
        int row = existingSignatures % perColumn;
        float xPos = pageSize.getWidth() - (SIG_WIDTH + margin) - column * (SIG_WIDTH + SIG_GAP);
        if (xPos < margin) {
            xPos = margin;
        }
        float yPos = baseY + row * rowHeight;
        return new Rectangle(xPos, yPos, SIG_WIDTH, SIG_HEIGHT);
    }
}
