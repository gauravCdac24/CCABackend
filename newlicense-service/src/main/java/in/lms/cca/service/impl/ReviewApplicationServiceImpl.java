package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.ReviewApplication;
import in.lms.cca.repository.ReviewApplicationRepository;
import in.lms.cca.service.ReviewApplicationService;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ReviewApplicationServiceImpl implements ReviewApplicationService {
	
	@Autowired
	private ReviewApplicationRepository reviewApplicationRepository;

	@Override
	public ReviewApplication addReview(ReviewApplication reviewApplication) {
	    // Save the ReviewApplication entity
	    ReviewApplication savedReviewApplication = reviewApplicationRepository.save(reviewApplication);
	    
	    return savedReviewApplication;
	}

	@Override
	public List<ReviewApplication> findByIntentId(IntentApplication intentApp) {
		// TODO Auto-generated method stub
		return reviewApplicationRepository.findByIntentId(intentApp);
	}

	@Override
	public ReviewApplication updateReview(ReviewApplication existingReviewApp) {
	    // Check if the review application exists in the database
	    if (reviewApplicationRepository.existsById(existingReviewApp.getReviewId())) {
	        // Save the updated ReviewApplication
	        ReviewApplication savedReviewApplication = reviewApplicationRepository.save(existingReviewApp);
	        return savedReviewApplication; // Return the saved ReviewApplication
	    } else {
	        // Handle the case where the ReviewApplication does not exist
	        throw new EntityNotFoundException("ReviewApplication not found with ID: " + existingReviewApp.getReviewId());
	    }
	}

	@Override
	public List<ReviewApplication> findAllData() {
		// TODO Auto-generated method stub
		return reviewApplicationRepository.findAllActiveData();
	}

	@Override
	public ReviewApplication findByIntentsId(IntentApplication intentApp) {
		// TODO Auto-generated method stub
		return reviewApplicationRepository.findByIntentsId(intentApp);
	}

	@Override
	public List<ReviewApplication> findByIntentesId(IntentApplication intentApp) {
		// TODO Auto-generated method stub
		return reviewApplicationRepository.findByIntentesId(intentApp);
	}

	@Override
	public Optional<ReviewApplication> downloadfile(String fileName) {
		// TODO Auto-generated method stub
		return reviewApplicationRepository.downloadfile(fileName);
	}


}
