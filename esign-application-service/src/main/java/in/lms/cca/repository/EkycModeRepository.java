package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.EkycMode;
import jakarta.transaction.Transactional;

public interface EkycModeRepository extends JpaRepository<EkycMode, Long>{

	@Query("FROM EkycMode a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId ORDER BY created DESC")
	List<EkycMode> findAllEKYCModeByAppId(Long esignLicenseeAppId);

	@Modifying
	@Transactional
	@Query("DELETE FROM EkycMode c WHERE c.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId")
	void deleteAllEKYCModeByAppId(@Param("esignLicenseeAppId") Long esignLicenseeAppId);

	@Query("FROM EkycMode a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.status = :status ORDER BY created DESC")
	List<EkycMode> getAllEKYCModeByAppIdAndStatus(Long esignLicenseeAppId, String status);

	@Modifying
	@Transactional
	@Query("UPDATE EkycMode a SET a.status='Inactive' WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.status='Active'")
	void inactiveAllEKYCModeByAppId(@Param("esignLicenseeAppId") Long esignLicenseeAppId);

	@Query("FROM EkycMode a WHERE a.ekycModeId = :id")
	EkycMode findEkycModeById(Long id);

	@Query("FROM EkycMode a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.status = :status AND a.isApprovalRequired = :isApprovalRequired ORDER BY created DESC")
	List<EkycMode> findAllEKYCModeByAppIdAndStatusAndRequired(Long esignLicenseeAppId, String status,
			Boolean isApprovalRequired);

	@Modifying
	@Transactional
	@Query("UPDATE EkycMode a SET a.status = :status WHERE a.esignLicenseeAppId.esignLicenseeAppId = :id AND (a.status = 'Active' OR a.status = 'Inactive')")
	void changeStatusOfAllEKYCModeByAppIdAndStatus(Long id, String status);

	@Query("SELECT DISTINCT a.ekycMode FROM EkycMode a WHERE a.esignLicenseeAppId.userName = :userName")
	List<String> findDistinctApprovedEkycModesByUsername(String userName);


}
