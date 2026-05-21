package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditCriteria;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_criteria", schema = "cca_logs_management")
public class AuditCriteriaLogs extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_criteria_id", length = 11)
	private Long logAuditCriteriaId;
	
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

	public Long getLogAuditCriteriaId() {
		return logAuditCriteriaId;
	}

	public void setLogAuditCriteriaId(Long logAuditCriteriaId) {
		this.logAuditCriteriaId = logAuditCriteriaId;
	}

	public AuditCriteriaLogs(Long logAuditCriteriaId, Long auditCriteriaId, String auditCriteriaTitle, String status,
			String createdBy, String updatedBy) {
		super();
		this.logAuditCriteriaId = logAuditCriteriaId;
		this.auditCriteriaId = auditCriteriaId;
		this.auditCriteriaTitle = auditCriteriaTitle;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public AuditCriteriaLogs(AuditCriteria e,String IPAddress,String operation,String userName) {
		
		this.auditCriteriaId = e.getAuditCriteriaId();
		this.auditCriteriaTitle = e.getAuditCriteriaTitle();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}
	
	
}
