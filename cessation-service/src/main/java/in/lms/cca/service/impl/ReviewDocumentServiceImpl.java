package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.LicenseCessationApplicationEntity;
import in.lms.cca.entity.ReviewDocumentEntity;
import in.lms.cca.repository.ReviewDocumentRepository;
import in.lms.cca.service.ReviewDocumentService;


@Service
@Transactional
public class ReviewDocumentServiceImpl implements ReviewDocumentService {
	
	@Autowired
	private ReviewDocumentRepository reviewDocumentRepository;

	@Override
	public Optional<ReviewDocumentEntity> saveData(ReviewDocumentEntity documentEntity) {
		if (documentEntity == null)
	        return Optional.empty();

	    try {
	       
	    	ReviewDocumentEntity savedReviewDocumentEntity = reviewDocumentRepository.save(documentEntity);
	       
	        return Optional.of(savedReviewDocumentEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<ReviewDocumentEntity> updateData(ReviewDocumentEntity documentEntity) {
		if (documentEntity == null)
	        return Optional.empty();
		
		if (documentEntity.getReviewId() == null)
	        return Optional.empty();

	    try {
	       
	    	ReviewDocumentEntity savedReviewDocumentEntity = reviewDocumentRepository.save(documentEntity);
	       
	        return Optional.of(savedReviewDocumentEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<ReviewDocumentEntity> getByCessationId(String cessationAppId) {
		// TODO Auto-generated method stub
		return reviewDocumentRepository.getByCessationId(cessationAppId);
	}

	@Override
	public List<ReviewDocumentEntity> getAllData(String cessationAppId) {
		// TODO Auto-generated method stub
		return reviewDocumentRepository.getAllData(cessationAppId);
	}

	@Override
	public List<ReviewDocumentEntity> getAllDatas(String cessationAppId) {
		// TODO Auto-generated method stub
		return reviewDocumentRepository.getAllDatas(cessationAppId);
	}

	@Override
	public ReviewDocumentEntity getDataByCessationId(String cessationAppId) {
		
		return reviewDocumentRepository.getDataByCessationId(cessationAppId);
	}

	
	
}
