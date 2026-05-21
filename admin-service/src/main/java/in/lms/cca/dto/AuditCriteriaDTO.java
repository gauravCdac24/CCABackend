package in.lms.cca.dto;

public class AuditCriteriaDTO {
	
	private String auditCriteriaId;
	private String auditCriteriaTitle;
	private String status;
	private String createdBy;
	private String updatedBy;
	private String created;
	
	
	public String getAuditCriteriaId() {
		return auditCriteriaId;
	}
	public void setAuditCriteriaId(String auditCriteriaId) {
		this.auditCriteriaId = auditCriteriaId;
	}
	public String getAuditCriteriaTitle() {
		return auditCriteriaTitle;
	}
	public void setAuditCriteriaTitle(String auditCriteriaTitle) {
		this.auditCriteriaTitle = auditCriteriaTitle;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	
	
	

}
