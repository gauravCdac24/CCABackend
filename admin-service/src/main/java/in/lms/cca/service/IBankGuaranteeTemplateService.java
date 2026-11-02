package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.BankGuaranteeTemplate;

public interface IBankGuaranteeTemplateService {

	Optional<BankGuaranteeTemplate> addTemplate(BankGuaranteeTemplate template);
	Optional<BankGuaranteeTemplate> updateTemplate(BankGuaranteeTemplate template);
	List<BankGuaranteeTemplate> getAllTemplates();
	List<BankGuaranteeTemplate> getAllActiveTemplates();
	List<BankGuaranteeTemplate> getAllInactiveTemplates();
	BankGuaranteeTemplate getByTemplateId(Long templateId);
	boolean deleteByTemplateId(Long templateId);
	Optional<BankGuaranteeTemplate> downloadFile(long templateId);
}
