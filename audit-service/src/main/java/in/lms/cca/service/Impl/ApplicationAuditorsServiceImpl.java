package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditScheduleEntity;
import in.lms.cca.payload.AuditCriteriaPayload;
import in.lms.cca.repository.ApplicationAuditorsRepository;
import in.lms.cca.service.ApplicationAuditorsService;

@Service
@Transactional
public class ApplicationAuditorsServiceImpl implements ApplicationAuditorsService {
	
	@Autowired 
	private ApplicationAuditorsRepository applicationAuditorsRepository;
	
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
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
	    // Modify the URI to include the query parameter
	    String uriTemplate = apigatewayServiceUrl + "/admin-service/data?userName=" + userName;

	    ResponseEntity<List<AuditCriteriaPayload>> response = restTemplate.exchange(
	        uriTemplate,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<AuditCriteriaPayload>>() {}
	    );

	    return response.getBody();
	}

	@Override
	public Optional<ApplicationAuditorsEntity> downloadfiles(String id) {
		// TODO Auto-generated method stub
		return applicationAuditorsRepository.downloadfiles(id);
	}

	@Override
	public List<AuditCriteriaPayload> getAllData(String sanitizedUserName) {
		 // Modify the URI to include the query parameter
	    String uriTemplate = apigatewayServiceUrl + "/admin-service/nc-data?userName=" + sanitizedUserName;

	    ResponseEntity<List<AuditCriteriaPayload>> response = restTemplate.exchange(
	        uriTemplate,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<AuditCriteriaPayload>>() {}
	    );

	    return response.getBody();
	}

	
}
