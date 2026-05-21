package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ESPPurposeEntity;
import jakarta.transaction.Transactional;

public interface ESPPurposeRepository extends JpaRepository<ESPPurposeEntity, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE ESPPurposeEntity a SET a.status='Inactive' WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.status='Active'")
	void inactiveAllPurposeByAppId(@Param("esignLicenseeAppId") Long esignLicenseeAppId);

	@Query("FROM ESPPurposeEntity a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.status = :status")
	List<ESPPurposeEntity> findAllESPPurposeByStatusAndLicenseAppId(@Param("esignLicenseeAppId") Long esignLicenseeAppId,
			@Param("status") String status);
	

}
