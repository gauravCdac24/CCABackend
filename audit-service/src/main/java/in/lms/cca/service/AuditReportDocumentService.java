package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditReportDocumentEntity;

public interface AuditReportDocumentService {

	Optional<AuditReportDocumentEntity> saveData(AuditReportDocumentEntity auditReportDocumentEntity);

	AuditReportDocumentEntity getByAuditId(ApplicationAuditEntity applicationAuditEntity);

	AuditReportDocumentEntity getActiveByAppAuditId(Long appAuditId);

	ApplicationAuditEntity resolveAuditReference(ApplicationAuditEntity applicationAuditEntity);

	int linkOrphanDocumentsToApplicant(Long appAuditId, List<Long> criteriaDocIds);

	List<AuditReportDocumentEntity> findAllWithNullAppAuditId();

	Optional<AuditReportDocumentEntity> updateData(AuditReportDocumentEntity documentEntity);

	List<AuditReportDocumentEntity> findByAuditId(Long appAuditId);

	AuditReportDocumentEntity findByAuditsId(Long appAuditId);

}
