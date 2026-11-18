package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.NotificationType;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer>{

	@Query("FROM NotificationType WHERE notificationTypeName=:typeName")
	NotificationType findNotificationTypeByName(String typeName);
	
}
