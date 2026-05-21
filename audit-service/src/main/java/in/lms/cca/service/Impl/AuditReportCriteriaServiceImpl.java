package in.lms.cca.service.Impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.payload.UserLoginDTO;
import in.lms.cca.repository.AuditReportCriteriaRepository;
import in.lms.cca.service.AuditReportCriteriaService;

@Service
@Transactional
public class AuditReportCriteriaServiceImpl implements AuditReportCriteriaService {
	
	@Autowired
	private AuditReportCriteriaRepository auditReportCriteriaRepository;
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;


	@Override
	public Optional<AuditReportCriteriaEntity> saveData(AuditReportCriteriaEntity auditReportCriteriaEntity) {
		
		if(auditReportCriteriaEntity==null)
		  return Optional.empty();
		 try {
			 AuditReportCriteriaEntity savedAuditReportCriteriaEntity = auditReportCriteriaRepository.save(auditReportCriteriaEntity);
	            return Optional.of(savedAuditReportCriteriaEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public Optional<AuditReportCriteriaEntity> findById(Long criteriaId) {
		// TODO Auto-generated method stub
		return auditReportCriteriaRepository.findById(criteriaId);
	}

	@Override
	public List<AuditReportCriteriaEntity> getAllData() {
		// TODO Auto-generated method stub
		return auditReportCriteriaRepository.findAllActiveData();
	}

	@Override
	public Optional<AuditReportCriteriaEntity> downloadfileBydDocumentName(String documentName) {
		// TODO Auto-generated method stub
		return auditReportCriteriaRepository.downloadfileBydDocumentName(documentName);
	}

	@Override
	public Optional<AuditReportCriteriaEntity> updateData(AuditReportCriteriaEntity auditReportCriteriaEntity) {
		if(auditReportCriteriaEntity==null)
			  return Optional.empty();
		
		if(auditReportCriteriaEntity.getCriteriaId()==null)
			  return Optional.empty();
			 try {
				 AuditReportCriteriaEntity savedAuditReportCriteriaEntity = auditReportCriteriaRepository.save(auditReportCriteriaEntity);
		            return Optional.of(savedAuditReportCriteriaEntity);
		        } catch (Exception e) {
		            return Optional.empty();
		        }
	}

	@Override
	public List<AuditReportCriteriaEntity> findByAuditId(Long appAuditId) {
		// TODO Auto-generated method stub
		return auditReportCriteriaRepository.findByAuditId(appAuditId);
	}

	@Override
	public void sendTheApplicantUserName(String applicantUserName) {
		 String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-status-by-username/{applicantUserName}";
		    
			
		    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
	}

	@Override
	public void changedTheApplicantUserName(String applicantUserName) {
		 String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-applicant-status-by-username/{applicantUserName}";
		    
		    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
	}

	@Override
	public UserLoginDTO findbyAgencyId(String auditAgencyId) {
	    // Define the URI template with a placeholder
	    String uriTemplate = apigatewayServiceUrl + "/auth-service/get-all-detail-by-userId/{userId}";

	    // Create a map for URI variables
	    Map<String, String> uriVariables = new HashMap<>();
	    uriVariables.put("userId", auditAgencyId);

	    // Build the URI using UriComponentsBuilder
	    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
	    URI uri = builder.buildAndExpand(uriVariables).toUri();

	    try {
	        // Make the REST call and specify the response type
	        ResponseEntity<UserLoginDTO> responseEntity = restTemplate.exchange(
	                uri,
	                HttpMethod.GET,
	                null, // No request body for GET
	                new ParameterizedTypeReference<UserLoginDTO>() {}
	        );

	        // Return the response body
	        return responseEntity.getBody();
	    } catch (Exception e) {
	        // Handle errors appropriately
	        throw new RuntimeException("Failed to fetch data for Audit Agency ID: " + auditAgencyId, e);
	    }
	}

	@Override
	public void changedTheStatus(String applicantUserName) {
		 String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-applicant-status/{applicantUserName}";
		    
		    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
		
	}

	@Override
	public void changedTheAproveOfRejection(String applicantUserName) {
		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-applicant-approve-for-rejection/{applicantUserName}";
	    
	    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
	}

	@Override
	public void changedTheStatusApprove(String applicantUserName) {
    String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-applicant-approve/{applicantUserName}";
	    
	    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
	}

	@Override
	public void changedTheApplicantRejection(String applicantUserName) {
       String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-applicant-cca-rejection/{applicantUserName}";
	    
	    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
	}

	@Override
	public AuditReportCriteriaEntity getAllDataByControlId(String controlId,
			ApplicationAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return null;
		}
		return auditReportCriteriaRepository.getAllDataByControlId(controlId,
				applicationAuditEntity.getAppAuditId());
	}

	@Override
	public AuditReportCriteriaEntity findByAuditControlId(String auditControlId) {
		return auditReportCriteriaRepository.findByAuditControlId(auditControlId);
	}

	@Override
	public AuditReportCriteriaEntity findActiveByAppAuditIdAndControlId(Long appAuditId, String auditControlId) {
		if (appAuditId == null || auditControlId == null || auditControlId.isBlank()) {
			return null;
		}
		return auditReportCriteriaRepository.findActiveByAppAuditIdAndControlId(appAuditId, auditControlId);
	}

	@Override
	public List<AuditReportCriteriaEntity> findActiveByAppAuditId(Long appAuditId) {
		if (appAuditId == null) {
			return List.of();
		}
		return auditReportCriteriaRepository.findByAuditId(appAuditId);
	}

	
	@Override
	public List<AuditReportCriteriaEntity> getAllDatas(ApplicationAuditEntity applicationAuditEntity) {
		// TODO Auto-generated method stub
		return auditReportCriteriaRepository.findAllActiveDatas(applicationAuditEntity);
	}


}
