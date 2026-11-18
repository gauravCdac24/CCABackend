package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.CPSDocument;
import in.lms.cca.entity.logs.CPSDocumentLog;
import in.lms.cca.repository.logs.CPSDocumentRepositoryLogs;

/**
 * Writes CPS audit logs in an independent transaction so failures
 * never mark the main CPS document transaction for rollback.
 */
@Service
public class CPSDocumentLogService {

	@Autowired
	private CPSDocumentRepositoryLogs logRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(CPSDocument document, String ipAddress, String operation, String userName,
			String clientHostname) {
		CPSDocumentLog entry = new CPSDocumentLog(document, ipAddress, operation, userName, clientHostname);
		logRepository.save(entry);
	}
}
