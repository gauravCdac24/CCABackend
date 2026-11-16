package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.logs.AuditAcceptanceLog;

public interface AuditAcceptanceRepositoryLogs extends JpaRepository<AuditAcceptanceLog, Long> {

}
