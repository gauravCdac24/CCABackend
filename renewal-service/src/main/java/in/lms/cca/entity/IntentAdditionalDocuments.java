package in.lms.cca.entity;

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
@Table(name = "intent_additional_documents", schema = "new_license_management")
public class IntentAdditionalDocuments extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intent_additional_doc_id", length = 11)
    private Long intentAdditionalDocId;

    @Column(name = "document_title", length = 250)
    private String documentTitle;

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
    
    

	public Long getIntentAdditionalDocId() {
		return intentAdditionalDocId;
	}

	public void setIntentAdditionalDocId(Long intentAdditionalDocId) {
		this.intentAdditionalDocId = intentAdditionalDocId;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
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

