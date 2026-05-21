package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.UserLoginDTO;

public interface AuditReportCriteriaService {

	Optional<AuditReportCriteriaEntity> saveData(AuditReportCriteriaEntity auditReportCriteriaEntity);

	Optional<AuditReportCriteriaEntity> findById(Long criteriaId);

	List<AuditReportCriteriaEntity> getAllData();

	Optional<AuditReportCriteriaEntity> downloadfileBydDocumentName(String documentName);

	Optional<AuditReportCriteriaEntity> updateData(AuditReportCriteriaEntity auditReportCriteriaEntity);

	List<AuditReportCriteriaEntity> findByAuditId(Long appAuditId);

	void sendTheApplicantUserName(String applicantUserName);

	void changedTheApplicantUserName(String applicantUserName);

	UserLoginDTO findbyAgencyId(String auditAgencyId);

	void changedTheStatus(String applicantUserName);

	void changedTheAproveOfRejection(String applicantUserName);

	void changedTheStatusApprove(String applicantUserName);

	void changedTheApplicantRejection(String applicantUserName);

	AuditReportCriteriaEntity getAllDataByControlId(String controlId, LicenseeAuditEntity applicationAuditEntity);

}
