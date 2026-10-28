package in.lms.cca.service;

import java.util.List;
import java.util.Optional;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.LicenseeAuditEntity;

public interface AuditReportDocumentService {

	Optional<AuditReportDocumentEntity> saveData(AuditReportDocumentEntity auditReportDocumentEntity);

	AuditReportDocumentEntity getByAuditId(LicenseeAuditEntity applicationAuditEntity);

	Optional<AuditReportDocumentEntity> updateData(AuditReportDocumentEntity documentEntity);

	List<AuditReportDocumentEntity> findByAuditId(Long appAuditId);

	AuditReportDocumentEntity findByAuditsId(Long appAuditId);

}
