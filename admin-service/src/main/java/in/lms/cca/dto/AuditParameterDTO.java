package in.lms.cca.dto;

public class AuditParameterDTO {

	private String auditParameterId;
	private String auditSubCriteriaId;
	private String auditParameterTitle;
	private String status;
	private String createdBy;
	private String updatedBy;
	private String created;
	
	
	public String getAuditParameterId() {
		return auditParameterId;
	}
	public void setAuditParameterId(String auditParameterId) {
		this.auditParameterId = auditParameterId;
	}
	public String getAuditSubCriteriaId() {
		return auditSubCriteriaId;
	}
	public void setAuditSubCriteriaId(String auditSubCriteriaId) {
		this.auditSubCriteriaId = auditSubCriteriaId;
	}
	public String getAuditParameterTitle() {
		return auditParameterTitle;
	}
	public void setAuditParameterTitle(String auditParameterTitle) {
		this.auditParameterTitle = auditParameterTitle;
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
