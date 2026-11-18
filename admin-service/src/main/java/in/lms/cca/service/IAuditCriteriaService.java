package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditCriteria;

public interface IAuditCriteriaService {

	Optional<AuditCriteria> addAuditCriteria(AuditCriteria auditCriteria);
	Optional<AuditCriteria> updateAuditCriteria(AuditCriteria auditCriteria);
	boolean deleteByAuditCriteriaId(Long auditCriteriaId);
	AuditCriteria getByAuditCriteriaId(Long auditCriteriaId);
	List<AuditCriteria> getAllAuditCriteria();
	List<AuditCriteria> getAllActiveAuditCriteria();
	List<AuditCriteria> getAllInActiveAuditCriteria();
	AuditCriteria getAuditCriteriaByTitle(String title);
	
}
