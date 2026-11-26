package in.lms.cca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.ApplicantAuditAcceptanceEntity;

public interface ApplicantAuditAcceptanceRepository extends JpaRepository<ApplicantAuditAcceptanceEntity, Long> {

	Optional<ApplicantAuditAcceptanceEntity> findFirstByApplicantUserNameOrderByAcceptanceIdDesc(
			String applicantUserName);
}
