package in.lms.cca.entity;


import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "undertaking_master",schema = "admin_user_management")
public class UndertakingMasterEntity  extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "undertaking_id",length = 11)
    private Long undertakingId;

    @Column(name = "undertakings_title", columnDefinition = "TEXT")
    private String undertakingsTitle;

    @Column(name = "IsMandatory", nullable = false)
    private Boolean isMandatory;

	@ManyToOne
	@JoinColumn(name = "app_type_master_id", referencedColumnName = "app_type_master_id")
	private ApplicationTypeMaster appTypeMasterId;
	
    @Column(name = "status",length = 8)
    private String status; 

    @Column(name = "created_by", length = 74)
    private String createdBy; 

    @Column(name = "Updated_by", length = 74)
    private String updatedBy; 

    // Getters and Setters
    public Long getUndertakingId() {
        return undertakingId;
    }

    public void setUndertakingId(Long undertakingId) {
        this.undertakingId = undertakingId;
    }

    public String getUndertakingsTitle() {
        return undertakingsTitle;
    }

    public void setUndertakingsTitle(String undertakingsTitle) {
        this.undertakingsTitle = undertakingsTitle;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

   

    public ApplicationTypeMaster getAppTypeMasterId() {
		return appTypeMasterId;
	}

	public void setAppTypeMasterId(ApplicationTypeMaster appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
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
