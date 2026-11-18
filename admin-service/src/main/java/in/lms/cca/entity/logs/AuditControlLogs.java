package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditControl;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
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
@Table(name="audit_control_log", schema = "cca_logs_management")
public class AuditControlLogs extends LogAbstractTimestampOperationEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_control_id", length = 11)
	private Long logAuditControlId;
	
	@Column(name = "audit_control_id", length = 11)
	private Long auditControlId;

	
    @Column(name = "control_desc", columnDefinition = "TEXT")
	private String controlDesc;
	

	
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


	public String getControlDesc() {
		return controlDesc;
	}

	public void setControlDesc(String controlDesc) {
		this.controlDesc = controlDesc;
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

	public Long getLogAuditControlId() {
		return logAuditControlId;
	}

	public void setLogAuditControlId(Long logAuditControlId) {
		this.logAuditControlId = logAuditControlId;
	}

	public AuditControlLogs(Long logAuditControlId, Long auditControlId, AuditParameterLogs auditParameterId,
			String controlDesc, AuditCheckLogs auditCheckId, AuditControlTypeLogs auditControlTypeId, String references,
			String status, String createdBy, String updatedBy) {
		super();
		this.logAuditControlId = logAuditControlId;
		this.auditControlId = auditControlId;
		
		this.controlDesc = controlDesc;
	
		this.references = references;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public AuditControlLogs(AuditControl e, String IPAddress,String operation,String userName) {
		
		this.auditControlId = e.getAuditControlId();
		this.controlDesc = e.getControlDesc();
		this.references = e.getReferences();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	
	
}
