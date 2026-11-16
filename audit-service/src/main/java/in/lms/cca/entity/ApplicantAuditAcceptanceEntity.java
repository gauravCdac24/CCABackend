package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "applicant_audit_acceptance", schema = "audit_management")
public class ApplicantAuditAcceptanceEntity extends AbstractTimestampEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acceptance_id")
	private Long acceptanceId;

	@Column(name = "applicant_user_name", length = 74)
	private String applicantUserName;

	@Column(name = "letter_downloaded")
	private Boolean letterDownloaded = Boolean.FALSE;

	@Column(name = "uploaded_file_name", length = 200)
	private String uploadedFileName;

	/** NONE / PENDING / COMPLETED */
	@Column(name = "esign_status", length = 20)
	private String esignStatus = "NONE";

	@Column(name = "signed_doc_id", length = 100)
	private String signedDocId;

	@Column(name = "status", length = 15)
	private String status = "Active";

	public Long getAcceptanceId() {
		return acceptanceId;
	}

	public void setAcceptanceId(Long acceptanceId) {
		this.acceptanceId = acceptanceId;
	}

	public String getApplicantUserName() {
		return applicantUserName;
	}

	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}

	public Boolean getLetterDownloaded() {
		return letterDownloaded;
	}

	public void setLetterDownloaded(Boolean letterDownloaded) {
		this.letterDownloaded = letterDownloaded;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public String getEsignStatus() {
		return esignStatus;
	}

	public void setEsignStatus(String esignStatus) {
		this.esignStatus = esignStatus;
	}

	public String getSignedDocId() {
		return signedDocId;
	}

	public void setSignedDocId(String signedDocId) {
		this.signedDocId = signedDocId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
