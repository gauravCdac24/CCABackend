package in.lms.cca.service;

import java.util.List;

import in.lms.cca.entity.NotificationType;

public interface INotificationTypeService {

	NotificationType saveNotificationType(NotificationType notificationType);
	List<NotificationType> getAllNotifications();
	NotificationType getNotificationTypeByName(String name);
}
