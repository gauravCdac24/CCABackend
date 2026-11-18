package in.lms.cca.dto;

import java.util.Date;

public class ClientLicenseDetailsDTO {

		private Long licenseId;
	    private String intentAppId;
	    private String licenseCertificate;
	    private String userName;
	    private String licenseName;
	    private String serialNo;
	    private Date issueDate;
	    private Date expiryDate;
	    private String status;
	    private String createdBy;
	    private String updatedBy;
	    private Date created;
	    private Date updated;
	    
	    
		public Long getLicenseId() {
			return licenseId;
		}
		public void setLicenseId(Long licenseId) {
			this.licenseId = licenseId;
		}
		public String getIntentAppId() {
			return intentAppId;
		}
		public void setIntentAppId(String intentAppId) {
			this.intentAppId = intentAppId;
		}
		public String getLicenseCertificate() {
			return licenseCertificate;
		}
		public void setLicenseCertificate(String licenseCertificate) {
			this.licenseCertificate = licenseCertificate;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getLicenseName() {
			return licenseName;
		}
		public void setLicenseName(String licenseName) {
			this.licenseName = licenseName;
		}
		public String getSerialNo() {
			return serialNo;
		}
		public void setSerialNo(String serialNo) {
			this.serialNo = serialNo;
		}
		public Date getIssueDate() {
			return issueDate;
		}
		public void setIssueDate(Date issueDate) {
			this.issueDate = issueDate;
		}
		public Date getExpiryDate() {
			return expiryDate;
		}
		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
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
		public Date getCreated() {
			return created;
		}
		public void setCreated(Date created) {
			this.created = created;
		}
		public Date getUpdated() {
			return updated;
		}
		public void setUpdated(Date updated) {
			this.updated = updated;
		}
    
    
	
    
    
}
