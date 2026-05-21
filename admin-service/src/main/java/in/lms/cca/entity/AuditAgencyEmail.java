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
@Table(name = "audit_agency_email", schema="admin_user_management")
public class AuditAgencyEmail extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agency_email_id", length = 11)
	private Long agencyEmailId;
	
	@ManyToOne
	@JoinColumn(name = "audit_agency_id", referencedColumnName = "audit_agency_id")
	private AuditAgency auditAgencyId;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "email_type", length = 11)
	private String emailType;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;
	

	public Long getAgencyEmailId() {
		return agencyEmailId;
	}

	public void setAgencyEmailId(Long agencyEmailId) {
		this.agencyEmailId = agencyEmailId;
	}

	public AuditAgency getAuditAgencyId() {
		return auditAgencyId;
	}

	public void setAuditAgencyId(AuditAgency auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
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
