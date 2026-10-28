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
@Table(name="audit_parameter", schema="admin_user_management")
public class AuditParameter extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_parameter_id", length = 11)
	private Long auditParameterId;
	
	@ManyToOne
	@JoinColumn(name = "audit_subcriteria_id", referencedColumnName = "audit_subcriteria_id")
	private AuditSubCriteria auditSubCriteriaId;
	
	@Column(name = "audit_parameter_title", length = 255)
	private String auditParameterTitle;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	
	
	public Long getAuditParameterId() {
		return auditParameterId;
	}

	public void setAuditParameterId(Long auditParameterId) {
		this.auditParameterId = auditParameterId;
	}

	public AuditSubCriteria getAuditSubCriteriaId() {
		return auditSubCriteriaId;
	}

	public void setAuditSubCriteriaId(AuditSubCriteria auditSubCriteriaId) {
		this.auditSubCriteriaId = auditSubCriteriaId;
	}

	public String getAuditParameterTitle() {
		return auditParameterTitle;
	}

	public void setAuditParameterTitle(String auditParameterTitle) {
		this.auditParameterTitle = auditParameterTitle;
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
