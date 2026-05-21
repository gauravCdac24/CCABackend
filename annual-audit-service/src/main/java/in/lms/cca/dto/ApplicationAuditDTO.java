package in.lms.cca.dto;

import java.time.LocalDateTime;

public class ApplicationAuditDTO {

    private Long appAuditId;
    private String intentAppId;
    private String remarks;
    private String reviewBy;
    private Boolean agencySelectionByApplicant;
    private String status;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;

    // Getters and Setters

    public Long getAppAuditId() {
        return appAuditId;
    }

    public void setAppAuditId(Long appAuditId) {
        this.appAuditId = appAuditId;
    }

    public String getIntentAppId() {
        return intentAppId;
    }

    public void setIntentAppId(String intentAppId) {
        this.intentAppId = intentAppId;
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

    public Boolean getAgencySelectionByApplicant() {
        return agencySelectionByApplicant;
    }

    public void setAgencySelectionByApplicant(Boolean agencySelectionByApplicant) {
        this.agencySelectionByApplicant = agencySelectionByApplicant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	@Override
	public String toString() {
		return "ApplicationAuditDTO [appAuditId=" + appAuditId + ", intentAppId=" + intentAppId + ", remarks=" + remarks
				+ ", reviewBy=" + reviewBy + ", agencySelectionByApplicant=" + agencySelectionByApplicant + ", status="
				+ status + ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy="
				+ updatedBy + "]";
	}
    
}
