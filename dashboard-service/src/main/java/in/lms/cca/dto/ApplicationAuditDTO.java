package in.lms.cca.dto;

import java.util.Date;

public class ApplicationAuditDTO{

    private Long appAuditId;
    private String intentAppId;
    private String remarks;
    private String reviewBy;
    private Date auditInitiatedOn;
    private Date auditCompletedOn;
    private String auditType;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String applicantUserName;
    private String applicantRemarks;
    
    
	public Long getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(Long appAuditId) {
		this.appAuditId = appAuditId;
	}
	public String getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReviewBy() {
		return reviewBy;
	}
	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}
	public Date getAuditInitiatedOn() {
		return auditInitiatedOn;
	}
	public void setAuditInitiatedOn(Date auditInitiatedOn) {
		this.auditInitiatedOn = auditInitiatedOn;
	}
	public Date getAuditCompletedOn() {
		return auditCompletedOn;
	}
	public void setAuditCompletedOn(Date auditCompletedOn) {
		this.auditCompletedOn = auditCompletedOn;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
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
	public String getApplicantUserName() {
		return applicantUserName;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	public String getApplicantRemarks() {
		return applicantRemarks;
	}
	public void setApplicantRemarks(String applicantRemarks) {
		this.applicantRemarks = applicantRemarks;
	}
    
    
    
    
	
}
