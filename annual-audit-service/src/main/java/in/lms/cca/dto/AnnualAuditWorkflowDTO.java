package in.lms.cca.dto;

import java.util.ArrayList;
import java.util.List;

public class AnnualAuditWorkflowDTO {

	private String auditScheduleId;
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
