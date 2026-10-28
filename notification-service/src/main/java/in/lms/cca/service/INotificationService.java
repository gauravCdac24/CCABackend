package in.lms.cca.service;

import java.util.List;

import in.lms.cca.entity.Notifications;

public interface INotificationService {

	Notifications saveNotification(Notifications notification);

	List<Notifications> getNotificationByUsernameAndRole(String username, String role);

	void readNotificationByUsernameAndRole(String id, String role);

	Notifications getNotificationById(Long id);
	
}
