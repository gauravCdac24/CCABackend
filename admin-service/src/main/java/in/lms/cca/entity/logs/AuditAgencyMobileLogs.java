package in.lms.cca.entity.logs;

import in.lms.cca.entity.AuditAgency;
import in.lms.cca.entity.AuditAgencyMobile;
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
@Table(name = "audit_agency_mobile_logs", schema = "cca_logs_management")
public class AuditAgencyMobileLogs extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_agency_mobile_id", length = 11)
	private Long logAgencyMobileId;
	
	@Column(name = "agency_mobile_id", length = 11)
	private Long agencyMobileId;
	
	@ManyToOne
	@JoinColumn(name = "audit_agency_id", referencedColumnName = "audit_agency_id")
	private AuditAgency auditAgencyId;
	
	@Column(name = "contactno", length = 10)
	private String contactNo;
	
	@Column(name = "areacode", length = 3)
	private String areaCode;
	
	@Column(name = "contact_type", length = 8)
	private String contactType;
	
	@Column(name = "status", length = 8)
	private String status;

	@Column(name = "client_host_name", nullable = false)
	private String clientHostName;
	
	public Long getLogAgencyMobileId() {
		return logAgencyMobileId;
	}

	public void setLogAgencyMobileId(Long logAgencyMobileId) {
		this.logAgencyMobileId = logAgencyMobileId;
	}

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

	public AuditAgencyMobileLogs(Long logAgencyMobileId, Long agencyMobileId, AuditAgency auditAgencyId,
			String contactNo, String areaCode, String contactType, String status) {
		super();
		this.logAgencyMobileId = logAgencyMobileId;
		this.agencyMobileId = agencyMobileId;
		this.auditAgencyId = auditAgencyId;
		this.contactNo = contactNo;
		this.areaCode = areaCode;
		this.contactType = contactType;
		this.status = status;
	}
	
	public AuditAgencyMobileLogs(AuditAgencyMobile e,String IPAddress,String operation,String userName) {
		
		this.agencyMobileId = e.getAgencyMobileId();
		this.auditAgencyId = e.getAuditAgencyId();
		this.contactNo = e.getContactNo();
		this.areaCode = e.getAreaCode();
		this.contactType = e.getContactType();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public String getClientHostName() {
		return clientHostName;
	}

	public void setClientHostName(String clientHostName) {
		this.clientHostName = clientHostName;
	}

	public AuditAgencyMobileLogs() {
		super();
		// TODO Auto-generated constructor stub
	}



}
