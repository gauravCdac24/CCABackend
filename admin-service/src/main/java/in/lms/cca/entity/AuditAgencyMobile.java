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
@Table(name = "audit_agency_mobile", schema="admin_user_management")
public class AuditAgencyMobile extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agency_mobile_id", length = 11)
	private Long agencyMobileId;
	
	@ManyToOne
	@JoinColumn(name = "audit_agency_id", referencedColumnName = "audit_agency_id")
	private AuditAgency auditAgencyId;
	
	@Column(name = "contactno", length = 10)
	private String contactNo;
	
	@Column(name = "areacode", length = 3)
	private String areaCode;
	
	@Column(name = "contact_type", length = 10)
	private String contactType;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;

	public Long getAgencyMobileId() {
		return agencyMobileId;
	}

	public void setAgencyMobileId(Long agencyMobileId) {
		this.agencyMobileId = agencyMobileId;
	}

	public AuditAgency getAuditAgencyId() {
		return auditAgencyId;
	}

	public void setAuditAgencyId(AuditAgency auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
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
