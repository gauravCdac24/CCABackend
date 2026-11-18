package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "license_review", schema = "license_issuance_management")
public class LicenseReview extends AbstractTimestampEntity {

	public static final String PENDING_CCA_REVIEW = "PENDING_CCA_REVIEW";
	public static final String SENT_TO_ADMIN = "SENT_TO_ADMIN";
	public static final String APPROVED = "APPROVED";

	public static final String FRESH = "FRESH";
	public static final String RENEWAL = "RENEWAL";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "license_review_id")
	private Long licenseReviewId;

	@Column(name = "intent_app_id", length = 255, nullable = false)
	private String intentAppId;

	@Column(name = "username", length = 50, nullable = false)
	private String userName;

	@Column(name = "serial_no", length = 24, nullable = false)
	private String serialNo;

	@Column(name = "issue_date", nullable = false)
	private Date issueDate;

	@Column(name = "expiry_date", nullable = false)
	private Date expiryDate;

	@Column(name = "public_key", columnDefinition = "TEXT", nullable = false)
	private String publicKey;

	@Column(name = "application_kind", length = 10, nullable = false)
	private String applicationKind;

	@Column(name = "status", length = 40, nullable = false)
	private String status;

	@Column(name = "cca_remark", columnDefinition = "TEXT")
	private String ccaRemark;

	@Column(name = "ca_serial", nullable = false)
	private Integer caSerial;

	@Column(name = "key_sequence", nullable = false)
	private Integer keySequence;

	@Column(name = "submitted_by", length = 74, nullable = false)
	private String submittedBy;

	@Column(name = "reviewed_by", length = 74)
	private String reviewedBy;

	public Long getLicenseReviewId() {
		return licenseReviewId;
	}

	public void setLicenseReviewId(Long licenseReviewId) {
		this.licenseReviewId = licenseReviewId;
	}

	public String getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getApplicationKind() {
		return applicationKind;
	}

	public void setApplicationKind(String applicationKind) {
		this.applicationKind = applicationKind;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCcaRemark() {
		return ccaRemark;
	}

	public void setCcaRemark(String ccaRemark) {
		this.ccaRemark = ccaRemark;
	}

	public Integer getCaSerial() {
		return caSerial;
	}

	public void setCaSerial(Integer caSerial) {
		this.caSerial = caSerial;
	}

	public Integer getKeySequence() {
		return keySequence;
	}

	public void setKeySequence(Integer keySequence) {
		this.keySequence = keySequence;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}
}
