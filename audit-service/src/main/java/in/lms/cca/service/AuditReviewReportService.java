package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditReviewReportEntity;

public interface AuditReviewReportService {

	Optional<AuditReviewReportEntity> saveData(AuditReviewReportEntity applicantShortcomingsReportEntity);

	List<AuditReviewReportEntity> findByAuditsId(Long shortcomingId);

	Optional<AuditReviewReportEntity> downloadfileBydDocumentName(String fileName);


}
