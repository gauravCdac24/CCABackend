package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EkycModeReview;

public interface IEkycModeReviewService {

	Optional<EkycModeReview> addEkycModeReview(EkycModeReview Obj);
	List<EkycModeReview> getAllEkycModeReview();
	List<EkycModeReview> getEkycModeReviewByReviewId(Long id);
	
}
