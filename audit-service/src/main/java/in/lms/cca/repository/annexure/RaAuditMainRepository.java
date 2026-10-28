
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.RaAuditMain;

public interface RaAuditMainRepository extends JpaRepository<RaAuditMain, Long>{

	@Query("FROM RaAuditMain a WHERE a.annexureMainId.annexureMainId = :id")
	RaAuditMain findByAnnexureId(Long id);
	
	@Query("FROM RaAuditMain a WHERE a.raAuditMainId = :id")
	RaAuditMain findByRaAuditMainId(Long id);
	
}








