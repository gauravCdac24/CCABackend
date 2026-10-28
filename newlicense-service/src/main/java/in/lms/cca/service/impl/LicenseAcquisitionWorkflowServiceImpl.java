package in.lms.cca.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.LicenseAcquisitionWorkflowDTO;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.PaymentVerification;
import in.lms.cca.repository.PaymentVarificationRepository;
import in.lms.cca.service.LicenseAcquisitionWorkflowService;
import in.lms.cca.util.global.ApplicationStatusUtil;

@Service
public class LicenseAcquisitionWorkflowServiceImpl implements LicenseAcquisitionWorkflowService {

	public static final String STEP_APPLICATION_FORM = "APPLICATION_FORM";
	public static final String STEP_UNDERTAKING_SUBMITTED = "UNDERTAKING_SUBMITTED";
	public static final String STEP_FEE_PAYMENT = "FEE_PAYMENT";
	public static final String STEP_PAYMENT_PROOF = "PAYMENT_PROOF";
	public static final String STEP_PAYMENT_VERIFICATION = "PAYMENT_VERIFICATION";
	public static final String STEP_COMMITTEE_REVIEW = "COMMITTEE_REVIEW";
	public static final String STEP_CCA_REVIEW = "CCA_REVIEW";
	public static final String STEP_AUDIT_PHASE = "AUDIT_PHASE";
	public static final String STEP_IN_PRINCIPLE = "IN_PRINCIPLE";
	public static final String STEP_LICENSE_DOCUMENTS = "LICENSE_DOCUMENTS";
	public static final String STEP_BANK_GUARANTEE = "BANK_GUARANTEE";
	public static final String STEP_LICENSE_ISSUED = "LICENSE_ISSUED";
	public static final String STEP_REJECTED = "REJECTED";
	public static final String STEP_COMPLETED = "COMPLETED";

	@Autowired
	private PaymentVarificationRepository paymentVerificationRepository;

	@Override
	public LicenseAcquisitionWorkflowDTO buildWorkflow(IntentApplication intentApplication, String role) {
		LicenseAcquisitionWorkflowDTO workflow = new LicenseAcquisitionWorkflowDTO();
		if (intentApplication == null || intentApplication.getIntentAppId() == null) {
			workflow.setAllowedActions(List.of());
			workflow.setReadOnly(true);
			return workflow;
		}

		workflow.setIntentAppId(String.valueOf(intentApplication.getIntentAppId()));
		workflow.setApplicationStatus(intentApplication.getApplicationStatus());
		String currentStep = resolveCurrentStep(intentApplication);
		workflow.setCurrentStep(currentStep);
		workflow.setAllowedActions(resolveAllowedActions(role, intentApplication, currentStep));
		workflow.setReadOnly(workflow.getAllowedActions().isEmpty()
				|| (workflow.getAllowedActions().size() == 1
						&& workflow.getAllowedActions().contains("VIEW")));
		return workflow;
	}

	@Override
	public boolean isPaymentVerificationCompleted(Long intentAppId) {
		if (intentAppId == null) {
			return false;
		}
		PaymentVerification verification =
				paymentVerificationRepository.getChangedThePaymentStatus(intentAppId);
		return verification != null;
	}

	@Override
	public boolean isPaymentVerificationPending(Long intentAppId) {
		return !isPaymentVerificationCompleted(intentAppId);
	}

