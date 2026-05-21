package in.lms.cca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.Notifications;
import in.lms.cca.repository.NotificationRepository;
import in.lms.cca.service.INotificationService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class NotificationServiceImpl implements INotificationService{

	@Autowired
	private NotificationRepository notificationRepo;
	
	@Override
	public Notifications saveNotification(Notifications notification) {
		
		return notificationRepo.save(notification);
		
	}

	@Override
	public List<Notifications> getNotificationByUsernameAndRole(String username, String role) {
		
		return notificationRepo.findNotificationByUsernameAndRole(username, role);
	}

	@Override
	public void readNotificationByUsernameAndRole(String id, String role) {
		
		 notificationRepo.readNotificationByUsernameAndRole(id, role);
		
	}

	@Override
	public Notifications getNotificationById(Long id) {
		return notificationRepo.findNotificationById(id);
		
	}

}
