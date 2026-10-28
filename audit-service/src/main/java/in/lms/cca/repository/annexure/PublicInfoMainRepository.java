
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.PublicInfoMain;

public interface PublicInfoMainRepository extends JpaRepository<PublicInfoMain, Long>{

	@Query("FROM PublicInfoMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	PublicInfoMain findByAnnexureId(Long annexureMainId);

	
}







