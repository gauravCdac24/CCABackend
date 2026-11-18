package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ekyc_mode_master", schema = "admin_user_management")
public class EKYCModeMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ekyc_mode_id", length = 2)
    private Integer ekycModeId;

    @Column(name = "ekyc_mode_title", length = 150, nullable = false)
    private String ekycModeTitle;

    @Column(name = "is_approval_required", nullable = false)
    private Boolean isApprovalRequired;

    @Column(name = "status", length = 8, nullable = false)
    private String status;  

    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;

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


    
    
}
