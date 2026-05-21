package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ApplicantShortcomingsReportEntity;
import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.repository.ApplicantShortcomingsReportRepository;
import in.lms.cca.service.ApplicantShortcomingsReportService;

@Service
@Transactional
public class ApplicantShortcomingsReportServiceImpl implements ApplicantShortcomingsReportService {
	
	@Autowired
	private ApplicantShortcomingsReportRepository applicantShortcomingsReportRepository;

	@Override
	public ApplicantShortcomingsReportEntity getByShortIdId(Long shortcomingId) {
		// TODO Auto-generated method stub
		return applicantShortcomingsReportRepository.getByShortIdId(shortcomingId);
	}

	@Override
	public Optional<ApplicantShortcomingsReportEntity> saveData(
			ApplicantShortcomingsReportEntity applicantShortcomingsReportEntity) {
		if (applicantShortcomingsReportEntity == null) {
            return Optional.empty();
        }
		

        try {
        	ApplicantShortcomingsReportEntity savedApplicantShortcomingsReportEntity = applicantShortcomingsReportRepository.save(applicantShortcomingsReportEntity);
            return Optional.of(savedApplicantShortcomingsReportEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}

	@Override
	public Optional<ApplicantShortcomingsReportEntity> updateData(ApplicantShortcomingsReportEntity documentsEntity) {
		if (documentsEntity == null) {
            return Optional.empty();
        }
		if (documentsEntity.getShortcomingId() == null) {
            return Optional.empty();
        }

        try {
        	ApplicantShortcomingsReportEntity savedApplicantShortcomingsReportEntity = applicantShortcomingsReportRepository.save(documentsEntity);
            return Optional.of(savedApplicantShortcomingsReportEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}

	@Override
	public List<ApplicantShortcomingsReportEntity> findByAuditsId(Long shortcomingId) {
		// TODO Auto-generated method stub
		return applicantShortcomingsReportRepository.findByAuditsId(shortcomingId);
	}

	@Override
	public Optional<ApplicantShortcomingsReportEntity> downloadfileBydDocumentName(String documentName) {
		// TODO Auto-generated method stub
		return applicantShortcomingsReportRepository.downloadfileBydDocumentName(documentName);
	}

}
