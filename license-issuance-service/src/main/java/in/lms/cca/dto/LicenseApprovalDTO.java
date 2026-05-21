package in.lms.cca.dto;


public class LicenseApprovalDTO{

    private Long licenseApprovalId;
    private String intentAppId;
    private String licenseStatus;
    private String createdBy;
    private String updatedBy;
    private String created;
    private String updated;
    
	public Long getLicenseApprovalId() {
		return licenseApprovalId;
	}
	public void setLicenseApprovalId(Long licenseApprovalId) {
		this.licenseApprovalId = licenseApprovalId;
	}
	public String getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getLicenseStatus() {
		return licenseStatus;
	}
	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
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
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
    
    
	
}
