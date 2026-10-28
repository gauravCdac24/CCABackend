package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicantShortcomingsReportEntity;

public interface ApplicantShortcomingsReportService {

	ApplicantShortcomingsReportEntity getByShortIdId(Long shortcomingId);

	Optional<ApplicantShortcomingsReportEntity> saveData(ApplicantShortcomingsReportEntity applicantShortcomingsReportEntity);

	Optional<ApplicantShortcomingsReportEntity> updateData(ApplicantShortcomingsReportEntity documentsEntity);

	List<ApplicantShortcomingsReportEntity> findByAuditsId(Long shortcomingId);

	Optional<ApplicantShortcomingsReportEntity> downloadfileBydDocumentName(String documentName);

}
