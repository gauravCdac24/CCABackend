package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicantActionTakenEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;

public interface ApplicationActionTakenRepository extends JpaRepository<ApplicantActionTakenEntity, Long> {

	
	
	@Query("SELECT d FROM ApplicantActionTakenEntity d WHERE d.auditReportCriteriaEntity=:reportCriteriaEntity AND d.status='Active'")
	ApplicantActionTakenEntity getAllByReportCriteria(@Param("reportCriteriaEntity")AuditReportCriteriaEntity reportCriteriaEntity);

	@Query("SELECT d FROM ApplicantActionTakenEntity d WHERE d.userName=:applicantUserName AND d.status='Active'")
	List<ApplicantActionTakenEntity> getAllApplicationTaken(String applicantUserName);

	@Query("SELECT d FROM ApplicantActionTakenEntity d WHERE d.actionReport=:documentName AND d.status='Active'")
	Optional<ApplicantActionTakenEntity> downloadfileBydDocumentName(@Param("documentName")String documentName);
	

}
