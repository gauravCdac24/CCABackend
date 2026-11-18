package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class StepThreeDTO {

	private String userName;
	private String esignApiSpecId;
	private String apiVersionId;
	private MultipartFile auditReportFile;
	private MultipartFile cpsFile;
	private String auditReportFileName;
	private String cpsFileName;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEsignApiSpecId() {
		return esignApiSpecId;
	}
	public void setEsignApiSpecId(String esignApiSpecId) {
		this.esignApiSpecId = esignApiSpecId;
	}
	public String getApiVersionId() {
		return apiVersionId;
	}
	public void setApiVersionId(String apiVersionId) {
		this.apiVersionId = apiVersionId;
	}
	public MultipartFile getAuditReportFile() {
		return auditReportFile;
	}
	public void setAuditReportFile(MultipartFile auditReportFile) {
		this.auditReportFile = auditReportFile;
	}
	public MultipartFile getCpsFile() {
		return cpsFile;
	}
	public void setCpsFile(MultipartFile cpsFile) {
		this.cpsFile = cpsFile;
	}
	public String getAuditReportFileName() {
		return auditReportFileName;
	}
	public void setAuditReportFileName(String auditReportFileName) {
		this.auditReportFileName = auditReportFileName;
	}
	public String getCpsFileName() {
		return cpsFileName;
	}
	public void setCpsFileName(String cpsFileName) {
		this.cpsFileName = cpsFileName;
	}
	
	
	
	
	

	
}
