package in.lms.cca.util.global;

public final class ApplicationStatusUtil {

	public static final String PENDING = "pending";
	public static final String SUBMITTED = "Submitted";
	public static final String FEE_PAYMENT = "Fee Payment";
	public static final String PAYMENT_PROOF_RECEIVED = "PaymentProof Recieved";
	public static final String EDIT_UPON_REVIEW = "Edit_Upon_Review";
	public static final String EDIT_UPON_REVIEW_SPACED = "Edit Upon Review";
	public static final String UNDER_REVIEW = "underReview";
	public static final String APPROVED_FROM_REVIEW = "Approved From Review";
	public static final String RECOMMENDED_REJECT_FOR_REVIEW = "Recommended Reject For Review";
	public static final String RECOMANDED_FOR_AUDIT = "RECOMANDED_FOR_AUDIT";
	public static final String AGENCY_SELECTION = "Agency Selection";
	public static final String RECOMMENDED_FOR_AUDIT_AGENCY = "Recomanded For Audit Agency";
	public static final String APPROVE_FROM_CCA = "Approve From CCA";
	public static final String RECOMMENDED_IN_PRINCIPLE = "RECOMMENDED_IN_PRINCIPLE_APPROVAL";
	public static final String IN_PRINCIPLE_GRANTED = "IN_PRINCIPLE_APPROVAL_GRANTED";
	public static final String DOCUMENT_SUBMITTED = "DOCUMENT_SUBMITTED_FOR_LICENSE";
	public static final String BANK_GUARANTEE_UPLOADED = "BANK_GUARANTEE_PROOF_UPLOADED";
	public static final String LICENSE_DETAILS_PENDING_CCA = "LICENSE_DETAILS_PENDING_CCA_REVIEW";
	public static final String LICENSE_DETAILS_SENT_TO_ADMIN = "LICENSE_DETAILS_SENT_TO_ADMIN";
	public static final String LICENSE_ISSUED = "LICENSE_ISSUED";
	public static final String APPLICATION_REJECTED = "Application Rejected";
	public static final String SEND_TO_REJECTION = "Send To Rejection";
	public static final String NC_ISSUED = "NC Issued By Auditor";
	public static final String NC_ACTION_TAKEN = "NC Action taken By Applicant";
	public static final String REVIEW_NC_REPORT = "Review NC Report";
	public static final String REVIEW_AUDIT_REPORT = "Review Audit Report";

	private ApplicationStatusUtil() {
	}

	public static String normalize(String status) {
		if (status == null) {
			return "";
		}
		return status.replaceAll("\\s+", "").toLowerCase();
	}

	public static boolean isPaymentProofReceived(String status) {
		return normalize(status).equals(normalize(PAYMENT_PROOF_RECEIVED));
	}

	public static boolean isEditUponReview(String status) {
		String normalized = normalize(status);
		return normalized.equals(normalize(EDIT_UPON_REVIEW))
				|| normalized.equals(normalize(EDIT_UPON_REVIEW_SPACED));
	}

	public static boolean isPending(String status) {
		return normalize(status).equals(normalize(PENDING));
	}

	public static boolean isSubmitted(String status) {
		return normalize(status).equals(normalize(SUBMITTED));
	}

	public static boolean isFeePayment(String status) {
		return normalize(status).equals(normalize(FEE_PAYMENT));
	}

	public static boolean isRejected(String status) {
		if (status == null) {
			return false;
		}
		String normalized = normalize(status);
		return normalized.contains("reject")
				|| normalized.equals(normalize(SEND_TO_REJECTION));
	}

	public static boolean isLicenseIssued(String status) {
		return normalize(status).equals(normalize(LICENSE_ISSUED));
	}
}
