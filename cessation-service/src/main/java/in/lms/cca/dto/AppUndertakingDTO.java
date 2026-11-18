package in.lms.cca.dto;

public class AppUndertakingDTO {
	
	  private Long appUndertakingId;
	    private String undertakingId; 
	    private String filename;
	    private Long cessationAppId;
	    private String created; 
	    private String updated; 
	    private String createdBy; 
	    private String updatedBy;
		public Long getAppUndertakingId() {
			return appUndertakingId;
		}
		public void setAppUndertakingId(Long appUndertakingId) {
			this.appUndertakingId = appUndertakingId;
		}
		public String getUndertakingId() {
			return undertakingId;
		}
		public void setUndertakingId(String undertakingId) {
			this.undertakingId = undertakingId;
		}
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		public Long getCessationAppId() {
			return cessationAppId;
		}
		public void setCessationAppId(Long cessationAppId) {
			this.cessationAppId = cessationAppId;
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
			return "AppUndertakingDTO [appUndertakingId=" + appUndertakingId + ", undertakingId=" + undertakingId
					+ ", filename=" + filename + ", cessationAppId=" + cessationAppId + ", created=" + created
					+ ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		} 
	    
	    
	    
	

}
