package in.lms.cca.entity;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "license_cessation-application", schema="cessation_management")
public class LicenseCessationApplicationEntity extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "cessation_app_id", length = 11)
    private Long cessationAppId;
    
    @Column(name="license_id", length = 74)
    private String licenseId; 
    
    @Column(name="cessation_notice", length = 100)
    private String cessationNotice;
    
    @Column(name="status", length = 50)
    private String status; 
    
    @Column(name="created_by", length = 74)
    private String createdBy; 
    
    @Column(name="updated_by",length = 74)
    private String updatedBy;
    
    @Column(name="user_name",length = 74)
    private String userName;

	public Long getCessationAppId() {
		return cessationAppId;
	}

	public void setCessationAppId(Long cessationAppId) {
		this.cessationAppId = cessationAppId;
	}

	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public String getCessationNotice() {
		return cessationNotice;
	}

	public void setCessationNotice(String cessationNotice) {
		this.cessationNotice = cessationNotice;
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
	
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "LicenseCessationApplicationEntity [cessationAppId=" + cessationAppId + ", licenseId=" + licenseId
				+ ", cessationNotice=" + cessationNotice + ", status=" + status + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", userName=" + userName + "]";
	}


	

}
