package in.lms.cca.dto.client;

import java.util.Date;

public class ApplicationDTO {

	private String intentAppId;
	private String applicationType;
	private String applicantUserName;
	private String acknowledgementNo;
	private String applicationCurrentStatus;
	private Date applicationInitiatedOn;
	private Date statusInitiatedOn;
	private String applicantName;
	
	
	
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public String getApplicantUserName() {
		return applicantUserName;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}
	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}
	public String getApplicationCurrentStatus() {
		return applicationCurrentStatus;
	}
	public void setApplicationCurrentStatus(String applicationCurrentStatus) {
		this.applicationCurrentStatus = applicationCurrentStatus;
	}
	public Date getApplicationInitiatedOn() {
		return applicationInitiatedOn;
	}
	public void setApplicationInitiatedOn(Date applicationInitiatedOn) {
		this.applicationInitiatedOn = applicationInitiatedOn;
	}
	public Date getStatusInitiatedOn() {
		return statusInitiatedOn;
	}
	public void setStatusInitiatedOn(Date statusInitiatedOn) {
		this.statusInitiatedOn = statusInitiatedOn;
	}
	@Override
	public String toString() {
		return "ApplicationDTO [intentAppId=" + intentAppId + ", applicationType=" + applicationType
				+ ", applicantUserName=" + applicantUserName + ", acknowledgementNo=" + acknowledgementNo
				+ ", applicationCurrentStatus=" + applicationCurrentStatus + ", applicationInitiatedOn="
				+ applicationInitiatedOn + ", statusInitiatedOn=" + statusInitiatedOn + ", applicantName="
				+ applicantName + "]";
	}
	
}
