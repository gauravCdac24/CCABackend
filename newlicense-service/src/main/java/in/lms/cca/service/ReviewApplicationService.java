package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.ReviewApplication;
import in.lms.cca.entity.ReviewDocuments;

public interface ReviewApplicationService {

	ReviewApplication addReview(ReviewApplication reviewApplication);

	List<ReviewApplication> findByIntentId(IntentApplication intentApp);

	ReviewApplication updateReview(ReviewApplication existingReviewApp);

	List<ReviewApplication> findAllData();

	ReviewApplication findByIntentsId(IntentApplication intentApp);

	List<ReviewApplication> findByIntentesId(IntentApplication intentApp);

	Optional<ReviewApplication> downloadfile(String fileName);

	

}
