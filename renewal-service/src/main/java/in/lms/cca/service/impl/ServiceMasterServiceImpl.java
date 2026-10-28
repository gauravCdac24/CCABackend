package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.master.ServiceMaster;
import in.lms.cca.repository.ServiceMasterRepository;
import in.lms.cca.service.IServiceMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServiceMasterServiceImpl implements IServiceMasterService {

    @Autowired
    private ServiceMasterRepository serviceMasterRepo;

    @Override
    public Optional<ServiceMaster> addServiceMaster(ServiceMaster obj) {
        if (obj == null) {
            return Optional.empty();
        }

        try {
            ServiceMaster savedServiceMaster = serviceMasterRepo.save(obj);
            return Optional.of(savedServiceMaster);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ServiceMaster> updateServiceMaster(ServiceMaster obj) {
        if (obj == null || obj.getServiceId() == null) { 
            return Optional.empty();
        }

        try {
            ServiceMaster updatedServiceMaster = serviceMasterRepo.save(obj);
            return Optional.of(updatedServiceMaster);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ServiceMaster getServiceMasterById(Long id) {
        return serviceMasterRepo.findByServiceMasterId(id); 
    }

    @Override
    public List<ServiceMaster> getAllServiceMaster() {
        return serviceMasterRepo.findAll(Sort.by(Sort.Direction.DESC, "created")); 
    }

	@Override
	public ServiceMaster getServiceMasterByName(String serviceName) {
		
		return serviceMasterRepo.findServiceMasterByName(serviceName);
	}
}
