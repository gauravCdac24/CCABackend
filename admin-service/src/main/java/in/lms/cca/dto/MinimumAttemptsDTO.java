package in.lms.cca.dto;

public class MinimumAttemptsDTO {
	
	private String attemptId;
	private Integer applicationScrutiny;
	private Integer applicationReview;
	private Integer applicationAudit;
	private Integer esignApplicationReview;
	private Integer annualAuditReview;
	private String status;
	private String created;
	private String updated;
	
	public String getAttemptId() {
		return attemptId;
	}
	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}
	public Integer getApplicationScrutiny() {
		return applicationScrutiny;
	}
	public void setApplicationScrutiny(Integer applicationScrutiny) {
		this.applicationScrutiny = applicationScrutiny;
	}
	public Integer getApplicationReview() {
		return applicationReview;
	}
	public void setApplicationReview(Integer applicationReview) {
		this.applicationReview = applicationReview;
	}
	public Integer getApplicationAudit() {
		return applicationAudit;
	}
	public void setApplicationAudit(Integer applicationAudit) {
		this.applicationAudit = applicationAudit;
	}
	public Integer getEsignApplicationReview() {
		return esignApplicationReview;
	}
	public void setEsignApplicationReview(Integer esignApplicationReview) {
		this.esignApplicationReview = esignApplicationReview;
	}
	public Integer getAnnualAuditReview() {
		return annualAuditReview;
	}
	public void setAnnualAuditReview(Integer annualAuditReview) {
		this.annualAuditReview = annualAuditReview;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
	


}
