package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AuditAcceptance;
import in.lms.cca.repository.AuditAcceptanceRepository;
import in.lms.cca.service.IAuditAcceptanceService;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AuditAcceptanceServiceImpl implements IAuditAcceptanceService {

	private static final String DOCUMENT_FOLDER = "auditacceptance/Document";

	@Autowired
	private AuditAcceptanceRepository documentRepo;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private AuditAcceptanceLogService logService;

	private void writeAuditLogSafely(AuditAcceptance document, String operation) {
		try {
			String ipAddress = request.getRemoteAddr();
			InetAddress clientAddress = InetAddress.getByName(ipAddress);
			String clientHostname = clientAddress.getHostName();
			String usernameFromToken = "1";
			logService.saveLog(document, ipAddress, operation, usernameFromToken, clientHostname);
		} catch (Exception logEx) {
			logEx.printStackTrace();
		}
	}

	@Override
	public Optional<AuditAcceptance> addDocument(AuditAcceptance document) {
		if (document == null) {
			return Optional.empty();
		}
		try {
			AuditAcceptance saved = documentRepo.save(document);
			writeAuditLogSafely(saved, Constant.CREATED);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<AuditAcceptance> updateDocument(AuditAcceptance document) {
		if (document == null || document.getAuditAcceptanceId() == null) {
			return Optional.empty();
		}
		try {
			AuditAcceptance saved = documentRepo.save(document);
			writeAuditLogSafely(saved, Constant.UPDATED);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<AuditAcceptance> getAllDocuments() {
		return documentRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<AuditAcceptance> getAllActiveDocuments() {
		return documentRepo.findAllActive();
	}

	@Override
	public List<AuditAcceptance> getAllInactiveDocuments() {
		return documentRepo.findAllInactive();
	}

	@Override
	public AuditAcceptance getById(Long auditAcceptanceId) {
		return documentRepo.findByAuditAcceptanceId(auditAcceptanceId);
	}

	@Override
	public boolean deleteById(Long auditAcceptanceId) {
		AuditAcceptance document = getById(auditAcceptanceId);
		if (document == null) {
			return false;
		}
		try {
			if (document.getFileName() != null) {
				DocumentFileUtil.deleteFile(document.getFileName(), DOCUMENT_FOLDER);
				DocumentFileUtil.deleteFile(document.getFileName(), "webapp/auditacceptance/Document");
			}
			documentRepo.deleteByAuditAcceptanceId(auditAcceptanceId);
			writeAuditLogSafely(document, Constant.DELETED);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Optional<AuditAcceptance> downloadFile(long auditAcceptanceId) {
		return documentRepo.findById(auditAcceptanceId);
	}
}
