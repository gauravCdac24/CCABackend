package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.DocumentMaster;
import in.lms.cca.repository.DocumentRepository;
import in.lms.cca.service.IDocumentService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DocumentServiceImpl implements IDocumentService{
	
	@Autowired
	private DocumentRepository documentRepository;
	
	

	@Override
	public DocumentMaster getDocumentByName(String documentName) {
		return documentRepository.getDocumentByName(documentName);
	}

	@Override
	public Optional<DocumentMaster> addDocument(DocumentMaster newDocument) {
		if(newDocument == null)
			return Optional.empty();
		
		try {
			return Optional.of(documentRepository.save(newDocument));
		}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<DocumentMaster> getAllDocument() {
		return documentRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public DocumentMaster getDocumentById(Long documentId) {
		return documentRepository.getDocumentById(documentId);
	}

	@Override
	public Optional<DocumentMaster> updateDocument(DocumentMaster c) {
		if(c == null)
			return Optional.empty();
		
		if(c.getDocumentId() == null)
			return Optional.empty();
		
		try {
			return Optional.of(documentRepository.save(c));
		}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<DocumentMaster> getAllActiveDocument() {
		// TODO Auto-generated method stub
		return documentRepository. getAllActiveDocument();
	}

}
