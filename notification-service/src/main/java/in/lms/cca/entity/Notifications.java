package in.lms.cca.entity;


import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "notifications", schema="notification_management")
public class Notifications extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id", length = 11)
	private Long notificationId;
	
	@ManyToOne
	@JoinColumn(name = "notification_type_id", referencedColumnName = "notification_type_id")
	private NotificationType notificationTypeId;

	
	@Column(name = "username", length = 74)
	private String username;
	
	@Column(name = "message")
	private String message;
	
	@Column(name = "isread")
	private boolean isread = false;
	
	@Column(name = "role", length = 100)
	private String role;
	

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public NotificationType getNotificationTypeId() {
		return notificationTypeId;
	}

	public void setNotificationTypeId(NotificationType notificationTypeId) {
		this.notificationTypeId = notificationTypeId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isIsread() {
		return isread;
	}

	public void setIsread(boolean isread) {
		this.isread = isread;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

		
}
