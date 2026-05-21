package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.Notifications;

public interface NotificationRepository extends JpaRepository<Notifications, Long>{

	@Query("FROM Notifications a WHERE a.username = :username AND a.role = :role AND (a.notificationTypeId.notificationTypeName = 'Dashboard' OR a.notificationTypeId.notificationTypeName = 'Email & Dashboard' OR a.notificationTypeId.notificationTypeName = 'SMS & Dashboard' OR a.notificationTypeId.notificationTypeName = 'All') ORDER BY created DESC")
	List<Notifications> findNotificationByUsernameAndRole(String username, String role);

	@Modifying
	@Query("UPDATE Notifications a SET a.isread = true WHERE a.username = :username AND a.role = :role")
	void readNotificationByUsernameAndRole(@Param("username") String username, @Param("role") String role);

	@Query("FROM Notifications WHERE notificationId = :id")
	Notifications findNotificationById(Long id);


}
