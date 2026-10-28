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
@Table(name = "intent_unique_code", schema="admin_user_management")
public class IntentUniqueCode extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unique_code_id", length = 11)
	private Long uniqueCodeId;
	
	@Column(name = "unique_code", length = 8)
	private Long uniqueCode;
	
	@Column(name = "email_id", length = 50, unique=true)
	private String emailId;
	
	@Column(name = "mobile_no", length = 10, unique=true)
	private String mobileNo;
	
	@Column(name = "organization_name", length = 75)
	private String organizationName;
	
	@ManyToOne
	@JoinColumn(name = "app_type_master_id", referencedColumnName = "app_type_master_id")
	private ApplicationTypeMaster appTypeMasterId;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "createdby", length = 74)
	private String createdBy;
	
	@Column(name = "updatedby", length = 74)
	private String updatedBy;


	public Long getUniqueCodeId() {
		return uniqueCodeId;
	}

	public void setUniqueCodeId(Long uniqueCodeId) {
		this.uniqueCodeId = uniqueCodeId;
	}

	public Long getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(Long uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public ApplicationTypeMaster getAppTypeMasterId() {
		return appTypeMasterId;
	}

	public void setAppTypeMasterId(ApplicationTypeMaster appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
	}
	
	
	
}
