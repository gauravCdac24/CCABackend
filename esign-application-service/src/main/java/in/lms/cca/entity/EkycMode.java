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
@Table(name = "ekyc_mode", schema="esign_application_management")
public class EkycMode extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ekyc_mode_id", length = 11)
    private Long ekycModeId;
	
	@Column(name = "ekycmode", length = 74)
	private String ekycMode;
	
	@Column(name = "ekycmodetitle", length = 100)
	private String ekycModeTitle;
	
	@Column(name = "is_approval_required", nullable = false)
    private Boolean isApprovalRequired;
	
	@ManyToOne
	@JoinColumn(name = "esign_licensee_appid", referencedColumnName = "esign_licensee_appid")
	private EsignLicenseeApplication esignLicenseeAppId;
	
	@Column(name = "approval_file", length = 100)
	private String fileName;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	public Long getEkycModeId() {
		return ekycModeId;
	}

	public void setEkycModeId(Long ekycModeId) {
		this.ekycModeId = ekycModeId;
	}

	public String getEkycMode() {
		return ekycMode;
	}

	public void setEkycMode(String ekycMode) {
		this.ekycMode = ekycMode;
	}

	public EsignLicenseeApplication getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(EsignLicenseeApplication esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEkycModeTitle() {
		return ekycModeTitle;
	}

	public void setEkycModeTitle(String ekycModeTitle) {
		this.ekycModeTitle = ekycModeTitle;
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

	public Boolean getIsApprovalRequired() {
		return isApprovalRequired;
	}

	public void setIsApprovalRequired(Boolean isApprovalRequired) {
		this.isApprovalRequired = isApprovalRequired;
	}
	
	
	
}
