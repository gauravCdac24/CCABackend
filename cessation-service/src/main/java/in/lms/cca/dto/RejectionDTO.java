package in.lms.cca.dto;

public class RejectionDTO {

	
	 private Long rejectionId;
	    private Long cessationAppId;
	    private String rejectionStatus; 
	    private String initiatedBy; 
	    private String created; 
	    private String updated; 
	    private String createdBy; 
	    private String updatedBy;
		public Long getRejectionId() {
			return rejectionId;
		}
		public void setRejectionId(Long rejectionId) {
			this.rejectionId = rejectionId;
		}
		public Long getCessationAppId() {
			return cessationAppId;
		}
		public void setCessationAppId(Long cessationAppId) {
			this.cessationAppId = cessationAppId;
		}
		public String getRejectionStatus() {
			return rejectionStatus;
		}
		public void setRejectionStatus(String rejectionStatus) {
			this.rejectionStatus = rejectionStatus;
		}
		public String getInitiatedBy() {
			return initiatedBy;
		}
		public void setInitiatedBy(String initiatedBy) {
			this.initiatedBy = initiatedBy;
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
			return "RejectionDTO [rejectionId=" + rejectionId + ", cessationAppId=" + cessationAppId
					+ ", rejectionStatus=" + rejectionStatus + ", initiatedBy=" + initiatedBy + ", created=" + created
					+ ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		} 
	    
	    
	
}
