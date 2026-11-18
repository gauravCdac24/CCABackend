package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.master.ServiceMaster;

public interface IServiceMasterService {

	Optional<ServiceMaster> addServiceMaster(ServiceMaster obj);
	Optional<ServiceMaster> updateServiceMaster(ServiceMaster obj);
	ServiceMaster getServiceMasterById(Long id);
	List<ServiceMaster> getAllServiceMaster();
	ServiceMaster getServiceMasterByName(String serviceName);
}
