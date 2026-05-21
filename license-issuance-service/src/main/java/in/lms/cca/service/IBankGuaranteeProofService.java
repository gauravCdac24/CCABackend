package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.BankGuaranteeProof;

public interface IBankGuaranteeProofService {

	Optional<BankGuaranteeProof> addBankGuaranteeProof(BankGuaranteeProof obj);
	Optional<BankGuaranteeProof> updateBankGuaranteeProof(BankGuaranteeProof obj);
	BankGuaranteeProof getBankGuaranteeProofById(Long id);
	List<BankGuaranteeProof> getAllBankGuaranteeProof();
	List<BankGuaranteeProof> getAllActiveBankGuaranteeProof();
	List<BankGuaranteeProof> getAllInactiveGuaranteeProof();
	
}
