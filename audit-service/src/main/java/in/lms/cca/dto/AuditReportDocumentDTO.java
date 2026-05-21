package in.lms.cca.dto;

public class AuditReportDocumentDTO {
	 private String criteriaDocId;
	    private String appAuditId;
	    private String document;
	    private String status;
	    private String created;
	    private String updated;
	    private String createdBy; // Encrypted
	    private String updatedBy; // Encrypted
		public String getCriteriaDocId() {
			return criteriaDocId;
		}
		public void setCriteriaDocId(String criteriaDocId) {
			this.criteriaDocId = criteriaDocId;
		}
		public String getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(String appAuditId) {
			this.appAuditId = appAuditId;
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
			return "AuditReportDocumentDTO [criteriaDocId=" + criteriaDocId + ", appAuditId=" + appAuditId
					+ ", document=" + document + ", status=" + status + ", created=" + created + ", updated=" + updated
					+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    
	    
}
