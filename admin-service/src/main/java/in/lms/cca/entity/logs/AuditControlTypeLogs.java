package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditControlType;
import in.lms.cca.util.global.AbstractTimestampEntity;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="log_audit_control_type", schema = "cca_logs_management")
public class AuditControlTypeLogs extends LogAbstractTimestampOperationEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_control_type_id", length = 4)
	private Integer logAuditControlTypeId;
	
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

	public Integer getLogAuditControlTypeId() {
		return logAuditControlTypeId;
	}

	public void setLogAuditControlTypeId(Integer logAuditControlTypeId) {
		this.logAuditControlTypeId = logAuditControlTypeId;
	}

	public AuditControlTypeLogs(Integer logAuditControlTypeId, Integer auditControlTypeId, String auditControlDesc,
			String status, String createdBy, String updatedBy) {
		super();
		this.logAuditControlTypeId = logAuditControlTypeId;
		this.auditControlTypeId = auditControlTypeId;
		this.auditControlDesc = auditControlDesc;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	
	public AuditControlTypeLogs(AuditControlType e,String IPAddress,String operation,String userName) {
		
		this.auditControlTypeId = e.getAuditControlTypeId();
		this.auditControlDesc = e.getAuditControlDesc();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	
	
	
	
}
