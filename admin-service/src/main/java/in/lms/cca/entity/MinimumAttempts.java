package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "minimum_attempts", schema="admin_user_management")
public class MinimumAttempts extends AbstractTimestampEntity{

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
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	
}
