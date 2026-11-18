package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EKYCModeMaster;


public interface IEKYCModeMasterService {

	Optional<EKYCModeMaster> addEKYCModeMaster(EKYCModeMaster Obj);
	Optional<EKYCModeMaster> updateEKYCModeMaster(EKYCModeMaster Obj);
	List<EKYCModeMaster> getAllEKYCModeMaster();
	List<EKYCModeMaster> getAllActiveEKYCModeMaster();
	List<EKYCModeMaster> getAllInactiveEKYCModeMaster();
	EKYCModeMaster getEKYCModeMasterById(Long id);
	EKYCModeMaster getByEKYCModeTitle(String title);
	
	
}
