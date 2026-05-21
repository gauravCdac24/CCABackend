package in.lms.cca.dto;

public class ApplicantActionTakenDTO {

	    private String actionTakenId;
	    private String auditNCId;
	    private String actionReport;
	    private String remarks;
	    private String status; // Possible values: Approved, Rejected, etc.
	    private String created;
	    private String updated;
	    private String createdBy;
	    private String updatedBy;
		public String getActionTakenId() {
			return actionTakenId;
		}
		public void setActionTakenId(String actionTakenId) {
			this.actionTakenId = actionTakenId;
		}
		public String getAuditNCId() {
			return auditNCId;
		}
		public void setAuditNCId(String auditNCId) {
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
			return "ApplicantActionTakenDTO [actionTakenId=" + actionTakenId + ", auditNCId=" + auditNCId
					+ ", actionReport=" + actionReport + ", remarks=" + remarks + ", status=" + status + ", created="
					+ created + ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    
	    
}
