package in.lms.cca.dto;

public class IntentApplicationDTO {

	private Long intentAppId;
	private String licenseeId;
	private String appTypeMasterId;
	private String userName;
	private String acknowledgementNo;
	private String applicationStatus;
	private String uniqueCode;
	
	public Long getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(Long intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getAppTypeMasterId() {
		return appTypeMasterId;
	}
	public void setAppTypeMasterId(String appTypeMasterId) {
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
	public String getLicenseeId() {
		return licenseeId;
	}
	public void setLicenseeId(String licenseeId) {
		this.licenseeId = licenseeId;
	}
	public String getUniqueCode() {
		return uniqueCode;
	}
	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}	
	
	
}
