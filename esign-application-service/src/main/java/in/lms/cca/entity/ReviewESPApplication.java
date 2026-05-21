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
@Table(name = "review_esp_application", schema="esign_application_management")
public class ReviewESPApplication extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esp_review_id", length = 11)
    private Long espReviewId;
	
	@ManyToOne
	@JoinColumn(name = "esign_licensee_appid", referencedColumnName = "esign_licensee_appid")
	private EsignLicenseeApplication esignLicenseeAppId;
	
	@Column(name = "ekycmode")
	private Boolean ekycMode;
	
	@Column(name = "coverletter")
	private Boolean coverLetter;
	
	@Column(name = "esign_api_version")
	private Boolean esignAPIVersion;
	
	@Column(name = "auditreport")
	private Boolean auditReport;
	
	@Column(name = "cpsdocument")
	private Boolean cpsDocument;
	
	@Column(name = "purpose")
	private Boolean purpose;
	
	@Column(name = "remarks", columnDefinition = "TEXT")
	private String remarks;
	
	@Column(name = "cca_remarks", columnDefinition = "TEXT")
	private String ccaRemarks;
	
	@Column(name = "status", length = 8)
	private String status;

	
	public Long getEspReviewId() {
		return espReviewId;
	}

	public void setEspReviewId(Long espReviewId) {
		this.espReviewId = espReviewId;
	}

	public EsignLicenseeApplication getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(EsignLicenseeApplication esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}

	public Boolean getEkycMode() {
		return ekycMode;
	}

	public void setEkycMode(Boolean ekycMode) {
		this.ekycMode = ekycMode;
	}

	public Boolean getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(Boolean coverLetter) {
		this.coverLetter = coverLetter;
	}

	public Boolean getEsignAPIVersion() {
		return esignAPIVersion;
	}

	public void setEsignAPIVersion(Boolean esignAPIVersion) {
		this.esignAPIVersion = esignAPIVersion;
	}

	public Boolean getAuditReport() {
		return auditReport;
	}

	public void setAuditReport(Boolean auditReport) {
		this.auditReport = auditReport;
	}

	public Boolean getCpsDocument() {
		return cpsDocument;
	}

	public void setCpsDocument(Boolean cpsDocument) {
		this.cpsDocument = cpsDocument;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCcaRemarks() {
		return ccaRemarks;
	}

	public void setCcaRemarks(String ccaRemarks) {
		this.ccaRemarks = ccaRemarks;
	}

	public Boolean getPurpose() {
		return purpose;
	}

	public void setPurpose(Boolean purpose) {
		this.purpose = purpose;
	}
	

	
	


}
