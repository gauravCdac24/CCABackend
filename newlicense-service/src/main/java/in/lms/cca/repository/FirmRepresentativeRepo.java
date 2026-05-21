package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.FirmAuthorizedRepresentative;

public interface FirmRepresentativeRepo extends JpaRepository<FirmAuthorizedRepresentative, Long> {

	@Query("SELECT d FROM FirmAuthorizedRepresentative d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status=:status")
	List<FirmAuthorizedRepresentative> findIntentAppById(@Param("intentAppId")Long intentAppId,@Param("status") String status);

	@Query("SELECT d FROM FirmAuthorizedRepresentative d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<FirmAuthorizedRepresentative> findIntentWitoutAppById(@Param("intentAppId")Long intentAppId);
	

}
