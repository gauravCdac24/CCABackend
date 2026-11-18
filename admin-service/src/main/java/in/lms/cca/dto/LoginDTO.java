package in.lms.cca.dto;

public class LoginDTO {
	
	     private String loginId;
	    private String userId;
	    private String password;
	    private String previousPassword;
	    private String userName;
	    private String emailId;
	    private String mobile;
	    private String createdBy;
	    private String updatedBy;
	    private String status;
	    private String created;
	    private String updated;
		public String getLoginId() {
			return loginId;
		}
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPreviousPassword() {
			return previousPassword;
		}
		public void setPreviousPassword(String previousPassword) {
			this.previousPassword = previousPassword;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
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
		@Override
		public String toString() {
			return "LoginDTO [loginId=" + loginId + ", userId=" + userId + ", password=" + password
					+ ", previousPassword=" + previousPassword + ", userName=" + userName + ", emailId=" + emailId
					+ ", mobile=" + mobile + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", status="
					+ status + ", created=" + created + ", updated=" + updated + "]";
		}
	    
	    
	    
	    

}
