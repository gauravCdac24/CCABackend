package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.payload.NotificationsDetailsRequest;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.service.INotificationClientService;

@Service
public class NotificationClientServiceImpl implements INotificationClientService{

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Override
	public String sendNotification(NotificationsRequest notification) {
		
		String uriTemplate = apigatewayServiceUrl + "/notification-service/send-notification";
		
		ResponseEntity<String> response = restTemplate.postForEntity(uriTemplate, notification, String.class);

		return response.getBody();
		
	}

	@Override
	public String sendNotificationUsingDetails(NotificationsDetailsRequest notification) {
		
		
		String uriTemplate = apigatewayServiceUrl + "/notification-service/send-notification-by-details";
		
		ResponseEntity<String> response = restTemplate.postForEntity(uriTemplate, notification, String.class);

		return response.getBody();

		
		
	}
	
	

	
	
}
