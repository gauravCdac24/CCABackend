package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IndivAdditionalDetails;

public interface IndivAdditionalDetailsRepository extends JpaRepository<IndivAdditionalDetails, Long>{

	@Query("SELECT d FROM IndivAdditionalDetails d WHERE d.intentAppId.intentAppId = :intentAppId")
	IndivAdditionalDetails findIntentAppById(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM IndivAdditionalDetails d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<IndivAdditionalDetails> findIntentWithoutStatusAppById(@Param("intentAppId")Long intentAppId);

}
