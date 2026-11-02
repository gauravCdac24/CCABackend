package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.CrossCertificationTemplate;

public interface ICrossCertificationTemplateService {

	Optional<CrossCertificationTemplate> addTemplate(CrossCertificationTemplate template);
	Optional<CrossCertificationTemplate> updateTemplate(CrossCertificationTemplate template);
	List<CrossCertificationTemplate> getAllTemplates();
	List<CrossCertificationTemplate> getAllActiveTemplates();
	List<CrossCertificationTemplate> getAllInactiveTemplates();
	CrossCertificationTemplate getByTemplateId(Long templateId);
	boolean deleteByTemplateId(Long templateId);
	Optional<CrossCertificationTemplate> downloadFile(long templateId);
}
