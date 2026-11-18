package in.lms.cca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.NotificationType;
import in.lms.cca.repository.NotificationTypeRepository;
import in.lms.cca.service.INotificationTypeService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class NotificationTypeServiceImpl implements INotificationTypeService{

	@Autowired
	private NotificationTypeRepository notificationTypeRepo;
	
	@Override
	public NotificationType saveNotificationType(NotificationType notificationType) {

		return notificationTypeRepo.save(notificationType);
	}

	@Override
	public List<NotificationType> getAllNotifications() {
		return notificationTypeRepo.findAll(Sort.by(Sort.Direction.DESC,"created"));
	}

	@Override
	public NotificationType getNotificationTypeByName(String name) {
	
		return notificationTypeRepo.findNotificationTypeByName(name);
	}

	
	
	
}
