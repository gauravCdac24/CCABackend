package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.BankDetails;

public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {

	@Query("SELECT d FROM BankDetails d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status='Active'")
	BankDetails findIntentAppById(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM BankDetails d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status=:status")
	BankDetails findIntentAppWithStatusById(@Param("intentAppId")Long intentAppId,@Param("status") String status);

	@Query("SELECT d FROM BankDetails d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<BankDetails> findIntentWithoutAppById(@Param("intentAppId")Long intentAppId);
}
