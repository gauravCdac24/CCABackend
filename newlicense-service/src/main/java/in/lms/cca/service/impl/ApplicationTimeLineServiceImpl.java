package in.lms.cca.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.repository.ApplicationTimeLineRepository;
import in.lms.cca.service.IApplicationTimeLineService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ApplicationTimeLineServiceImpl implements IApplicationTimeLineService{

	@Autowired
	private ApplicationTimeLineRepository appRepo;
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;

	
	@Override
	public List<ApplicationTimeLine> getAllApplicationTimeLine() {
		
		return appRepo.findAll(Sort.by(Sort.Direction.DESC,"created"));
		
	}

	@Override
	public List<ApplicationTimeLine> getByIntentApplicationId(Long intentAppId) {
		
		return appRepo.findByIntentApplicationId(intentAppId);
		
	}

	@Override
	public Optional<ApplicationTimeLine> addApplicationTimeLine(ApplicationTimeLine obj) {

		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	ApplicationTimeLine appObj = appRepo.save(obj);
   
	        return Optional.of(appObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
		
	}

	@Override
	public Optional<ApplicationTimeLine> updateApplicationTimeLine(ApplicationTimeLine obj) {
		
		if (obj == null)
	        return Optional.empty();
		if(obj.getAppTimelineId() == null)
			return Optional.empty();

	    try {
	       
	    	ApplicationTimeLine appObj = appRepo.save(obj);
   
	        return Optional.of(appObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	    
	}

	@Override
	public List<AuditAgencySelectionDTO> getAllData(String userName) {

	    String uriTemplate = apigatewayServiceUrl + "/audit-service/get-all-details-by-userName/{userName}";
	    Map<String, String> uriVariables = new HashMap<>();
	    uriVariables.put("userName", userName);

	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
	    URI uri = builder.buildAndExpand(uriVariables).toUri();

	    try {
	        // Use ParameterizedTypeReference to handle List<AuditAgencySelectionDTO>
	        ResponseEntity<List<AuditAgencySelectionDTO>> response =
	                restTemplate.exchange(
	                        uri,
	                        HttpMethod.GET,
	                        null, // No request body for GET
	                        new ParameterizedTypeReference<List<AuditAgencySelectionDTO>>() {}
	                );

	        return response.getBody();

	    } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
	        return new java.util.ArrayList<>();
	    } catch (RestClientException e) {
	        e.printStackTrace();
	        return new java.util.ArrayList<>();
	    }
	}
	

	 
	
}
