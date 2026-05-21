package in.lms.cca.util.global;

public final class ApplicationStatusUtil {

	public static final String PAYMENT_PROOF_RECEIVED = "PaymentProof Recieved";

	private ApplicationStatusUtil() {
	}

	public static boolean isPaymentProofReceived(String status) {
		if (status == null) {
			return false;
		}
		String normalized = status.replaceAll("\\s+", "").toLowerCase();
		return normalized.equals("paymentproofrecieved");
	}
}
