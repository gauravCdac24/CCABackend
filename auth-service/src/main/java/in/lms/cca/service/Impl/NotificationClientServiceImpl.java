package in.lms.cca.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.payload.NotificationsDetailsRequest;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.service.INotificationClientService;

@Service
public class NotificationClientServiceImpl implements INotificationClientService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;

	@Value("${notification.service.url:}")
	private String notificationServiceUrl;

	private String notificationBase() {
		if (notificationServiceUrl != null && !notificationServiceUrl.isBlank()) {
			return notificationServiceUrl.replaceAll("/$", "") + "/notification-service";
		}
		return apigatewayServiceUrl.replaceAll("/$", "") + "/api/notification-service";
	}

	@Override
	public String sendNotification(NotificationsRequest notification) {
		String uri = notificationBase() + "/send-notification";
		System.out.println("Calling notification: " + uri);
		ResponseEntity<String> response = restTemplate.postForEntity(uri, notification, String.class);
		return response.getBody();
	}

	@Override
	public String sendNotificationUsingDetails(NotificationsDetailsRequest notification) {
		String uri = notificationBase() + "/send-notification-by-details";
		System.out.println("Calling notification-by-details: " + uri);
		ResponseEntity<String> response = restTemplate.postForEntity(uri, notification, String.class);
		return response.getBody();
	}

}
