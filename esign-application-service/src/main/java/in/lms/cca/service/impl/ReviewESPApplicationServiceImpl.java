package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ReviewESPApplication;
import in.lms.cca.repository.ReviewESPApplicationRepository;
import in.lms.cca.service.IReviewESPApplicationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewESPApplicationServiceImpl implements IReviewESPApplicationService{

	@Autowired
	private ReviewESPApplicationRepository repo;
	
	@Override
	public Optional<ReviewESPApplication> addReviewESPApplication(ReviewESPApplication obj) {
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	ReviewESPApplication savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	    	
	        return Optional.empty();
	    }
	}

	@Override
	public List<ReviewESPApplication> getReviewESPApplicationByStatus(String status) {
		
		return repo.findReviewESPApplicationByStatus(status);
	}

	@Override
	public List<ReviewESPApplication> getReviewESPApplicationByStatusAndId(Long id, String status) {
	
		return repo.findReviewESPApplicationByStatusAndId(id, status);
	}

	

}
