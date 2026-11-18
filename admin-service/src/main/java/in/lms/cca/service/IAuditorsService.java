package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.Auditors;

public interface IAuditorsService {

	Optional<Auditors> addAuditAuditors(Auditors auditorObj);
	Optional<Auditors> updateAuditors(Auditors auditorObj);
	List<Auditors> getAllAuditors();
	Auditors getAuditorById(Long id);
	List<Auditors> getAllActiveAuditors();
	List<Auditors> getAllInActiveAuditors();
	List<Auditors> findByAuditAgencyId(Long auditAgencyId);
}
