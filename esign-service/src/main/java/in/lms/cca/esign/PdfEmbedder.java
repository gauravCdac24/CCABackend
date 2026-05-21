package in.lms.cca.esign;

import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.signatures.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.File;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PdfEmbedder {

    public String pdfSigner(String src, String dest, String signatureValue, String certificateString) throws Exception {
        // Clean and decode Base64-encoded signature value
        signatureValue = signatureValue.trim().replaceAll("[^A-Za-z0-9+/=]", "");
        byte[] decodedSignatureValue = Base64.getDecoder().decode(signatureValue);

        // Clean and decode Base64-encoded certificate
        certificateString = certificateString.trim()
                .replaceAll("-----BEGIN CERTIFICATE-----|-----END CERTIFICATE-----", "")
                .replaceAll("\\s+", "");
        byte[] decodedCertificate = Base64.getDecoder().decode(certificateString);

        // Add security provider
        Security.addProvider(new BouncyCastleProvider());

        // Load certificate
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream certificateInputStream = new ByteArrayInputStream(decodedCertificate);
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);

        // Initialize variables
        Calendar signingDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss XXX");
        String formattedDate = sdf.format(signingDate.getTime());
        String signerName = getSignerName(certificate);

        // Read initial PDF
        PdfReader reader = new PdfReader(src);
        PdfDocument pdfDocument = new PdfDocument(reader);
        int numberOfPages = pdfDocument.getNumberOfPages();
        pdfDocument.close();
        reader.close();

        if (numberOfPages < 1) {
            throw new IllegalStateException("PDF has no pages");
        }

        // External signature
        IExternalSignature externalSignature = new IExternalSignature() {
            @Override
            public byte[] sign(byte[] messageHash) throws GeneralSecurityException {
                return decodedSignatureValue;
            }

            @Override
            public String getDigestAlgorithmName() {
                return DigestAlgorithms.SHA256;
            }

            @Override
            public String getSignatureAlgorithmName() {
                return "RSA";
            }

            @Override
            public ISignatureMechanismParams getSignatureMechanismParameters() {
                return null;
            }
        };

        // Incremental signing: Process one page at a time
        String currentSrc = src;
        String tempDest;
        for (int i = 1; i <= numberOfPages; i++) {
            // Create temporary output file for this iteration
            tempDest = i == numberOfPages ? dest : "temp_" + i + ".pdf";

            // Set up PDF reader and writer
            PdfReader tempReader = new PdfReader(currentSrc);
            PdfWriter tempWriter = new PdfWriter(tempDest);
            StampingProperties stampingProperties = new StampingProperties().useAppendMode();
            PdfSigner signer = new PdfSigner(tempReader, tempWriter, stampingProperties);

            PdfDocument tempDoc = signer.getDocument();
            PdfPage page = tempDoc.getPage(i);
            Rectangle pageSize = page.getPageSize();

            // Set up signature field position
            float sigWidth = 200;
            float sigHeight = 50;
            float xPos = pageSize.getWidth() - (sigWidth + 20);
            float yPos = 10;
            Rectangle rect = new Rectangle(xPos, yPos, sigWidth, sigHeight);

            // Create signature appearance
            SignatureFieldAppearance appearance = new SignatureFieldAppearance("sig_" + i);
            appearance.setContent("Digitally signed by " + signerName + "\nDate: " + formattedDate);
            appearance.setFontSize(8);
            signer.setSignatureAppearance(appearance);
            signer.setFieldName("sig_" + i);

            // Set signer properties for this page
            signer.setPageRect(rect)
                  .setPageNumber(i) // Dynamic: sign current page
                  .setSignDate(signingDate);

            // Sign this page
            signer.signDetached(
                    new BouncyCastleDigest(),
                    externalSignature,
                    new X509Certificate[]{certificate},
                    null, null, null,
                    32768,//original 8192 kb
                    PdfSigner.CryptoStandard.CMS
            );

            // Close resources
            tempDoc.close();
            tempReader.close();
            tempWriter.close();

            // Update source for next iteration
            if (i < numberOfPages) {
                currentSrc = tempDest;
            }
        }

        // Clean up temporary files
        for (int i = 1; i < numberOfPages; i++) {
            new File("temp_" + i + ".pdf").delete();
        }

        return "Document signed successfully with certificate on all pages.";
    }

    
    
    public static String findSignerName(String certificateString) throws CertificateException {
    	
    	if (certificateString == null || certificateString.trim().isEmpty()) {
            throw new IllegalArgumentException("The provided certificate string is empty or null. Check the eSign XML payload parsing.");
        }
    	// Clean and decode Base64-encoded certificate
        certificateString = certificateString.trim()
                .replaceAll("-----BEGIN CERTIFICATE-----|-----END CERTIFICATE-----", "")
                .replaceAll("\\s+", "");
        byte[] decodedCertificate = Base64.getDecoder().decode(certificateString);

        // Add security provider
        

        // Load certificate
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream certificateInputStream = new ByteArrayInputStream(decodedCertificate);
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certificateInputStream);

    	
    	
    	
    	
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