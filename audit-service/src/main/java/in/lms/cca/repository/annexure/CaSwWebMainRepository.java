
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CaSwWebMain;

public interface CaSwWebMainRepository extends JpaRepository<CaSwWebMain, Long>{

	@Query("FROM CaSwWebMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	CaSwWebMain findByAnnexureId(Long annexureMainId);
	
}



