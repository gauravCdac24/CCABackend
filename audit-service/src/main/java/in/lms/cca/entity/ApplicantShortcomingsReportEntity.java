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
@Table(name = "application_short_comings_report",schema="audit_management")

public class ApplicantShortcomingsReportEntity extends AbstractTimestampEntity{
	

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "short_coming_report_id",length = 11)
		
	    private Long shortcomingReportId;
	    
	    @ManyToOne
	    @JoinColumn(name = "short_coming_id", referencedColumnName = "short_coming_id")
		
	    private AuditShortComingsEntity shortcomingId;
	    @Column(name = "short_coming_report", length = 74)
	    private String shortcomingReport;
	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;

	@Column(name="status",length = 15)
	       private String status;
		public Long getShortcomingReportId() {
			return shortcomingReportId;
		}
		public void setShortcomingReportId(Long shortcomingReportId) {
			this.shortcomingReportId = shortcomingReportId;
		}
		public AuditShortComingsEntity getShortcomingId() {
			return shortcomingId;
		}
		public void setShortcomingId(AuditShortComingsEntity shortcomingId) {
			this.shortcomingId = shortcomingId;
		}
		public String getShortcomingReport() {
			return shortcomingReport;
		}
		public void setShortcomingReport(String shortcomingReport) {
			this.shortcomingReport = shortcomingReport;
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
			return "ApplicantShortcomingsReportEntity [shortcomingReportId=" + shortcomingReportId + ", shortcomingId="
					+ shortcomingId + ", shortcomingReport=" + shortcomingReport + ", remarks=" + remarks + ", status="
					+ status + ", createdBy=" + createdBy
					+ ", updatedBy=" + updatedBy + "]";
		}
		
	    

}
