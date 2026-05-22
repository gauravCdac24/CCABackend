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

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PdfEmbedder {

    private static final String FIELD_NAME = "CDAC_ESIGN_SIG";

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

        // setUnethicalReading(true) allows iText to open PDFs with corrupted tag
        // structures or non-standard internal layouts without throwing an exception.
        PdfReader reader = new PdfReader(src);
        reader.setUnethicalReading(true);

        StampingProperties props = new StampingProperties().useAppendMode();
        PdfSigner signer = new PdfSigner(reader, preparedStream, props);

        // Position signature box bottom-right of page 1
        PdfDocument tmpDoc = signer.getDocument();
        PdfPage page = tmpDoc.getPage(1);
        Rectangle pageSize = page.getPageSize();
        float sigWidth = 200, sigHeight = 50;
        float xPos = pageSize.getWidth() - (sigWidth + 20);
        float yPos = 10;

        SignatureFieldAppearance appearance = new SignatureFieldAppearance(FIELD_NAME);
        appearance.setContent("Digitally signed by " + (signerName != null ? signerName : ""));
        appearance.setFontSize(8);

        signer.setSignatureAppearance(appearance);
        signer.setFieldName(FIELD_NAME);
        signer.setPageNumber(1);
        signer.setPageRect(new Rectangle(xPos, yPos, sigWidth, sigHeight));
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
        PdfFormField sigField = acroForm.getField(FIELD_NAME);
        if (sigField == null) {
            preparedDoc.close();
            throw new IllegalStateException("Signature field '" + FIELD_NAME + "' not found after preparation");
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

        try (OutputStream out = new FileOutputStream(dest)) {
            PdfSigner.signDeferred(preparedDoc, FIELD_NAME, out,
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
}
