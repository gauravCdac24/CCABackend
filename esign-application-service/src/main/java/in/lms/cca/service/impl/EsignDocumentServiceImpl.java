package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EsignDocument;
import in.lms.cca.repository.EsignDocumentRepository;
import in.lms.cca.service.IEsignDocumentService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EsignDocumentServiceImpl implements IEsignDocumentService{
	
	@Autowired
	private EsignDocumentRepository repo;

	@Override
	public Optional<EsignDocument> addEsignDocument(EsignDocument obj) {
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	EsignDocument savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	    	
	        return Optional.empty();
	    }
	}

	@Override
	public List<EsignDocument> getEsignDocumentByAppIdDocumentTypeAndStatus(Long esignLicenseeAppId, String documentType, String status) {
		
		return repo.findEsignDocumentByAppIdDocumentTypeAndStatus(esignLicenseeAppId, documentType, status);
		
	}

	@Override
	public EsignDocument getEsignDocumentById(Long id) {
		
		return repo.findEsignDocumentById(id);
	}

	@Override
	public void inactiveAllDocumentByAppId(Long id) {
		
		repo.inactiveAllDocumentByAppId(id);
		
	}

	@Override
	public EsignDocument getEsignDocumentByFileName(String fileName) {
		
		return repo.findEsignDocumentByFileName(fileName);
	}
	
	
	
}
