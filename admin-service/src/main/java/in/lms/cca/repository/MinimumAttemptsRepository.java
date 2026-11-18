package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.MinimumAttempts;

public interface MinimumAttemptsRepository extends JpaRepository<MinimumAttempts, Long>{

	//Find By Minimum Attempts Id
	@Query("FROM MinimumAttempts a WHERE a.attemptId=:attemptId")
	MinimumAttempts findByMinimumAttemptsId (@Param("attemptId")Long attemptId);

	@Query("FROM MinimumAttempts WHERE status = 'Active' ORDER BY created DESC")
	List<MinimumAttempts> findAllActiveMinimumAttempts();
	
	@Query("FROM MinimumAttempts WHERE status = 'Inactive' ORDER BY created DESC")
	List<MinimumAttempts> findAllInActiveMinimumAttempts();

	@Query("DELETE FROM MinimumAttempts c WHERE c.attemptId=:minimumId")
	boolean deleteMinimumAttempts(Long minimumId);

	@Query("FROM MinimumAttempts WHERE status = 'Active' ORDER BY created DESC")
	List<MinimumAttempts> findAllActiveMinimumAttempt();
	
}
