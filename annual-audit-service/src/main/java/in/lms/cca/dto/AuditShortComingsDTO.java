package in.lms.cca.dto;

public class AuditShortComingsDTO {

	    private String shortcomingId;
	    private String appAuditId;
	    private String shortcomings;
	    private String remarks;
	    private String status;
	    private String created;
	    private String updated;
	    private String createdBy; // Encrypted
	    private String updatedBy; // Encrypted
		public String getShortcomingId() {
			return shortcomingId;
		}
		public void setShortcomingId(String shortcomingId) {
			this.shortcomingId = shortcomingId;
		}
		public String getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(String appAuditId) {
			this.appAuditId = appAuditId;
		}
		public String getShortcomings() {
			return shortcomings;
		}
		public void setShortcomings(String shortcomings) {
			this.shortcomings = shortcomings;
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
			return "AuditShortComingsDTO [shortcomingId=" + shortcomingId + ", appAuditId=" + appAuditId
					+ ", shortcomings=" + shortcomings + ", remarks=" + remarks + ", status=" + status + ", created="
					+ created + ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    

}
