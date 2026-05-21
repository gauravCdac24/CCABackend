package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ReviewESPApplication;


public interface IReviewESPApplicationService {
	Optional<ReviewESPApplication> addReviewESPApplication(ReviewESPApplication obj);

	List<ReviewESPApplication> getReviewESPApplicationByStatus(String status);
	List<ReviewESPApplication> getReviewESPApplicationByStatusAndId(Long id, String status);

	
}
