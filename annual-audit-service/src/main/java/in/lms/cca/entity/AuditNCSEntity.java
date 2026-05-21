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
@Table(name = "audit_ncs",schema="annual_audit_management")

public class AuditNCSEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_nc_id",length = 11)
	    private Long auditNCId;
	
	 @ManyToOne
	    @JoinColumn(name = "licensee_audit_id", referencedColumnName = "licensee_audit_id")
	    private LicenseeAuditEntity licenseeAuditId;
	 
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
		
		public LicenseeAuditEntity getLicenseeAuditId() {
			return licenseeAuditId;
		}
		public void setLicenseeAuditId(LicenseeAuditEntity licenseeAuditId) {
			this.licenseeAuditId = licenseeAuditId;
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
			return "AuditNCSEntity [auditNCId=" + auditNCId + ", licenseeAuditId=" + licenseeAuditId + ", ncs=" + ncs
					+ ", remarks=" + remarks + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", status="
					+ status + "]";
		}
		
	    
	    
}
