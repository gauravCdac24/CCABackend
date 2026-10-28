package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ApplicationTypeMaster;
import in.lms.cca.repository.ApplicationTypeMasterRepository;
import in.lms.cca.service.IApplicationTypeMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ApplicationTypeMasterServiceImpl implements IApplicationTypeMasterService{

	@Autowired
	private ApplicationTypeMasterRepository appTypeRepo;
	
	@Override
	public Optional<ApplicationTypeMaster> addApplicationType(ApplicationTypeMaster obj) {
		if(obj == null) {
			return Optional.empty();
		}
			
		try { 
			ApplicationTypeMaster apptypeobj = appTypeRepo.save(obj);
	        return Optional.of(apptypeobj);
	    
		}catch(Exception e) {
				
				return Optional.empty();
		}		

	}

	@Override
	public Optional<ApplicationTypeMaster> updateApplicationType(ApplicationTypeMaster obj) {
		if(obj == null) {
			return Optional.empty();
		}
		
		if(obj.getAppTypeMasterId() == null) {
			return Optional.empty();
		}
			
		try { 
			ApplicationTypeMaster apptypeobj = appTypeRepo.save(obj);
	        return Optional.of(apptypeobj);
	    
		}catch(Exception e) {
				
				return Optional.empty();
		}		
	}

	@Override
	public ApplicationTypeMaster getApplicationTypeMasterById(Long id) {
		
		return appTypeRepo.findByApplicationTypeId(id);
	}

	@Override
	public List<ApplicationTypeMaster> getAllApplicationTypeMaster() {
		return appTypeRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));

	}

	@Override
	public ApplicationTypeMaster getApplicationTypeMasterByName(String appType) {
		
		return appTypeRepo.findApplicationTypeMasterByName(appType);
	}

	
	
}
