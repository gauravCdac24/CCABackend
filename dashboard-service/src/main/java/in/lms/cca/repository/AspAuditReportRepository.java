package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AspAuditReport;

public interface AspAuditReportRepository extends JpaRepository<AspAuditReport, Long> {

	@Query("FROM AspAuditReport a WHERE a.aspId = :aspId ORDER BY a.auditReportDate ASC, a.aspAuditReportId ASC")
	List<AspAuditReport> findByAspIdOrderByAuditReportDate(@Param("aspId") Long aspId);
}
