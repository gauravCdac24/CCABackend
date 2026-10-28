package in.lms.cca.entity;

import in.lms.cca.entity.master.DocumentsMaster;
import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "intent_documents", schema = "new_license_management")
public class IntentDocuments extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intent_documents_id", length = 11)
    private Long intentDocumentsId;

    @ManyToOne
	@JoinColumn(name = "documents_id", referencedColumnName = "documents_id")
    private DocumentsMaster documentsId;

    @ManyToOne
	@JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;

    @Column(name = "file_name", length = 30)
    private String fileName;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;  

	public Long getIntentDocumentsId() {
		return intentDocumentsId;
	}

	public void setIntentDocumentsId(Long intentDocumentsId) {
		this.intentDocumentsId = intentDocumentsId;
	}

	public DocumentsMaster getDocumentsId() {
		return documentsId;
	}

	public void setDocumentsId(DocumentsMaster documentsId) {
		this.documentsId = documentsId;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
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
    
}
