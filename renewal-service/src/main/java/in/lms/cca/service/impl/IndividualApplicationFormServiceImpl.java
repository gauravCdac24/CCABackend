package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.IndivAdditionalDetails;
import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.master.ApplicationTypeMaster;
import in.lms.cca.repository.IndivAplicationMasterRepository;
import in.lms.cca.service.IIndividualApplicationFormService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class IndividualApplicationFormServiceImpl implements IIndividualApplicationFormService{

	@Autowired
	private IndivAplicationMasterRepository indivAppRepo;
	
	@Override
	public Optional<IndivApplication> addIndivApplication(IndivApplication indivApplication) {
		if(indivApplication == null) {
			return Optional.empty();
		}
			
		try { 
			IndivApplication apptypeobj = indivAppRepo.save(indivApplication);
	        return Optional.of(apptypeobj);
	    
		}catch(Exception e) {
				
				return Optional.empty();
		}		

	}

//	@Override
//	public Optional<ApplicationTypeMaster> updateApplicationType(ApplicationTypeMaster obj) {
//		if(obj == null) {
//			return Optional.empty();
//		}
//		
//		if(obj.getAppTypeMasterId() == null) {
//			return Optional.empty();
//		}
//			
//		try { 
//			ApplicationTypeMaster apptypeobj = indivAppRepo.save(obj);
//	        return Optional.of(apptypeobj);
//	    
//		}catch(Exception e) {
//				
//				return Optional.empty();
//		}		
//	}

	@Override
	public ApplicationTypeMaster getApplicationTypeMasterById(Long id) {
		
		return indivAppRepo.findByApplicationTypeId(id);
	}

//	@Override
//	public List<ApplicationTypeMaster> getAllApplicationTypeMaster() {
//		return indivAppRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
//
//	}

	@Override
	public ApplicationTypeMaster getApplicationTypeMasterByName(String appType) {
		
		return indivAppRepo.findApplicationTypeMasterByName(appType);
	}

	@Override
	public Optional<ApplicationTypeMaster> updateApplicationType(ApplicationTypeMaster obj) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<ApplicationTypeMaster> getAllApplicationTypeMaster() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndivApplication findIntentAppById(Long intentAppId,String status) {
		// TODO Auto-generated method stub
		return indivAppRepo.findIntentAppById(intentAppId,status);
	}

	@Override
	public Optional<IndivApplication> updateIndivApplication(IndivApplication indivApplication) {
		if(indivApplication == null)
			return Optional.empty();
		
		if(indivApplication.getIndivApplicationId() == null)
			return Optional.empty();
		
		try {
			IndivApplication savedIndivApplication = indivAppRepo.save(indivApplication);
            return Optional.of(savedIndivApplication);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

	@Override
	public String getFullNameByIntentAppId(Long intentAppId) {
		// TODO Auto-generated method stub
		return indivAppRepo.getFullNameByIntentAppId(intentAppId);
	}

	@Override
	public List<IndivApplication> findIntentWithoutStatusAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return indivAppRepo.findIntentWithoutStatusAppById(intentAppId);
	}

}
