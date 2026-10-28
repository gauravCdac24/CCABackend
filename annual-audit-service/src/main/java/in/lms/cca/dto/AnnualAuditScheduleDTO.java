package in.lms.cca.dto;

public class AnnualAuditScheduleDTO {
	
	
	private String auditScheduleId;
    private String licenseId;
    private String startDate;
    private String status;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
    private String userName;
    
    
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAuditScheduleId() {
		return auditScheduleId;
	}
	public void setAuditScheduleId(String auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
	}
	public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
	@Override
	public String toString() {
		return "AnnualAuditScheduleDTO [auditScheduleId=" + auditScheduleId + ", licenseId=" + licenseId
				+ ", startDate=" + startDate + ", status=" + status + ", created=" + created + ", updated=" + updated
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", userName=" + userName + "]";
	}
	
    
    

}
