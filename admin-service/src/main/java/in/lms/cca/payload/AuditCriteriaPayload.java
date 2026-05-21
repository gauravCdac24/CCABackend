package in.lms.cca.payload;

import java.util.List;

public class AuditCriteriaPayload {
	
	private String auditCriteria;
	private List<AuditSubCriteriaPayload> auditSubCriteria;
	public String getAuditCriteria() {
		return auditCriteria;
	}
	public void setAuditCriteria(String auditCriteria) {
		this.auditCriteria = auditCriteria;
	}
	public List<AuditSubCriteriaPayload> getAuditSubCriteria() {
		return auditSubCriteria;
	}
	public void setAuditSubCriteria(List<AuditSubCriteriaPayload> auditSubCriteria) {
		this.auditSubCriteria = auditSubCriteria;
	}
	@Override
	public String toString() {
		return "AuditCriteriaPayload [auditCriteria=" + auditCriteria + ", auditSubCriteria=" + auditSubCriteria + "]";
	}
	
	

}
