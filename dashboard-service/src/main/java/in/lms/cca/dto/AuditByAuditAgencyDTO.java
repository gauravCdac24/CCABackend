package in.lms.cca.dto;

import java.util.Date;

public class AuditByAuditAgencyDTO {

	private String auditors;
	private String auditConductedFor;
	private String auditType;
	private Date auditInitiatedOn;
	private Date auditCompletedOn;
	
	
	
	
	public String getAuditors() {
		return auditors;
	}
	public void setAuditors(String auditors) {
		this.auditors = auditors;
	}
	public String getAuditConductedFor() {
		return auditConductedFor;
	}
	public void setAuditConductedFor(String auditConductedFor) {
		this.auditConductedFor = auditConductedFor;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public Date getAuditInitiatedOn() {
		return auditInitiatedOn;
	}
	public void setAuditInitiatedOn(Date auditInitiatedOn) {
		this.auditInitiatedOn = auditInitiatedOn;
	}
	public Date getAuditCompletedOn() {
		return auditCompletedOn;
	}
	public void setAuditCompletedOn(Date auditCompletedOn) {
		this.auditCompletedOn = auditCompletedOn;
	}
	
	
	
	
	
}
