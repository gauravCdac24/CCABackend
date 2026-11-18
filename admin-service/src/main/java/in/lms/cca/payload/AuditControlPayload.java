package in.lms.cca.payload;

public class AuditControlPayload {
	
	private String auditControllId;
	private String auditControl;
    private String AuditCheck;
    private String controlType;
    private String references;
	public String getAuditControl() {
		return auditControl;
	}
	public void setAuditControl(String auditControl) {
		this.auditControl = auditControl;
	}
	public String getAuditCheck() {
		return AuditCheck;
	}
	public void setAuditCheck(String auditCheck) {
		AuditCheck = auditCheck;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getReferences() {
		return references;
	}
	public void setReferences(String references) {
		this.references = references;
	}
	
	public String getAuditControllId() {
		return auditControllId;
	}
	public void setAuditControllId(String auditControllId) {
		this.auditControllId = auditControllId;
	}
	@Override
	public String toString() {
		return "AuditControlPayload [auditControllId=" + auditControllId + ", auditControl=" + auditControl
				+ ", AuditCheck=" + AuditCheck + ", controlType=" + controlType + ", references=" + references + "]";
	}
	
    

}
