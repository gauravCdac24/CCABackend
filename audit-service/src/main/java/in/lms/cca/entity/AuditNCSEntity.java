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
@Table(name = "audit_ncs",schema="audit_management")

public class AuditNCSEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_nc_id",length = 11)
	    private Long auditNCId;
	
	 @ManyToOne
	    @JoinColumn(name = "app_audit_id", referencedColumnName = "app_audit_id")
	    private ApplicationAuditEntity appAuditId;
	 
	 @Column(name = "ncs",length = 250)
	    private String ncs; 
	    
	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;



	@Column(name="status",length = 15)
	       private String status;
		public Long getAuditNCId() {
			return auditNCId;
		}
		public void setAuditNCId(Long auditNCId) {
			this.auditNCId = auditNCId;
		}
		public ApplicationAuditEntity getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(ApplicationAuditEntity appAuditId) {
			this.appAuditId = appAuditId;
		}
		public String getNcs() {
			return ncs;
		}
		public void setNcs(String ncs) {
			this.ncs = ncs;
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
			return "AuditNCSEntity [auditNCId=" + auditNCId + ", appAuditId=" + appAuditId + ", ncs=" + ncs + ", remarks="
					+ remarks + ", status=" + status + ", createdBy="
					+ createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    
	    
}
