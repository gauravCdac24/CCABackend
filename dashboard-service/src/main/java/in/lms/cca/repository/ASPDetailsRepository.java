package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.ASPDetails;

public interface ASPDetailsRepository extends JpaRepository<ASPDetails, Long>{

	@Query("FROM ASPDetails a WHERE a.caUsername = :username ORDER BY created DESC")
	List<ASPDetails> findASPDetailsByUsername(String username);

	@Query("FROM ASPDetails a WHERE a.aspId = :id")
	ASPDetails findASPDetailsById(Long id);

	
	@Query("FROM ASPDetails a WHERE a.caUsername = :username AND a.aspName = :aspName")
	ASPDetails findASPDetailsByUsernameAndName(String username, String aspName);

	@Query("FROM ASPDetails a WHERE a.caUsername = :username AND a.emailId = :emailId")
	ASPDetails findASPDetailsByUsernameAndEmailId(String username, String emailId);

}
