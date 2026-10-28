package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.SubServiceMasterEntity;

public interface ISubServiceMasterService {
	
	Optional<SubServiceMasterEntity> addSubServiceMaster(SubServiceMasterEntity obj);
	Optional<SubServiceMasterEntity> updateSubServiceMaster(SubServiceMasterEntity obj);
	SubServiceMasterEntity getSubServiceMasterById(Long id);
	List<SubServiceMasterEntity> getAllSubServiceMaster();
	SubServiceMasterEntity getSubServiceMasterByName(String subServiceName);

}
