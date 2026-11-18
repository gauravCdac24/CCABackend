package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationAuditEntity;

public interface ApplicationAuditRepository extends JpaRepository<ApplicationAuditEntity, Long> {

	@Query("SELECT d FROM ApplicationAuditEntity d WHERE d.applicantUserName = :applicantUserName AND d.status='Active'")
	ApplicationAuditEntity getByApplicantUserName(@Param("applicantUserName")String applicantUserName);

	@Query("SELECT d FROM ApplicationAuditEntity d WHERE d.applicantUserName = :applicantUserName")
	List<ApplicationAuditEntity> getByApplicantUserNames(@Param("applicantUserName")String applicantUserName);

	@Query("SELECT d FROM ApplicationAuditEntity d WHERE d.appAuditId = :appAuditId AND d.status='Active'")
	ApplicationAuditEntity getAuditDetailsById(@Param("appAuditId")Long appAuditId);

}
