package in.lms.cca;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import in.lms.cca.util.golbal.PublicKeyCertificate;

class PublicKeyCertificateTest {

	@Test
	void acceptsBeginAndEndMarkersAndIgnoresHyphens() {
		assertTrue(PublicKeyCertificate.hasEnvelope("""
				-----BEGIN CERTIFICATE-----
				MIIB
				-----END CERTIFICATE-----
				"""));
		assertTrue(PublicKeyCertificate.hasEnvelope("""
				BEGIN CERTIFICATE
				MIIB
				END CERTIFICATE
				"""));
	}

	@Test
	void rejectsMissingMarkers() {
		assertFalse(PublicKeyCertificate.hasEnvelope("""
				-----BEGIN PUBLIC KEY-----
				MIIB
				-----END PUBLIC KEY-----
				"""));
		assertFalse(PublicKeyCertificate.hasEnvelope("-----BEGIN CERTIFICATE-----"));
		assertFalse(PublicKeyCertificate.hasEnvelope(null));
		assertThrows(IllegalArgumentException.class,
				() -> PublicKeyCertificate.requireEnvelope("not a certificate"));
	}
}
