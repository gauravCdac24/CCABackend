package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_auditors",schema="audit_management")

public class ApplicationAuditorsEntity extends AbstractTimestampEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "app_auditor_id",length = 11)
    private Long appAuditorId;
	 
	 @ManyToOne
	    @JoinColumn(name = "app_audit_id", referencedColumnName = "app_audit_id")
    private ApplicationAuditEntity appAuditId;
	 @Column(name = "full_name",length = 50)
    private String fullName;
	 @Column(name = "certification_type",length = 11)
    private String certificationType;
	 @Column(name = "reviewed_by",length = 75)
    private String reviewBy;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "certification_expiry")
    private Date certificateExpiry;
    
    @Column(name = "certification_document",length = 100)
    private String certificateDocument;
    
    @Column(name = "undertaking_document",length = 100)
    private String undertakingDocument;
    
    
    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by",length = 74)
    private String updatedBy;

       @Column(name="status",length = 15)
       private String status;
	public Long getAppAuditorId() {
		return appAuditorId;
	}
	public void setAppAuditorId(Long appAuditorId) {
		this.appAuditorId = appAuditorId;
	}
	public ApplicationAuditEntity getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(ApplicationAuditEntity appAuditId) {
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
	
	public String getUndertakingDocument() {
		return undertakingDocument;
	}
	public void setUndertakingDocument(String undertakingDocument) {
		this.undertakingDocument = undertakingDocument;
	}
	@Override
	public String toString() {
		return "ApplicationAuditorsEntity [appAuditorId=" + appAuditorId + ", appAuditId=" + appAuditId + ", fullName="
				+ fullName + ", certificationType=" + certificationType + ", reviewBy=" + reviewBy + ", remarks="
				+ remarks + ", certificateExpiry=" + certificateExpiry + ", certificateDocument=" + certificateDocument
				+ ", undertakingDocument=" + undertakingDocument + ", createdBy=" + createdBy + ", updatedBy="
				+ updatedBy + ", status=" + status + "]";
	}
	
	
    
}