package in.lms.cca.service.Impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.StatusChangedOfLicense;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.AuditAgencySelectionDTO;
import in.lms.cca.repository.AuditAgencySelectionRepository;
import in.lms.cca.service.AuditAgencySelectionService;

@Service
@Transactional
public class AuditAgencySelectionServiceImpl implements AuditAgencySelectionService {
	
	@Autowired
	private AuditAgencySelectionRepository agencySelectionRepository;
	
	
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${apigateway.service.url}")
		private String apigatewayServiceUrl;


	@Override
	public Optional<AuditAgencySelectionEntity> addAuditAgencySelection(
			AuditAgencySelectionEntity agencySelectionEntity) {
		if (agencySelectionEntity == null) {
            return Optional.empty();
        }

        try {
        	AuditAgencySelectionEntity savedAuditAgencySelectionEntity = agencySelectionRepository.save(agencySelectionEntity);
            return Optional.of(savedAuditAgencySelectionEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
}

	@Override
	public void changedTheApplicantStatus(StatusChangedOfLicense changedOfLicense) {
	    String uriTemplate = apigatewayServiceUrl + "/license-issuance-service/change-license-status";

	    restTemplate.postForObject(uriTemplate, changedOfLicense, String.class);
	}


	@Override
	public List<AuditAgencySelectionEntity> getAllServiceMaster() {
		// TODO Auto-generated method stub
		return agencySelectionRepository.findAll();
	}

	@Override
	public AuditAgencySelectionEntity findbyAuditId(LicenseeAuditEntity appAuditId) {
		// TODO Auto-generated method stub
		return agencySelectionRepository.findbyAuditId(appAuditId);
	}

	@Override
	public List<AuditAgencySelectionEntity> getAllAuditId(Long appAuditId) {
		// TODO Auto-generated method stub
		return agencySelectionRepository. getAllAuditId(appAuditId);
	}

	@Override
	public Optional<AuditAgencySelectionEntity> upDateData(AuditAgencySelectionEntity selectionEntity) {
		if (selectionEntity == null) {
            return Optional.empty();
        }
		if (selectionEntity.getAgencySelectionId() == null) {
            return Optional.empty();
        }

        try {
        	AuditAgencySelectionEntity savedAuditAgencySelectionEntity = agencySelectionRepository.save(selectionEntity);
            return Optional.of(savedAuditAgencySelectionEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}

	@Override
	public List<AuditAgencySelectionEntity> getAllDetailsByAuditAgencyId(String userId) {
		// TODO Auto-generated method stub
		return agencySelectionRepository.getAllDetailsByAuditId(userId);
	}



}
