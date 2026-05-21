package in.lms.cca.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.util.golbal.EncryptionUtil;



@Service
public class ClientService{

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	


	public String eSignDocument(String url, String oid, String reqid, String token) {
		
		try {
			
		String uriTemplate = apigatewayServiceUrl + url;
		
	
		
		String eSignDocId = oid;
		String  signedDocument = reqid;
				
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate)
                .queryParam("id", eSignDocId)
				.queryParam("did", signedDocument);
            

		URI uri = builder.build().toUri();
		
		//Set header
		 HttpHeaders headers = new HttpHeaders();
	        if (token != null) {
	            headers.set("Authorization", token);
	        }

	     // Create HTTP entity with headers
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	    
	     // Execute request with headers
	        restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		
		
		
		return "Success";
		
		}catch (RestClientException ex) {
	        ex.printStackTrace();
	        throw new RuntimeException("Failed to eSign the document.");
	    }
		
	}

	
}
