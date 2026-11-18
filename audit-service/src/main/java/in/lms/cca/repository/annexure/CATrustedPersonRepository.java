
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CATrustedPerson;

public interface CATrustedPersonRepository extends JpaRepository<CATrustedPerson, Long>{

	@Query("FROM CATrustedPerson a WHERE a.personMainId.personMainId = :id ORDER BY personId")
	List<CATrustedPerson> findByCATrustedPersonMainId(Long id);
	
	@Query("FROM CATrustedPerson a WHERE a.personId = :id")
	CATrustedPerson findByCATrustedPersonId(Long id);
	
	
}












