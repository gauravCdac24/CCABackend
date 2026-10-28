package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import in.lms.cca.entity.logs.AuditCheckLogs;

public interface AuditCheckRepositoryLogs extends JpaRepository<AuditCheckLogs, Long>{

		
		
}
