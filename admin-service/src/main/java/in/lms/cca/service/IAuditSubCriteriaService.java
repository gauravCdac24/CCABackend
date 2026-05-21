package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditSubCriteria;

public interface IAuditSubCriteriaService {

	Optional<AuditSubCriteria> addAuditSubCriteria(AuditSubCriteria auditSubCriteriaObj);
	Optional<AuditSubCriteria> updateAuditSubCriteria(AuditSubCriteria auditSubCriteriaObj);
	boolean deleteByAuditSubCriteriaId(Long auditSubCriteriaId);
	boolean deleteByAuditCriteriaId(Long auditCriteriaId);
	AuditSubCriteria getByAuditSubCriteriaId(Long auditSubCriteriaId);
	List<AuditSubCriteria> getAllByAuditCriteriaId(Long auditCriteriaId);
	List<AuditSubCriteria> getAllActiveByAuditCriteriaId(Long auditCriteriaId);
	List<AuditSubCriteria> getAllInactiveByAuditCriteriaId(Long auditCriteriaId);
	List<AuditSubCriteria> getAllActiveAuditSubCriteria();
	List<AuditSubCriteria> getAllEnabledForAuditorView();
	List<AuditSubCriteria> getAllInActiveAuditSubCriteria();
	List<AuditSubCriteria> getAllAuditSubCriteria();
	AuditSubCriteria getAuditSubCriteriaByTitle(String title);

	
}
