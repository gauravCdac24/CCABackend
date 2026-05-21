package in.lms.cca.service.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.service.IUserLoginService;

@Service
public class UserLoginServiceImpl implements IUserLoginService {
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;


	 @Override
		public void addUser(UserLoginDTO newUser) throws Exception {
		 
		
	    	String uriTemplate = apigatewayServiceUrl + "/auth-service/add-new-user-login-intent";
	        restTemplate.postForObject(uriTemplate, newUser, String.class);
		
		 
	    }



	 	@Override
	    public UserLoginDTO changeLoginStatusById(String userId) {
	    	String uriTemplate = apigatewayServiceUrl + "/auth-service/change-intent-login-status/{userId}";
	    	  
		    	 Map<String, String> uriVariables = new HashMap<>();
		 		 uriVariables.put("userId", userId);
		 		
		 		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
		 		URI uri = builder.buildAndExpand(uriVariables).toUri();
		 		 
		 		return restTemplate.getForObject(uri, UserLoginDTO.class);
	    }



	@Override
	public void addUserAuditAgency(UserLoginDTO newUser) throws Exception {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/add-new-user-login-audit-agency";
        restTemplate.postForObject(uriTemplate, newUser, String.class);
		
	}



	@Override
	public UserLoginDTO changeAuditAgencyRoleStatusById(String userId) {
		
		String uriTemplate = apigatewayServiceUrl + "/auth-service/statusauditagency/{userId}";
  	  
	    	 Map<String, String> uriVariables = new HashMap<>();
	 		uriVariables.put("userId", userId);
	 		
	 		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
	 		URI uri = builder.buildAndExpand(uriVariables).toUri();
	 		 
	 		return restTemplate.getForObject(uri, UserLoginDTO.class);
	}



	@Override
	public List<UserLoginDTO> getAllUserLogins() {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/alls";
		try {
			
		
        ResponseEntity<UserLoginDTO[]> responseEntity = restTemplate.getForEntity(uriTemplate, UserLoginDTO[].class);
        return Arrays.asList(responseEntity.getBody());
        
	}
catch (RestClientException e) {
	        
	        // Return an empty list instead of crashing
	        return Collections.emptyList();
	    }
	}
	/*----*/
	//
	@Override
	public List<UserLoginDTO> getAllIntentUserLogins() {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/get-all-intent-logins";
        ResponseEntity<UserLoginDTO[]> responseEntity = restTemplate.getForEntity(uriTemplate, UserLoginDTO[].class);
        return Arrays.asList(responseEntity.getBody());
	}

	@Override
	public void changeIntentLoginStatusByUserId(String userId, String updatedBy) {

	
	    String uriTemplate = apigatewayServiceUrl + "/auth-service/change-intent-account-status";
	    
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
	                                    .queryParam("userId", userId)
	                                    .queryParam("updatedBy", updatedBy);

	    URI uri = builder.build().toUri();

	    
	    restTemplate.getForObject(uri, String.class);
	}

	@Override
	public List<UserLoginDTO> getAllUserLoginsAccount() {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/get-all-user-logins";
        ResponseEntity<UserLoginDTO[]> responseEntity = restTemplate.getForEntity(uriTemplate, UserLoginDTO[].class);
        return Arrays.asList(responseEntity.getBody());
	}
	
	
}
