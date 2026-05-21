package in.lms.cca.service;

import java.util.List;
import java.util.Optional;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.AuditAgencyFormDto;

public interface ApplicationAuditService {

	Optional<LicenseeAuditEntity> addApplicationAudit(LicenseeAuditEntity applicationAuditEntity);

	void processAuditForm(AuditAgencyFormDto formDto);

	LicenseeAuditEntity getByApplicantUserName(String ApplicantUserName);

	void changeIntentStatusByUserName(String applicantUserName);

	void rejectedByUserName(String applicantUserName);

	void approvedByUserName(String applicantUserName);

	Optional<LicenseeAuditEntity> updateAuditor(LicenseeAuditEntity applicationAuditEntity);

	List<LicenseeAuditEntity> getByApplicantUserNames(String applicantUserName);

	Optional<LicenseeAuditEntity> updateData(LicenseeAuditEntity auditEntity);

	LicenseeAuditEntity getAuditDetailsById(Long appAuditId);

	List<LicenseeAuditEntity> getAll();

}
