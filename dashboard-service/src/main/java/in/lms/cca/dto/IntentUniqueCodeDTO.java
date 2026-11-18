package in.lms.cca.dto;

public class IntentUniqueCodeDTO{

	private Long uniqueCodeId;
	private Long uniqueCode;
	private String emailId;
	private String mobileNo;
	private String organizationName;
	
	
	public Long getUniqueCodeId() {
		return uniqueCodeId;
	}
	public void setUniqueCodeId(Long uniqueCodeId) {
		this.uniqueCodeId = uniqueCodeId;
	}
	public Long getUniqueCode() {
		return uniqueCode;
	}
	public void setUniqueCode(Long uniqueCode) {
		this.uniqueCode = uniqueCode;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}



	
}
