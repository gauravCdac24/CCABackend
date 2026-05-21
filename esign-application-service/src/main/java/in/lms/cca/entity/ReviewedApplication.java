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
@Table(name = "reviewed_application", schema="esign_application_management")
public class ReviewedApplication extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewed_application", length = 11)
    private Long reviewedApplicationId;
	
	@ManyToOne
	@JoinColumn(name = "esign_main_appid", referencedColumnName = "esign_main_appid")
	private EsignAPIVersion esignMainAppId;
	
	@ManyToOne
	@JoinColumn(name = "coverletter", referencedColumnName = "esigndocid")
	private EsignDocument coverLetter;
	
	@ManyToOne
	@JoinColumn(name = "auditreport", referencedColumnName = "esigndocid")
	private EsignDocument auditReport;
	
	@ManyToOne
	@JoinColumn(name = "cpsdocument", referencedColumnName = "esigndocid")
	private EsignDocument cpsDocument;
	
	@ManyToOne
	@JoinColumn(name = "esp_review_id", referencedColumnName = "esp_review_id")
	private ReviewESPApplication espReviewId;
	
	@ManyToOne
	@JoinColumn(name = "purpose_id", referencedColumnName = "purpose_id")
	private ESPPurposeEntity purposeId;

	public Long getReviewedApplicationId() {
		return reviewedApplicationId;
	}

	public void setReviewedApplicationId(Long reviewedApplicationId) {
		this.reviewedApplicationId = reviewedApplicationId;
	}

	public EsignAPIVersion getEsignMainAppId() {
		return esignMainAppId;
	}

	public void setEsignMainAppId(EsignAPIVersion esignMainAppId) {
		this.esignMainAppId = esignMainAppId;
	}

	public EsignDocument getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(EsignDocument coverLetter) {
		this.coverLetter = coverLetter;
	}

	public EsignDocument getAuditReport() {
		return auditReport;
	}

	public void setAuditReport(EsignDocument auditReport) {
		this.auditReport = auditReport;
	}

	public EsignDocument getCpsDocument() {
		return cpsDocument;
	}

	public void setCpsDocument(EsignDocument cpsDocument) {
		this.cpsDocument = cpsDocument;
	}

	public ReviewESPApplication getEspReviewId() {
		return espReviewId;
	}

	public void setEspReviewId(ReviewESPApplication espReviewId) {
		this.espReviewId = espReviewId;
	}

	public ESPPurposeEntity getPurposeId() {
		return purposeId;
	}

	public void setPurposeId(ESPPurposeEntity purposeId) {
		this.purposeId = purposeId;
	}


	
	
	
	

}
