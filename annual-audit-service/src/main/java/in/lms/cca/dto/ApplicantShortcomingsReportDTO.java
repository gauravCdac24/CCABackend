package in.lms.cca.dto;

public class ApplicantShortcomingsReportDTO {
	
	    private Long shortcomingReportId;
	    private Long shortcomingId;
	    private String shortcomingReport;
	    private String remarks;
	    private String status;
	    private String created;
	    private String updated;
	    private String createdBy; // Encrypted
	    private String updatedBy; // Encrypted
		public Long getShortcomingReportId() {
			return shortcomingReportId;
		}
		public void setShortcomingReportId(Long shortcomingReportId) {
			this.shortcomingReportId = shortcomingReportId;
		}
		public Long getShortcomingId() {
			return shortcomingId;
		}
		public void setShortcomingId(Long shortcomingId) {
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
		public String getCreated() {
			return created;
		}
		public void setCreated(String created) {
			this.created = created;
		}
		public String getUpdated() {
			return updated;
		}
		public void setUpdated(String updated) {
			this.updated = updated;
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
			return "ApplicantShortcomingsReportDTO [shortcomingReportId=" + shortcomingReportId + ", shortcomingId="
					+ shortcomingId + ", shortcomingReport=" + shortcomingReport + ", remarks=" + remarks + ", status="
					+ status + ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy
					+ ", updatedBy=" + updatedBy + "]";
		}
	    
	    

}
