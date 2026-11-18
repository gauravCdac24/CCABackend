package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_subcriteria", schema="admin_user_management")
public class AuditSubCriteria extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_subcriteria_id", length = 11)
	private Long auditSubCriteriaId;
	
	@ManyToOne
	@JoinColumn(name = "audit_criteria_id", referencedColumnName = "audit_criteria_id")
	private AuditCriteria auditCriteriaId;
	
	@Column(name = "audit_subcriteria_title", length = 255)
	private String auditSubCriteriaTitle;
	
	@Column(name="isvisible")
	private boolean isVisible;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;
	

	public Long getAuditSubCriteriaId() {
		return auditSubCriteriaId;
	}

	public void setAuditSubCriteriaId(Long auditSubCriteriaId) {
		this.auditSubCriteriaId = auditSubCriteriaId;
	}

	public AuditCriteria getAuditCriteriaId() {
		return auditCriteriaId;
	}

	public void setAuditCriteriaId(AuditCriteria auditCriteriaId) {
		this.auditCriteriaId = auditCriteriaId;
	}

	public String getAuditSubCriteriaTitle() {
		return auditSubCriteriaTitle;
	}

	public void setAuditSubCriteriaTitle(String auditSubCriteriaTitle) {
		this.auditSubCriteriaTitle = auditSubCriteriaTitle;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
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
