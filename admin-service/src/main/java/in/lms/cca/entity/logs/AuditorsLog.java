package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditAgency;
import in.lms.cca.entity.Auditors;
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
@Table(name = "auditors_logs", schema = "cca_logs_management")
public class AuditorsLog extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_auditors_id", length = 11)
	private Long logAuditorsId;
	
	@Column(name = "auditors_id", length = 11)
	private Long auditorsId;
	
	@ManyToOne
	@JoinColumn(name = "audit_agency_id", referencedColumnName = "audit_agency_id")
	private AuditAgency auditAgencyId;
	
	@Column(name = "salutation", length = 6)
	private String salutation;
	
	@Column(name = "first_name", length = 30)
	private String firstName;
	
	@Column(name = "middle_name", length = 30)
	private String middleName;
	
	@Column(name = "last_name", length = 45)
	private String lastName;
	
	@Column(name = "status", length = 8)
	private String status;

	
	@Column(name = "client_host_name", nullable = false)
	private String clientHostName;
	
	
	public Long getLogAuditorsId() {
		return logAuditorsId;
	}

	public void setLogAuditorsId(Long logAuditorsId) {
		this.logAuditorsId = logAuditorsId;
	}

	public Long getAuditorsId() {
		return auditorsId;
	}

	public void setAuditorsId(Long auditorsId) {
		this.auditorsId = auditorsId;
	}

	public AuditAgency getAuditAgencyId() {
		return auditAgencyId;
	}

	public void setAuditAgencyId(AuditAgency auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AuditorsLog(Long logAuditorsId, Long auditorsId, AuditAgency auditAgencyId, String salutation,
			String firstName, String middleName, String lastName, String status) {
		super();
		this.logAuditorsId = logAuditorsId;
		this.auditorsId = auditorsId;
		this.auditAgencyId = auditAgencyId;
		this.salutation = salutation;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.status = status;
	}
	
	public AuditorsLog(Auditors e,String IPAddress,String operation,String userName) {
		
		this.auditorsId = e.getAuditorsId();
		this.auditAgencyId = e.getAuditAgencyId();
		this.salutation = e.getSalutation();
		this.firstName = e.getFirstName();
		this.middleName = e.getMiddleName();
		this.lastName = e.getLastName();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public AuditorsLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClientHostName() {
		return clientHostName;
	}

	public void setClientHostName(String clientHostName) {
		this.clientHostName = clientHostName;
	}

	
	

}
