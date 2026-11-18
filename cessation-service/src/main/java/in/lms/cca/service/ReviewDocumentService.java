package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ReviewDocumentEntity;

public interface ReviewDocumentService {

	Optional<ReviewDocumentEntity> saveData(ReviewDocumentEntity documentEntity);
	
	Optional<ReviewDocumentEntity> updateData(ReviewDocumentEntity documentEntity);

	Optional<ReviewDocumentEntity> getByCessationId(String cessationAppId);

	List<ReviewDocumentEntity> getAllData(String cessationAppId);

	List<ReviewDocumentEntity> getAllDatas(String cessationAppId);

	ReviewDocumentEntity getDataByCessationId(String cessationAppId);

}
