package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.master.ApplicationTypeMaster;


public interface IIndividualApplicationFormService {

	
	Optional<ApplicationTypeMaster> updateApplicationType(ApplicationTypeMaster obj);
	ApplicationTypeMaster getApplicationTypeMasterById(Long id);
	List<ApplicationTypeMaster> getAllApplicationTypeMaster();
	ApplicationTypeMaster getApplicationTypeMasterByName(String appType);
	Optional<IndivApplication> addIndivApplication(IndivApplication indivApplication);
	IndivApplication findIntentAppById(Long intentAppId, String status);
	Optional<IndivApplication> updateIndivApplication(IndivApplication indivApplication);
	String getFullNameByIntentAppId(Long intentAppId);
	List<IndivApplication> findIntentWithoutStatusAppById(Long intentAppId);
	
	
}
