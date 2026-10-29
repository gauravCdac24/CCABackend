package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.AspAuditReport;

public interface AspAuditReportRepository extends JpaRepository<AspAuditReport, Long> {
}
