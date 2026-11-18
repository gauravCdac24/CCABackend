package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.CrossCertificationTemplate;
import in.lms.cca.entity.logs.CrossCertificationTemplateLog;
import in.lms.cca.repository.logs.CrossCertificationTemplateRepositoryLogs;

@Service
public class CrossCertificationTemplateLogService {

	@Autowired
	private CrossCertificationTemplateRepositoryLogs logRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(CrossCertificationTemplate template, String ipAddress, String operation, String userName,
			String clientHostname) {
		CrossCertificationTemplateLog entry = new CrossCertificationTemplateLog(template, ipAddress, operation, userName,
				clientHostname);
		logRepository.save(entry);
	}
}
