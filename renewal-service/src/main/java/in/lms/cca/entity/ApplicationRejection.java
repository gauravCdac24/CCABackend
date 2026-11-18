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
@Table(name = "application_rejection", schema="new_license_management")
public class ApplicationRejection extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rejection_id", length = 11)
	private Long rejectionId;
	
	@ManyToOne
	@JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
	private IntentApplication intentAppId;
	
	@Column(name = "rejection_status", length = 8)
	private String rejectionStatus;
	
	@Column(name = "initiated_by", length = 74)
	private String initiatedBy;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	
	public Long getRejectionId() {
		return rejectionId;
	}

	public void setRejectionId(Long rejectionId) {
		this.rejectionId = rejectionId;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getRejectionStatus() {
		return rejectionStatus;
	}

	public void setRejectionStatus(String rejectionStatus) {
		this.rejectionStatus = rejectionStatus;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
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
