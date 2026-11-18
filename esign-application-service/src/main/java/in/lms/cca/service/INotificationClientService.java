package in.lms.cca.service;

import in.lms.cca.payload.NotificationsDetailsRequest;
import in.lms.cca.payload.NotificationsRequest;

public interface INotificationClientService {

	String sendNotification(NotificationsRequest notification);
	String sendNotificationUsingDetails(NotificationsDetailsRequest notification);
}
