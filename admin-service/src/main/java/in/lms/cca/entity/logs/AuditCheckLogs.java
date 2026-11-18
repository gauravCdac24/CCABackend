package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditCheck;
import in.lms.cca.util.global.AbstractTimestampEntity;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_check_logs", schema = "cca_logs_management")
public class AuditCheckLogs extends LogAbstractTimestampOperationEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_check_id", length = 11)
	private Long logAuditCheckId;
	
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

	public Long getLogAuditCheckId() {
		return logAuditCheckId;
	}

	public void setLogAuditCheckId(Long logAuditCheckId) {
		this.logAuditCheckId = logAuditCheckId;
	}

	public AuditCheckLogs(Long logAuditCheckId, Long auditCheckId, String auditCheckDesc, String status, String createdBy,
			String updatedBy) {
		super();
		this.logAuditCheckId = logAuditCheckId;
		this.auditCheckId = auditCheckId;
		this.auditCheckDesc = auditCheckDesc;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public AuditCheckLogs(AuditCheck e,String IPAddress,String operation,String userName) {
		
		this.auditCheckId = e.getAuditCheckId();
		this.auditCheckDesc = e.getAuditCheckDesc();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
		
	}

	public AuditCheckLogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
