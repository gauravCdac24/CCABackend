package in.lms.cca.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.Address;
import in.lms.cca.entity.AuditAgency;
import in.lms.cca.entity.AuditAgencyEmail;
import in.lms.cca.entity.AuditAgencyMobile;
import in.lms.cca.entity.Auditors;
import in.lms.cca.entity.logs.AddressLogs;
import in.lms.cca.entity.logs.AuditAgencyEmailLogs;
import in.lms.cca.entity.logs.AuditAgencyLogs;
import in.lms.cca.entity.logs.AuditAgencyMobileLogs;
import in.lms.cca.entity.logs.AuditorsLog;
import in.lms.cca.repository.logs.AddressRepositoryLogs;
import in.lms.cca.repository.logs.AuditAgencyEmailRepositoryLogs;
import in.lms.cca.repository.logs.AuditAgencyMobileRepositoryLogs;
import in.lms.cca.repository.logs.AuditAgencyRepositoryLogs;
import in.lms.cca.repository.logs.AuditorsRepositoryLogs;

/**
 * Audit rows and their log rows were saved in one transaction. A log insert
 * failure (missing host name, or a value longer than the log column) marked
 * that transaction rollback-only and the agency update returned 500 even
 * though the service had already caught the exception.
 */
@Service
public class AuditChangeLogService {

	private final AddressRepositoryLogs addressLogs;
	private final AuditAgencyRepositoryLogs agencyLogs;
	private final AuditAgencyMobileRepositoryLogs mobileLogs;
	private final AuditAgencyEmailRepositoryLogs emailLogs;
	private final AuditorsRepositoryLogs auditorLogs;

	public AuditChangeLogService(AddressRepositoryLogs addressLogs, AuditAgencyRepositoryLogs agencyLogs,
			AuditAgencyMobileRepositoryLogs mobileLogs, AuditAgencyEmailRepositoryLogs emailLogs,
			AuditorsRepositoryLogs auditorLogs) {
		this.addressLogs = addressLogs;
		this.agencyLogs = agencyLogs;
		this.mobileLogs = mobileLogs;
		this.emailLogs = emailLogs;
		this.auditorLogs = auditorLogs;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addressChanged(Address address, String ipAddress, String clientHostname, String operation, String userName) {
		AddressLogs row = new AddressLogs(address, ipAddress, operation, userName);
		if (row.getStatus() == null || row.getStatus().isBlank()) {
			row.setStatus("Active");
		}
		row.setClientHostName(host(clientHostname));
		addressLogs.save(row);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void agencyChanged(AuditAgency agency, String ipAddress, String clientHostname, String operation, String userName) {
		AuditAgencyLogs row = new AuditAgencyLogs(agency, ipAddress, operation, userName);
		row.setClientHostName(host(clientHostname));
		agencyLogs.save(row);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void mobileChanged(AuditAgencyMobile mobile, String ipAddress, String clientHostname, String operation, String userName) {
		AuditAgencyMobileLogs row = new AuditAgencyMobileLogs(mobile, ipAddress, operation, userName);
		row.setClientHostName(host(clientHostname));
		mobileLogs.save(row);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void emailChanged(AuditAgencyEmail email, String ipAddress, String clientHostname, String operation, String userName) {
		AuditAgencyEmailLogs row = new AuditAgencyEmailLogs(email, ipAddress, operation, userName);
		row.setClientHostName(host(clientHostname));
		emailLogs.save(row);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void auditorChanged(Auditors auditor, String ipAddress, String clientHostname, String operation, String userName) {
		AuditorsLog row = new AuditorsLog(auditor, ipAddress, operation, userName);
		row.setClientHostName(host(clientHostname));
		auditorLogs.save(row);
	}

	private String host(String clientHostname) {
		return clientHostname != null && !clientHostname.isBlank() ? clientHostname : "unknown";
	}
}
