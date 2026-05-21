package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import in.lms.cca.entity.BankGuaranteeProof;

public interface BankGuaranteeProofRepository extends JpaRepository<BankGuaranteeProof, Long>{


	@Query("FROM BankGuaranteeProof a WHERE a.proofId=:id")
	BankGuaranteeProof findByBankGuaranteeProofId(@Param("id")Long id);

	@Query("FROM BankGuaranteeProof WHERE status='Active' order by created DESC")
	List<BankGuaranteeProof> findAllActiveBankGuaranteeProof();
	
	@Query("FROM BankGuaranteeProof WHERE status='Inactive' order by created DESC")
	List<BankGuaranteeProof> findAllInActiveBankGuaranteeProof();

	
	
}
