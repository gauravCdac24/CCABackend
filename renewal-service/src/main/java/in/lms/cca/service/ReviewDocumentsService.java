package in.lms.cca.service;

import java.util.List;

import in.lms.cca.entity.ReviewDocuments;

public interface ReviewDocumentsService {

	void addAllDocument(List<ReviewDocuments> reviewDocuments);

	List<ReviewDocuments> getAllDetailsByReviewId(Long reviewId);

	ReviewDocuments updateReviewField(ReviewDocuments existingField);

}
