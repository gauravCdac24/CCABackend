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
@Table(name = "scrutiny_documents", schema = "new_license_management")
public class ScrutinyDocuments extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrutiny_doc_id", length = 11)
    private Long scrutinyDocId;

    @ManyToOne
    @JoinColumn(name = "scrutiny_id", referencedColumnName = "scrutiny_id")
    private ScrutinyApplication scrutinyId;

    @ManyToOne
    @JoinColumn(name = "intent_documents_id", referencedColumnName = "intent_documents_id")
    private IntentDocuments intentDocumentsId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getScrutinyDocId() {
		return scrutinyDocId;
	}

	public void setScrutinyDocId(Long scrutinyDocId) {
		this.scrutinyDocId = scrutinyDocId;
	}

	public ScrutinyApplication getScrutinyId() {
		return scrutinyId;
	}

	public void setScrutinyId(ScrutinyApplication scrutinyId) {
		this.scrutinyId = scrutinyId;
	}

	public IntentDocuments getIntentDocumentsId() {
		return intentDocumentsId;
	}

	public void setIntentDocumentsId(IntentDocuments intentDocumentsId) {
		this.intentDocumentsId = intentDocumentsId;
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
