package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.AuditAgencyFormDto;
import in.lms.cca.repository.ApplicationAuditRepository;
import in.lms.cca.service.ApplicationAuditService;

@Service
@Transactional
public class ApplicationAuditServiceImpl implements ApplicationAuditService {
	
	@Autowired
	private ApplicationAuditRepository applicationAuditRepository;
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;


	@Override
	public Optional<LicenseeAuditEntity> addApplicationAudit(LicenseeAuditEntity applicationAuditEntity) {
		 if (applicationAuditEntity == null) {
	            return Optional.empty();
	        }

	        try {
	        	LicenseeAuditEntity savedApplicationAuditEntity = applicationAuditRepository.save(applicationAuditEntity);
	            return Optional.of(savedApplicationAuditEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}


	@Override
	public void processAuditForm(AuditAgencyFormDto formDto) {
		// Process basic fields
        System.out.println("Audit Agency Name: " + formDto.getAuditAgencyName());
        System.out.println("Intent App ID: " + formDto.getIntentAppId());

        // Process undertaking file
        MultipartFile undertakingFile = formDto.getUndertakingFile();
        if (undertakingFile != null) {
            try {
                System.out.println("Undertaking File: " + undertakingFile.getOriginalFilename());
            } catch (Exception e) {
                System.err.println("Error reading undertaking file: " + e.getMessage());
            }
        }

        // Process auditor descriptions
        formDto.getAuditorDesscription().forEach(auditor -> {
            System.out.println("Auditor Name: " + auditor.getAuditorName());
            System.out.println("Certificate Type: " + auditor.getCertificateType());
            MultipartFile file = auditor.getFile();
            if (file != null) {
                try {
                    System.out.println("Auditor File: " + file.getOriginalFilename());
                } catch (Exception e) {
                    System.err.println("Error reading auditor file: " + e.getMessage());
                }
            }
        });

        // Process audit scope
        formDto.getAuditScope().forEach(scope -> {
            System.out.println("Audit Title: " + scope.getAuditTitle());
            System.out.println("Description: " + scope.getDescription());
            System.out.println("Start Date: " + scope.getStartDate());
            System.out.println("End Date: " + scope.getEndDate());
        });
    }


	@Override
	public LicenseeAuditEntity getByApplicantUserName(String ApplicantUserName) {
		// TODO Auto-generated method stub
		return applicationAuditRepository.getByApplicantUserName(ApplicantUserName);
	}


	@Override
	public void changeIntentStatusByUserName(String applicantUserName) {
		  String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-intent-status-by-username/{applicantUserName}";
		    
			
		    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
	}


	@Override
	public void rejectedByUserName(String applicantUserName) {
		 String uriTemplate = apigatewayServiceUrl + "/newlicense-service/reject-by-username/{applicantUserName}";
		    
			
		    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
	}


	@Override
	public void approvedByUserName(String applicantUserName) {
		 String uriTemplate = apigatewayServiceUrl + "/newlicense-service/approve-by-username/{applicantUserName}";
		    
			
		    restTemplate.getForObject(uriTemplate, String.class, applicantUserName);
		
	}


	@Override
	public Optional<LicenseeAuditEntity> updateAuditor(LicenseeAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null) {
            return Optional.empty();
        }
		
		if (applicationAuditEntity.getLicenseeAuditId() == null) {
            return Optional.empty();
        }

        try {
        	LicenseeAuditEntity savedApplicationAuditEntity = applicationAuditRepository.save(applicationAuditEntity);
            return Optional.of(savedApplicationAuditEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}


	@Override
	public List<LicenseeAuditEntity> getByApplicantUserNames(String applicantUserName) {
		// TODO Auto-generated method stub
		return applicationAuditRepository.getByApplicantUserNames(applicantUserName);
	}


	@Override
	public Optional<LicenseeAuditEntity> updateData(LicenseeAuditEntity auditEntity) {
		 if (auditEntity == null) {
	            return Optional.empty();
	        }
		 
		 if (auditEntity.getLicenseeAuditId() == null) {
	            return Optional.empty();
	        }

	        try {
	        	LicenseeAuditEntity savedApplicationAuditEntity = applicationAuditRepository.save(auditEntity);
	            return Optional.of(savedApplicationAuditEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}


	@Override
	public LicenseeAuditEntity getAuditDetailsById(Long appAuditId) {
		// TODO Auto-generated method stub
		return applicationAuditRepository.getAuditDetailsById(appAuditId);
	}


	@Override
	public List<LicenseeAuditEntity> getAll() {
		// TODO Auto-generated method stub
		return applicationAuditRepository.findAll();
	}

}
