package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.FirmPartnerDetails;

public interface FirmPartnerRepository extends JpaRepository<FirmPartnerDetails, Long> {

	
	@Query("SELECT d FROM FirmPartnerDetails d WHERE d.intentAppId.intentAppId = :intentAppId")
	 List<FirmPartnerDetails> findIntentAppById(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM FirmPartnerDetails d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<FirmPartnerDetails> findIntentAppWithoutStatusById(@Param("intentAppId")Long intentAppId);

}
