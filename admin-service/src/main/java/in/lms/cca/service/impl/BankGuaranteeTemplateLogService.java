package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.BankGuaranteeTemplate;
import in.lms.cca.entity.logs.BankGuaranteeTemplateLog;
import in.lms.cca.repository.logs.BankGuaranteeTemplateRepositoryLogs;

@Service
public class BankGuaranteeTemplateLogService {

	@Autowired
	private BankGuaranteeTemplateRepositoryLogs logRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(BankGuaranteeTemplate template, String ipAddress, String operation, String userName,
			String clientHostname) {
		BankGuaranteeTemplateLog entry = new BankGuaranteeTemplateLog(template, ipAddress, operation, userName,
				clientHostname);
		logRepository.save(entry);
	}
}
