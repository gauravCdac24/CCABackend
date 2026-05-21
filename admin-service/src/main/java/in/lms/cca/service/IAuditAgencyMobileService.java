package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditAgencyMobile;
import in.lms.cca.entity.Intent;

public interface IAuditAgencyMobileService {

	Optional<AuditAgencyMobile> addAuditAgencyMobile(AuditAgencyMobile auditAgencyMobile);
	Optional<AuditAgencyMobile> updateAuditAgencyMobile(AuditAgencyMobile auditAgencyMobile);
	boolean deleteMobileByAuditAgencyId(Long auditAgencyId);
	boolean deleteByAuditAgencyMobileId(Long auditAgencyMobileId);
	List<AuditAgencyMobile> getAllActiveAuditAgencyMobile();
	List<AuditAgencyMobile> getAllInActiveAuditAgencyMobile();
	AuditAgencyMobile getAuditAgencyMobileById(Long agencyMobileId);
	List<AuditAgencyMobile> getAllAuditAgencyMobileByAuditAgencyId(Long auditAgencyId);
	List<AuditAgencyMobile> getAllActiveAuditAgencyMobileByAuditAgencyId(Long auditAgencyId);
	List<AuditAgencyMobile> getAllInactiveAuditAgencyMobileByAuditAgencyId(Long auditAgencyId);
	List<AuditAgencyMobile> getAllAuditAgencyMobile();
	List<AuditAgencyMobile> findByAuditAgencyId(Long auditAgencyId);

	
	
}
