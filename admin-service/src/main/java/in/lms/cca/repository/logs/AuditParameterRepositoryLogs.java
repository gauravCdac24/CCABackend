package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import in.lms.cca.entity.logs.AuditParameterLogs;

public interface AuditParameterRepositoryLogs extends JpaRepository<AuditParameterLogs, Long>{

	

	
}
