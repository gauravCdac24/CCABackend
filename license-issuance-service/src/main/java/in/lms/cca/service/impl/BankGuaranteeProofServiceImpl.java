package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.BankGuaranteeProof;
import in.lms.cca.repository.BankGuaranteeProofRepository;
import in.lms.cca.service.IBankGuaranteeProofService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BankGuaranteeProofServiceImpl implements IBankGuaranteeProofService{

	@Autowired
	private BankGuaranteeProofRepository repo;
	
	@Override
	public Optional<BankGuaranteeProof> addBankGuaranteeProof(BankGuaranteeProof obj) {
		
		if(obj == null) {
			return Optional.empty();
		}
		try { 
	        
			BankGuaranteeProof bankGuaranteeObj = repo.save(obj);
	        return Optional.of(bankGuaranteeObj);
	    
		}catch(Exception e) {
				return Optional.empty();
		}	
		
	}

	@Override
	public Optional<BankGuaranteeProof> updateBankGuaranteeProof(BankGuaranteeProof obj) {
		
		if(obj == null) {
			return Optional.empty();
		}
		
		if(obj.getProofId() == null) {
			return Optional.empty();
		}
		
		try {     
			BankGuaranteeProof bankGuaranteeObj = repo.save(obj);
	        return Optional.of(bankGuaranteeObj);
		}catch(Exception e) {
				return Optional.empty();
		}
		
	}

	@Override
	public BankGuaranteeProof getBankGuaranteeProofById(Long id) {
		return repo.findByBankGuaranteeProofId(id);
	}

	@Override
	public List<BankGuaranteeProof> getAllBankGuaranteeProof() {
		return repo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<BankGuaranteeProof> getAllActiveBankGuaranteeProof() {
		return repo.findAllActiveBankGuaranteeProof();
	}

	@Override
	public List<BankGuaranteeProof> getAllInactiveGuaranteeProof() {
		return repo.findAllInActiveBankGuaranteeProof();
	}


	
	
	
}
