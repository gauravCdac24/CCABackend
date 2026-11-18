package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.master.DocumentsMaster;

public interface IDocumentsMasterService {

	Optional<DocumentsMaster> addDocumentsMaster(DocumentsMaster obj);
	Optional<DocumentsMaster> updateDocumentsMaster(DocumentsMaster obj);
	DocumentsMaster getDocumentsMasterById(Long id);
	List<DocumentsMaster> getAllDocumentsMaster();
	DocumentsMaster getDocumentsMasterByName(String docName);
	
}
