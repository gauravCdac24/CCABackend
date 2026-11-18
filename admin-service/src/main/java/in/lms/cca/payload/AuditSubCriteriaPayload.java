package in.lms.cca.payload;

import java.util.List;

public class AuditSubCriteriaPayload {
	
	private String auditCriteriaId;
	private String auditSubCriteriaId;
	private String auditSubCriteria;
	private String status;
	private boolean visible;
	private List<AuditParameterPayload> auditParameterPayload;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getAuditSubCriteriaId() {
		return auditSubCriteriaId;
	}

	public void setAuditSubCriteriaId(String auditSubCriteriaId) {
		this.auditSubCriteriaId = auditSubCriteriaId;
	}

	public String getAuditSubCriteria() {
		return auditSubCriteria;
	}
	public void setAuditSubCriteria(String auditSubCriteria) {
		this.auditSubCriteria = auditSubCriteria;
	}
	public List<AuditParameterPayload> getAuditParameterPayload() {
		return auditParameterPayload;
	}
	public void setAuditParameterPayload(List<AuditParameterPayload> auditParameterPayload) {
		this.auditParameterPayload = auditParameterPayload;
	}
	
	public String getAuditCriteriaId() {
		return auditCriteriaId;
	}
	public void setAuditCriteriaId(String auditCriteriaId) {
		this.auditCriteriaId = auditCriteriaId;
	}
	@Override
	public String toString() {
		return "AuditSubCriteriaPayload [auditCriteriaId=" + auditCriteriaId + ", auditSubCriteria=" + auditSubCriteria
				+ ", auditParameterPayload=" + auditParameterPayload + "]";
	}
	
	
	
	

}
