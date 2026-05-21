package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.DocumentMaster;

public interface IDocumentService {

	DocumentMaster getDocumentByName(String documentName);

	Optional<DocumentMaster> addDocument(DocumentMaster newDocument);

	List<DocumentMaster> getAllDocument();

	DocumentMaster getDocumentById(Long documentId);

	Optional<DocumentMaster> updateDocument(DocumentMaster c);

	List<DocumentMaster> getAllActiveDocument();

}
