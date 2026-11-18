package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ReviewCessationDocumentEntity;
import in.lms.cca.entity.ReviewDocumentEntity;
import in.lms.cca.repository.ReviewCessationDocumentRepository;
import in.lms.cca.service.ReviewCessationDocumentService;

@Service
@Transactional
public class ReviewCessationDocumentServiceImpl implements ReviewCessationDocumentService {
	
	@Autowired
	private ReviewCessationDocumentRepository reviewCessationDocumentRepository;

	@Override
	public Optional<ReviewCessationDocumentEntity> saveData(ReviewCessationDocumentEntity cessationDocumentEntity) {
		if (cessationDocumentEntity == null)
	        return Optional.empty();

	    try {
	       
	    	ReviewCessationDocumentEntity savedReviewCessationDocumentEntity = reviewCessationDocumentRepository.save(cessationDocumentEntity);
	       
	        return Optional.of(savedReviewCessationDocumentEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	
	@Override
	public Optional<ReviewCessationDocumentEntity> updateData(ReviewCessationDocumentEntity cessationDocumentEntity) {
		if (cessationDocumentEntity == null)
	        return Optional.empty();
		
		if (cessationDocumentEntity.getReviewDocId() == null)
	        return Optional.empty();

	    try {
	       
	    	ReviewCessationDocumentEntity savedReviewCessationDocumentEntity = reviewCessationDocumentRepository.save(cessationDocumentEntity);
	       
	        return Optional.of(savedReviewCessationDocumentEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}


	@Override
	public List<ReviewCessationDocumentEntity> getByRewievId(Long reviewId) {
		// TODO Auto-generated method stub
		return reviewCessationDocumentRepository.getByRewievId(reviewId);
	}


	@Override
	public List<ReviewCessationDocumentEntity> getAllData(ReviewDocumentEntity documentEntity) {
		// TODO Auto-generated method stub
		return reviewCessationDocumentRepository.getAllData(documentEntity);
	}


	@Override
	public List<ReviewCessationDocumentEntity> getAllDatas(ReviewDocumentEntity documentEntity) {
		// TODO Auto-generated method stub
		return reviewCessationDocumentRepository.getAllDatas(documentEntity);
	}




}
