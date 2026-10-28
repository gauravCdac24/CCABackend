
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.SelfAssessmentMain;

public interface SelfAssessmentMainRepository extends JpaRepository<SelfAssessmentMain, Long>{

	@Query("FROM SelfAssessmentMain a WHERE a.annexureMainId.annexureMainId = :id")
	SelfAssessmentMain findByAnnexureId(Long id);
	
	@Query("FROM SelfAssessmentMain a WHERE a.selfAssMainId = :id")
	SelfAssessmentMain findBySelfAssessmentMainId(Long id);
	
}







