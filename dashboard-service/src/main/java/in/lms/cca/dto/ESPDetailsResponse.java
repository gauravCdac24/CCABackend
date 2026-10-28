package in.lms.cca.dto;

import java.util.Date;

public class ESPDetailsResponse {
	
	private String licenseeName;
	private Date licenseIssueDate;
	private Date licenseExpiryDate;
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
	public Date getEmpanelmentDate() {
		return empanelmentDate;
	}
	public void setEmpanelmentDate(Date empanelmentDate) {
		this.empanelmentDate = empanelmentDate;
	}
	
	

}
