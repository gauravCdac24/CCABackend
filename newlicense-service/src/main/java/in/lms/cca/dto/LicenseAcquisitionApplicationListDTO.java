package in.lms.cca.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LicenseAcquisitionApplicationListDTO {

	private Long intentAppId;
	private String userName;
	private String applicationStatus;
	private Date created;
	private String currentStep;
	private boolean readOnly;
	private List<String> allowedActions = new ArrayList<>();

	public Long getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(Long intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<String> getAllowedActions() {
		return allowedActions;
	}

	public void setAllowedActions(List<String> allowedActions) {
		this.allowedActions = allowedActions;
	}
}
