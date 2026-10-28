package in.lms.cca.dto;

public class AuditAgencyDetailsDTO {

	private String auditAgencyName;
	private String mobileNumbers;
	private String emailIds;
	
	
	public AuditAgencyDetailsDTO(String auditAgencyName, String mobileNumbers, String emailIds) {
		super();
		this.auditAgencyName = auditAgencyName;
		this.mobileNumbers = mobileNumbers;
		this.emailIds = emailIds;
	}
	
	public String getAuditAgencyName() {
		return auditAgencyName;
	}
	public void setAuditAgencyName(String auditAgencyName) {
		this.auditAgencyName = auditAgencyName;
	}
	public String getMobileNumbers() {
		return mobileNumbers;
	}
	public void setMobileNumbers(String mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}
	public String getEmailIds() {
		return emailIds;
	}
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}
	
	
	
}
