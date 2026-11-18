package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AdditionalDocumentEntity;
import in.lms.cca.entity.State;

public interface IAdditionalDocumentService {

	AdditionalDocumentEntity getDocumentName(String documentname);

	Optional<AdditionalDocumentEntity> addState(AdditionalDocumentEntity newAdditionalDocumentEntity);

	List<AdditionalDocumentEntity> getAllAdditionalDocumentEntity();

	List<AdditionalDocumentEntity> getAllActiveAdditionalDocumentEntity();

	List<AdditionalDocumentEntity> getAllInactiveAdditionalDocumentEntity();

	AdditionalDocumentEntity getAdditionalDocumentEntityById(Long id);

	boolean deleteByAdditionalDocumentEntityId(Long stateId);

	Optional<AdditionalDocumentEntity> updateState(AdditionalDocumentEntity newAdditionalDocumentEntity);

}
