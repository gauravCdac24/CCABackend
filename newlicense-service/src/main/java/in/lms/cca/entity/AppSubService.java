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
@Table(name = "app_sub_service", schema = "new_license_management")
public class AppSubService extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_sub_service_id", length = 11)
    private Long appSubServiceId;

    @Column(name = "sub_service_id", length = 74)
    private String subServiceId;

    @ManyToOne
    @JoinColumn(name = "app_service_id", referencedColumnName = "app_service_id")
    private AppServices appServiceId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;
    
    

	public Long getAppSubServiceId() {
		return appSubServiceId;
	}

	public void setAppSubServiceId(Long appSubServiceId) {
		this.appSubServiceId = appSubServiceId;
	}

	public String getSubServiceId() {
		return subServiceId;
	}

	public void setSubServiceId(String subServiceId) {
		this.subServiceId = subServiceId;
	}

	public AppServices getAppServiceId() {
		return appServiceId;
	}

	public void setAppServiceId(AppServices appServiceId) {
		this.appServiceId = appServiceId;
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
