package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditParameter;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_parameter", schema = "cca_logs_management")
public class AuditParameterLogs extends LogAbstractTimestampOperationEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_parameter_id", length = 11)
	private Long logAuditParameterId;
	
	@Column(name = "audit_parameter_id", length = 11)
	private Long auditParameterId;
	
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

	public Long getLogAuditParameterId() {
		return logAuditParameterId;
	}

	public void setLogAuditParameterId(Long logAuditParameterId) {
		this.logAuditParameterId = logAuditParameterId;
	}

	public AuditParameterLogs(Long logAuditParameterId, Long auditParameterId, AuditSubCriteriaLogs auditSubCriteriaId,
			String auditParameterTitle, String status, String createdBy, String updatedBy) {
		super();
		this.logAuditParameterId = logAuditParameterId;
		this.auditParameterId = auditParameterId;
		this.auditParameterTitle = auditParameterTitle;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
	
	
	public AuditParameterLogs(AuditParameter e,String IPAddress,String operation,String userName) {
		
		this.auditParameterId = e.getAuditParameterId();
		this.auditParameterTitle = e.getAuditParameterTitle();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}
	
	
}
