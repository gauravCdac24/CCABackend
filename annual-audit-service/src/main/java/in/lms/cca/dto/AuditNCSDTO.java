package in.lms.cca.dto;

public class AuditNCSDTO {

	    private String auditNCId;
	    private String appAuditId;
	    private String ncs; // Non-Conformities
	    private String remarks;
	    private String status; // Expected values could be Approved, Rejected, etc.
	    private String created;
	    private String updated;
	    private String createdBy;
	    private String updatedBy;
		public String getAuditNCId() {
			return auditNCId;
		}
		public void setAuditNCId(String auditNCId) {
			this.auditNCId = auditNCId;
		}
		public String getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(String appAuditId) {
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
			return "AuditNCSDTO [auditNCId=" + auditNCId + ", appAuditId=" + appAuditId + ", ncs=" + ncs + ", remarks="
					+ remarks + ", status=" + status + ", created=" + created + ", updated=" + updated + ", createdBy="
					+ createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    
	    
}
