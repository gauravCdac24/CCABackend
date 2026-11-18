package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.IntentDTO;
import in.lms.cca.dto.LoginDTO;
import in.lms.cca.entity.Intent;

public interface IIntentService {

	Optional<Intent> addIntent(Intent intentObj);
	Optional<Intent> updateIntent(Intent intentObj);
	List<Intent> getAllIntent();
	List<Intent> getAllActiveIntent();
	List<Intent> getAllInactiveIntent();
	Intent getIntentById(Long id);
	Intent getIntentByAddressId(Long id);
	boolean deleteByIntentId(long intent);
	LoginDTO getIntentByUserName(String userName);
	Intent getIntentByUserId(Long userId);
	Intent registerIntent(IntentDTO intentObj) throws Exception;
	
}
