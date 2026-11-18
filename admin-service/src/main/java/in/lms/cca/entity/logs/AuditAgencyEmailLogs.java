package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditAgency;
import in.lms.cca.entity.AuditAgencyEmail;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_agency_email_logs", schema = "cca_logs_management")
public class AuditAgencyEmailLogs extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_agency_email_id", length = 11)
	private Long LogAgencyEmailId;
	
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

	@Column(name = "client_host_name", nullable = false)
	private String clientHostName;
	
	public Long getLogAgencyEmailId() {
		return LogAgencyEmailId;
	}

	public void setLogAgencyEmailId(Long logAgencyEmailId) {
		LogAgencyEmailId = logAgencyEmailId;
	}

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

	public AuditAgencyEmailLogs(Long logAgencyEmailId, Long agencyEmailId, AuditAgency auditAgencyId, String email,
			String emailType, String status) {
		super();
		LogAgencyEmailId = logAgencyEmailId;
		this.agencyEmailId = agencyEmailId;
		this.auditAgencyId = auditAgencyId;
		this.email = email;
		this.emailType = emailType;
		this.status = status;
	}
	
	public AuditAgencyEmailLogs(AuditAgencyEmail e,String IPAddress,String operation,String userName) {
		
		this.agencyEmailId = e.getAgencyEmailId();
		this.auditAgencyId = e.getAuditAgencyId();
		this.email = e.getEmail();
		this.emailType = e.getEmailType();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public AuditAgencyEmailLogs(Long logAgencyEmailId, Long agencyEmailId, AuditAgency auditAgencyId, String email,
			String emailType, String status, String clientHostName) {
		super();
		LogAgencyEmailId = logAgencyEmailId;
		this.agencyEmailId = agencyEmailId;
		this.auditAgencyId = auditAgencyId;
		this.email = email;
		this.emailType = emailType;
		this.status = status;
		this.clientHostName = clientHostName;
	}

	public String getClientHostName() {
		return clientHostName;
	}

	public void setClientHostName(String clientHostName) {
		this.clientHostName = clientHostName;
	}

	public AuditAgencyEmailLogs() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	

}
