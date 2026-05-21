package in.lms.cca.dto;


public class ApplicationTimeLineDTO {

	private Long appTimelineId;
	private IntentApplicationDTO intentAppId;
	private String applicationStatus;
	private String initiatedBy;
	
	
	public Long getAppTimelineId() {
		return appTimelineId;
	}
	public void setAppTimelineId(Long appTimelineId) {
		this.appTimelineId = appTimelineId;
	}
	public IntentApplicationDTO getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(IntentApplicationDTO intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public String getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	
	
	
}
