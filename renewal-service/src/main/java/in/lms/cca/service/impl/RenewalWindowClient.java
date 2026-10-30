package in.lms.cca.service.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.RenewalWindowView;

@Component
public class RenewalWindowClient {

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	public RenewalWindowView window(String username) {
		URI uri = UriComponentsBuilder
				.fromUriString(apigatewayServiceUrl + "/api/license-issuance-service/renewal-window-by-username")
				.queryParam("id", username)
				.build()
				.toUri();
		return restTemplate.getForObject(uri, RenewalWindowView.class);
	}

	public ResponseEntity<String> get(String path) {
		String url = apigatewayServiceUrl + "/api/license-issuance-service" + path;
		return restTemplate.exchange(url, HttpMethod.GET, null, String.class);
	}

	public ResponseEntity<String> post(String path, Object body) {
		String url = apigatewayServiceUrl + "/api/license-issuance-service" + path;
		return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), String.class);
	}
}
