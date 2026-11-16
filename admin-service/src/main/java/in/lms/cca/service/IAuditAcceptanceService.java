package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditAcceptance;

public interface IAuditAcceptanceService {

	Optional<AuditAcceptance> addDocument(AuditAcceptance document);
	Optional<AuditAcceptance> updateDocument(AuditAcceptance document);
	List<AuditAcceptance> getAllDocuments();
	List<AuditAcceptance> getAllActiveDocuments();
	List<AuditAcceptance> getAllInactiveDocuments();
	AuditAcceptance getById(Long auditAcceptanceId);
	boolean deleteById(Long auditAcceptanceId);
	Optional<AuditAcceptance> downloadFile(long auditAcceptanceId);
}
