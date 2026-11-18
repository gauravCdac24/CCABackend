package in.lms.cca.dto;

import java.time.LocalDate;

public class RenewalWindowView {

	private String window;
	private boolean canApply;
	private boolean delayDocumentRequired;
	private long daysLeft;
	private LocalDate issueDate;
	private LocalDate expiryDate;
	private LocalDate extensionGrantedOn;
	private LocalDate extensionUntil;
	private String message;
	private Long licenseId;
	private String serialNo;
	private String licenseName;
	private String userName;

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}

	public boolean isCanApply() {
		return canApply;
	}

	public void setCanApply(boolean canApply) {
		this.canApply = canApply;
	}

	public boolean isDelayDocumentRequired() {
		return delayDocumentRequired;
	}

	public void setDelayDocumentRequired(boolean delayDocumentRequired) {
		this.delayDocumentRequired = delayDocumentRequired;
	}

	public long getDaysLeft() {
		return daysLeft;
	}

	public void setDaysLeft(long daysLeft) {
		this.daysLeft = daysLeft;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDate getExtensionGrantedOn() {
		return extensionGrantedOn;
	}

	public void setExtensionGrantedOn(LocalDate extensionGrantedOn) {
		this.extensionGrantedOn = extensionGrantedOn;
	}

	public LocalDate getExtensionUntil() {
		return extensionUntil;
	}

	public void setExtensionUntil(LocalDate extensionUntil) {
		this.extensionUntil = extensionUntil;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
