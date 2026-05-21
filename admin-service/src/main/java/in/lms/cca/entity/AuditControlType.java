package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_control_type", schema="admin_user_management")
public class AuditControlType extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_control_type_id", length = 4)
	private Integer auditControlTypeId;
	
	@Column(name = "audit_control_desc", length=14)
	private String auditControlDesc;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	
	public Integer getAuditControlTypeId() {
		return auditControlTypeId;
	}

	public void setAuditControlTypeId(Integer auditControlTypeId) {
		this.auditControlTypeId = auditControlTypeId;
	}

	public String getAuditControlDesc() {
		return auditControlDesc;
	}

	public void setAuditControlDesc(String auditControlDesc) {
		this.auditControlDesc = auditControlDesc;
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
