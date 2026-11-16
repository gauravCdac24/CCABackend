package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AuditAcceptance;
import in.lms.cca.entity.logs.AuditAcceptanceLog;
import in.lms.cca.repository.logs.AuditAcceptanceRepositoryLogs;

@Service
public class AuditAcceptanceLogService {

	@Autowired
	private AuditAcceptanceRepositoryLogs logRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(AuditAcceptance document, String ipAddress, String operation, String userName,
			String clientHostname) {
		AuditAcceptanceLog entry = new AuditAcceptanceLog(document, ipAddress, operation, userName, clientHostname);
		logRepository.save(entry);
	}
}
