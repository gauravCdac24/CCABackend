package in.lms.cca.dto;

import java.util.List;

public class ASPDetailsDTO{

	    private String aspId;
	    private String caUsername;
	    private String aspName;
	    private String emailId;
	    private String countryId;
	    private String stateId;
	    private String onBoardingDate;
	    private String exitDate;
	    private String apiSpecificationId;
	    private String apiVersionId;
	    private List<AuditReportItem> auditReports;

	    public static class AuditReportItem {
	    	private String fileName;
	    	private String fileContent;
	    	private String auditDate;

	    	public String getFileName() {
	    		return fileName;
	    	}
	    	public void setFileName(String fileName) {
	    		this.fileName = fileName;
	    	}
	    	public String getFileContent() {
	    		return fileContent;
	    	}
	    	public void setFileContent(String fileContent) {
	    		this.fileContent = fileContent;
	    	}
	    	public String getAuditDate() {
	    		return auditDate;
	    	}
	    	public void setAuditDate(String auditDate) {
	    		this.auditDate = auditDate;
	    	}
	    }
	    
	    
		public String getAspId() {
			return aspId;
		}
		public void setAspId(String aspId) {
			this.aspId = aspId;
		}
		public String getCaUsername() {
			return caUsername;
		}
		public void setCaUsername(String caUsername) {
			this.caUsername = caUsername;
		}
		public String getAspName() {
			return aspName;
		}
		public void setAspName(String aspName) {
			this.aspName = aspName;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getCountryId() {
			return countryId;
		}
		public void setCountryId(String countryId) {
			this.countryId = countryId;
		}
		public String getStateId() {
			return stateId;
		}
		public void setStateId(String stateId) {
			this.stateId = stateId;
		}
		public String getOnBoardingDate() {
			return onBoardingDate;
		}
		public void setOnBoardingDate(String onBoardingDate) {
			this.onBoardingDate = onBoardingDate;
		}
		public String getExitDate() {
			return exitDate;
		}
		public void setExitDate(String exitDate) {
			this.exitDate = exitDate;
		}
		public String getApiSpecificationId() {
			return apiSpecificationId;
		}
		public void setApiSpecificationId(String apiSpecificationId) {
			this.apiSpecificationId = apiSpecificationId;
		}
		public String getApiVersionId() {
			return apiVersionId;
		}
		public void setApiVersionId(String apiVersionId) {
			this.apiVersionId = apiVersionId;
		}
		public List<AuditReportItem> getAuditReports() {
			return auditReports;
		}
		public void setAuditReports(List<AuditReportItem> auditReports) {
			this.auditReports = auditReports;
		}
		
	    
	    
		
	    
	    
		
}
