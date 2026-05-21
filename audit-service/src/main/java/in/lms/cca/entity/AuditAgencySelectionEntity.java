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
@Table(name = "audit_agency_selection",schema="audit_management")
public class AuditAgencySelectionEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agency_selection_id",length = 11)
    private Long agencySelectionId;
    
    @Column(name = "audit_agency_id",length =11)
    private String auditAgencyId;
    
    @ManyToOne
    @JoinColumn(name = "app_audit_id", referencedColumnName = "app_audit_id")
    private ApplicationAuditEntity appAuditId;
    
    @Column(name = "reviewed_by",length = 74)
    private String reviewedBy;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by",length = 74)
    private String updatedBy;



@Column(name="status",length = 15)
       private String status;
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
	public ApplicationAuditEntity getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(ApplicationAuditEntity appAuditId) {
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
		return "AuditAgencySelectionEntity [agencySelectionId=" + agencySelectionId + ", auditAgencyId=" + auditAgencyId
				+ ", appAuditId=" + appAuditId + ", status=" + status + ", reviewedBy=" + reviewedBy + ", remarks="
				+ remarks + ",  createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}

  
}