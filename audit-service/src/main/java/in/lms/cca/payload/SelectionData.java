package in.lms.cca.payload;

public class SelectionData {
	
	private String applicantUserName;
	/**
	 * The audit agency's USER ID (e.g., "UCCA20260130"), NOT the display name.
	 * This field must contain the audit agency's unique identifier for proper data linkage.
	 */
	private String auditAgencyId;
	private String intentAppId;
	
	public String getAuditAgencyId() {
		return auditAgencyId;
	}
	public void setAuditAgencyId(String auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}
	public String getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}
	
	public String getApplicantUserName() {
		return applicantUserName;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	@Override
	public String toString() {
		return "SelectionData [applicantUserName=" + applicantUserName + ", auditAgencyId=" + auditAgencyId
				+ ", intentAppId=" + intentAppId + "]";
	}
}

