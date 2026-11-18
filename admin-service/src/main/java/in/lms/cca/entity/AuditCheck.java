package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_check", schema="admin_user_management")
public class AuditCheck extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_check_id", length = 11)
	private Long auditCheckId;
	
	
    @Column(name = "audit_check_desc", columnDefinition = "TEXT")
	private String auditCheckDesc;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	public Long getAuditCheckId() {
		return auditCheckId;
	}

	public void setAuditCheckId(Long auditCheckId) {
		this.auditCheckId = auditCheckId;
	}

	public String getAuditCheckDesc() {
		return auditCheckDesc;
	}

	public void setAuditCheckDesc(String auditCheckDesc) {
		this.auditCheckDesc = auditCheckDesc;
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
