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
@Table(name = "esp_purpose", schema="esign_application_management")
public class ESPPurposeEntity extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purpose_id", length = 11)
    private Long purposeId;
	
	 @Column(name = "purpose", length = 11)
	 private String purpose;
	 
	 @Column(name = "status", length = 8)
	 private String status;
		
	 @Column(name = "created_by", length = 74)
	 private String createdBy;
		
	@Column(name = "updated_by", length = 74)
	private String updatedBy;
	 
	@ManyToOne
	@JoinColumn(name = "esign_licensee_appid", referencedColumnName = "esign_licensee_appid")
	private EsignLicenseeApplication esignLicenseeAppId;

	
	
	public ESPPurposeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ESPPurposeEntity(Long purposeId, String purpose, String status, String createdBy, String updatedBy,
			EsignLicenseeApplication esignLicenseeAppId) {
		super();
		this.purposeId = purposeId;
		this.purpose = purpose;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.esignLicenseeAppId = esignLicenseeAppId;
	}

	public ESPPurposeEntity(String purpose, String status, String createdBy, String updatedBy,
			EsignLicenseeApplication esignLicenseeAppId) {
		super();
		
		this.purpose = purpose;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.esignLicenseeAppId = esignLicenseeAppId;
	}

	public Long getPurposeId() {
		return purposeId;
	}

	public void setPurposeId(Long purposeId) {
		this.purposeId = purposeId;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
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

	public EsignLicenseeApplication getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(EsignLicenseeApplication esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}

	
	
	
	
	
	
	 
	 

}
