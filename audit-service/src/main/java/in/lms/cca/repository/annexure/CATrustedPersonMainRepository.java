
package in.lms.cca.repository.annexure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CATrustedPersonMain;

public interface CATrustedPersonMainRepository extends JpaRepository<CATrustedPersonMain, Long>{

	
	
	
	@Query("FROM CATrustedPersonMain a WHERE a.annexureMainId.annexureMainId = :id")
	CATrustedPersonMain findByAnnexureId(Long id);
	
	
	
}












