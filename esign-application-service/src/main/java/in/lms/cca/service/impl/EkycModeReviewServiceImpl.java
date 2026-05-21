package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EkycModeReview;
import in.lms.cca.repository.EkycModeReviewRepository;
import in.lms.cca.service.IEkycModeReviewService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EkycModeReviewServiceImpl implements IEkycModeReviewService{

	@Autowired
	private EkycModeReviewRepository repo;

	@Override
	public Optional<EkycModeReview> addEkycModeReview(EkycModeReview Obj) {
		
		if (Obj == null)
	        return Optional.empty();

	    try {
	       
	    	EkycModeReview savedObj = repo.save(Obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	    
	}

	@Override
	public List<EkycModeReview> getAllEkycModeReview() {
		return repo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<EkycModeReview> getEkycModeReviewByReviewId(Long id) {
		return repo.findEkycModeReviewByReviewId(id);
	}
	
	

}
