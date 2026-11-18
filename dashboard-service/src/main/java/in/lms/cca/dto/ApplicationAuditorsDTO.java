package in.lms.cca.dto;

import java.util.Date;

public class ApplicationAuditorsDTO{

	private Long appAuditorId;
    private ApplicationAuditDTO appAuditId;
    private String fullName;
    private String certificationType;
    private String reviewBy;
    private String remarks;
    private Date certificateExpiry;
    private String certificateDocument;
    private String undertakingDocument;
    private String createdBy;
    private String updatedBy;
    private String status;
    private Date created;
    private Date updated;
    
    
	public Long getAppAuditorId() {
		return appAuditorId;
	}
	public void setAppAuditorId(Long appAuditorId) {
		this.appAuditorId = appAuditorId;
	}
	public ApplicationAuditDTO getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(ApplicationAuditDTO appAuditId) {
		this.appAuditId = appAuditId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getCertificationType() {
		return certificationType;
	}
	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}
	public String getReviewBy() {
		return reviewBy;
	}
	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getCertificateExpiry() {
		return certificateExpiry;
	}
	public void setCertificateExpiry(Date certificateExpiry) {
		this.certificateExpiry = certificateExpiry;
	}
	public String getCertificateDocument() {
		return certificateDocument;
	}
	public void setCertificateDocument(String certificateDocument) {
		this.certificateDocument = certificateDocument;
	}
	public String getUndertakingDocument() {
		return undertakingDocument;
	}
	public void setUndertakingDocument(String undertakingDocument) {
		this.undertakingDocument = undertakingDocument;
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