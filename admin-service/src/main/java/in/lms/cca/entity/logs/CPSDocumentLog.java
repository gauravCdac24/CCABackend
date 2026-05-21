package in.lms.cca.entity.logs;

import java.util.Date;

import in.lms.cca.entity.CPSDocument;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cps_document_logs", schema="cca_logs_management")
public class CPSDocumentLog extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_cps_doc_id", length = 11)
	private Long logCpsDocId;
	
	@Column(name = "cps_doc_id", length = 11)
	private Long cpsDocId;
	
	@Column(name = "version", length = 7)
	private String version;
	
	@Column(name = "publish_date")
	private Date publishDate;
	
	@Column(name = "filename", length = 30)
	private String fileName;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	public Long getCpsDocId() {
		return cpsDocId;
	}

	public void setCpsDocId(Long cpsDocId) {
		this.cpsDocId = cpsDocId;
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

	public Long getLogCpsDocId() {
		return logCpsDocId;
	}

	public void setLogCpsDocId(Long logCpsDocId) {
		this.logCpsDocId = logCpsDocId;
	}

	public CPSDocumentLog(Long logCpsDocId, Long cpsDocId, String version, Date publishDate, String fileName,
			String documentName, String status, String createdBy, String updatedBy) {
		super();
		this.logCpsDocId = logCpsDocId;
		this.cpsDocId = cpsDocId;
		this.version = version;
		this.publishDate = publishDate;
		this.fileName = fileName;
		this.status = status;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}
	
	public CPSDocumentLog(CPSDocument e,String IPAddress,String operation,String userName) {
		
		this.cpsDocId = e.getCpsDocId();
		this.version = e.getVersion();
		this.publishDate = e.getPublishDate();
		this.fileName = e.getFileName();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}
	
	
 
}
