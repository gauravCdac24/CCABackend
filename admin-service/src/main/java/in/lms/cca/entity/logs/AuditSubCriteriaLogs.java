package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditSubCriteria;
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
@Table(name="log_audit_subcriteria", schema = "cca_logs_management" )
public class AuditSubCriteriaLogs extends LogAbstractTimestampOperationEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_subcriteria_id", length = 11)
	private Long logAuditSubCriteriaId;
	
	@Column(name = "audit_subcriteria_id", length = 11)
	private Long auditSubCriteriaId;
	
	
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

	public Long getLogAuditSubCriteriaId() {
		return logAuditSubCriteriaId;
	}

	public void setLogAuditSubCriteriaId(Long logAuditSubCriteriaId) {
		this.logAuditSubCriteriaId = logAuditSubCriteriaId;
	}

	public AuditSubCriteriaLogs(Long logAuditSubCriteriaId, Long auditSubCriteriaId, AuditCriteriaLogs auditCriteriaId,
			String auditSubCriteriaTitle, boolean isVisible, String status, String createdBy, String updatedBy) {
		super();
		this.logAuditSubCriteriaId = logAuditSubCriteriaId;
		this.auditSubCriteriaId = auditSubCriteriaId;
		this.auditSubCriteriaTitle = auditSubCriteriaTitle;
		this.isVisible = isVisible;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public AuditSubCriteriaLogs(AuditSubCriteria e,String IPAddress,String operation,String userName) {
		
		this.auditSubCriteriaId = e.getAuditSubCriteriaId();
		this.auditSubCriteriaTitle = e.getAuditSubCriteriaTitle();
		this.isVisible = e.isVisible();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	
	
}
