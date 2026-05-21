
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CAServicesMain;


public interface CAServicesMainRepository extends JpaRepository<CAServicesMain, Long>{

	@Query("FROM CAServicesMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	CAServicesMain findByAnnexureId(Long annexureMainId);
	
}


