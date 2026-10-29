package in.lms.cca.dto;

public class LicenseFormDefaultsDTO {

	private String applicationKind;
	private String serialNo;
	private String previousSerialNo;
	private String issueDate;
	private String expiryDate;
	private boolean issueDateEditable;
	private String publicKey;
	private String ccaRemark;
	private Integer caSerial;
	private Integer keySequence;
	private Integer rootCaKeyCount;
	private Long reviewId;
	private String reviewStatus;

	public String getApplicationKind() {
		return applicationKind;
	}

	public void setApplicationKind(String applicationKind) {
		this.applicationKind = applicationKind;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPreviousSerialNo() {
		return previousSerialNo;
	}

	public void setPreviousSerialNo(String previousSerialNo) {
		this.previousSerialNo = previousSerialNo;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isIssueDateEditable() {
		return issueDateEditable;
	}

	public void setIssueDateEditable(boolean issueDateEditable) {
		this.issueDateEditable = issueDateEditable;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
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

	public Integer getRootCaKeyCount() {
		return rootCaKeyCount;
	}

	public void setRootCaKeyCount(Integer rootCaKeyCount) {
		this.rootCaKeyCount = rootCaKeyCount;
	}

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
}
