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
@Table(name = "audit_schedule",schema="annual_audit_management")

public class AuditScheduleEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_schedule_id",length = 11)
    private Long auditScheduleId;
	
	 @ManyToOne
	    @JoinColumn(name = "licensee_audit_id", referencedColumnName = "licensee_audit_id")
	    private LicenseeAuditEntity licenseeAuditId;
	 
	 @Column(name = "audit_type",length = 50)
    private String auditType;
	 
	 @Column(name = "description", columnDefinition = "TEXT")
    private String description;
	 
		@Column(name = "start_date")
    private Date startDate;
		@Column(name = "end_date")
    private Date endDate;
    
		 @Column(name = "created_by", length = 74)
		    private String createdBy;

		    @Column(name = "updated_by",length = 74)
		    private String updatedBy;



		@Column(name="status",length = 15)
		       private String status;
	public Long getAuditScheduleId() {
		return auditScheduleId;
	}
	public void setAuditScheduleId(Long auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public LicenseeAuditEntity getLicenseeAuditId() {
		return licenseeAuditId;
	}
	public void setLicenseeAuditId(LicenseeAuditEntity licenseeAuditId) {
		this.licenseeAuditId = licenseeAuditId;
	}
	@Override
	public String toString() {
		return "AuditScheduleEntity [auditScheduleId=" + auditScheduleId + ", licenseeAuditId=" + licenseeAuditId
				+ ", auditType=" + auditType + ", description=" + description + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", status="
				+ status + "]";
	}
	

}
