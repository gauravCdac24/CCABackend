
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.LocationDetails;

public interface LocationDetailsRepository extends JpaRepository<LocationDetails, Long>{

	@Query("FROM LocationDetails a WHERE a.locationDetailsId = :id")
	LocationDetails findByLocationDetailsId(Long id);

	@Query("FROM LocationDetails a WHERE a.locationMainId.locationMainId = :id ORDER BY locationDetailsId")
	List<LocationDetails> findByLocationMainId(Long id);
	
}






