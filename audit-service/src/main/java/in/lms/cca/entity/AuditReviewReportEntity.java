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
@Table(name = "auditor_review_report_entity",schema="audit_management")

public class AuditReviewReportEntity extends AbstractTimestampEntity {
	
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "auditor_review_report_id",length = 11)
		
	    private Long auditorReviewReportId;
	    
	    @ManyToOne
	    @JoinColumn(name = "short_coming_id", referencedColumnName = "short_coming_id")
		
	    private AuditShortComingsEntity shortcomingId;
	    @Column(name = "auditor_review_report", length = 74)
	    private String auditorReviewReport;
	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;

	      @Column(name="status",length = 15)
	       private String status;

		public Long getAuditorReviewReportId() {
			return auditorReviewReportId;
		}

		public void setAuditorReviewReportId(Long auditorReviewReportId) {
			this.auditorReviewReportId = auditorReviewReportId;
		}

		public AuditShortComingsEntity getShortcomingId() {
			return shortcomingId;
		}

		public void setShortcomingId(AuditShortComingsEntity shortcomingId) {
			this.shortcomingId = shortcomingId;
		}

		public String getAuditorReviewReport() {
			return auditorReviewReport;
		}

		public void setAuditorReviewReport(String auditorReviewReport) {
			this.auditorReviewReport = auditorReviewReport;
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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	      
	      
	      

}
