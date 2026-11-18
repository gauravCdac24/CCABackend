package in.lms.cca.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.LdifDocument;
import in.lms.cca.repository.LdifDocumentRepository;
import in.lms.cca.service.ILdifDocumentService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LdifDocumentServiceImpl implements ILdifDocumentService{

	@Autowired
	private LdifDocumentRepository repo;

	@Override
	public Optional<LdifDocument> addLdifDocument(LdifDocument obj) {
		
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	LdifDocument savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
		
		
	}
	
	
	
}
