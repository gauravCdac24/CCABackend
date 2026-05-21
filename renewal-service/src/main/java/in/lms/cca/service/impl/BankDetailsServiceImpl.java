package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.BankDetails;
import in.lms.cca.entity.master.DocumentsMaster;
import in.lms.cca.repository.BankDetailsRepository;
import in.lms.cca.service.BankDetailsService;

@Service
@Transactional
public class BankDetailsServiceImpl implements BankDetailsService{
	@Autowired
	private BankDetailsRepository bankDetailsRepo;

	@Override
	public Optional<BankDetails> addBankDetails(BankDetails bankDetails) {
		 if (bankDetails == null) {
	            return Optional.empty();
	        }

	        try {
	        	BankDetails savedBankDetails = bankDetailsRepo.save(bankDetails);
	            return Optional.of(savedBankDetails);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public BankDetails findIntentAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return bankDetailsRepo.findIntentAppById(intentAppId);
	}

	@Override
	public Optional<BankDetails> updateBankDetails(BankDetails bankDetails) {
		if(bankDetails == null)
			return Optional.empty();
		
		if(bankDetails.getBankDetailsId() == null)
			return Optional.empty();
		
		try {
			BankDetails savedBankDetails = bankDetailsRepo.save(bankDetails);
            return Optional.of(savedBankDetails);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

	@Override
	public BankDetails findIntentAppWithStatusById(Long intentAppId, String status) {
		// TODO Auto-generated method stub
		return bankDetailsRepo.findIntentAppWithStatusById(intentAppId,status);
	}

	@Override
	public List<BankDetails> findIntentWithoutAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return bankDetailsRepo.findIntentWithoutAppById(intentAppId);
	}

	
	

}
