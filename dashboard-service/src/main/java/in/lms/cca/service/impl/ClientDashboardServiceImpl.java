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

import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.AnnualAuditScheduleDTO;
import in.lms.cca.dto.AppLocationDTO;
import in.lms.cca.dto.ApplicationAuditorsDTO;
import in.lms.cca.dto.ApplicationTimeLineDTO;
import in.lms.cca.dto.AuditAgencyDTO;
import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.dto.ClientLicenseDetailsDTO;
import in.lms.cca.dto.ESPDTO;
import in.lms.cca.dto.ESPWithEkycModeDTO;
import in.lms.cca.dto.FirmApplicationMainDTO;
import in.lms.cca.dto.GovtOrganizationApplicationMainDTO;
import in.lms.cca.dto.IndivAddressMainDTO;
import in.lms.cca.dto.IndivApplicationMainDTO;
import in.lms.cca.dto.IntentUniqueCodeDTO;
import in.lms.cca.dto.LicenseeAuditorsDTO;
import in.lms.cca.dto.StateDTO;
import in.lms.cca.service.IClientDashboardService;
import in.lms.cca.util.global.EncryptionUtil;

@Service
public class ClientDashboardServiceImpl implements IClientDashboardService{

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
	public List<ESPDTO> getAllApprovedESP() {
		
		String uriTemplate = apigatewayServiceUrl + "/esign-application-service/get-esp-application-approved-for-esign-go-live";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<ESPDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<ESPDTO>>() {}
	    		);

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }

	}


	@Override
	public String isESP(String username) {
		
		try {
		
		String uriTemplate = apigatewayServiceUrl + "/esign-application-service/check-for-esp";
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
                .queryParam("id", EncryptionUtil.encrypt(username));
            

		URI uri = builder.build().toUri();
		
		return restTemplate.getForObject(uri, String.class);
		
		}catch (RestClientException ex) {
	        ex.printStackTrace();
	        throw new RuntimeException("Failed to check ESP status");
	    }
		
	}


	@Override
	public List<StateDTO> getAllStates() {
		
		 String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-state";
		    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

		    URI uri = builder.build().toUri();

		    try {
		    	ResponseEntity<List<StateDTO>> response = restTemplate.exchange(
		    		    uri,
		    		    HttpMethod.GET,
		    		    null,  
		    		    new ParameterizedTypeReference<List<StateDTO>>() {}
		    		);

		        
		        return response.getBody() != null ? response.getBody() : Collections.emptyList();

		    } catch (RestClientException ex) {
		        ex.printStackTrace();
		        return Collections.emptyList(); 
		    }
	}


	@Override
	public List<ClientLicenseDetailsDTO> getAllLicenseDetails() {
		 String uriTemplate = apigatewayServiceUrl + "/license-issuance-service/get-all-license-details";
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
	public List<ESPWithEkycModeDTO> getAllESPWithEkycModeApproved() {
		
		
		String uriTemplate = apigatewayServiceUrl + "/esign-application-service/get-all-esp-with-ekyc-mode-approved";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<ESPWithEkycModeDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<ESPWithEkycModeDTO>>() {}
	    		);

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
		
		
	}


	@Override
	public List<StateDTO> getAllStateByCountryName(String countryName) {
		
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-state-by-country-name";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
	                                    .queryParam("id", countryName);
	    URI uri = builder.build().toUri();

	    try {
	        
	        ResponseEntity<List<StateDTO>> response = restTemplate.exchange(
	            uri,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<List<StateDTO>>() {}
	        );

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();
	    } catch (RestClientException ex) {
	        return Collections.emptyList();
	    }
	      
	    
	}


	@Override
	public List<ClientLicenseDetailsDTO> getAllInactiveLicenseDetails() {
		
		String uriTemplate = apigatewayServiceUrl + "/license-issuance-service/get-all-inactive-license-details";
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
	public List<AnnualAuditScheduleDTO> getAllAnnualAuditSchedule() {
		String uriTemplate = apigatewayServiceUrl + "/annual-audit-service/get-annual-audit-schedule-report";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<AnnualAuditScheduleDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<AnnualAuditScheduleDTO>>() {}
	    		);

	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public List<ApplicationTimeLineDTO> getAllIntentApplicationTimeLine() {
		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/get-all-application-timeline";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<ApplicationTimeLineDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<ApplicationTimeLineDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public List<AuditAgencySelectionDTO> getAllSelectedAuditAgency() {
		String uriTemplate = apigatewayServiceUrl + "/audit-service/get-all-audit-selection";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<AuditAgencySelectionDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<AuditAgencySelectionDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public List<ApplicationAuditorsDTO> getAllActiveApplicationAuditors() {
		String uriTemplate = apigatewayServiceUrl + "/audit-service/get-all-active-auditors";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<ApplicationAuditorsDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<ApplicationAuditorsDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }

	}


	@Override
	public List<AuditAgencyDTO> getAllAuditAgencyList() {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-audit-agency";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<AuditAgencyDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<AuditAgencyDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public FirmApplicationMainDTO getFirmApplicationByUsername(String username) {
	    String uriTemplate = apigatewayServiceUrl + "/newlicense-service/get-firm-application-form-by-username";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
	                                    .queryParam("userName", username);
	    URI uri = builder.build().toUri();

	    try {
	        ResponseEntity<FirmApplicationMainDTO> response = restTemplate.exchange(
	            uri,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<FirmApplicationMainDTO>() {}
	        );

	        
	        return response.getBody() != null ? response.getBody() : null;
	    } catch (RestClientException ex) {
	        return null;
	    }
	}


	@Override
	public GovtOrganizationApplicationMainDTO getGovtOrgAppByUsername(String username) {
		
		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/get-government-agency-application-form-by-username";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
	                                    .queryParam("userName", username);
	    URI uri = builder.build().toUri();

	    try {
	        ResponseEntity<GovtOrganizationApplicationMainDTO> response = restTemplate.exchange(
	            uri,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<GovtOrganizationApplicationMainDTO>() {}
	        );

	        
	        return response.getBody() != null ? response.getBody() : null;
	    } catch (RestClientException ex) {
	        return null;
	    }
	    

	}


	@Override
	public IndivApplicationMainDTO getIndivAppByUsername(String username) {
		

		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/get-individual-application-form-by-username";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
	                                    .queryParam("userName", username);
	    URI uri = builder.build().toUri();

	    try {
	        ResponseEntity<IndivApplicationMainDTO> response = restTemplate.exchange(
	            uri,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<IndivApplicationMainDTO>() {}
	        );

	        return response.getBody() != null ? response.getBody() : null;
	    } catch (RestClientException ex) {
	        return null;
	    }

		
		
		
	}


	@Override
	public List<LicenseeAuditorsDTO> getAllActiveAnnualApplicationAuditors() {
		String uriTemplate = apigatewayServiceUrl + "/annual-audit-service/get-all-active-auditors";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<LicenseeAuditorsDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<LicenseeAuditorsDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }

	}


	@Override
	public List<IntentUniqueCodeDTO> getAllIntentUniqueCode() {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-intent-unique-code";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<IntentUniqueCodeDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<IntentUniqueCodeDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public List<AddressDTO> getAllAddress() {
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-address";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<AddressDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<AddressDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	@Override
	public IndivAddressMainDTO getIndivAddressByUsername(String userName) {
		
		
		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/get-second-individual-application-form-by-username";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<IndivAddressMainDTO> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<IndivAddressMainDTO>() {}
	    		);

	    	
	        return response.getBody();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return null; 
	    }
		
	}


	@Override
	public List<AppLocationDTO> getAllActiveAppLocations() {
		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/get-all-active-applocation";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);

	    URI uri = builder.build().toUri();

	    try {
	    	ResponseEntity<List<AppLocationDTO>> response = restTemplate.exchange(
	    		    uri,
	    		    HttpMethod.GET,
	    		    null,  
	    		    new ParameterizedTypeReference<List<AppLocationDTO>>() {}
	    		);

	    	if(response == null)
	    		return Collections.emptyList(); 
	        
	        return response.getBody() != null ? response.getBody() : Collections.emptyList();

	    } catch (RestClientException ex) {
	        ex.printStackTrace();
	        return Collections.emptyList(); 
	    }
	}


	




}
