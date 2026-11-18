package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AgreementTemplate;
import in.lms.cca.entity.logs.AgreementTemplateLog;
import in.lms.cca.repository.logs.AgreementTemplateRepositoryLogs;

@Service
public class AgreementTemplateLogService {

	@Autowired
	private AgreementTemplateRepositoryLogs logRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(AgreementTemplate template, String ipAddress, String operation, String userName,
			String clientHostname) {
		AgreementTemplateLog entry = new AgreementTemplateLog(template, ipAddress, operation, userName,
				clientHostname);
		logRepository.save(entry);
	}
}
