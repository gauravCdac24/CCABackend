package in.lms.cca.entity.logs;

import java.util.Date;

import in.lms.cca.entity.AgreementTemplate;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agreement_template_logs", schema = "cca_logs_management")
public class AgreementTemplateLog extends LogAbstractTimestampOperationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_template_id")
	private Long logTemplateId;

	@Column(name = "template_id")
	private Long templateId;

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

	public AgreementTemplateLog() {
		super();
	}

	public AgreementTemplateLog(AgreementTemplate template, String ipAddress, String operation, String userName) {
		this(template, ipAddress, operation, userName, "unknown");
	}

	public AgreementTemplateLog(AgreementTemplate template, String ipAddress, String operation, String userName,
			String clientHostName) {
		this.templateId = template.getTemplateId();
		this.version = template.getVersion();
		this.publishDate = template.getPublishDate();
		this.fileName = template.getFileName();
		this.status = template.getStatus();
		this.createdBy = template.getCreatedBy();
		this.updatedBy = template.getUpdatedBy();
		this.clientHostName = clientHostName != null && !clientHostName.isBlank() ? clientHostName : "unknown";
		this.setIPAddress(ipAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public Long getLogTemplateId() {
		return logTemplateId;
	}

	public void setLogTemplateId(Long logTemplateId) {
		this.logTemplateId = logTemplateId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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
