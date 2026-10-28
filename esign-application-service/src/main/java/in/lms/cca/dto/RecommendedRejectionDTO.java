package in.lms.cca.dto;

public class RecommendedRejectionDTO {

	private String esignLicenseeAppId;
	private String remarks;
	private String userName;
	private Boolean isreject;
	
	
	public String getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}
	public void setEsignLicenseeAppId(String esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Boolean getIsreject() {
		return isreject;
	}
	public void setIsreject(Boolean isreject) {
		this.isreject = isreject;
	}
	
	
	
}
