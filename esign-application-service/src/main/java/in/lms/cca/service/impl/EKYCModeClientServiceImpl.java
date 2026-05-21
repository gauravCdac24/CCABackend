package in.lms.cca.service.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.EKYCModeClientDTO;
import in.lms.cca.service.IeKYCModeClientService;
import in.lms.cca.util.golbal.EncryptionUtil;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EKYCModeClientServiceImpl implements IeKYCModeClientService{

	@Autowired
	private RestTemplate restTemplate;

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;

	@Override
	public List<EKYCModeClientDTO> getAlleKYCModes() {
		
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-all-ekyc-mode";
	    
	    URI uri = URI.create(uriTemplate);
	    EKYCModeClientDTO[] responseArray = restTemplate.getForObject(uri, EKYCModeClientDTO[].class);
	    return responseArray != null ? Arrays.asList(responseArray) : Collections.emptyList();
	}

	@Override
	public EKYCModeClientDTO getEKYCModeById(Long id) {
		
		String uriTemplate = apigatewayServiceUrl + "/admin-service/get-ekyc-mode-by-id";
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
                .queryParam("id", EncryptionUtil.encrypt(id.toString()));
            

		URI uri = builder.build().toUri();
		
		return restTemplate.getForObject(uri, EKYCModeClientDTO.class);

	}

	
}
