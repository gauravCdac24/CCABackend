package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ESPPurposeEntity;
import in.lms.cca.entity.EsignLicenseeApplication;
import in.lms.cca.repository.ESPPurposeRepository;
import in.lms.cca.service.IESPPurposeService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ESPPurposeServiceImpl implements IESPPurposeService{

	@Autowired
	private ESPPurposeRepository repo;
	
	@Override
	public void inactiveAllPurposeByAppId(Long esignLicenseeAppId) {
		
		repo.inactiveAllPurposeByAppId(esignLicenseeAppId);
		
	}

	@Override
	public List<ESPPurposeEntity> getAllESPPurposeByStatusAndLicenseAppId(
			Long esignLicenseeAppId, String status) {
		
		
		
		return repo.findAllESPPurposeByStatusAndLicenseAppId(esignLicenseeAppId, status);
	}

	@Override
	public Optional<ESPPurposeEntity> addPurpose(ESPPurposeEntity obj) {
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	ESPPurposeEntity savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
		
	}

}
