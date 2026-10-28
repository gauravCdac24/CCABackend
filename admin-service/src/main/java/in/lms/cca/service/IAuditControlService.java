package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.AuditReportCriteriaDTO;
import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.AuditControl;

public interface IAuditControlService {

	Optional<AuditControl> addAuditControl(AuditControl auditControl);
	Optional<AuditControl> updateAuditControl(AuditControl auditControl);
	
	boolean deleteByAuditControlId(Long auditControlId);
	AuditControl getByAuditControlId(Long auditControlId);
	
	//Find All
	List<AuditControl> getAllAuditControl();
	List<AuditControl> getAllActiveAuditControl();
	List<AuditControl> getAllInActiveAuditControl();

	List<AuditControl> getAllAuditControlByParameterId(Long auditParameterId);
	List<AuditControl> getAllActiveAuditControlByParameterId(Long auditParameterId);
	List<AuditControl> getAllInActiveAuditControlByParameterId(Long auditParameterId);
	
	List<AuditControl> getAllAuditControlByCheckId(Long auditCheckId);
	List<AuditControl> getAllActiveAuditControlByCheckId(Long auditCheckId);
	List<AuditControl> getAllInActiveAuditControlByCheckId(Long auditCheckId);
	
	List<AuditControl> getAllAuditControlByControlType(Long auditControlTypeId);
	List<AuditControl> getAllActiveAuditControlByControlType(Long auditControlTypeId);
	List<AuditControl> getAllInActiveAuditControlByControlType(Long auditControlTypeId);
	AuditControl getAuditControlByDesc(String desc);
	List<AuditRequestDTO> getAllAuditRequestByUserName(String userName);
	List<AuditReportCriteriaDTO> getAllNCAuditReportCriteria(String userName);
}
