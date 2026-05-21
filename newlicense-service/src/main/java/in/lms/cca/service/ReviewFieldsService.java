package in.lms.cca.service;

import java.util.List;

import in.lms.cca.entity.ReviewFields;

public interface ReviewFieldsService {

	void addAll(List<ReviewFields> reviewFieldsList);

	List<ReviewFields> getAllDetailsByReviewId(Long reviewId);

	ReviewFields updateReviewField(ReviewFields existingField);

	ReviewFields findByReviewApplicationId(Long reviewId);

	List<ReviewFields> finByReviewFieldsByApplicationId(Long reviewId);

	List<ReviewFields> reviewDatafields(Long reviewId);

}
