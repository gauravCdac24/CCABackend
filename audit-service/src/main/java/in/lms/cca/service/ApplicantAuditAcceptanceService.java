package in.lms.cca.service;

import java.util.Optional;

import in.lms.cca.entity.ApplicantAuditAcceptanceEntity;

public interface ApplicantAuditAcceptanceService {

	Optional<ApplicantAuditAcceptanceEntity> findActiveByApplicantUserName(String applicantUserName);

	ApplicantAuditAcceptanceEntity save(ApplicantAuditAcceptanceEntity entity);

	ApplicantAuditAcceptanceEntity getOrCreate(String applicantUserName);
}
