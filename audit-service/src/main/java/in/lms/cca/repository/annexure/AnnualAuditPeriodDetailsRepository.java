package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.AnnualAuditPeriodDetails;

public interface AnnualAuditPeriodDetailsRepository extends JpaRepository<AnnualAuditPeriodDetails, Long>{

	@Query("FROM AnnualAuditPeriodDetails a WHERE a.periodDetailsId = :id")
	AnnualAuditPeriodDetails getByAnnualAuditPeriodId(Long id);

	@Query("FROM AnnualAuditPeriodDetails a WHERE a.periodMainId.periodMainId = :periodMainId ORDER BY periodDetailsId")
	List<AnnualAuditPeriodDetails> getByAnnualAuditPeriodMainId(Long periodMainId);


	
}
