package in.lms.cca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ReviewFields;
import in.lms.cca.repository.ReviewFieldsRepository;
import in.lms.cca.service.ReviewFieldsService;

@Service
@Transactional
public class ReviewFieldsServiceImpl implements ReviewFieldsService {
	
	@Autowired
	private ReviewFieldsRepository reviewFieldsRepository;

	@Override
	public void addAll(List<ReviewFields> reviewFieldsList) {
	    if (reviewFieldsList != null && !reviewFieldsList.isEmpty()) {
	        // Save the list of ReviewFields
	        reviewFieldsRepository.saveAll(reviewFieldsList);
	    }
	}

	@Override
	public List<ReviewFields> getAllDetailsByReviewId(Long reviewId) {
		// TODO Auto-generated method stub
		return  reviewFieldsRepository.getAllDetailsByReviewId(reviewId);
	}

	@Override
	public ReviewFields updateReviewField(ReviewFields existingField) {
	    // Check if the existingField has a valid ID to avoid creating a new record
	    if (existingField.getReviewFieldId() == null) {
	        throw new IllegalArgumentException("ReviewField ID must not be null for update.");
	    }

	    // Save the updated ReviewFields object to the database
	    return reviewFieldsRepository.save(existingField);
	}


	
}
