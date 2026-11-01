package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditControlEntity;
import in.lms.cca.payload.AuditCriteriaPayload;
import in.lms.cca.repository.ApplicationAuditorsRepository;
import in.lms.cca.service.ApplicationAuditorsService;
import in.lms.cca.service.AuditService;

@Service
@Transactional
public class ApplicationAuditorsServiceImpl implements ApplicationAuditorsService {
	
	@Autowired 
	private ApplicationAuditorsRepository applicationAuditorsRepository;
	
	
	 @Autowired
	 private RestTemplate restTemplate;

	 @Autowired
	 private AuditService auditService;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;

	@Override
	public Optional<ApplicationAuditorsEntity> processAuditForm(ApplicationAuditorsEntity auditorDescriptiones) {
		 if (auditorDescriptiones == null) {
	            return Optional.empty();
	        }

	        try {
	        	ApplicationAuditorsEntity savedApplicationAuditorsEntity = applicationAuditorsRepository.save(auditorDescriptiones);
	            return Optional.of(savedApplicationAuditorsEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<ApplicationAuditorsEntity> getAllDataByAuditId(Long appAuditId) {
		// TODO Auto-generated method stub
		return applicationAuditorsRepository.getAllDataByAuditId(appAuditId);
	}

	@Override
	public Optional<ApplicationAuditorsEntity> downloadfile(Long id) {
		// TODO Auto-generated method stub
		return applicationAuditorsRepository.downloadfile(id);
	}

	@Override
	public Optional<ApplicationAuditorsEntity> aprovedAuditForm(ApplicationAuditorsEntity auditorDescriptiones) {
		 if (auditorDescriptiones == null) {
	            return Optional.empty();
	        }
		 if (auditorDescriptiones.getAppAuditorId() == null) {
	            return Optional.empty();
	        }

	        try {
	        	ApplicationAuditorsEntity savedApplicationAuditorsEntity = applicationAuditorsRepository.save(auditorDescriptiones);
	            return Optional.of(savedApplicationAuditorsEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public Optional<ApplicationAuditorsEntity> updateData(ApplicationAuditorsEntity auditorsEntity) {
		if (auditorsEntity == null) {
            return Optional.empty();
        }

		if (auditorsEntity.getAppAuditorId() == null) {
            return Optional.empty();
        }
		
        try {
        	ApplicationAuditorsEntity savedApplicationAuditorsEntity = applicationAuditorsRepository.save(auditorsEntity);
            return Optional.of(savedApplicationAuditorsEntity);
        } catch (Exception e) {
            return Optional.empty();
        }

}

	@Override
	public List<ApplicationAuditorsEntity> getAllActiveActiveAuditor() {
		// TODO Auto-generated method stub
		return applicationAuditorsRepository.getAllActiveActiveAuditor();
	}

	@Override
	public List<AuditCriteriaPayload> getAllDatas(String userName) {
	    String selectedCriteriaIds = selectedCriteriaIdsForReport(userName);
	    String uriTemplate = apigatewayServiceUrl + "/api/admin-service/data?userName=" + userName
	            + "&includeAllControls=true";
	    if (!selectedCriteriaIds.isEmpty()) {
	        uriTemplate += "&selectedCriteriaIds=" + selectedCriteriaIds;
	    }

	    ResponseEntity<List<AuditCriteriaPayload>> response = restTemplate.exchange(
	        uriTemplate,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<AuditCriteriaPayload>>() {}
	    );

	    return response.getBody();
	}

	/**
	 * Audit agency checkboxes store criteria ids 1-11. Pass those groups so a
	 * reviewed control inside a marked group can still be found. The report
	 * itself prints only rows the auditor asked for and later reviewed.
	 */
	private String selectedCriteriaIdsForReport(String userName) {
	    List<AuditControlEntity> controls = auditService.getActiveAuditControlsByUser(userName);
	    if (controls == null || controls.isEmpty()) {
	        return "";
	    }
	    return controls.stream()
	            .filter(control -> control != null && control.getControlId() != null
	                    && control.getControlId() >= 1 && control.getControlId() <= 11)
	            .map(control -> String.valueOf(control.getControlId()))
	            .distinct()
	            .collect(Collectors.joining(","));
	}

	@Override
	public Optional<ApplicationAuditorsEntity> downloadfiles(String id) {
		// Multiple Active auditor rows share the same undertaking document after
		// multi-auditor submit; Optional single-result queries fail with NonUniqueResult.
		List<ApplicationAuditorsEntity> matches =
				applicationAuditorsRepository.findActiveByUndertakingDocument(id);
		if (matches == null || matches.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(matches.get(0));
	}

	@Override
	public List<AuditCriteriaPayload> getAllData(String sanitizedUserName) {
		 // Modify the URI to include the query parameter
	    String uriTemplate = apigatewayServiceUrl + "/api/admin-service/nc-data?userName=" + sanitizedUserName;

	    ResponseEntity<List<AuditCriteriaPayload>> response = restTemplate.exchange(
	        uriTemplate,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<AuditCriteriaPayload>>() {}
	    );

	    return response.getBody();
	}

	
}
