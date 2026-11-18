package in.lms.cca.entity;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "esign_main_application", schema="esign_application_management")
public class EsignAPIVersion extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esign_main_appid", length = 11)
    private Long esignMainAppId;
	
	@ManyToOne
	@JoinColumn(name = "esign_licensee_appid", referencedColumnName = "esign_licensee_appid")
	private EsignLicenseeApplication esignLicenseeAppId;
	
	@Column(name = "esignapispecificationid", length = 74)
	private String esignAPISpecId;
	
	@Column(name = "esignapiverid", length = 74)
	private String esignAPIVerId;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;
	

	public Long getEsignMainAppId() {
		return esignMainAppId;
	}

	public void setEsignMainAppId(Long esignMainAppId) {
		this.esignMainAppId = esignMainAppId;
	}

	public EsignLicenseeApplication getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(EsignLicenseeApplication esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}

	public String getEsignAPISpecId() {
		return esignAPISpecId;
	}

	public void setEsignAPISpecId(String esignAPISpecId) {
		this.esignAPISpecId = esignAPISpecId;
	}

	public String getEsignAPIVerId() {
		return esignAPIVerId;
	}

	public void setEsignAPIVerId(String esignAPIVerId) {
		this.esignAPIVerId = esignAPIVerId;
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
