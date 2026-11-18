package in.lms.cca.service;

import in.lms.cca.dto.LicenseFormDefaultsDTO;

public interface ILicenseReviewService {

	LicenseFormDefaultsDTO getFormDefaults(String intentAppId, String applicantUsername);

	void submitForReview(String intentAppId, String applicantUsername, String submittedBy, String serialNo,
			String issueDate, String expiryDate, String publicKey);

	void approve(Long reviewId, String remark, String reviewedBy);

	void sendToAdmin(Long reviewId, String remark, String reviewedBy);
}
