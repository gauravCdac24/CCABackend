package in.lms.cca.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.payload.LocationDetailsPayload;
import in.lms.cca.repository.IntentApplicationRepository;
import in.lms.cca.service.IIntentApplicationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class IntentApplicationServiceImpl implements IIntentApplicationService{
	
	@Autowired
	private IntentApplicationRepository intentAppRepo;
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;


	@Override
	public Optional<IntentApplication> addIntentApp(IntentApplication intentApp) {
		if(intentApp == null) {
			return Optional.empty();
		}
			
		try { 
			IntentApplication intentApbj = intentAppRepo.save(intentApp);
	        return Optional.of(intentApbj);
	    
		}catch(Exception e) {
				
				return Optional.empty();
		}			}

	@Override
	public Long addUsers(AddressDTO indivAddressDTO) {
		
		String uriTemplate = apigatewayServiceUrl + "/admin-service/adds-address";
	       
       return restTemplate.postForObject(uriTemplate, indivAddressDTO, Long.class);
	}

	@Override
	public IntentApplication findIntentAppById(String userName) {
		List<IntentApplication> apps = intentAppRepo.findAllByUserName(userName);
		return apps.isEmpty() ? null : apps.get(0);
	}

	@Override
	public Long addUser(AddressDTO locationDetailsPayload) {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/save-address";
	       
	       return restTemplate.postForObject(uriTemplate, locationDetailsPayload, Long.class);	}
	

	@Override
	public AddressDTO getAllLocationByAddressId(String addressId) {
		
		    String uriTemplate = apigatewayServiceUrl + "/admin-service/get-address-by-id/{addressId}";
  	  
	    	 Map<String, String> uriVariables = new HashMap<>();
	 		 uriVariables.put("addressId", addressId);
	 		
	 		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
	 		URI uri = builder.buildAndExpand(uriVariables).toUri();
	 		 
	 		return restTemplate.getForObject(uri, AddressDTO.class);
	}

	@Override
	public Optional<IntentApplication> updateIntentApp(IntentApplication intentApp) {
		if(intentApp == null) {
			return Optional.empty();
		}
		if(intentApp.getIntentAppId() == null) {
			return Optional.empty();
		}
			
		try { 
			IntentApplication intentApbj = intentAppRepo.save(intentApp);
	        return Optional.of(intentApbj);
	    
		}catch(Exception e) {
				
				return Optional.empty();
		}			

		
		
  }

	@Override
	public Long updateUsers(AddressDTO indivAddressDTO) {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/updates-address";
	       
	       return restTemplate.postForObject(uriTemplate, indivAddressDTO, Long.class);
	}

	@Override
	public Long updateUser(LocationDetailsPayload locationDetail) {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/updates-address";
	       
	       return restTemplate.postForObject(uriTemplate, locationDetail, Long.class);
	}

	@Override
	public IntentApplication getIntentByIntentAppId(Long intentAppId) {
		
		return intentAppRepo.findByIntentAppId(intentAppId);
	}

	

	/*---*/
	@Override
	public List<IntentApplication> getAllIntentApplication() {
		
		return intentAppRepo.findAll();
	}

	@Override
	public List<IntentApplication> getPaymentProofPendingVerification() {
		return intentAppRepo.findWithActivePaymentProofPendingVerification();
	}

	@Override
	public List<IntentApplication> getAllServiceMaster() {
		// TODO Auto-generated method stub
		return intentAppRepo.findAll();
	}
}
