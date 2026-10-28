
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CAServicesDetails;

public interface CAServicesDetailsRepository extends JpaRepository<CAServicesDetails, Long>{


	@Query("FROM CAServicesDetails a WHERE a.caServicesDetailsId = :id")
	CAServicesDetails getByCAServicesDetailsId(Long id);

	@Query("FROM CAServicesDetails a WHERE a.caServicesMainId.caServicesMainId = :caServicesMainId ORDER BY caServicesDetailsId")
	List<CAServicesDetails> getByCAServicesMainId(Long caServicesMainId);
	
	
}



