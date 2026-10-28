package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.LicenseeAuditEntity;

public interface ApplicationAuditRepository extends JpaRepository<LicenseeAuditEntity, Long> {

	@Query("SELECT d FROM LicenseeAuditEntity d WHERE d.applicantUserName = :applicantUserName AND d.status='Active'")
	LicenseeAuditEntity getByApplicantUserName(@Param("applicantUserName")String applicantUserName);

	@Query("SELECT d FROM LicenseeAuditEntity d WHERE d.applicantUserName = :applicantUserName AND d.status='Active' AND d.auditScheduleId.auditScheduleId = :auditScheduleId")
	LicenseeAuditEntity getActiveByApplicantUserNameAndAuditScheduleId(
			@Param("applicantUserName") String applicantUserName,
			@Param("auditScheduleId") Long auditScheduleId);

	@Query("SELECT d FROM LicenseeAuditEntity d WHERE d.applicantUserName = :applicantUserName")
	List<LicenseeAuditEntity> getByApplicantUserNames(@Param("applicantUserName")String applicantUserName);

	@Query("SELECT d FROM LicenseeAuditEntity d WHERE d.LicenseeAuditId = :appAuditId AND d.status='Active'")
	LicenseeAuditEntity getAuditDetailsById(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM LicenseeAuditEntity d WHERE d.auditScheduleId.auditScheduleId = :auditScheduleId")
	List<LicenseeAuditEntity> findByAuditScheduleId(@Param("auditScheduleId") Long auditScheduleId);

	@Query("SELECT d FROM LicenseeAuditEntity d WHERE d.auditScheduleId.auditScheduleId = :auditScheduleId AND d.status='Active'")
	LicenseeAuditEntity findActiveByAuditScheduleId(@Param("auditScheduleId") Long auditScheduleId);

}
