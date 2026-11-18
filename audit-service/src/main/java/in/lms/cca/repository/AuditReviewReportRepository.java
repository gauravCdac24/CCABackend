package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AuditReviewReportEntity;

public interface AuditReviewReportRepository extends JpaRepository<AuditReviewReportEntity, Long> {
	
	@Query("SELECT d FROM AuditReviewReportEntity d WHERE d.shortcomingId.shortcomingId = :shortcomingId")
	List<AuditReviewReportEntity> findByAuditsId(Long shortcomingId);

	@Query("SELECT d FROM AuditReviewReportEntity d WHERE d.auditorReviewReport = :fileName")
	Optional<AuditReviewReportEntity> downloadfileBydDocumentName(String fileName);


}
