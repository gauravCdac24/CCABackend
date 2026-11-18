
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.RevocationMain;

public interface RevocationMainRepository extends JpaRepository<RevocationMain, Long>{

	@Query("FROM RevocationMain a WHERE a.annexureMainId.annexureMainId = :id")
	RevocationMain findByAnnexureId(Long id);
	
	@Query("FROM RevocationMain a WHERE a.revocationMainId = :id")
	RevocationMain findByRevocationMainId(Long id);
	
}






