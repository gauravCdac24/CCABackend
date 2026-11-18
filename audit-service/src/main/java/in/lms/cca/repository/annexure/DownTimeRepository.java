
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.DownTime;

public interface DownTimeRepository extends JpaRepository<DownTime, Long>{


	@Query("FROM DownTime a WHERE a.annexureMainId.annexureMainId = :id")
	DownTime findByAnnexureId(Long id);
	
	@Query("FROM DownTime a WHERE a.downTimeId = :id")
	DownTime findByDownTimeId(Long id);
	
	
}