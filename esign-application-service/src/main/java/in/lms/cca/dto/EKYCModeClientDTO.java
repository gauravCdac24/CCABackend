package in.lms.cca.dto;

import java.util.Date;

public class EKYCModeClientDTO{

    private Integer ekycModeId;
    private String ekycModeTitle;
    private Boolean isApprovalRequired;
    private String status;
    private String createdBy;
    private String updatedBy;
    private Date created;
    private Date updated;
    
	public Integer getEkycModeId() {
		return ekycModeId;
	}
	public void setEkycModeId(Integer ekycModeId) {
		this.ekycModeId = ekycModeId;
	}
	public String getEkycModeTitle() {
		return ekycModeTitle;
	}
	public void setEkycModeTitle(String ekycModeTitle) {
		this.ekycModeTitle = ekycModeTitle;
	}
	public Boolean getIsApprovalRequired() {
		return isApprovalRequired;
	}
	public void setIsApprovalRequired(Boolean isApprovalRequired) {
		this.isApprovalRequired = isApprovalRequired;
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
    
    
}

