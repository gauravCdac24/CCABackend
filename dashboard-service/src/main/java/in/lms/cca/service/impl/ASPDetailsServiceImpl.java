package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ASPDetails;
import in.lms.cca.repository.ASPDetailsRepository;
import in.lms.cca.service.IASPDetailsService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ASPDetailsServiceImpl implements IASPDetailsService{

	@Autowired
	private ASPDetailsRepository aspRepo;
	
	@Override
	public Optional<ASPDetails> addASPDetails(ASPDetails obj) {
		
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	ASPDetails savedObj = aspRepo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<ASPDetails> getAllASPDetails() {
		return aspRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<ASPDetails> getASPDetailsByUsername(String username) {
		return aspRepo.findASPDetailsByUsername(username);
	}

	@Override
	public ASPDetails getASPDetailsById(Long id) {
		return aspRepo.findASPDetailsById(id);
	}

	@Override
	public ASPDetails getASPDetailsByUsernameAndName(String username, String aspName) {
		
		return aspRepo.findASPDetailsByUsernameAndName(username, aspName);
	}

	@Override
	public ASPDetails getASPDetailsByUsernameAndEmailId(String username, String emailId) {

		return aspRepo.findASPDetailsByUsernameAndEmailId(username, emailId);
	}

}
