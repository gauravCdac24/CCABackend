package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ReviewedApplication;
import in.lms.cca.repository.ReviewedApplicationRepository;
import in.lms.cca.service.IReviewedApplicationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewedApplicationServiceImpl implements IReviewedApplicationService{

	@Autowired
	private ReviewedApplicationRepository repo;
	
	@Override
	public Optional<ReviewedApplication> addReviewedApplication(ReviewedApplication Obj) {
		
		if (Obj == null)
	        return Optional.empty();

	    try {
	       
	    	ReviewedApplication savedObj = repo.save(Obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<ReviewedApplication> getAllReviewedApplication() {
		
		return repo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<ReviewedApplication> getReviewedApplicationByReviewId(Long id) {
		
		return repo.findReviewedApplicationByReviewId(id);
	}

	

}
