package in.lms.cca.service;

import in.lms.cca.dto.LicenseAcquisitionWorkflowDTO;
import in.lms.cca.entity.IntentApplication;

public interface LicenseAcquisitionWorkflowService {

	LicenseAcquisitionWorkflowDTO buildWorkflow(IntentApplication intentApplication, String role);

	boolean isPaymentVerificationCompleted(Long intentAppId);

	boolean isPaymentVerificationPending(Long intentAppId);
}
