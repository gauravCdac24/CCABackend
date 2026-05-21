package in.lms.cca.payload;

public class IntentApplicationPayload {
	private Long intentAppId;            
    private String licenseId;          
    private Long appTypeMasterId;        
    private String userName;             
    private String acknowledgementNo;    
    private String applicationStatus;
    private String Status;
    private String created;           
    private String updated;         
    private String createdBy;           
    private String updatedBy;
	public Long getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(Long intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	public Long getAppTypeMasterId() {
		return appTypeMasterId;
	}
	public void setAppTypeMasterId(Long appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}
	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
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
    
    

}
