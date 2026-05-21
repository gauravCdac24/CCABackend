package in.lms.cca.entity.logs;

import in.lms.cca.entity.MinimumAttempts;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "minimum_attempt_logs", schema = "cca_logs_management")
public class MinimumAttemptLog extends LogAbstractTimestampOperationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attempt_id", length = 11)
	private Long attemptId;
	
	@Column(name = "application_scrutiny", length = 2)
	private Integer applicationScrutiny;
	
	@Column(name = "application_review", length = 2)
	private Integer applicationReview;
	
	@Column(name = "application_audit", length = 2)
	private Integer applicationAudit;
	
	@Column(name = "esign_application_review", length = 2)
	private Integer esignApplicationReview;
	
	@Column(name = "annual_audit_review", length = 2)
	private Integer annualAuditReview;
	
	@Column(name = "status", length = 8)
	private String status;

	public Long getAttemptId() {
		return attemptId;
	}

	public void setAttemptId(Long attemptId) {
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

	public MinimumAttemptLog(Long attemptId, Integer applicationScrutiny, Integer applicationReview,
			Integer applicationAudit, Integer esignApplicationReview, Integer annualAuditReview, String status) {
		super();
		this.attemptId = attemptId;
		this.applicationScrutiny = applicationScrutiny;
		this.applicationReview = applicationReview;
		this.applicationAudit = applicationAudit;
		this.esignApplicationReview = esignApplicationReview;
		this.annualAuditReview = annualAuditReview;
		this.status = status;
	}
	
	public MinimumAttemptLog(MinimumAttempts e,String IPAddress,String operation,String userName) {
		
		this.applicationScrutiny = e.getApplicationScrutiny();
		this.applicationReview = e.getApplicationReview();
		this.applicationAudit = e.getApplicationAudit();
		this.esignApplicationReview = e.getEsignApplicationReview();
		this.annualAuditReview = e.getAnnualAuditReview();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}
	
	

}
