package in.lms.cca.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.repository.ApplicationTimeLineRepository;
import in.lms.cca.service.IApplicationTimeLineService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ApplicationTimeLineServiceImpl implements IApplicationTimeLineService{

	@Autowired
	private ApplicationTimeLineRepository repo;
	
	@Override
	public Optional<ApplicationTimeLine> addApplicationTimeLine(ApplicationTimeLine obj) {
		
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	ApplicationTimeLine savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
		
	}
	
	


}

	
	