package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_control", schema="admin_user_management")
public class AuditControl extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_control_id", length = 11)
	private Long auditControlId;
	
	@ManyToOne
	@JoinColumn(name = "audit_parameter_id", referencedColumnName = "audit_parameter_id")
	private AuditParameter auditParameterId;
	
	
    @Column(name = "control_desc", columnDefinition = "TEXT")
	private String controlDesc;
	
	@OneToOne
	@JoinColumn(name = "audit_check_id", referencedColumnName = "audit_check_id")
	private AuditCheck auditCheckId;
	
	@OneToOne
	@JoinColumn(name = "audit_control_type_id", referencedColumnName = "audit_control_type_id")
	private AuditControlType auditControlTypeId;
	
	@ManyToOne
	@JoinColumn(name = "self_audit_control_id", referencedColumnName = "audit_control_id")
	private AuditControl selfAuditControlId;
	
	 @Column(name = "effective_date")
	 private Date effectiveDate;

	 @Column(name = "valid_till")
	 private Date validTill;
	
	@Column(name = "reference_info", length = 60)
	private String references;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	public Long getAuditControlId() {
		return auditControlId;
	}

	public void setAuditControlId(Long auditControlId) {
		this.auditControlId = auditControlId;
	}

	public AuditParameter getAuditParameterId() {
		return auditParameterId;
	}

	public void setAuditParameterId(AuditParameter auditParameterId) {
		this.auditParameterId = auditParameterId;
	}

	public String getControlDesc() {
		return controlDesc;
	}

	public void setControlDesc(String controlDesc) {
		this.controlDesc = controlDesc;
	}

	public AuditCheck getAuditCheckId() {
		return auditCheckId;
	}

	public void setAuditCheckId(AuditCheck auditCheckId) {
		this.auditCheckId = auditCheckId;
	}

	public AuditControlType getAuditControlTypeId() {
		return auditControlTypeId;
	}

	public void setAuditControlTypeId(AuditControlType auditControlTypeId) {
		this.auditControlTypeId = auditControlTypeId;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
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

	public AuditControl getSelfAuditControlId() {
		return selfAuditControlId;
	}

	public void setSelfAuditControlId(AuditControl selfAuditControlId) {
		this.selfAuditControlId = selfAuditControlId;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	
	
	
}
