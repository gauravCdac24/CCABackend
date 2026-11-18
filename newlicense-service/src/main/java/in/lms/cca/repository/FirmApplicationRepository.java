package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.FirmApplication;

public interface FirmApplicationRepository extends JpaRepository<FirmApplication, Long> {

	
	@Query("SELECT d FROM FirmApplication d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status =:status")
	FirmApplication findIntentAppById(@Param("intentAppId")Long intentAppId, @Param("status")String status);

	@Query("SELECT d.officeName FROM FirmApplication d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<String> getFirmNameByIntentAppId(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM FirmApplication d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<FirmApplication> findIntentWithoutStatusAppById(@Param("intentAppId")Long intentAppId);

}
