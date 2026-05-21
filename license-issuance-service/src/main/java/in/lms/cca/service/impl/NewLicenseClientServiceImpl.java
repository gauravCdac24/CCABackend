package in.lms.cca.service.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.service.INewLicenseClientService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NewLicenseClientServiceImpl implements INewLicenseClientService{

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String safeGet(URI uri) {
		try {
			return restTemplate.getForObject(uri, String.class);
		} catch (Exception ex) {
			System.err.println("Optional license-issuance downstream call failed: " + uri + " -> " + ex.getMessage());
			return null;
		}
	}

	@Override
	public String changeIntentApplicationStatus(String intentId, String status, String initiatedBy) {
		String uriTemplate = apigatewayServiceUrl + "/newlicense-service/change-intent-application-status";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate).queryParam("id", intentId)
				.queryParam("status", status).queryParam("initiatedby", initiatedBy);

		URI uri = builder.build().toUri();

		try {
			return restTemplate.getForObject(uri, String.class);
		} catch (Exception ex) {
			throw new IllegalStateException(
					"change-intent-application-status failed for intentAppId=" + intentId + ": " + ex.getMessage(), ex);
		}
	}

	@Override
	public String changeApplicantRoleToLicensee(String ausername) {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/change-applicant-role-to-licensee";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate).queryParam("username",
				ausername);
		return safeGet(builder.build().toUri());
	}

	@Override
	public String changeAnnexureDetails(String ausername) {
		String uriTemplate = apigatewayServiceUrl + "/audit-service/change-annexure-details";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate).queryParam("username",
				ausername);
		return safeGet(builder.build().toUri());
	}
	
	

}
