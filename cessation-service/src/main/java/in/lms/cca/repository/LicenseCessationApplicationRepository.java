package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.LicenseCessationApplicationEntity;

public interface LicenseCessationApplicationRepository extends JpaRepository<LicenseCessationApplicationEntity, Long>{

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.licenseId = :licenseId")
	List<LicenseCessationApplicationEntity> findByLicenseId(@Param("licenseId")String licenseId);

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.licenseId = :licenseId AND a.status!='Inactive'")
	LicenseCessationApplicationEntity getByLicenseById(@Param("licenseId")String licenseId);

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.status='Active'")
	List<LicenseCessationApplicationEntity> getAllData();

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.cessationAppId = :cessationAppId AND a.status='Active'")
	LicenseCessationApplicationEntity getNoticeDocumentById(@Param("cessationAppId")Long cessationAppId);

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.status='cessation_form_submitted'")
	List<LicenseCessationApplicationEntity> getAllDataForUndertaking();

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.status='Recommand For Rejection'")
	List<LicenseCessationApplicationEntity> getAllDataForUndertakings();

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.status='approval_from_cca_officer'")
	List<LicenseCessationApplicationEntity> getAllDataForUndertakinges();

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.cessationAppId = :cessationAppId AND a.status!='Inactive'")
	LicenseCessationApplicationEntity getNoticeDocumentsById(@Param("cessationAppId")Long cessationAppId);

	@Query("FROM LicenseCessationApplicationEntity a WHERE a.status!='Inactive'")
	List<LicenseCessationApplicationEntity> getAllDataForUndertakingess();

}
