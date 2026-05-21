package in.lms.cca.dto;



public class AuditReportCriteriaDTO {
	
	 private String criteriaId;
	    private String auditControlId;
	    private String appAuditId;
	    private String compliance; 
	    private String document;
	    private String status; 
	    private String created;
	    private String updated;
	    private String createdBy;
	    private String updatedBy;
		@Override
		public String toString() {
			return "AuditReportCriteriaDTO [criteriaId=" + criteriaId + ", auditControlId=" + auditControlId
					+ ", appAuditId=" + appAuditId + ", compliance=" + compliance + ", document=" + document
					+ ", status=" + status + ", created=" + created + ", updated=" + updated + ", createdBy="
					+ createdBy + ", updatedBy=" + updatedBy + "]";
		}
		public String getCriteriaId() {
			return criteriaId;
		}
		public void setCriteriaId(String criteriaId) {
			this.criteriaId = criteriaId;
		}
		public String getAuditControlId() {
			return auditControlId;
		}
		public void setAuditControlId(String auditControlId) {
			this.auditControlId = auditControlId;
		}
		public String getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(String appAuditId) {
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
		
	
		
		public AuditReportCriteriaDTO(String criteriaId, String auditControlId, String appAuditId, String compliance,
				String document, String status, String created, String updated, String createdBy, String updatedBy) {
			super();
			this.criteriaId = criteriaId;
			this.auditControlId = auditControlId;
			this.appAuditId = appAuditId;
			this.compliance = compliance;
			this.document = document;
			this.status = status;
			this.created = created;
			this.updated = updated;
			this.createdBy = createdBy;
			this.updatedBy = updatedBy;
		}
		public AuditReportCriteriaDTO() {
			
		}
		

	    

}
