package in.lms.cca.service;

import java.util.List;

import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.AuditControlEntity;

public interface AuditControlSelectionService {

	String saveControls(List<AuditRequestDTO> requests);

	List<AuditControlEntity> getActiveControls(String userName, Long auditScheduleId);

	List<AuditRequestDTO> getActiveControlDtos(String userName, Long auditScheduleId);
}
