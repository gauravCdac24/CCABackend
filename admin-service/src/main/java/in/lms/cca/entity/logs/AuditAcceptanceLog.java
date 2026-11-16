package in.lms.cca.entity.logs;

import java.util.Date;

import in.lms.cca.entity.AuditAcceptance;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_acceptance_logs", schema = "cca_logs_management")
public class AuditAcceptanceLog extends LogAbstractTimestampOperationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_acceptance_id")
	private Long logAuditAcceptanceId;

	@Column(name = "audit_acceptance_id")
	private Long auditAcceptanceId;

	@Column(name = "version", length = 10)
	private String version;

	@Column(name = "publish_date")
	private Date publishDate;

	@Column(name = "filename", length = 120)
	private String fileName;

	@Column(name = "status", length = 8)
	private String status;

	@Column(name = "created_by", length = 74)
	private String createdBy;

	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	@Column(name = "client_host_name", length = 100, nullable = false)
	private String clientHostName;

	public AuditAcceptanceLog() {
		super();
	}

	public AuditAcceptanceLog(AuditAcceptance document, String ipAddress, String operation, String userName) {
		this(document, ipAddress, operation, userName, "unknown");
	}

	public AuditAcceptanceLog(AuditAcceptance document, String ipAddress, String operation, String userName,
			String clientHostName) {
		this.auditAcceptanceId = document.getAuditAcceptanceId();
		this.version = document.getVersion();
		this.publishDate = document.getPublishDate();
		this.fileName = document.getFileName();
		this.status = document.getStatus();
		this.createdBy = document.getCreatedBy();
		this.updatedBy = document.getUpdatedBy();
		this.clientHostName = clientHostName != null && !clientHostName.isBlank() ? clientHostName : "unknown";
		this.setIPAddress(ipAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public Long getLogAuditAcceptanceId() {
		return logAuditAcceptanceId;
	}

	public void setLogAuditAcceptanceId(Long logAuditAcceptanceId) {
		this.logAuditAcceptanceId = logAuditAcceptanceId;
	}

	public Long getAuditAcceptanceId() {
		return auditAcceptanceId;
	}

	public void setAuditAcceptanceId(Long auditAcceptanceId) {
		this.auditAcceptanceId = auditAcceptanceId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getClientHostName() {
		return clientHostName;
	}

	public void setClientHostName(String clientHostName) {
		this.clientHostName = clientHostName;
	}
}
