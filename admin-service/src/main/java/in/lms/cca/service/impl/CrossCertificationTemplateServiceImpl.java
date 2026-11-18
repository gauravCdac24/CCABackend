package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.CrossCertificationTemplate;
import in.lms.cca.repository.CrossCertificationTemplateRepository;
import in.lms.cca.service.ICrossCertificationTemplateService;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class CrossCertificationTemplateServiceImpl implements ICrossCertificationTemplateService {

	private static final String TEMPLATE_FOLDER = "crosscertificationtemplate/Document";

	@Autowired
	private CrossCertificationTemplateRepository templateRepo;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CrossCertificationTemplateLogService logService;

	private void writeAuditLogSafely(CrossCertificationTemplate template, String operation) {
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
	public Optional<CrossCertificationTemplate> addTemplate(CrossCertificationTemplate template) {
		if (template == null) {
			return Optional.empty();
		}
		try {
			CrossCertificationTemplate saved = templateRepo.save(template);
			writeAuditLogSafely(saved, Constant.CREATED);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<CrossCertificationTemplate> updateTemplate(CrossCertificationTemplate template) {
		if (template == null || template.getTemplateId() == null) {
			return Optional.empty();
		}
		try {
			CrossCertificationTemplate saved = templateRepo.save(template);
			writeAuditLogSafely(saved, Constant.UPDATED);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<CrossCertificationTemplate> getAllTemplates() {
		return templateRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<CrossCertificationTemplate> getAllActiveTemplates() {
		return templateRepo.findAllActiveTemplates();
	}

	@Override
	public List<CrossCertificationTemplate> getAllInactiveTemplates() {
		return templateRepo.findAllInactiveTemplates();
	}

	@Override
	public CrossCertificationTemplate getByTemplateId(Long templateId) {
		return templateRepo.findByTemplateId(templateId);
	}

	@Override
	public boolean deleteByTemplateId(Long templateId) {
		CrossCertificationTemplate template = getByTemplateId(templateId);
		if (template == null) {
			return false;
		}
		try {
			if (template.getFileName() != null) {
				DocumentFileUtil.deleteFile(template.getFileName(), TEMPLATE_FOLDER);
				DocumentFileUtil.deleteFile(template.getFileName(), "webapp/crosscertificationtemplate/Document");
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
	public Optional<CrossCertificationTemplate> downloadFile(long templateId) {
		return templateRepo.findById(templateId);
	}
}
