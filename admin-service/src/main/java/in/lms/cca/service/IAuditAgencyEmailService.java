package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditAgencyEmail;

public interface IAuditAgencyEmailService {

	Optional<AuditAgencyEmail> addAuditAgencyEmail(AuditAgencyEmail auditAgencyEmail);
	Optional<AuditAgencyEmail> updateAuditAgencyEmail(AuditAgencyEmail auditAgencyEmail);
	boolean deleteEmailByAuditAgencyId(Long auditAgencyId);
	boolean deleteByAuditAgencyEmailId(Long auditAgencyEmailId);
	List<AuditAgencyEmail> getAllActiveAuditAgencyEmail();
	List<AuditAgencyEmail> getAllInActiveAuditAgencyEmail();
	AuditAgencyEmail getAuditAgencyEmailById(Long agencyEmailId);
	List<AuditAgencyEmail> getAllAuditAgencyEmailByAuditAgencyId(Long auditAgencyId);
	List<AuditAgencyEmail> getAllActiveAuditAgencyEmailByAuditAgencyId(Long auditAgencyId);
	List<AuditAgencyEmail> getAllInactiveAuditAgencyEmailByAuditAgencyId(Long auditAgencyId);
	List<AuditAgencyEmail> getAllAuditAgencyEmail();
	List<AuditAgencyEmail> findByAuditAgencyId(Long auditAgencyId);
	
}
