package in.lms.cca.dto;

import java.util.Date;

public class ESPDTO {

    private Long esignLicenseeAppId;
    private String licenseId;
    private String applicationStatus;
    private String applicationNumber;
    private String userName;
    private String createdBy;
    private String updatedBy;
    private Date created;
    private Date updated;
    private Date empanelmentDate;
    
    
	public Long getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}
	public void setEsignLicenseeAppId(Long esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}
	public String getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getEmpanelmentDate() {
		return empanelmentDate;
	}
	public void setEmpanelmentDate(Date empanelmentDate) {
		this.empanelmentDate = empanelmentDate;
	}
    
    
    
	
}
