package in.lms.cca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicantAuditAcceptanceEntity;

public interface ApplicantAuditAcceptanceRepository extends JpaRepository<ApplicantAuditAcceptanceEntity, Long> {

	@Query("SELECT a FROM ApplicantAuditAcceptanceEntity a WHERE a.applicantUserName = :userName AND a.status = 'Active'")
	Optional<ApplicantAuditAcceptanceEntity> findActiveByApplicantUserName(@Param("userName") String userName);
}
