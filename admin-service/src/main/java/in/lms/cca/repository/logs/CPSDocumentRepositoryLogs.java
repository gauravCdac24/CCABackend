package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import in.lms.cca.entity.logs.CPSDocumentLog;

public interface CPSDocumentRepositoryLogs extends JpaRepository<CPSDocumentLog, Long>{

	
}
