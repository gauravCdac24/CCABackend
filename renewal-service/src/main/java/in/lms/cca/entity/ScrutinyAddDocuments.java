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
@Table(name = "scrutiny_add_documents", schema = "new_license_management")
public class ScrutinyAddDocuments extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrutiny_add_doc_id", length = 11)
    private Long scrutinyAddDocId;

    @ManyToOne
    @JoinColumn(name = "scrutiny_id", referencedColumnName = "scrutiny_id")
    private ScrutinyApplication scrutinyId;

    @ManyToOne
	@JoinColumn(name = "intent_additional_doc_id", referencedColumnName = "intent_additional_doc_id")
    private IntentAdditionalDocuments intentAdditionalDocId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getScrutinyAddDocId() {
		return scrutinyAddDocId;
	}

	public void setScrutinyAddDocId(Long scrutinyAddDocId) {
		this.scrutinyAddDocId = scrutinyAddDocId;
	}

	public ScrutinyApplication getScrutinyId() {
		return scrutinyId;
	}

	public void setScrutinyId(ScrutinyApplication scrutinyId) {
		this.scrutinyId = scrutinyId;
	}

	public IntentAdditionalDocuments getIntentAdditionalDocId() {
		return intentAdditionalDocId;
	}

	public void setIntentAdditionalDocId(IntentAdditionalDocuments intentAdditionalDocId) {
		this.intentAdditionalDocId = intentAdditionalDocId;
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
