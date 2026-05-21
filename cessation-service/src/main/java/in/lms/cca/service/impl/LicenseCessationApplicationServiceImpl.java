package in.lms.cca.service.impl;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.entity.LicenseCessationApplicationEntity;
import in.lms.cca.repository.LicenseCessationApplicationRepository;
import in.lms.cca.service.LicenseCessationApplicationService;

@Service
@Repository
public class LicenseCessationApplicationServiceImpl implements LicenseCessationApplicationService{

	@Autowired
	private LicenseCessationApplicationRepository licenseCessationApplicationRepository;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	
	
	@Override
	public List<LicenseCessationApplicationEntity> findByLicenseId(String licenseId) {
		
		return licenseCessationApplicationRepository.findByLicenseId(licenseId);
	}

	@Override
	public Optional<LicenseCessationApplicationEntity> saveAllData(
			LicenseCessationApplicationEntity licenseCessationApplicationEntity) {
		if (licenseCessationApplicationEntity == null)
	        return Optional.empty();

	    try {
	       
	    	LicenseCessationApplicationEntity savedLicenseCessationApplicationEntity = licenseCessationApplicationRepository.save(licenseCessationApplicationEntity);
	       
	        return Optional.of(savedLicenseCessationApplicationEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<LicenseCessationApplicationEntity> updateAllData(
			LicenseCessationApplicationEntity applicationEntity) {
		if (applicationEntity == null)
	        return Optional.empty();
		
		if (applicationEntity.getCessationAppId() == null)
	        return Optional.empty();

	    try {
	       
	    	LicenseCessationApplicationEntity savedLicenseCessationApplicationEntity = licenseCessationApplicationRepository.save(applicationEntity);
	       
	        return Optional.of(savedLicenseCessationApplicationEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public LicenseCessationApplicationEntity getByLicenseById(String licenseId) {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getByLicenseById(licenseId);
	}

	@Override
	public Optional<LicenseCessationApplicationEntity> getByCessationById(String cessationAppId) {
		
		return licenseCessationApplicationRepository.findById(Long.valueOf(cessationAppId));
	}

	@Override
	public List<LicenseCessationApplicationEntity> getAllData() {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getAllData();
	}

	@Override
	public LicenseCessationApplicationEntity getNoticeDocumentById(Long cessationAppId) {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getNoticeDocumentById(cessationAppId);
	}

	@Override
	public List<LicenseCessationApplicationEntity> getAllDataForUndertaking() {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getAllDataForUndertaking();
	}

	@Override
	public List<LicenseCessationApplicationEntity> getAllDataForUndertakings() {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getAllDataForUndertakings();
	}

	@Override
	public String changeLicenseeRoleToExLicensee(String userName) {
		
			
			
			String uriTemplate = apigatewayServiceUrl + "/auth-service/change-licensee-role-to-exlicensee";
		    
		    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
		                                    .queryParam("username", userName);
		                                

		    URI uri = builder.build().toUri();

		    return restTemplate.getForObject(uri, String.class);

		}

	@Override
	public List<LicenseCessationApplicationEntity> getAllDataForUndertakinges() {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getAllDataForUndertakinges();
	}

	@Override
	public LicenseCessationApplicationEntity getNoticeDocumentsById(Long cessationAppId) {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getNoticeDocumentsById(cessationAppId);
	}

	@Override
	public List<LicenseCessationApplicationEntity> getAllDataForUndertakingess() {
		// TODO Auto-generated method stub
		return licenseCessationApplicationRepository.getAllDataForUndertakingess();
	}

}
