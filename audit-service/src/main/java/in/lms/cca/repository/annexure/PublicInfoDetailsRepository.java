
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.PublicInfoDetails;

public interface PublicInfoDetailsRepository extends JpaRepository<PublicInfoDetails, Long>{

	@Query("FROM PublicInfoDetails a WHERE a.publicInfoDetailsId = :id")
	PublicInfoDetails findByPublicInfoDetailsId(Long id);

	@Query("FROM PublicInfoDetails a WHERE a.publicInfoMainId.publicInfoMainId = :id ORDER BY publicInfoDetailsId")
	List<PublicInfoDetails> findByPublicInfoMainId(Long id);

	
}







