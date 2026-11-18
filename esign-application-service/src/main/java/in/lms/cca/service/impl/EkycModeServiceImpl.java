package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EkycMode;
import in.lms.cca.repository.EkycModeRepository;
import in.lms.cca.service.IEkycModeService;

@Service
@Repository
public class EkycModeServiceImpl implements IEkycModeService{

	@Autowired
	private EkycModeRepository repo;
	
	@Override
	public Optional<EkycMode> addekycMode(EkycMode obj) {
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	EkycMode savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<EkycMode> getAllEKYCModeByAppId(Long esignLicenseeAppId) {
		return repo.findAllEKYCModeByAppId(esignLicenseeAppId);
	}

	@Override
	public void deleteAllEKYCModeByAppId(Long esignLicenseeAppId) {
		repo.deleteAllEKYCModeByAppId(esignLicenseeAppId);
	}

	@Override
	public List<EkycMode> getAllEKYCModeByAppIdAndStatus(Long esignLicenseeAppId, String status) {
		
		return repo.getAllEKYCModeByAppIdAndStatus(esignLicenseeAppId, status);
	}

	@Override
	public void inactiveAllEKYCModeByAppId(Long esignLicenseeAppId) {
		repo.inactiveAllEKYCModeByAppId(esignLicenseeAppId);
		
	}

	@Override
	public EkycMode getEkycModeById(Long id) {
		
		return repo.findEkycModeById(id);
	}

	@Override
	public List<EkycMode> getAllEKYCModeByAppIdAndStatusAndRequired(Long esignLicenseeAppId, String status,
			Boolean isApprovalRequired) {
		
		return repo.findAllEKYCModeByAppIdAndStatusAndRequired(esignLicenseeAppId, status,
				isApprovalRequired);
	}

	@Override
	public void changeStatusOfAllEKYCModeByAppIdAndStatus(Long id, String status) {
		
		repo.changeStatusOfAllEKYCModeByAppIdAndStatus(id, status);
		
	}

	@Override
	public List<String> getDistinctApprovedEkycModesByUsername(String userName) {
		
		return repo.findDistinctApprovedEkycModesByUsername(userName);
	}


	
}
