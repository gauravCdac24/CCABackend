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

	private Long parseCessationAppId(String cessationAppId) {
		if (cessationAppId == null || cessationAppId.isBlank()) {
			return null;
		}
		return Long.valueOf(cessationAppId.trim());
	}

	@Override
	public Optional<ReviewDocumentEntity> getByCessationId(String cessationAppId) {
		Long appId = parseCessationAppId(cessationAppId);
		if (appId == null) {
			return Optional.empty();
		}
		return reviewDocumentRepository.getByCessationId(appId);
	}

	@Override
	public List<ReviewDocumentEntity> getAllData(String cessationAppId) {
		Long appId = parseCessationAppId(cessationAppId);
		if (appId == null) {
			return List.of();
		}
		return reviewDocumentRepository.getAllData(appId);
	}

	@Override
	public List<ReviewDocumentEntity> getAllDatas(String cessationAppId) {
		Long appId = parseCessationAppId(cessationAppId);
		if (appId == null) {
			return List.of();
		}
		return reviewDocumentRepository.getAllDatas(appId);
	}

	@Override
	public ReviewDocumentEntity getDataByCessationId(String cessationAppId) {
		Long appId = parseCessationAppId(cessationAppId);
		if (appId == null) {
			return null;
		}
		return reviewDocumentRepository.getDataByCessationId(appId);
	}

	
	
}
