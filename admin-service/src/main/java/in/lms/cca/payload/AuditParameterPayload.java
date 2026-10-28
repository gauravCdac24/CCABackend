package in.lms.cca.payload;

import java.util.List;

public class AuditParameterPayload {
	private String auditParameter;
	private List<AuditControlPayload> auditControlPayload;
	public String getAuditParameter() {
		return auditParameter;
	}
	public void setAuditParameter(String auditParameter) {
		this.auditParameter = auditParameter;
	}
	public List<AuditControlPayload> getAuditControlPayload() {
		return auditControlPayload;
	}
	public void setAuditControlPayload(List<AuditControlPayload> auditControlPayload) {
		this.auditControlPayload = auditControlPayload;
	}
	@Override
	public String toString() {
		return "AuditParameterPayload [auditParameter=" + auditParameter + ", auditControlPayload="
				+ auditControlPayload + "]";
	}
	
	

}
