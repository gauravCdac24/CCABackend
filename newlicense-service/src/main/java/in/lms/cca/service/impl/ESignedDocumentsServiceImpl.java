package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ESignedDocumentsEntity;
import in.lms.cca.repository.ESignedDocumentsRepository;
import in.lms.cca.service.EsignedDocumentsService;

@Service
@Transactional
public class ESignedDocumentsServiceImpl implements EsignedDocumentsService {
	
	@Autowired
	private ESignedDocumentsRepository documentsRepository;

	@Override
	public ESignedDocumentsEntity getEsignDocumentById(Long eSignDocId) {
	
		return documentsRepository.getEsignDocumentById( eSignDocId);
	}

	@Override
	public Optional<ESignedDocumentsEntity> addEsignDocument(ESignedDocumentsEntity edoc) {
		if (edoc == null) {
            return Optional.empty();
        }

        try {
        	ESignedDocumentsEntity savedESignedDocumentsEntity = documentsRepository.save(edoc);
            return Optional.of(savedESignedDocumentsEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}

	@Override
	public List<ESignedDocumentsEntity> getEsignDocumentByUserName(String eSignDocId) {
		// TODO Auto-generated method stub
		return documentsRepository.getEsignDocumentByUserName( eSignDocId);
	}
	
	

}
