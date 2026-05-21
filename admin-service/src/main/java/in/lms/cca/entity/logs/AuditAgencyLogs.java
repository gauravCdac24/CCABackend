package in.lms.cca.entity.logs;

import java.util.Date;

import in.lms.cca.entity.Address;
import in.lms.cca.entity.AuditAgency;
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
@Table(name = "audit_agency_logs", schema = "cca_logs_management")
public class AuditAgencyLogs extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_audit_agency_id", length = 11)
	private Long logAuditAgencyId;
	
	@Column(name = "audit_agency_id", length = 11)
	private Long auditAgencyId;
	
//	@OneToOne
	@ManyToOne
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address addressId;
	
	@Column(name = "agency_name", length = 50)
	private String agencyName;
	
	@Column(name = "effective_from")
	private Date effectiveFrom;
	
	@Column(name = "effective_to")
	private Date effectiveTo;
	
	@Column(name = "status", length = 12)
	private String status;

	public Long getLogAuditAgencyId() {
		return logAuditAgencyId;
	}

	public void setLogAuditAgencyId(Long logAuditAgencyId) {
		this.logAuditAgencyId = logAuditAgencyId;
	}

	public Long getAuditAgencyId() {
		return auditAgencyId;
	}

	public void setAuditAgencyId(Long auditAgencyId) {
		this.auditAgencyId = auditAgencyId;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AuditAgencyLogs(Long logAuditAgencyId, Long auditAgencyId, Address addressId, String agencyName,
			Date effectiveFrom, Date effectiveTo, String status) {
		super();
		this.logAuditAgencyId = logAuditAgencyId;
		this.auditAgencyId = auditAgencyId;
		this.addressId = addressId;
		this.agencyName = agencyName;
		this.effectiveFrom = effectiveFrom;
		this.effectiveTo = effectiveTo;
		this.status = status;
	}

	public AuditAgencyLogs(AuditAgency e,String IPAddress,String operation,String userName) {
		
		this.auditAgencyId = e.getAuditAgencyId();
		this.addressId = e.getAddressId();
		this.agencyName = e.getAgencyName();
		this.effectiveFrom = e.getEffectiveFrom();
		this.effectiveTo = e.getEffectiveTo();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public AuditAgencyLogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
