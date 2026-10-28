package in.lms.cca.dto;

public class StatusChangedOfLicense {
	
	private String status;
	private String licenseId;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	@Override
	public String toString() {
		return "StatusChangedOfLicense [status=" + status + ", licenseId=" + licenseId + "]";
	}
	
	
	
	

}
