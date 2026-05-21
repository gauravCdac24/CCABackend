package in.lms.cca.dto;

public class CADetailsDTO {

	private String caName;
	private String mobileNumbers;
	private String emailIds;
	
	
	public CADetailsDTO(String caName, String mobileNumbers, String emailIds) {
		super();
		this.caName = caName;
		this.mobileNumbers = mobileNumbers;
		this.emailIds = emailIds;
	}
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
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
