package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationTypeMaster;




public interface IApplicationTypeMasterService {

	Optional<ApplicationTypeMaster> addApplicationType(ApplicationTypeMaster obj);
	Optional<ApplicationTypeMaster> updateApplicationType(ApplicationTypeMaster obj);
	ApplicationTypeMaster getApplicationTypeMasterById(Long id);
	List<ApplicationTypeMaster> getAllApplicationTypeMaster();
	ApplicationTypeMaster getApplicationTypeMasterByName(String appType);
	
}
