package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.PaymentProof;

public interface PaymentProofRepository extends JpaRepository<PaymentProof, Long> {

	@Query("SELECT d FROM PaymentProof d WHERE d.indivApplicationId.intentAppId = :intentAppId AND d.status='Active'")
	PaymentProof getChangedThePaymentStatus(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM PaymentProof d WHERE d.indivApplicationId.intentAppId = :decryptedIntentId AND d.status='Active'")
	PaymentProof getAllIntentApplicationByIntentId(@Param("decryptedIntentId")Long decryptedIntentId); 

}
