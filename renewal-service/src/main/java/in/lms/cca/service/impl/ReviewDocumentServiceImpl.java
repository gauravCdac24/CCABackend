package in.lms.cca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ReviewDocuments;
import in.lms.cca.repository.ReviewDocumentsRepository;
import in.lms.cca.service.ReviewDocumentsService;


@Service
@Transactional
public class ReviewDocumentServiceImpl implements ReviewDocumentsService {
	
	@Autowired
	private ReviewDocumentsRepository reviewDocumentsRepository;

	@Override
	public void addAllDocument(List<ReviewDocuments> reviewDocuments) {
	    if (reviewDocuments != null && !reviewDocuments.isEmpty()) {
	        // Save the list of ReviewDocuments
	        reviewDocumentsRepository.saveAll(reviewDocuments);
	    }
	}

	@Override
	public List<ReviewDocuments> getAllDetailsByReviewId(Long reviewId) {
		// TODO Auto-generated method stub
		return reviewDocumentsRepository.getAllDetailsByReviewId(reviewId);
	}

	@Override
	public ReviewDocuments updateReviewField(ReviewDocuments existingField) {
		 if (existingField.getReviewId() == null) {
		        throw new IllegalArgumentException("ReviewField ID must not be null for update.");
		    }

		    // Save the updated ReviewFields object to the database
		    return reviewDocumentsRepository.save(existingField);
	}
	
}