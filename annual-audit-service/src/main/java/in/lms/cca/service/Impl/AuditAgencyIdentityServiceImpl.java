package in.lms.cca.service.Impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.repository.AuditAgencyDirectoryRepository;
import in.lms.cca.service.AuditAgencyIdentityService;
import in.lms.cca.util.global.EncryptionUtil;

@Service
public class AuditAgencyIdentityServiceImpl implements AuditAgencyIdentityService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AuditAgencyDirectoryRepository auditAgencyDirectoryRepository;

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;

	@Override
	public String resolveAuditAgencyId(String agencyLoginOrId) {
		if (agencyLoginOrId == null || agencyLoginOrId.isBlank()) {
			return null;
		}

		String candidate = agencyLoginOrId.trim();
		if (isNumericId(candidate)) {
			return candidate;
		}

		String encryptedUsername = EncryptionUtil.encrypt(candidate);
		if (encryptedUsername != null && !encryptedUsername.isBlank()) {
			Optional<Long> byCreatedBy =
					auditAgencyDirectoryRepository.findActiveIdByCreatedBy(encryptedUsername);
			if (byCreatedBy.isPresent()) {
				return String.valueOf(byCreatedBy.get());
			}
		}

		String firstName = lookupFirstName(candidate);
		if (firstName != null && !firstName.isBlank()) {
			Optional<Long> byName =
					auditAgencyDirectoryRepository.findActiveIdByAgencyName(firstName.trim());
			if (byName.isPresent()) {
				return String.valueOf(byName.get());
			}
			String fromAdmin = lookupAgencyIdByName(firstName.trim());
			if (fromAdmin != null) {
				return fromAdmin;
			}
		}

		String fromAdminUsername = lookupAgencyIdByUsername(candidate);
		if (fromAdminUsername != null) {
			return fromAdminUsername;
		}

		return candidate;
	}

	private String lookupAgencyIdByUsername(String username) {
		String encrypted = EncryptionUtil.encrypt(username);
		if (encrypted == null || encrypted.isBlank()) {
			return null;
		}
		try {
			String url = apigatewayServiceUrl + "/api/admin-service/get-audit-agency-by-username/" + encrypted;
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			return extractAgencyId(response);
		} catch (Exception ex) {
			return null;
		}
	}

	private String lookupFirstName(String username) {
		try {
			String url = UriComponentsBuilder
					.fromHttpUrl(apigatewayServiceUrl + "/api/auth-service/get-all-detail-by-username/" + username)
					.toUriString();
			ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				Object firstName = response.getBody().get("firstName");
				return firstName == null ? null : String.valueOf(firstName);
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

	private String lookupAgencyIdByName(String agencyName) {
		try {
			String url = apigatewayServiceUrl + "/api/admin-service/get-audit-agency-by-name/" + agencyName;
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			return extractAgencyId(response);
		} catch (Exception ex) {
			return null;
		}
	}

	private String extractAgencyId(ResponseEntity<String> response) {
		if (response == null || response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
			return null;
		}
		String body = response.getBody().trim();
		if (body.isEmpty() || body.toLowerCase().contains("no audit agency found")) {
			return null;
		}
		return isNumericId(body) ? body : null;
	}

	private boolean isNumericId(String value) {
		if (value == null || value.isBlank()) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isDigit(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
