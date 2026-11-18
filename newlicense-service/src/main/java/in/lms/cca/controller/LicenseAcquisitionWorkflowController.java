package in.lms.cca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.LicenseAcquisitionWorkflowDTO;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.service.LicenseAcquisitionWorkflowService;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@CrossOrigin
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
public class LicenseAcquisitionWorkflowController {

	@Autowired
	private IIntentApplicationService intentApplicationService;

	@Autowired
	private LicenseAcquisitionWorkflowService licenseAcquisitionWorkflowService;

	@GetMapping("/get-license-acquisition-workflow")
	public ResponseEntity<?> getLicenseAcquisitionWorkflow(
			@RequestParam("intentAppId") String intentAppId,
			@RequestParam(value = "role", defaultValue = "APPLICANT") String role) {
		try {
			String decryptedId = intentAppId;
			if (EncryptionUtil.decrypt(intentAppId) != null) {
				decryptedId = EncryptionUtil.decrypt(intentAppId);
			}
			IntentApplication intentApplication =
					intentApplicationService.getIntentByIntentAppId(Long.valueOf(decryptedId));
			if (intentApplication == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
			}
			LicenseAcquisitionWorkflowDTO workflow =
					licenseAcquisitionWorkflowService.buildWorkflow(intentApplication, role);
			return ResponseEntity.ok(workflow);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unable to resolve license acquisition workflow.");
		}
	}
}
