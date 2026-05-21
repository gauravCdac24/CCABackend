package in.lms.cca.dto;

public class AuditAgencySelectionDTO {

    private Long agencySelectionId; // Optional for creation, required for updates
    private String auditAgencyId;   // ID of the selected audit agency
    private Long appAuditId;        // ID of the associated application audit entity
    private String reviewedBy;      // Name of the reviewer
    private String remarks;         // Remarks or comments
    private String createdBy;       // Creator of the record
    private String updatedBy;       // Updater of the record
    private String status; 
    private String aplicantUserName;

    // Getters and setters
    
    
    public Long getAgencySelectionId() {
        return agencySelectionId;
    }

    public String getAplicantUserName() {
		return aplicantUserName;
	}

	public void setAplicantUserName(String aplicantUserName) {
		this.aplicantUserName = aplicantUserName;
	}

	public void setAgencySelectionId(Long agencySelectionId) {
        this.agencySelectionId = agencySelectionId;
    }

    public String getAuditAgencyId() {
        return auditAgencyId;
    }

    public void setAuditAgencyId(String auditAgencyId) {
        this.auditAgencyId = auditAgencyId;
    }

    public Long getAppAuditId() {
        return appAuditId;
    }

    public void setAppAuditId(Long appAuditId) {
        this.appAuditId = appAuditId;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "AuditAgencySelectionDTO [agencySelectionId=" + agencySelectionId + ", auditAgencyId=" + auditAgencyId
				+ ", appAuditId=" + appAuditId + ", reviewedBy=" + reviewedBy + ", remarks=" + remarks + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", status=" + status + ", aplicantUserName="
				+ aplicantUserName + "]";
	}

   
}
