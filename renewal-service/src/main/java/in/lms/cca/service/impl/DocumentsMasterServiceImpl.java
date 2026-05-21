package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.master.DocumentsMaster;
import in.lms.cca.repository.DocumentsMasterRepository;
import in.lms.cca.service.IDocumentsMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DocumentsMasterServiceImpl implements IDocumentsMasterService {

    @Autowired
    private DocumentsMasterRepository documentsRepo;

    @Override
    public Optional<DocumentsMaster> addDocumentsMaster(DocumentsMaster obj) {
        if (obj == null) {
            return Optional.empty();
        }

        try {
            DocumentsMaster savedDocument = documentsRepo.save(obj);
            return Optional.of(savedDocument);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<DocumentsMaster> updateDocumentsMaster(DocumentsMaster obj) {
        if (obj == null || obj.getDocumentsId() == null) { 
            return Optional.empty();
        }

        try {
            DocumentsMaster updatedDocument = documentsRepo.save(obj);
            return Optional.of(updatedDocument);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public DocumentsMaster getDocumentsMasterById(Long id) {
        return documentsRepo.findByDocumentsMasterId(id); 
    }

    @Override
    public List<DocumentsMaster> getAllDocumentsMaster() {
        return documentsRepo.findAll(Sort.by(Sort.Direction.DESC, "created")); 
    }

	@Override
	public DocumentsMaster getDocumentsMasterByName(String docName) {
		
		return documentsRepo.findDocumentsMasterByName(docName);
	}
}
