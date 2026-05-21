package in.lms.cca.dto;

public class AuditAgencySelectionDTO {

    private String agencySelectionId;
    private String auditAgencyId;
    private String appAuditId;
    private String status;
    private String reviewedBy;
    private String remarks;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
	public String getAgencySelectionId() {
		return agencySelectionId;
	}
	public void setAgencySelectionId(String agencySelectionId) {
		this.agencySelectionId = agencySelectionId;
	}
	public String getAuditAgencyId() {
		return auditAgencyId;
	}
	public void setAuditAgencyId(String auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}
	public String getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(String appAuditId) {
		this.appAuditId = appAuditId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReviewedBy() {
		return reviewedBy;
	}
	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
		return "AuditAgencySelectionDTO [agencySelectionId=" + agencySelectionId + ", auditAgencyId=" + auditAgencyId
				+ ", appAuditId=" + appAuditId + ", status=" + status + ", reviewedBy=" + reviewedBy + ", remarks="
				+ remarks + ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}

  
}