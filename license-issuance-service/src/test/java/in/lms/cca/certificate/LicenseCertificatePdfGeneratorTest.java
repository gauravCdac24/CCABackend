package in.lms.cca.certificate;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LicenseCertificatePdfGeneratorTest {

	@Test
	void rendersFilledCertificate() throws Exception {
		LicenseCertificateContent content = new LicenseCertificateContent();
		content.setAuthorityName("Gaurav Singh");
		content.setLocation("Delhi, Delhi, Delhi, Delhi, Jasola, Delhi, 110063, India");
		content.setIssueDate(Date.from(LocalDate.of(2026, 10, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		content.setPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE7kSamplePublicKeyValueForLayoutCheck0123456789");

		LicenseCertificatePdfGenerator generator = new LicenseCertificatePdfGenerator();
		byte[] pdf = generator.render(content);
		assertTrue(pdf.length > 1000);
		assertTrue(pdf[0] == '%' && pdf[1] == 'P' && pdf[2] == 'D' && pdf[3] == 'F');

		Path sample = Path.of("target", "cca-license-sample.pdf");
		Files.createDirectories(sample.getParent());
		Files.write(sample, pdf);

		try (InputStream form = getClass().getResourceAsStream("/certificate/form-blank.png")) {
			BufferedImage image = ImageIO.read(form);
			assertEquals(1654, image.getWidth());
			assertEquals(2339, image.getHeight());
			generator.paintHindiOn(image, content.getAuthorityName(), content.getLocation());
			Path preview = Path.of("target", "hindi-paragraph-preview.png");
			ImageIO.write(image.getSubimage(120, 720, 1450, 280), "png", preview.toFile());
		}
	}
}
