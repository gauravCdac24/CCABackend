package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.BankDetails;
import in.lms.cca.entity.BankDraft;
import in.lms.cca.repository.BankDraftRepository;
import in.lms.cca.service.BankDraftService;

@Service
@Transactional
public class BankDraftServiceImpl implements BankDraftService{
	
	@Autowired
	private BankDraftRepository bankDraftRepo;

	@Override
	public Optional<BankDraft> addBankDraft(BankDraft bankDraft) {
		 if (bankDraft == null) {
	            return Optional.empty();
	        }

	        try {
	        	BankDraft savedBankDraft = bankDraftRepo.save(bankDraft);
	            return Optional.of(savedBankDraft);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public BankDraft findByindivApplicationId(Long intentAppId) {
		// TODO Auto-generated method stub
		return bankDraftRepo.findByindivApplicationId(intentAppId);
	}

	@Override
	public Optional<BankDraft> updateBankDraft(BankDraft bankDraft) {
		if(bankDraft == null)
			return Optional.empty();
		
		if(bankDraft.getBankDraftId() == null)
			return Optional.empty();
		
		try {
			BankDraft savedBankDraft = bankDraftRepo.save(bankDraft);
            return Optional.of(savedBankDraft);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

	@Override
	public BankDraft findIntentAppWithStatusById(Long intentAppId, String status) {
		// TODO Auto-generated method stub
		return bankDraftRepo.findIntentAppWithStatusById(intentAppId,status);
	}

	@Override
	public BankDraft findByindivWithoutApplicationId(Long intentAppId) {
		// TODO Auto-generated method stub
List<BankDraft> drafts = bankDraftRepo.findByindivWithoutApplicationId(intentAppId); 
		
		// Return the first one, or null if empty
		if (drafts != null && !drafts.isEmpty()) {
			return drafts.get(0);
		}
		return null;
		//return bankDraftRepo.findByindivWithoutApplicationId(intentAppId);
	}

	@Override
	public List<BankDraft> findIntentWithoutAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return bankDraftRepo.findByindivWithoutApplicationIds(intentAppId);
	}
	
	
	

}
