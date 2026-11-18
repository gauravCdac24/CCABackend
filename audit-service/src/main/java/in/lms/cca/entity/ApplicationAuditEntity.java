package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_audit",schema="audit_management")

public class ApplicationAuditEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_audit_id",length = 11)
    private Long appAuditId;
	
	@Column(name = "intent_app_id", columnDefinition = "TEXT")
    private String intentAppId;
	  
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "review_by",length = 74)
    private String reviewBy;
    
    @Column(name = "audit_initiated_on")
    private Date auditInitiatedOn;
    
    @Column(name = "audit_completed_on")
    private Date auditCompletedOn;
   
    @Column(name = "audit_type", length = 100)
    private String auditType;
    
    @Column(name="status",length = 15)
    private String status;
    
    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by",length = 74)
    private String updatedBy;

    @Column(name = "applicant_userName",length = 74)
    private String applicantUserName;
    
    @Column(name = "applicant_remarks", columnDefinition = "TEXT")
    private String applicantRemarks;

	/** NONE / DOWNLOADED / UPLOADED / SIGNED — applicant audit acceptance letter */
	@Column(name = "acceptance_letter_status", length = 20)
	private String acceptanceLetterStatus;

	@Column(name = "acceptance_letter_file", length = 150)
	private String acceptanceLetterFile;

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

	public String getAcceptanceLetterStatus() {
		return acceptanceLetterStatus;
	}

	public void setAcceptanceLetterStatus(String acceptanceLetterStatus) {
		this.acceptanceLetterStatus = acceptanceLetterStatus;
	}

	public String getAcceptanceLetterFile() {
		return acceptanceLetterFile;
	}

	public void setAcceptanceLetterFile(String acceptanceLetterFile) {
		this.acceptanceLetterFile = acceptanceLetterFile;
	}

}