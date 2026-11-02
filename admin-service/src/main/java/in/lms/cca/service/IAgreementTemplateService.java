package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AgreementTemplate;

public interface IAgreementTemplateService {

	Optional<AgreementTemplate> addTemplate(AgreementTemplate template);
	Optional<AgreementTemplate> updateTemplate(AgreementTemplate template);
	List<AgreementTemplate> getAllTemplates();
	List<AgreementTemplate> getAllActiveTemplates();
	List<AgreementTemplate> getAllInactiveTemplates();
	AgreementTemplate getByTemplateId(Long templateId);
	boolean deleteByTemplateId(Long templateId);
	Optional<AgreementTemplate> downloadFile(long templateId);
}
