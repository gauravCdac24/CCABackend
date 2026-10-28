package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.CPSDocument;
import in.lms.cca.repository.CPSDocumentRepository;
import in.lms.cca.service.ICPSDocumentService;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class CPSDocumentImpl implements ICPSDocumentService{

	@Autowired
	private CPSDocumentRepository cpsRepo;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CPSDocumentLogService cpsDocumentLogService;

	private void writeAuditLogSafely(CPSDocument document, String operation) {
		try {
			String ipAddress = request.getRemoteAddr();
			InetAddress clientAddress = InetAddress.getByName(ipAddress);
			String clientHostname = clientAddress.getHostName();
			String usernameFromToken = "1";
			cpsDocumentLogService.saveLog(document, ipAddress, operation, usernameFromToken, clientHostname);
		} catch (Exception logEx) {
			// Logging must never roll back the main CPS document transaction.
			logEx.printStackTrace();
		}
	}

	
	@Override
	public Optional<CPSDocument> addCPSDocument(CPSDocument cpsDocumentObj) {
		
		if(cpsDocumentObj == null)
			return Optional.empty();
		
		
		try { 
	        CPSDocument savedCPSDocument = cpsRepo.save(cpsDocumentObj);
	        writeAuditLogSafely(savedCPSDocument, Constant.CREATED);
	        return Optional.of(savedCPSDocument);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<CPSDocument> updateCPSDocument(CPSDocument cpsDocumentObj) {
		if(cpsDocumentObj == null)
			return Optional.empty();
		
		if(cpsDocumentObj.getCpsDocId() == null)
			return Optional.empty();
		

		try { 
	        CPSDocument savedCPSDocument = cpsRepo.save(cpsDocumentObj);
	        writeAuditLogSafely(savedCPSDocument, Constant.UPDATED);
	        return Optional.of(savedCPSDocument);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<CPSDocument> getAllCPSDocument() {
		return cpsRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<CPSDocument> getAllActiveCPSDocument() {
		return cpsRepo.findAllActiveCPSDocument();
	}

	@Override
	public List<CPSDocument> getAllInactiveCPSDocument() {
		return cpsRepo.findAllInActiveCPSDocument();
	}

	@Override
	public CPSDocument getByCPSDocId(Long cpsDocId) {
		return cpsRepo.findByCPSDocId(cpsDocId);
	}

	@Override
	public boolean deleteBycpsDocId(Long cpsDocId) {
		
		CPSDocument cpsdoc = getByCPSDocId(cpsDocId);
		
		if(cpsdoc == null) {
			return false;
		}
		try { 
	        if (cpsdoc.getFileName() != null) {
	        	DocumentFileUtil.deleteFile(cpsdoc.getFileName(), "cps/Document");
	        	DocumentFileUtil.deleteFile(cpsdoc.getFileName(), "webapp/cps/Document");
	        }
	        cpsRepo.deleteBycpsDocId(cpsDocId);
	        writeAuditLogSafely(cpsdoc, Constant.DELETED);
	        return true;
	    
	}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public CPSDocument getCPSDocById(long cpsDocuId) {
		return cpsRepo.findByCPSDocId(cpsDocuId);
	}

	@Override
	public Optional<CPSDocument> downloadfile(long cpsDocuId) {
		return  cpsRepo.findById(cpsDocuId);
	}

}
