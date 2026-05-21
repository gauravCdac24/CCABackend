package in.lms.cca.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.payload.UserResponse;
import in.lms.cca.service.IUserServiceClient;

@Service
public class UserServiceClientImpl implements IUserServiceClient{

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	
	@Override
	public UserResponse getUserDetailsByUsername(String username) {
		
		String uriTemplate = apigatewayServiceUrl+"/auth-service/get-by-username/{username}";
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("username", username);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
		URI uri = builder.buildAndExpand(uriVariables).toUri();
		
		return restTemplate.getForObject(uri, UserResponse.class);
	}
	
	

}
