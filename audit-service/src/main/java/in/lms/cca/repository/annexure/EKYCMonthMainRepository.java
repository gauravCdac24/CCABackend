
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.EKYCMonthMain;

public interface EKYCMonthMainRepository extends JpaRepository<EKYCMonthMain, Long>{

	@Query("FROM EKYCMonthMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	EKYCMonthMain findByAnnexureId(Long annexureMainId);


	
}




