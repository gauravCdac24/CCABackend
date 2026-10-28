package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicantActionTakenEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;

public interface ApplicationActionTakenService {

	Optional<ApplicantActionTakenEntity> saveData(ApplicantActionTakenEntity actionTakenEntity);

	ApplicantActionTakenEntity getAllByReportCriteria(AuditReportCriteriaEntity reportCriteriaEntity);

	Optional<ApplicantActionTakenEntity> updateData(ApplicantActionTakenEntity actionTakenEntity);

	List<ApplicantActionTakenEntity> getAllCriteriaData();

	List<ApplicantActionTakenEntity> getAllApplicationTaken(String applicantUserName);

	Optional<ApplicantActionTakenEntity> downloadfileBydDocumentName(String documentName);

}
