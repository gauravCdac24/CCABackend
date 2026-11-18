package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.payload.AuditAgencyFormDto;

public interface ApplicationAuditService {

	Optional<ApplicationAuditEntity> addApplicationAudit(ApplicationAuditEntity applicationAuditEntity);

	void processAuditForm(AuditAgencyFormDto formDto);

	ApplicationAuditEntity getByApplicantUserName(String ApplicantUserName);

	void changeIntentStatusByUserName(String applicantUserName);

	void rejectedByUserName(String applicantUserName);

	void approvedByUserName(String applicantUserName);

	Optional<ApplicationAuditEntity> updateAuditor(ApplicationAuditEntity applicationAuditEntity);

	List<ApplicationAuditEntity> getByApplicantUserNames(String applicantUserName);

	Optional<ApplicationAuditEntity> updateData(ApplicationAuditEntity auditEntity);

	ApplicationAuditEntity getAuditDetailsById(Long appAuditId);

	List<ApplicationAuditEntity> getAll();

	void changedApplicantStatus(String applicantUserName);

	void changedApplicantStatusByAuditor(String applicantUserName);

	void acceptApplicantStatusByAuditor(String applicantUserName);

}
