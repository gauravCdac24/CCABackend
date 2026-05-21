package in.lms.cca.entity;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "esign_document", schema="esign_application_management")
public class EsignDocument extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esigndocid", length = 11)
    private Long eSignDocId;
	
	@ManyToOne
	@JoinColumn(name = "esign_licensee_appid", referencedColumnName = "esign_licensee_appid")
	private EsignLicenseeApplication esignLicenseeAppId;

	@Column(name = "esign_doc_type", length = 100)
	private String esignDocType;

	
	@Column(name = "filename", length = 100)
	private String fileName;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	@Column(name = "esign_doc", length = 74)
	private String esignDocument;
	

	public Long geteSignDocId() {
		return eSignDocId;
	}

	public void seteSignDocId(Long eSignDocId) {
		this.eSignDocId = eSignDocId;
	}

	public String getEsignDocType() {
		return esignDocType;
	}

	public void setEsignDocType(String esignDocType) {
		this.esignDocType = esignDocType;
	}

	public EsignLicenseeApplication getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(EsignLicenseeApplication esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
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

	public String getEsignDocument() {
		return esignDocument;
	}

	public void setEsignDocument(String esignDocument) {
		this.esignDocument = esignDocument;
	}

	

	
	
}
