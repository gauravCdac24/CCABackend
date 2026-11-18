package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.PaymentVerification;

public interface PaymentVarificationRepository extends JpaRepository<PaymentVerification, Long> {

	@Query("SELECT d FROM PaymentVerification d WHERE d.indivApplicationId = :intentApp AND d.status='Active'")
	PaymentVerification getByIntentId(@Param("intentApp")IntentApplication intentApp);

	@Query("SELECT d FROM PaymentVerification d WHERE d.indivApplicationId.intentAppId = :intentApp AND d.status='Active'")
	PaymentVerification getChangedThePaymentStatus(@Param("intentApp")Long intentAppId);

}
