package in.lms.cca.payload;


public class NotificationsRequest {

	private String notificationType;
	private String username;
	private String subject;
	private String message;
	private String notificationDescription;
	private String role;
	
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNotificationDescription() {
		return notificationDescription;
	}
	public void setNotificationDescription(String notificationDescription) {
		this.notificationDescription = notificationDescription;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	
	
	
	

	/** Matches sms.templates.<key> in notification-service. */
	private String smsTemplateKey;
	/** Values for {#var#}/{#num#} in order. */
	private java.util.List<String> smsVars;

	public String getSmsTemplateKey() {
		return smsTemplateKey;
	}
	public void setSmsTemplateKey(String smsTemplateKey) {
		this.smsTemplateKey = smsTemplateKey;
	}
	public java.util.List<String> getSmsVars() {
		return smsVars;
	}
	public void setSmsVars(java.util.List<String> smsVars) {
		this.smsVars = smsVars;
	}

}
