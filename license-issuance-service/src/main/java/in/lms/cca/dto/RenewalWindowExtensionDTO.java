package in.lms.cca.dto;

import java.time.LocalDate;

public class RenewalWindowExtensionDTO {

	private Long extensionId;
	private Long licenseId;
	private String userName;
	private String licenseName;
	private String serialNo;
	private LocalDate grantedOn;
	private LocalDate extensionUntil;
	private String grantedBy;
	private String remarks;
	private String status;

	public Long getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}

	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public LocalDate getGrantedOn() {
		return grantedOn;
	}

	public void setGrantedOn(LocalDate grantedOn) {
		this.grantedOn = grantedOn;
	}

	public LocalDate getExtensionUntil() {
		return extensionUntil;
	}

	public void setExtensionUntil(LocalDate extensionUntil) {
		this.extensionUntil = extensionUntil;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
