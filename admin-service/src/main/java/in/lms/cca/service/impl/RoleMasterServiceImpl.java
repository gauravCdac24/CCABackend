package in.lms.cca.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.GetRoles;
import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.RoleMaster;
import in.lms.cca.repository.RoleMasterRepository;
import in.lms.cca.service.IRoleMasterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleMasterServiceImpl implements IRoleMasterService{

	@Autowired
	private RoleMasterRepository roleMasterRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	 private final Set<String> clientHostnames = new HashSet<>();
	 
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;

	
	@Override
	public Optional<RoleMaster> addRole(RoleMaster roleObj) {
		
		if(roleObj == null)
			return Optional.empty();
		
		try {
			return Optional.of(roleMasterRepo.save(roleObj));
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<RoleMaster> updateRole(RoleMaster roleObj) {
		if(roleObj == null)
			return Optional.empty();
		
		if(roleObj.getRoleId() == null)
			return Optional.empty();
		
		try {
			return Optional.of(roleMasterRepo.save(roleObj));
		}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteRoleById(Integer id) {
		
		RoleMaster roleObj = roleMasterRepo.findByRoleId(id);
		
		if(roleObj == null)
			return false;
		
		try {
			roleMasterRepo.deleteByRoleId(id);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public RoleMaster getRoleById(Integer id) {
		return roleMasterRepo.findByRoleId(id);
	}

	@Override
	public List<RoleMaster> getAllRoles() {
	
		return roleMasterRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<RoleMaster> getAllActiveRoles() {
		return roleMasterRepo.findAllActiveRoles();
	}

	@Override
	public List<RoleMaster> getAllInactiveRoles() {
		return roleMasterRepo.findAllInActiveRoles();
	}

	@Override
	public RoleMaster getRoleByName(String name) {
		return roleMasterRepo.findByRoleName(name);
	}

	@Override
	public RoleMaster getRoleMasterByPath(String path) {
		
		return roleMasterRepo.findRoleMasterByPath(path);
	}

	@Override
	public RoleMaster getRoleMasterByHomePage(String homepath) {
		
		return roleMasterRepo.findRoleMasterByHomePage(homepath);
	}

	@Override
	public List<GetRoles> getRoleByUserId(String userId) {
	    String uriTemplate = apigatewayServiceUrl + "/auth-service/getrolebyuserid/{userId}";
	    
	    Map<String, String> uriVariables = new HashMap<>();
	    uriVariables.put("userId", userId);
	    
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
	    URI uri = builder.buildAndExpand(uriVariables).toUri();
	    
	    // Use exchange to specify the type of response
	    ResponseEntity<List<GetRoles>> responseEntity = restTemplate.exchange(
	            uri,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<List<GetRoles>>() {}
	    );

	    // Return the body of the response
	    return responseEntity.getBody();
	}

	@Override
	public void addAssignRole(StaffLoginDTO staffLoginDTO) {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/add-new-cca-staff-login";
	    restTemplate.postForObject(uriTemplate, staffLoginDTO, String.class);
	}

	@Override
	public List<StaffLoginDTO> getDetailsByUserId(String id) {
		 String uriTemplate = apigatewayServiceUrl + "/auth-service/getdetailsbyuserid/{userId}";
		    
		    Map<String, String> uriVariables = new HashMap<>();
		    uriVariables.put("userId", id);
		    
		    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
		    URI uri = builder.buildAndExpand(uriVariables).toUri();
		    
		    // Use exchange to specify the type of response
		    ResponseEntity<List<StaffLoginDTO>> responseEntity = restTemplate.exchange(
		            uri,
		            HttpMethod.GET,
		            null,
		            new ParameterizedTypeReference<List<StaffLoginDTO>>() {}
		    );

		    // Return the body of the response
		    return responseEntity.getBody();
	}

	@Override
	public void changeUserRoleStatus(String loginId) {
	    // Construct the URL with the loginId path variable
	    String uriTemplate = apigatewayServiceUrl + "/auth-service/change-user-role-status/{loginId}";

	    // Set up the URI variables map
	    Map<String, String> uriVariables = new HashMap<>();
	    uriVariables.put("loginId", loginId);

	    // Build the URI using UriComponentsBuilder
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
	    URI uri = builder.buildAndExpand(uriVariables).toUri();

	    // Use exchange method to specify the response type as a String to inspect the raw response
	    ResponseEntity<String> responseEntity = restTemplate.exchange(
	        uri,
	        HttpMethod.GET,
	        null, // No need for a request body for a GET request
	        String.class
	    );

	    // Print the raw response body to debug
	    System.out.println("Raw Response: " + responseEntity.getBody());
	}

	@Override
	public void changeUserRoleStatus(GetRoles getRoles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GetRoles> getRoleByUserNAme(String userNames) {
		 String uriTemplate = apigatewayServiceUrl + "/auth-service/getrolebyusername/{userNames}";
		    
		    Map<String, String> uriVariables = new HashMap<>();
		    uriVariables.put("userNames", userNames);
		    
		    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
		    URI uri = builder.buildAndExpand(uriVariables).toUri();
		    
		    // Use exchange to specify the type of response
		    ResponseEntity<List<GetRoles>> responseEntity = restTemplate.exchange(
		            uri,
		            HttpMethod.GET,
		            null,
		            new ParameterizedTypeReference<List<GetRoles>>() {}
		    );

		    // Return the body of the response
		    return responseEntity.getBody();
		}

}
