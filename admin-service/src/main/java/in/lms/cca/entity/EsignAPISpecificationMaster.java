package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "esign_api_specification_master", schema = "admin_user_management")
public class EsignAPISpecificationMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esign_api_specid", length = 11)
    private Long esignApiSpecId;

    @Column(name = "api_specification", length = 250, nullable = false)
    private String apiSpecification;

    @Column(name = "status", length = 8, nullable = false)
    private String status;

    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;

	public Long getEsignApiSpecId() {
		return esignApiSpecId;
	}

	public void setEsignApiSpecId(Long esignApiSpecId) {
		this.esignApiSpecId = esignApiSpecId;
	}

	public String getApiSpecification() {
		return apiSpecification;
	}

	public void setApiSpecification(String apiSpecification) {
		this.apiSpecification = apiSpecification;
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

