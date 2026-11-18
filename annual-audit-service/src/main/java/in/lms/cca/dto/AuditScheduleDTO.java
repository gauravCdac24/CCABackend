package in.lms.cca.dto;

public class AuditScheduleDTO {

    private String auditScheduleId;
    private String appAuditId;
    private String auditType;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
	public String getAuditScheduleId() {
		return auditScheduleId;
	}
	public void setAuditScheduleId(String auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
	}
	public String getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(String appAuditId) {
		this.appAuditId = appAuditId;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
		return "AuditScheduleDTO [auditScheduleId=" + auditScheduleId + ", appAuditId=" + appAuditId + ", auditType="
				+ auditType + ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", status=" + status + ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}
    
    

}
