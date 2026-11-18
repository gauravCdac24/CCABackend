
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.InternalAuditMain;

public interface InternalAuditMainRepository extends JpaRepository<InternalAuditMain, Long>{

	@Query("FROM InternalAuditMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	InternalAuditMain getByAnnexureId(Long annexureMainId);


	
}








