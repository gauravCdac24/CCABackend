package in.lms.cca.util.golbal;

/**
 * Public key text must be wrapped as a certificate. Hyphens in the marker lines are ignored,
 * so {@code -----BEGIN CERTIFICATE-----} and {@code BEGIN CERTIFICATE} are the same marker.
 */
public final class PublicKeyCertificate {

	public static final String INVALID_ENVELOPE = "The first line of the public key must be -----BEGIN CERTIFICATE----- and the last line must be -----END CERTIFICATE-----.";

	private PublicKeyCertificate() {
	}

	public static void requireEnvelope(String publicKey) {
		if (!hasEnvelope(publicKey)) {
			throw new IllegalArgumentException(INVALID_ENVELOPE);
		}
	}

	public static boolean hasEnvelope(String publicKey) {
		if (publicKey == null) {
			return false;
		}
		String first = null;
		String last = null;
		int contentLines = 0;
		for (String line : publicKey.split("\\R", -1)) {
			String marker = line.replace("-", "").trim();
			if (marker.isEmpty()) {
				continue;
			}
			if (first == null) {
				first = marker;
			}
			last = marker;
			contentLines++;
		}
		return contentLines >= 2
				&& "BEGIN CERTIFICATE".equals(first)
				&& "END CERTIFICATE".equals(last);
	}
}
