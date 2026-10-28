package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import in.lms.cca.entity.logs.AuditorsLog;

public interface AuditorsRepositoryLogs extends JpaRepository<AuditorsLog, Long>{

	
}
