package in.lms.cca.dto;

import java.util.Date;

public class CustomizedLicensingReportDTO {

	private String licenseeName;
	private Date licenseIssueDate;
	private Date licenseExpiryDate;
	private String licenseeType;
	private String licenseType;
	private Date empanelmentDate;
	
	
	public String getLicenseeName() {
		return licenseeName;
	}
	public void setLicenseeName(String licenseeName) {
		this.licenseeName = licenseeName;
	}
	public Date getLicenseIssueDate() {
		return licenseIssueDate;
	}
	public void setLicenseIssueDate(Date licenseIssueDate) {
		this.licenseIssueDate = licenseIssueDate;
	}
	public Date getLicenseExpiryDate() {
		return licenseExpiryDate;
	}
	public void setLicenseExpiryDate(Date licenseExpiryDate) {
		this.licenseExpiryDate = licenseExpiryDate;
	}
	public String getLicenseeType() {
		return licenseeType;
	}
	public void setLicenseeType(String licenseeType) {
		this.licenseeType = licenseeType;
	}
	public Date getEmpanelmentDate() {
		return empanelmentDate;
	}
	public void setEmpanelmentDate(Date empanelmentDate) {
		this.empanelmentDate = empanelmentDate;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	
	
	
}
