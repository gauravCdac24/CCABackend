package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ReviewCessationDocumentEntity;
import in.lms.cca.entity.ReviewDocumentEntity;

public interface ReviewCessationDocumentService {

	Optional<ReviewCessationDocumentEntity> saveData(ReviewCessationDocumentEntity cessationDocumentEntity);
	
	Optional<ReviewCessationDocumentEntity> updateData(ReviewCessationDocumentEntity cessationDocumentEntity);

	List<ReviewCessationDocumentEntity> getByRewievId(Long reviewId);

	List<ReviewCessationDocumentEntity> getAllData(ReviewDocumentEntity documentEntity);

	List<ReviewCessationDocumentEntity> getAllDatas(ReviewDocumentEntity documentEntity);

}
