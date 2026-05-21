package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.ASPDetails;

public interface ASPDetailsRepository extends JpaRepository<ASPDetails, Long>{


	@Query("FROM ASPDetails a WHERE a.annexureMainId.annexureMainId = :id")
	ASPDetails findByAnnexureId(Long id);
	
	@Query("FROM ASPDetails a WHERE a.aspDetailsId = :id")
	ASPDetails findByASPDetailsId(Long id);

	
	
}












