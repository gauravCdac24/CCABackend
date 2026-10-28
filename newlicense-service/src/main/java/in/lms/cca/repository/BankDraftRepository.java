package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.BankDraft;

public interface BankDraftRepository extends JpaRepository<BankDraft, Long> {

	@Query("SELECT d FROM BankDraft d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status='Active'")
	BankDraft findByindivApplicationId(@Param("intentAppId")Long intentAppId);
	
	@Query("SELECT d FROM BankDraft d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status=:status")
	BankDraft findIntentAppWithStatusById(@Param("intentAppId")Long intentAppId,@Param("status")String status);

	@Query("SELECT d FROM BankDraft d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<BankDraft> findByindivWithoutApplicationId(@Param("intentAppId")Long intentAppId);

//	BankDraft findByindivWithoutApplicationId(@Param("intentAppId")Long intentAppId);
	@Query("SELECT d FROM BankDraft d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<BankDraft> findByindivWithoutApplicationIds(@Param("intentAppId")Long intentAppId);

}
