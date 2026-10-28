package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.GovtOrganizationApplication;

public interface GovernmentAgencyRepository extends JpaRepository<GovtOrganizationApplication, Long>{

	@Query("SELECT d FROM GovtOrganizationApplication d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status=:status")
	GovtOrganizationApplication findIntentAppById(@Param("intentAppId")Long intentAppId, @Param("status")String status);

	@Query("SELECT d.orgName FROM GovtOrganizationApplication d WHERE d.intentAppId.intentAppId = :intentAppId")
	String getOrganizationNameByIntentAppId(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM GovtOrganizationApplication d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status='Active'")
	GovtOrganizationApplication findWithoutIntentAppById(@Param("intentAppId")Long intentAppId);
	

}
