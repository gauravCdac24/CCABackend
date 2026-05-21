package in.lms.cca.entity;

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
@Table(name = "application_audit",schema="annual_audit_management")

public class LicenseeAuditEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "licensee_audit_id",length = 11)
    private Long LicenseeAuditId;
	 @ManyToOne
	    @JoinColumn(name = "audit_schedule_id", referencedColumnName = "audit_schedule_id")
    private AnnualAuditScheduleEntity auditScheduleId;
	  
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "review_by",length = 74)
    private String reviewBy;
    
    
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
    

  
    public Long getLicenseeAuditId() {
		return LicenseeAuditId;
	}

	public void setLicenseeAuditId(Long licenseeAuditId) {
		LicenseeAuditId = licenseeAuditId;
	}

	public AnnualAuditScheduleEntity getAuditScheduleId() {
		return auditScheduleId;
	}

	public void setAuditScheduleId(AnnualAuditScheduleEntity auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
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

    
    
	public String getApplicantRemarks() {
		return applicantRemarks;
	}

	public void setApplicantRemarks(String applicantRemarks) {
		this.applicantRemarks = applicantRemarks;
	}

	public String getApplicantUserName() {
		return applicantUserName;
	}

	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}

	@Override
	public String toString() {
		return "LicenseeAuditEntity [LicenseeAuditId=" + LicenseeAuditId + ", auditScheduleId=" + auditScheduleId
				+ ", remarks=" + remarks + ", reviewBy=" + reviewBy + ", status=" + status + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", applicantUserName=" + applicantUserName + ", applicantRemarks="
				+ applicantRemarks + "]";
	}

	
	
}
