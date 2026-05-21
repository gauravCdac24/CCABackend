package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ESignedDocumentsEntity;

public interface EsignedDocumentsService {

	ESignedDocumentsEntity getEsignDocumentById(Long eSignDocId);

	Optional<ESignedDocumentsEntity> addEsignDocument(ESignedDocumentsEntity edoc);

	List<ESignedDocumentsEntity> getEsignDocumentByUserName(String eSignDocId);

}
