package in.lms.cca.service.impl;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.ClientLicenseDetailsDTO;
import in.lms.cca.dto.ESignAPIVersionMasterDTO;
import in.lms.cca.service.IClientLicenseeService;

@Service
public class ClientLicenseeServiceImpl implements IClientLicenseeService{

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Override
	public List<ClientLicenseDetailsDTO> getAllLicenseDetailsByUsername(String username) {

	    String uriTemplate = apigatewayServiceUrl + "/license-issuance-service/get-all-license-details-by-username";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
	                                    .queryParam("id", username);
	    URI uri = builder.build().toUri();

	    try {
	        
	        ResponseEntity<List<ClientLicenseDetailsDTO>> response = restTemplate.exchange(
	            uri,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<List<ClientLicenseDetailsDTO>>() {}
	        );

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();
	    } catch (RestClientException ex) {
	        return Collections.emptyList();
	    }
	}


	@Override
	public List<ClientLicenseDetailsDTO> getAllActiveLicenseDetails() {
	    String uriTemplate = apigatewayServiceUrl + "/license-issuance-service/get-all-active-license-details";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<ClientLicenseDetailsDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<ClientLicenseDetailsDTO>>() {}
	    		);

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public List<ESignAPIVersionMasterDTO> getAllAPIVersion() {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-esign-api-version";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<ESignAPIVersionMasterDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<ESignAPIVersionMasterDTO>>() {}
	    		);

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


}
