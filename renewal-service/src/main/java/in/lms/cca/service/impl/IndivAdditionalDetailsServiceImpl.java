package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.IndivAdditionalDetails;
import in.lms.cca.entity.IndivEmails;
import in.lms.cca.repository.IndivAdditionalDetailsRepository;
import in.lms.cca.service.IndivAdditionalDetailsService;
@Service
@Transactional
public class IndivAdditionalDetailsServiceImpl implements IndivAdditionalDetailsService {
	
	@Autowired
	private IndivAdditionalDetailsRepository indivAdditionalDetailsRepo;
	
	@Override
	public Optional<IndivAdditionalDetails> addIndivAdditionalDetails(IndivAdditionalDetails indivAdditionalDetails) {
		  if (indivAdditionalDetails == null) {
	            return Optional.empty();
	        }

	        try {
	        	IndivAdditionalDetails savedIndivAdditionalDetails = indivAdditionalDetailsRepo.save(indivAdditionalDetails);
	            return Optional.of(savedIndivAdditionalDetails);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public IndivAdditionalDetails findIntentAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return indivAdditionalDetailsRepo.findIntentAppById(intentAppId);
	}

	@Override
	public Optional<IndivAdditionalDetails> updateIndivAdditionalDetails(IndivAdditionalDetails indivAdditionalDetails) {
		if(indivAdditionalDetails == null)
			return Optional.empty();
		
		if(indivAdditionalDetails.getIndivAdditionalDetailsId() == null)
			return Optional.empty();
		
		try {
			IndivAdditionalDetails savedIndivAdditionalDetails = indivAdditionalDetailsRepo.save(indivAdditionalDetails);
            return Optional.of(savedIndivAdditionalDetails);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

	@Override
	public List<IndivAdditionalDetails> findIntentWithoutStatusAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return indivAdditionalDetailsRepo.findIntentWithoutStatusAppById(intentAppId);
	}



}
