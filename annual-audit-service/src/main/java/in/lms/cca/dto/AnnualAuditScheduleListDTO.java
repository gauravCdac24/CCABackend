package in.lms.cca.dto;

import java.util.ArrayList;
import java.util.List;

public class AnnualAuditScheduleListDTO {

	private String auditScheduleId;
	private String licenseId;
	private String userName;
	private String scheduleStartDate;
	private String status;
	private boolean agencySelected;
	private String scheduleState;
	private String currentStep;
	private boolean readOnly;
	private List<String> allowedActions = new ArrayList<>();

	public String getAuditScheduleId() {
		return auditScheduleId;
	}

	public void setAuditScheduleId(String auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
	}

	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getScheduleStartDate() {
		return scheduleStartDate;
	}

	public void setScheduleStartDate(String scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAgencySelected() {
		return agencySelected;
	}

	public void setAgencySelected(boolean agencySelected) {
		this.agencySelected = agencySelected;
	}

	public String getScheduleState() {
		return scheduleState;
	}

	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
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
