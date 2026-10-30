package in.lms.cca.dto;

public class GrantRenewalExtensionRequest {

	private String username;
	private String grantedBy;
	private String remarks;
	private String extensionDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGrantedBy() {
		return grantedBy;
	}

	public void setGrantedBy(String grantedBy) {
		this.grantedBy = grantedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getExtensionDate() {
		return extensionDate;
	}

	public void setExtensionDate(String extensionDate) {
		this.extensionDate = extensionDate;
	}
}
