package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class LicenseCessationApplicationDTO {
	
	    private Long cessationAppId;
	    private String licenseId; 
	    private MultipartFile cessationNotice;
	    private String status; 
	    private String created; 
	    private String updated;
	    private String createdBy; 
	    private String updatedBy;
	    private String userName;
		public Long getCessationAppId() {
			return cessationAppId;
		}
		public void setCessationAppId(Long cessationAppId) {
			this.cessationAppId = cessationAppId;
		}
		public String getLicenseId() {
			return licenseId;
		}
		public void setLicenseId(String licenseId) {
			this.licenseId = licenseId;
		}
		
		public MultipartFile getCessationNotice() {
			return cessationNotice;
		}
		public void setCessationNotice(MultipartFile cessationNotice) {
			this.cessationNotice = cessationNotice;
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
		
		
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		@Override
		public String toString() {
			return "LicenseCessationApplicationDTO [cessationAppId=" + cessationAppId + ", licenseId=" + licenseId
					+ ", cessationNotice=" + cessationNotice + ", status=" + status + ", created=" + created
					+ ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", userName="
					+ userName + "]";
		}
				
	    
	    

}
