package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.entity.LicenseDetails;
import in.lms.cca.repository.LicenseDetailsRepository;
import in.lms.cca.service.ILicenseDetailsService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LicenseDetailsServiceImpl implements ILicenseDetailsService{

	@Autowired
	private LicenseDetailsRepository repo;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Optional<LicenseDetails> addLicenseDetails(LicenseDetails obj) {

		if(obj == null) {
			return Optional.empty();
		}
		try { 
	        
			LicenseDetails licenseDetailsObj = repo.save(obj);
	        return Optional.of(licenseDetailsObj);
	    
		}catch(Exception e) {
				e.printStackTrace();
				return Optional.empty();
		}
		
		
	}

	@Override
	public Optional<LicenseDetails> updateLicenseDetails(LicenseDetails obj) {
		
		if(obj == null) {
			return Optional.empty();
		}
		
		if(obj.getLicenseId() == null) {
			return Optional.empty();
		}
		
		try { 
	        
			LicenseDetails licenseDetailsObj = repo.save(obj);
	        return Optional.of(licenseDetailsObj);
	    
		}catch(Exception e) {
				return Optional.empty();
		}
		
	}

	@Override
	public LicenseDetails getLicenseDetailsById(Long id) {
		
		return repo.findByLicenseDetailsId(id);
	}

	@Override
	public List<LicenseDetails> getAllLicenseDetails() {

		return repo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<LicenseDetails> getAllActiveLicenseDetails() {
		
		return repo.findAllActiveLicenseDetails();
	}

	@Override
	public List<LicenseDetails> getAllInactiveLicenseDetails() {
		
		return repo.findAllInactiveLicenseDetails();
	}

	@Override
	public List<LicenseDetails> getActiveLicenseDetailsByUsername(String username) {
		
		return repo.findActiveLicenseDetailsByUsername(username);
	}

	@Override
	public List<LicenseDetails> getInactiveLicenseDetailsByUsername(String applicantUsername) {
		
		return repo.findInactiveLicenseDetailsByUsername(applicantUsername);
	}

	@Override
	public List<LicenseDetails> getAllLicenseDetailsByUsername(String username) {
		
		return repo.findAllLicenseDetailsByUsername(username);
	}

	@Override
	public List<LicenseDetails> getActiveLicenseDetails() {
	
		return repo.findAllActiveLicenseDetails();
	}

	@Override
	public void changedTheReviewStatus(String applicantUsername) {
		try {
			String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-review-status/{applicantUsername}";
			restTemplate.getForObject(uriTemplate, String.class, applicantUsername);
		} catch (Exception ex) {
			System.err.println("change-review-status skipped for " + applicantUsername + ": " + ex.getMessage());
		}
	}

	@Override
	public void changedThePaymentStatus(String applicantUsername) {
		try {
			String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-payment-status/{applicantUsername}";
			restTemplate.getForObject(uriTemplate, String.class, applicantUsername);
		} catch (Exception ex) {
			System.err.println("change-payment-status skipped for " + applicantUsername + ": " + ex.getMessage());
		}
	}

}
