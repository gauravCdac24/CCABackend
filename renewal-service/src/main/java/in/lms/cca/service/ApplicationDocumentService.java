package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.IntentAdditionalDocuments;
import in.lms.cca.entity.ReviewDocuments;

public interface ApplicationDocumentService {
	
	Optional<ApplicationDocument> addApplicationDocument(ApplicationDocument passportDocument);

	List<ApplicationDocument> findByindivApplicationId(Long intentAppId);

	List<ApplicationDocument> findIntentAppById(Long intentAppId);

	Optional<ApplicationDocument> updateApplicationDocument(ApplicationDocument passportDocument);

	Optional<ApplicationDocument> downloadfile(Long id);

	List<ApplicationDocument> findIntentWithoutStatusAppById(Long intentAppId);

	

}
