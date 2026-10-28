package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EsignDocument;

public interface IEsignDocumentService{
	
	Optional<EsignDocument> addEsignDocument(EsignDocument obj);

	List<EsignDocument> getEsignDocumentByAppIdDocumentTypeAndStatus(Long esignLicenseeAppId, String documentType, String status);

	EsignDocument getEsignDocumentById(Long id);

	void inactiveAllDocumentByAppId(Long id);

	EsignDocument getEsignDocumentByFileName(String fileName);
	
}
