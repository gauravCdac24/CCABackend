package in.lms.cca.dto;

public class UndertakingMasterDTO {
	
	    private String undertakingId;
	    private String undertakingsTitle;
	    private Boolean isMandatory;
	    private String appTypeMasterId;
	    private String status;
	    private String created;
	    private String updated;
	    private String createdBy;
	    private String updatedBy;
		public String getUndertakingId() {
			return undertakingId;
		}
		public void setUndertakingId(String undertakingId) {
			this.undertakingId = undertakingId;
		}
		public String getUndertakingsTitle() {
			return undertakingsTitle;
		}
		public void setUndertakingsTitle(String undertakingsTitle) {
			this.undertakingsTitle = undertakingsTitle;
		}
		public Boolean getIsMandatory() {
			return isMandatory;
		}
		public void setIsMandatory(Boolean isMandatory) {
			this.isMandatory = isMandatory;
		}
		public String getAppTypeMasterId() {
			return appTypeMasterId;
		}
		public void setAppTypeMasterId(String appTypeMasterId) {
			this.appTypeMasterId = appTypeMasterId;
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
			return "UndertakingMasterDTO [undertakingId=" + undertakingId + ", undertakingsTitle=" + undertakingsTitle
					+ ", isMandatory=" + isMandatory + ", appTypeMasterId=" + appTypeMasterId + ", status=" + status
					+ ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy="
					+ updatedBy + "]";
		} 
	    
	    


}
