package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.CSRDocument;
import in.lms.cca.repository.CSRDocumentRepository;
import in.lms.cca.service.ICSRDocumentService;
import in.lms.cca.util.global.DocumentFileUtil;

@Service
@Transactional
public class CSRDocumentImpl implements ICSRDocumentService {

	@Autowired
	private CSRDocumentRepository csrRepo;

	@Override
	public Optional<CSRDocument> addCSRDocument(CSRDocument csrDocumentObj) {
		if (csrDocumentObj == null) {
			return Optional.empty();
		}
		try {
			CSRDocument saved = csrRepo.save(csrDocumentObj);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<CSRDocument> updateCSRDocument(CSRDocument csrDocumentObj) {
		if (csrDocumentObj == null || csrDocumentObj.getCsrDocId() == null) {
			return Optional.empty();
		}
		try {
			CSRDocument saved = csrRepo.save(csrDocumentObj);
			return Optional.of(saved);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<CSRDocument> getAllCSRDocument() {
		return csrRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<CSRDocument> getAllActiveCSRDocument() {
		return csrRepo.findAllActiveCSRDocument();
	}

	@Override
	public List<CSRDocument> getAllInactiveCSRDocument() {
		return csrRepo.findAllInActiveCSRDocument();
	}

	@Override
	public CSRDocument getByCSRDocId(Long csrDocId) {
		return csrRepo.findByCSRDocId(csrDocId);
	}

	@Override
	public boolean deleteBycsrDocId(Long csrDocId) {
		CSRDocument csrdoc = getByCSRDocId(csrDocId);
		if (csrdoc == null) {
			return false;
		}
		try {
			if (csrdoc.getFileName() != null) {
				DocumentFileUtil.deleteFile(csrdoc.getFileName(), "csr/Document");
				DocumentFileUtil.deleteFile(csrdoc.getFileName(), "webapp/csr/Document");
			}
			csrRepo.deleteBycsrDocId(csrDocId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public CSRDocument getCSRDocById(long csrDocuId) {
		return csrRepo.findByCSRDocId(csrDocuId);
	}

	@Override
	public Optional<CSRDocument> downloadfile(long csrDocuId) {
		return csrRepo.findById(csrDocuId);
	}
}
