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
@Table(name = "audit_report_document",schema="annual_audit_management")

public class AuditReportDocumentEntity extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_doc_id",length = 11)
	
	 private Long criteriaDocId;
	 @ManyToOne
	    @JoinColumn(name = "licensee_audit_id", referencedColumnName = "licensee_audit_id")
	    private LicenseeAuditEntity licenseeAuditId;
	  @Column(name = "document",length = 100)
	    private String document;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;

	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;

	@Column(name="status",length = 15)
	       private String status;
		public Long getCriteriaDocId() {
			return criteriaDocId;
		}
		public void setCriteriaDocId(Long criteriaDocId) {
			this.criteriaDocId = criteriaDocId;
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
		public LicenseeAuditEntity getLicenseeAuditId() {
			return licenseeAuditId;
		}
		public void setLicenseeAuditId(LicenseeAuditEntity licenseeAuditId) {
			this.licenseeAuditId = licenseeAuditId;
		}
		@Override
		public String toString() {
			return "AuditReportDocumentEntity [criteriaDocId=" + criteriaDocId + ", licenseeAuditId=" + licenseeAuditId
					+ ", document=" + document + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", remarks="
					+ remarks + ", status=" + status + "]";
		}
		
	    
	    
}
