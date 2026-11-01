package in.lms.cca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.service.AuditControlSelectionService;
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.global.AnnualAuditContext;

@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class AuditControlSelectionController {

	@Autowired
	private AuditControlSelectionService auditControlSelectionService;

	@PostMapping("/save-audit-control")
	public ResponseEntity<?> saveAuditControls(@RequestBody List<AuditRequestDTO> auditRequests) {
		try {
			String message = auditControlSelectionService.saveControls(auditRequests);
			return ResponseEntity.ok(message);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unable to update audit controls. Please try again.");
		}
	}

	@GetMapping("/audit-controls")
	public ResponseEntity<?> getAllAuditControls(@RequestParam String userName,
			@RequestParam(value = "auditScheduleId", required = false) Long auditScheduleId) {
		try {
			List<AuditRequestDTO> auditRequestDTOs = auditControlSelectionService.getActiveControlDtos(userName,
					auditScheduleId != null ? auditScheduleId : AnnualAuditContext.getAuditScheduleId());
			if (auditRequestDTOs.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(auditRequestDTOs);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Unable to fetch audit controls. Please try again.");
		}
	}

	@GetMapping("/audit-controls/{userName}")
	public ResponseEntity<List<AuditRequestDTO>> getAllAuditControlsByPath(@PathVariable String userName,
			@RequestParam(value = "auditScheduleId", required = false) Long auditScheduleId) {
		try {
			List<AuditRequestDTO> auditRequestDTOs = auditControlSelectionService.getActiveControlDtos(userName,
					auditScheduleId != null ? auditScheduleId : AnnualAuditContext.getAuditScheduleId());
			return ResponseEntity.ok(auditRequestDTOs);
		} catch (Exception ex) {
			return ResponseEntity.ok(List.of());
		}
	}
}
