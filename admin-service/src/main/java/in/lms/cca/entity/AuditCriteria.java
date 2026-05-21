package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_criteria", schema="admin_user_management")
public class AuditCriteria extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_criteria_id", length = 11)
	private Long auditCriteriaId;
	
	@Column(name = "audit_criteria_title", length = 255)
	private String auditCriteriaTitle;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	public Long getAuditCriteriaId() {
		return auditCriteriaId;
	}

	public void setAuditCriteriaId(Long auditCriteriaId) {
		this.auditCriteriaId = auditCriteriaId;
	}

	public String getAuditCriteriaTitle() {
		return auditCriteriaTitle;
	}

	public void setAuditCriteriaTitle(String auditCriteriaTitle) {
		this.auditCriteriaTitle = auditCriteriaTitle;
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
