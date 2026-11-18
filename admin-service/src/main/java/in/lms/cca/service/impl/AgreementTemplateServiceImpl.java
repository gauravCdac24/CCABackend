package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AgreementTemplate;
import in.lms.cca.repository.AgreementTemplateRepository;
import in.lms.cca.service.IAgreementTemplateService;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AgreementTemplateServiceImpl implements IAgreementTemplateService {

	private static final String TEMPLATE_FOLDER = "agreementtemplate/Document";

	@Autowired
	private AgreementTemplateRepository templateRepo;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private AgreementTemplateLogService logService;

	private void writeAuditLogSafely(AgreementTemplate template, String operation) {
		try {
			String ipAddress = request.getRemoteAddr();
			InetAddress clientAddress = InetAddress.getByName(ipAddress);
			String clientHostname = clientAddress.getHostName();
			String usernameFromToken = "1";
			logService.saveLog(template, ipAddress, operation, usernameFromToken, clientHostname);
		} catch (Exception logEx) {
			logEx.printStackTrace();
		}
	}

	@Override
	public Optional<AgreementTemplate> addTemplate(AgreementTemplate template) {
		if (template == null) {
			return Optional.empty();
		}
		try {
			AgreementTemplate saved = templateRepo.save(template);
			writeAuditLogSafely(saved, Constant.CREATED);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<AgreementTemplate> updateTemplate(AgreementTemplate template) {
		if (template == null || template.getTemplateId() == null) {
			return Optional.empty();
		}
		try {
			AgreementTemplate saved = templateRepo.save(template);
			writeAuditLogSafely(saved, Constant.UPDATED);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<AgreementTemplate> getAllTemplates() {
		return templateRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<AgreementTemplate> getAllActiveTemplates() {
		return templateRepo.findAllActiveTemplates();
	}

	@Override
	public List<AgreementTemplate> getAllInactiveTemplates() {
		return templateRepo.findAllInactiveTemplates();
	}

	@Override
	public AgreementTemplate getByTemplateId(Long templateId) {
		return templateRepo.findByTemplateId(templateId);
	}

	@Override
	public boolean deleteByTemplateId(Long templateId) {
		AgreementTemplate template = getByTemplateId(templateId);
		if (template == null) {
			return false;
		}
		try {
			if (template.getFileName() != null) {
				DocumentFileUtil.deleteFile(template.getFileName(), TEMPLATE_FOLDER);
				DocumentFileUtil.deleteFile(template.getFileName(), "webapp/agreementtemplate/Document");
			}
			templateRepo.deleteByTemplateId(templateId);
			writeAuditLogSafely(template, Constant.DELETED);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Optional<AgreementTemplate> downloadFile(long templateId) {
		return templateRepo.findById(templateId);
	}
}
