package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import in.lms.cca.entity.logs.MinimumAttemptLog;

public interface MinimumAttemptsRepositoryLogs extends JpaRepository<MinimumAttemptLog, Long>{


	
}
