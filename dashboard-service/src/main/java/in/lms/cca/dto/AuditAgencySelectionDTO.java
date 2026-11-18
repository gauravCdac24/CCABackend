package in.lms.cca.dto;

import java.util.Date;

public class AuditAgencySelectionDTO{

    private Long agencySelectionId;
    private String auditAgencyId;
    private ApplicationAuditDTO appAuditId;
    private String reviewedBy;
    private String remarks;
    private String createdBy;
    private String updatedBy;
    private String status;
    private Date created;
    private Date updated;
    
    
	public Long getAgencySelectionId() {
		return agencySelectionId;
	}
	public void setAgencySelectionId(Long agencySelectionId) {
		this.agencySelectionId = agencySelectionId;
	}
	public String getAuditAgencyId() {
		return auditAgencyId;
	}
	public void setAuditAgencyId(String auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}
	public ApplicationAuditDTO getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(ApplicationAuditDTO appAuditId) {
		this.appAuditId = appAuditId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
    
    
    
  
}