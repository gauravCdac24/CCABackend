package in.lms.cca.payload;

import java.util.List;

public class AuditSubCriteriaPayload {
	
	private String auditCriteriaId;
	private String auditSubCriteria;
	private List<AuditParameterPayload> auditParameterPayload;
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
	public AuditSubCriteriaPayload() {
		super();
		this.auditCriteriaId = auditCriteriaId;
		this.auditSubCriteria = auditSubCriteria;
		this.auditParameterPayload = auditParameterPayload;
	}
	
	
	
	

}
