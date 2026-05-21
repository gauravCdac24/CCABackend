package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.SubServiceMasterEntity;
import in.lms.cca.repository.SubServiceRepository;
import in.lms.cca.service.ISubServiceMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SubServiceMasterServiceImpl implements ISubServiceMasterService{
	
	  @Autowired
	    private SubServiceRepository subServiceRepo;

	@Override
	public Optional<SubServiceMasterEntity> addSubServiceMaster(SubServiceMasterEntity obj) {
		  if (obj == null) {
	            return Optional.empty();
	        }

	        try {
	            SubServiceMasterEntity savedSubServiceMaster = subServiceRepo.save(obj);
	            return Optional.of(savedSubServiceMaster);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public Optional<SubServiceMasterEntity> updateSubServiceMaster(SubServiceMasterEntity obj) {
		System.out.println(obj.getSubServiceId());
		 if (obj == null || obj.getSubServiceId() == null) { 
	            return Optional.empty();
	        }

	        try {
	        	SubServiceMasterEntity updatedSubServiceMaster = subServiceRepo.save(obj);
	            return Optional.of(updatedSubServiceMaster);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return Optional.empty();
	        }
	}

	@Override
	public SubServiceMasterEntity getSubServiceMasterById(Long id) {
		// TODO Auto-generated method stub
		 return subServiceRepo.findBySubServiceMasterId(id); 
	}

	@Override
	public List<SubServiceMasterEntity> getAllSubServiceMaster() {
		// TODO Auto-generated method stub
		  return subServiceRepo.findAll(Sort.by(Sort.Direction.DESC, "created")); 
	}

	@Override
	public SubServiceMasterEntity getSubServiceMasterByName(String subServiceName) {
		// TODO Auto-generated method stub
		return subServiceRepo.findSubServiceMasterByName(subServiceName);
	}

}
