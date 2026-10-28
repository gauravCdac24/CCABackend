package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.IntentUniqueCode;

public interface IIntentUniqueCodeService {

	Long generateUniqueCode();
	Optional<IntentUniqueCode> addUniqueCode(IntentUniqueCode obj);
	Optional<IntentUniqueCode> updateUniqueCode(IntentUniqueCode obj);
	IntentUniqueCode getByActiveUniqueCode(Long id);
	List<IntentUniqueCode> getAllUniqueCode();
	IntentUniqueCode getUniqueCodeById(Long id);
	IntentUniqueCode getUniqueCodeByEmailId(String emailId);
	IntentUniqueCode getUniqueCodeByMobileNo(String mobileNo);
	IntentUniqueCode getUniqueCodeByOrganizationName(String organizationName);
	IntentUniqueCode getActiveUniqueCodeById(Long id);
	IntentUniqueCode getByUniqueCode(Long ucode);
	
}
