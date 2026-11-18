package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "intent_application", schema="new_license_management")
public class IntentApplication extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intent_app_id", length = 11)
	private Long intentAppId;
	
	@Column(name = "licensee_id",length = 74)
	private String licenseeId;

	@Column(name = "app_type_master_id",length = 30)
	private String appTypeMasterId;
	
	@Column(name = "user_name", length = 74)
	private String userName;
	
	@Column(name = "unique_code", length = 10)
	private String uniqueCode;
	
	@Column(name = "acknowledgementno", length = 100)
	private String acknowledgementNo;
	
	@Column(name = "application_status", length = 200)
	private String applicationStatus;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	
	public Long getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(Long intentAppId) {
		this.intentAppId = intentAppId;
	}

	
	public String getAppTypeMasterId() {
		return appTypeMasterId;
	}

	public void setAppTypeMasterId(String appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
	}

	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}

	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLicenseeId() {
		return licenseeId;
	}

	public void setLicenseeId(String licenseeId) {
		this.licenseeId = licenseeId;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}
	
	

}