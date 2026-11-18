package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationTimeLine;

public interface ApplicationTimeLineRepository extends JpaRepository<ApplicationTimeLine, Long>{
	
	@Query("FROM ApplicationTimeLine WHERE intentAppId.intentAppId = :intentAppId ORDER BY created DESC")
	List<ApplicationTimeLine> findByIntentApplicationId(@Param("intentAppId")Long intentAppId);

}
