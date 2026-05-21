package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditCheck;

public interface IAuditCheckService {

	Optional<AuditCheck> addAuditCheck(AuditCheck auditCheck);
	Optional<AuditCheck> updateAuditCheck(AuditCheck auditCheck);
	AuditCheck getAuditCheckById(Long id);
	List<AuditCheck> getAllActiveAuditCheck();
	List<AuditCheck> getAllInActiveAuditCheck();
	List<AuditCheck> getAllAuditCheck();
	boolean deleteAuditCheckById(Long id);
	AuditCheck getAuditCheckByDesc(String desc);
	
}
