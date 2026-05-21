package in.lms.cca.payload;

public class SelectionData {
	
	private String applicantUserName;
	private String auditAgencyName;
	private String licenseeId;
	private String auditAgencyId;
	
	public String getAuditAgencyName() {
		return auditAgencyName;
	}
	public void setAuditAgencyName(String auditAgencyName) {
		this.auditAgencyName = auditAgencyName;
	}
	
	public String getApplicantUserName() {
		return applicantUserName;
	}
	
	
	public String getLicenseeId() {
		return licenseeId;
	}
	public void setLicenseeId(String licenseeId) {
		this.licenseeId = licenseeId;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	
	
	
	
	public String getAuditAgencyId() {
		return auditAgencyId;
	}
	public void setAuditAgencyId(String auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}
	@Override
	public String toString() {
		return "SelectionData [applicantUserName=" + applicantUserName + ", auditAgencyName=" + auditAgencyName
				+ ", licenseeId=" + licenseeId + ", auditAgencyId=" + auditAgencyId + "]";
	}
	
	
	

}
