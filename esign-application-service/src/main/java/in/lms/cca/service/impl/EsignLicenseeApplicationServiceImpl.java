package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EsignLicenseeApplication;
import in.lms.cca.repository.EsignLicenseeApplicationRepository;
import in.lms.cca.service.IEsignLicenseeApplicationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EsignLicenseeApplicationServiceImpl implements IEsignLicenseeApplicationService{

	@Autowired
	private EsignLicenseeApplicationRepository esignLicenseeRepo;
	
	@Override
	public Optional<EsignLicenseeApplication> addEsignLicenseeApplication(EsignLicenseeApplication obj) {
		
		 if (obj == null)
		        return Optional.empty();

		    try {
		       
		    	EsignLicenseeApplication savedObj = esignLicenseeRepo.save(obj);
		       
		        return Optional.of(savedObj);
		    } catch (Exception e) {
		        return Optional.empty();
		    }
	}

	@Override
	public List<EsignLicenseeApplication> getApplicationByUsernameAndStatus(String username, String status) {
		
		return esignLicenseeRepo.findApplicationByUsernameAndStatus(username, status);
	}

	@Override
	public List<EsignLicenseeApplication> getApplicationByUsernameAndNotStatus(String username, String status) {
		
		return esignLicenseeRepo.getApplicationByUsernameAndNotStatus(username, status);
	}

	@Override
	public List<EsignLicenseeApplication> getAllApplication() {
		
		return esignLicenseeRepo.findAll(Sort.by(Sort.Direction.DESC,"created"));
	}

	@Override
	public List<EsignLicenseeApplication> getAllApplicationForReview() {
		
		return esignLicenseeRepo.findAllApplicationForReview();
	}

	@Override
	public EsignLicenseeApplication getApplicationById(Long id) {
		
		return esignLicenseeRepo.findApplicationById(id);
	}

	@Override
	public List<EsignLicenseeApplication> getAllApplicationByStatus(String status) {
		
		return esignLicenseeRepo.findAllApplicationByStatus(status);
	}

	@Override
	public List<EsignLicenseeApplication> getApplicationByUsernameAndNotStatus(String username, String status1,
			String status2) {
		
		return esignLicenseeRepo.findApplicationByUsernameAndNotStatus(username, status1,
				status2);
	}

	@Override
	public List<EsignLicenseeApplication> getApplicationByUsernameAndStatus(String username, String status1,
			String status2) {
		
		return esignLicenseeRepo.findApplicationByUsernameAndStatus(username, status1,
				status2);
	}

	@Override
	public List<EsignLicenseeApplication> getApplicationByUsername(String username) {
		
		return esignLicenseeRepo.findApplicationByUsername(username);
	}

	@Override
	public Optional<EsignLicenseeApplication> updateEsignLicenseeApplication(EsignLicenseeApplication espApplication) {
		if (espApplication == null)
	        return Optional.empty();
		if (espApplication.getEsignLicenseeAppId() == null)
	        return Optional.empty();

	    try {
	       
	    	EsignLicenseeApplication savedObj = esignLicenseeRepo.save(espApplication);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	
	
}
