package in.lms.cca.dto;


public class ReviewApplicationDTO {

	private String esignLicenseeAppId;
	private Boolean ekycMode;
	private Boolean coverLetter;
	private Boolean esignAPIVersion;
	private Boolean auditReport;
	private Boolean cpsDocument;
	private Boolean purpose;
	private String remarks;
	private String userName;
	private Boolean isreject;
	
	
	public String getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}
	public void setEsignLicenseeAppId(String esignLicenseeAppId) {
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Boolean getIsreject() {
		return isreject;
	}
	public void setIsreject(Boolean isreject) {
		this.isreject = isreject;
	}
	public Boolean getPurpose() {
		return purpose;
	}
	public void setPurpose(Boolean purpose) {
		this.purpose = purpose;
	}
	
	
	
	
}
