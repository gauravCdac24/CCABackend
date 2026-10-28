package in.lms.cca.dto;

public class LicenseeAuditDTO {
	
    private Long LicenseeAuditId;
    private AnnualAuditScheduleDTO auditScheduleId;
    private String remarks;
    private String reviewBy;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String applicantUserName;
    private String applicantRemarks;
    
	public Long getLicenseeAuditId() {
		return LicenseeAuditId;
	}
	public void setLicenseeAuditId(Long licenseeAuditId) {
		LicenseeAuditId = licenseeAuditId;
	}
	public AnnualAuditScheduleDTO getAuditScheduleId() {
		return auditScheduleId;
	}
	public void setAuditScheduleId(AnnualAuditScheduleDTO auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReviewBy() {
		return reviewBy;
	}
	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getApplicantUserName() {
		return applicantUserName;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	public String getApplicantRemarks() {
		return applicantRemarks;
	}
	public void setApplicantRemarks(String applicantRemarks) {
		this.applicantRemarks = applicantRemarks;
	}

    

}
