package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ReviewedApplication;

public interface IReviewedApplicationService {

	Optional<ReviewedApplication> addReviewedApplication(ReviewedApplication Obj);
	List<ReviewedApplication> getAllReviewedApplication();
	List<ReviewedApplication> getReviewedApplicationByReviewId(Long id);
	
}
