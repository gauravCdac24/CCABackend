package in.lms.cca.service;

import in.lms.cca.dto.AnnualAuditWorkflowDTO;
import in.lms.cca.entity.AnnualAuditScheduleEntity;

public interface AnnualAuditWorkflowService {

	AnnualAuditWorkflowDTO buildWorkflow(AnnualAuditScheduleEntity schedule, String role);

	Long resolveActiveScheduleId(String userName);

	String resolveCcaUndertakingReviewStatus(Long auditScheduleId);
}
