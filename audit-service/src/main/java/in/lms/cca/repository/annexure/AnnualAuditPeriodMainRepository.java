package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.AnnualAuditPeriodMain;

public interface AnnualAuditPeriodMainRepository extends JpaRepository<AnnualAuditPeriodMain, Long>{

	@Query("FROM AnnualAuditPeriodMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	AnnualAuditPeriodMain findByAnnexureId(Long annexureMainId);


	
}







