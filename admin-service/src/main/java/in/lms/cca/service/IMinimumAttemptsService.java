package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.MinimumAttempts;

public interface IMinimumAttemptsService {

	Optional<MinimumAttempts> addMinimumAttempts(MinimumAttempts minimumAttemptsObj);
	Optional<MinimumAttempts> updateMinimumAttempts(MinimumAttempts minimumAttemptsObj);
	List<MinimumAttempts> getAllMinimumAttempts();
	MinimumAttempts getMinimumAttemptsById(Long id);
	boolean deleteByMinimumAttemptId(Long minimumId);
	List<MinimumAttempts> getAllActiveMinimumAttempts();
	
}
