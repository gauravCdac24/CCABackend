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
@Table(name = "audit_report-criteria",schema="audit_management")

public class AuditReportCriteriaEntity extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_id",length = 11)
	
	 private Long criteriaId;
	
	 
		@Column(name="audit_control_id",length = 30)
	    private String auditControlId;
	 
	 @ManyToOne
	    @JoinColumn(name = "app_audit_id", referencedColumnName = "app_audit_id")
	    private ApplicationAuditEntity appAuditId;
	 
	 @Column(name = "compliance", length = 50)
	    private String compliance;
	 
	 @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	 
	 @Column(name = "document", length = 100)
	    private String document;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;



	@Column(name="status",length = 15)
	       private String status;
		
		public Long getCriteriaId() {
			return criteriaId;
		}
		public void setCriteriaId(Long criteriaId) {
			this.criteriaId = criteriaId;
		}
		public String getAuditControlId() {
			return auditControlId;
		}
		public void setAuditControlId(String auditControlId) {
			this.auditControlId = auditControlId;
		}
		public ApplicationAuditEntity getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(ApplicationAuditEntity appAuditId) {
			this.appAuditId = appAuditId;
		}
		public String getCompliance() {
			return compliance;
		}
		public void setCompliance(String compliance) {
			this.compliance = compliance;
		}
		public String getDocument() {
			return document;
		}
		public void setDocument(String document) {
			this.document = document;
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
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		@Override
		public String toString() {
			return "AuditReportCriteriaEntity [criteriaId=" + criteriaId + ", auditControlId=" + auditControlId
					+ ", appAuditId=" + appAuditId + ", compliance=" + compliance + ", remarks=" + remarks
					+ ", document=" + document + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", status="
					+ status + "]";
		}
	    
	    

}
