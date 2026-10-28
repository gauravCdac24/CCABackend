package in.lms.cca.service;



import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditAgency;

public interface IAuditAgencyService {

	Optional<AuditAgency> addAuditAgency(AuditAgency auditAgency);
	Optional<AuditAgency> updateAddress(AuditAgency auditAgency);
	AuditAgency getAuditAgencyById(Long id);
	List<AuditAgency> getAllAuditAgency(Long id);
	List<AuditAgency> getAllActiveAuditAgency(Long id);
	List<AuditAgency> getAllInactiveAuditAgency(Long id);
	boolean deleteAuditAgencyById(Long id);
	List<AuditAgency> getAllAuditAgency();
	AuditAgency getAuditAgencyByCreatedBy(String createdBy);
	AuditAgency getAuditAgencyByAgencyName(String agencyName);

	
	
}
