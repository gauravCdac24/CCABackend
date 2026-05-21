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
@Table(name = "application_action_taken",schema="annual_audit_management")
public class ApplicantActionTakenEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_taken_id",length = 11)
	    private Long actionTakenId;
	
	@ManyToOne
    @JoinColumn(name = "audit_nc_id", referencedColumnName = "audit_nc_id")
	
	    private AuditNCSEntity auditNCId;
	    
	    @Column(name = "action_report",length = 30)
	    private String actionReport;
	    
	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;



	@Column(name="status",length = 15)
	       private String status;
	    
	    
		public Long getActionTakenId() {
			return actionTakenId;
		}
		public void setActionTakenId(Long actionTakenId) {
			this.actionTakenId = actionTakenId;
		}
		
		public AuditNCSEntity getAuditNCId() {
			return auditNCId;
		}
		public void setAuditNCId(AuditNCSEntity auditNCId) {
			this.auditNCId = auditNCId;
		}
		public String getActionReport() {
			return actionReport;
		}
		public void setActionReport(String actionReport) {
			this.actionReport = actionReport;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
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
		@Override
		public String toString() {
			return "ApplicantActionTakenEntity [actionTakenId=" + actionTakenId + ", auditNCId=" + auditNCId
					+ ", actionReport=" + actionReport + ", remarks=" + remarks + ", status=" + status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    
	    
}
