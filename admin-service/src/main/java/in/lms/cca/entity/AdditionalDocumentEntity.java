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
@Table(name = "additional_document", schema="admin_user_management")
public class AdditionalDocumentEntity extends AbstractTimestampEntity{
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "additional_document_id", length = 11)
	private Long additionalDocumentId;
	
	@ManyToOne
	@JoinColumn(name = "app_type_master_id", referencedColumnName = "app_type_master_id")
	private ApplicationTypeMaster appTypeMasterId;
	
	@Column(name = "document_name", length = 60)
	private String documentName;
	
	@Column(name = "status", length = 8)
	private String status;

	public Long getAdditionalDocumentId() {
		return additionalDocumentId;
	}

	public void setAdditionalDocumentId(Long additionalDocumentId) {
		this.additionalDocumentId = additionalDocumentId;
	}

	public ApplicationTypeMaster getAppTypeMasterId() {
		return appTypeMasterId;
	}

	public void setAppTypeMasterId(ApplicationTypeMaster appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
