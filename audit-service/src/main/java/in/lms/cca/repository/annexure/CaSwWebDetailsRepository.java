
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CaSwWebDetails;

public interface CaSwWebDetailsRepository extends JpaRepository<CaSwWebDetails, Long>{

	@Query("FROM CaSwWebDetails a WHERE a.caWebDetailsId = :id")
	CaSwWebDetails getByCaSwWebDetailsId(Long id);

	@Query("FROM CaSwWebDetails a WHERE a.caWebMainId.caWebMainId = :caWebMainId ORDER BY caWebDetailsId")
	List<CaSwWebDetails> getByCaSwWebMainId(Long caWebMainId);
	
}