	private String resolveCurrentStep(IntentApplication intentApplication) {
		String status = intentApplication.getApplicationStatus();
		Long intentAppId = intentApplication.getIntentAppId();

		if (ApplicationStatusUtil.isLicenseIssued(status)) {
			return STEP_LICENSE_ISSUED;
		}
		if (ApplicationStatusUtil.isRejected(status)) {
			return STEP_REJECTED;
		}
		if (ApplicationStatusUtil.normalize(status)
				.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.BANK_GUARANTEE_UPLOADED))) {
			return STEP_BANK_GUARANTEE;
		}
		if (ApplicationStatusUtil.normalize(status)
				.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.DOCUMENT_SUBMITTED))) {
			return STEP_LICENSE_DOCUMENTS;
		}
		if (ApplicationStatusUtil.normalize(status)
				.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.IN_PRINCIPLE_GRANTED))) {
			return STEP_IN_PRINCIPLE;
		}
		if (ApplicationStatusUtil.normalize(status)
				.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMMENDED_IN_PRINCIPLE))) {
			return STEP_IN_PRINCIPLE;
		}
		if (isAuditPhaseStatus(status)) {
			return STEP_AUDIT_PHASE;
		}
		if (isCcaReviewStatus(status)) {
			return STEP_CCA_REVIEW;
		}
		if (ApplicationStatusUtil.isEditUponReview(status)
				|| ApplicationStatusUtil.normalize(status)
						.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.UNDER_REVIEW))) {
			return STEP_COMMITTEE_REVIEW;
		}
		if (ApplicationStatusUtil.isPaymentProofReceived(status)) {
			return isPaymentVerificationCompleted(intentAppId)
					? STEP_COMMITTEE_REVIEW
					: STEP_PAYMENT_VERIFICATION;
		}
		if (ApplicationStatusUtil.isFeePayment(status)) {
			return STEP_PAYMENT_PROOF;
		}
		if (ApplicationStatusUtil.isSubmitted(status)) {
			return STEP_FEE_PAYMENT;
		}
		if (ApplicationStatusUtil.isPending(status)) {
			return STEP_APPLICATION_FORM;
		}
		return STEP_COMPLETED;
	}

	private List<String> resolveAllowedActions(String role, IntentApplication intentApplication,
			String currentStep) {
		String normalizedRole = role == null ? "APPLICANT" : role.toUpperCase().replace('-', '_');
		List<String> actions = new ArrayList<>();

		switch (normalizedRole) {
		case "APPLICANT":
		case "LICENSEE":
			addApplicantActions(actions, intentApplication, currentStep);
			break;
		case "CCA_OFFICER":
		case "CCAOFFICER":
			addCcaOfficerActions(actions, intentApplication, currentStep);
			break;
		case "CCA":
			addCcaActions(actions, intentApplication, currentStep);
			break;
		case "REVIEW_COMMITTEE":
		case "REVIEWCOMMITTEE":
			addReviewCommitteeActions(actions, intentApplication, currentStep);
			break;
		case "AUDIT_AGENCY":
		case "AUDITAGENCY":
			addAuditAgencyActions(actions, intentApplication, currentStep);
			break;
		default:
			addApplicantActions(actions, intentApplication, currentStep);
			break;
		}

		if (actions.isEmpty() && !STEP_REJECTED.equals(currentStep)) {
			actions.add("VIEW");
		}
		return actions;
	}

	private void addApplicantActions(List<String> actions, IntentApplication intentApplication,
			String currentStep) {
		switch (currentStep) {
		case STEP_APPLICATION_FORM:
			actions.add("EDIT_FORM");
			actions.add("TRACKER");
			break;
		case STEP_FEE_PAYMENT:
			actions.add("PAYMENT");
			break;
		case STEP_PAYMENT_PROOF:
			actions.add("UPLOAD_PAYMENT_PROOF");
			break;
		case STEP_COMMITTEE_REVIEW:
			if (ApplicationStatusUtil.isEditUponReview(intentApplication.getApplicationStatus())) {
				actions.add("EDIT_FORM");
			}
			actions.add("VIEW");
			actions.add("TRACKER");
			actions.add("DOWNLOAD");
			break;
		case STEP_IN_PRINCIPLE:
			if (ApplicationStatusUtil.normalize(intentApplication.getApplicationStatus())
					.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.IN_PRINCIPLE_GRANTED))) {
				actions.add("UPLOAD_LICENSE_DOCUMENTS");
			}
			actions.add("VIEW");
			actions.add("TRACKER");
			actions.add("DOWNLOAD");
			break;
		case STEP_AUDIT_PHASE:
			addAuditPhaseApplicantActions(actions, intentApplication.getApplicationStatus());
			actions.add("VIEW");
			actions.add("TRACKER");
			actions.add("DOWNLOAD");
			break;
		default:
			if (!STEP_PAYMENT_VERIFICATION.equals(currentStep)) {
				actions.add("VIEW");
				actions.add("TRACKER");
				actions.add("DOWNLOAD");
			}
			break;
		}
	}

	private void addAuditPhaseApplicantActions(List<String> actions, String status) {
		if (ApplicationStatusUtil.normalize(status)
				.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMANDED_FOR_AUDIT))) {
			actions.add("SELECT_AUDIT_AGENCY");
		}
		if (ApplicationStatusUtil.normalize(status)
				.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.NC_ISSUED))) {
			actions.add("NC_ACTION");
		}
	}

	private void addCcaOfficerActions(List<String> actions, IntentApplication intentApplication,
			String currentStep) {
		if (STEP_PAYMENT_VERIFICATION.equals(currentStep)
				&& isPaymentVerificationPending(intentApplication.getIntentAppId())) {
			actions.add("VERIFY_PAYMENT");
			return;
		}
		if (STEP_LICENSE_DOCUMENTS.equals(currentStep)) {
			actions.add("VERIFY_BANK_GUARANTEE");
			return;
		}
		if (STEP_BANK_GUARANTEE.equals(currentStep)) {
			actions.add("VIEW");
			return;
		}
		if (STEP_COMMITTEE_REVIEW.equals(currentStep)
				|| STEP_PAYMENT_VERIFICATION.equals(currentStep)
				|| STEP_LICENSE_DOCUMENTS.equals(currentStep)) {
			actions.add("VIEW");
		}
	}

	private void addCcaActions(List<String> actions, IntentApplication intentApplication,
			String currentStep) {
		if (STEP_CCA_REVIEW.equals(currentStep)) {
			actions.add("REVIEW_APPLICATION");
			return;
		}
		if (STEP_IN_PRINCIPLE.equals(currentStep)
				&& ApplicationStatusUtil.normalize(intentApplication.getApplicationStatus())
						.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMMENDED_IN_PRINCIPLE))) {
			actions.add("GRANT_IN_PRINCIPLE");
			return;
		}
		if (STEP_BANK_GUARANTEE.equals(currentStep)) {
			actions.add("ISSUE_LICENSE");
			return;
		}
		if (STEP_AUDIT_PHASE.equals(currentStep)
				&& ApplicationStatusUtil.normalize(intentApplication.getApplicationStatus())
						.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMMENDED_FOR_AUDIT_AGENCY))) {
			actions.add("REVIEW_AUDIT_AGENCY");
			return;
		}
		actions.add("VIEW");
	}

	private void addReviewCommitteeActions(List<String> actions, IntentApplication intentApplication,
			String currentStep) {
		if (STEP_COMMITTEE_REVIEW.equals(currentStep)) {
			actions.add("REVIEW_APPLICATION");
			return;
		}
		if (STEP_AUDIT_PHASE.equals(currentStep)
				&& ApplicationStatusUtil.normalize(intentApplication.getApplicationStatus())
						.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.REVIEW_NC_REPORT))) {
			actions.add("REVIEW_NC");
			return;
		}
		actions.add("VIEW");
	}

	private void addAuditAgencyActions(List<String> actions, IntentApplication intentApplication,
			String currentStep) {
		if (!STEP_AUDIT_PHASE.equals(currentStep)) {
			return;
		}
		String status = ApplicationStatusUtil.normalize(intentApplication.getApplicationStatus());
		if (status.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMMENDED_FOR_AUDIT_AGENCY))
				|| status.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.AGENCY_SELECTION))) {
			actions.add("SUBMIT_UNDERTAKING");
		} else if (status.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.REVIEW_AUDIT_REPORT))) {
			actions.add("SUBMIT_AUDIT_REPORT");
		} else if (status.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.NC_ACTION_TAKEN))) {
			actions.add("VERIFY_NC");
		} else {
			actions.add("VIEW");
		}
	}

	private boolean isAuditPhaseStatus(String status) {
		String normalized = ApplicationStatusUtil.normalize(status);
		return normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMANDED_FOR_AUDIT))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.AGENCY_SELECTION))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMMENDED_FOR_AUDIT_AGENCY))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.APPROVE_FROM_CCA))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.REVIEW_AUDIT_REPORT))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.NC_ISSUED))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.NC_ACTION_TAKEN))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.REVIEW_NC_REPORT));
	}

	private boolean isCcaReviewStatus(String status) {
		String normalized = ApplicationStatusUtil.normalize(status);
		return normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.APPROVED_FROM_REVIEW))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.RECOMMENDED_REJECT_FOR_REVIEW))
				|| normalized.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.SEND_TO_REJECTION));
	}
}
