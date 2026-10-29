package in.lms.cca.certificate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LicenseCertificatePdfGeneratorTest {

	@Test
	void rendersFilledCertificate() throws Exception {
		LicenseCertificateContent content = new LicenseCertificateContent();
		content.setAuthorityName("Sample Certifying Authority Pvt. Ltd.");
		content.setLocation("Block 12, Connaught Place, New Delhi, 110001");
		content.setIssueDate(Date.from(LocalDate.of(2026, 6, 17).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		content.setPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE7kSamplePublicKeyValueForLayoutCheck0123456789");

		byte[] pdf = new LicenseCertificatePdfGenerator().render(content);
		assertTrue(pdf.length > 1000);
		assertTrue(pdf[0] == '%' && pdf[1] == 'P' && pdf[2] == 'D' && pdf[3] == 'F');

		Path sample = Path.of("target", "cca-license-sample.pdf");
		Files.createDirectories(sample.getParent());
		Files.write(sample, pdf);
	}
}
