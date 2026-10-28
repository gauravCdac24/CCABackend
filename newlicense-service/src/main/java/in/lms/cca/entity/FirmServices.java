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
@Table(name = "firm_services", schema = "new_license_management")
public class FirmServices extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firm_service_id", length = 11)
    private Long firmServiceId;

    @Column(name = "service_id", length = 74)
    private String serviceId;

    @ManyToOne
   	@JoinColumn(name = "firm_application_id", referencedColumnName = "firm_application_id")
    private FirmApplication firmApplicationId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getFirmServiceId() {
		return firmServiceId;
	}

	public void setFirmServiceId(Long firmServiceId) {
		this.firmServiceId = firmServiceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public FirmApplication getFirmApplicationId() {
		return firmApplicationId;
	}

	public void setFirmApplicationId(FirmApplication firmApplicationId) {
		this.firmApplicationId = firmApplicationId;
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
