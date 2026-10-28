
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.LocationMain;

public interface LocationMainRepository extends JpaRepository<LocationMain, Long>{

	@Query("FROM LocationMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	LocationMain findByAnnexureId(Long annexureMainId);
	
}








