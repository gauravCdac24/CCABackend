package in.lms.cca.dto;

public class AuditAgencyEmailDTO {
	private String auditAgencyEmailId;
	private String emailType;
	private String email;
	private String status;
	private String created;
	private String updated;
	public String getAuditAgencyEmailId() {
		return auditAgencyEmailId;
	}
	public void setAuditAgencyEmailId(String auditAgencyEmailId) {
		this.auditAgencyEmailId = auditAgencyEmailId;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	
}
