package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.master.ApplicationTypeMaster;

public interface IndivAplicationMasterRepository extends JpaRepository<IndivApplication, Long>{

	@Query("FROM ApplicationTypeMaster a WHERE a.appTypeMasterId=:appTypeMasterId")
	ApplicationTypeMaster findByApplicationTypeId(@Param("appTypeMasterId")Long appTypeMasterId);

	@Query("SELECT d FROM ApplicationTypeMaster d WHERE d.appType = :appType")
	ApplicationTypeMaster findApplicationTypeMasterByName(String appType);

	@Query("FROM ApplicationTypeMaster a ORDER BY a.appTypeMasterId ASC")
	List<ApplicationTypeMaster> findAllApplicationTypeMasters();

	@Query("FROM IndivApplication d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status=:status")
	IndivApplication findIntentAppById(@Param("intentAppId")Long intentAppId,@Param("status") String status);

	@Query("SELECT CONCAT(d.salutation1, ' ', d.firstName1, ' ', d.middleName1, ' ', d.lastName1) AS fullName " +
		       "FROM IndivApplication d " +
		       "WHERE d.intentAppId.intentAppId = :intentAppId")
		List<String> getFullNameByIntentAppId(@Param("intentAppId") Long intentAppId);

	@Query("FROM IndivApplication d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<IndivApplication> findIntentWithoutStatusAppById(@Param("intentAppId")Long intentAppId);

	
}
