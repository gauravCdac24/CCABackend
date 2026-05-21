package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditReviewReportEntity;
import in.lms.cca.repository.AuditReviewReportRepository;
import in.lms.cca.service.AuditReviewReportService;


@Service
@Transactional
public class AuditReviewReportServiceImpl implements AuditReviewReportService {
	
	
	@Autowired
	private AuditReviewReportRepository auditReviewReportRepository;

	@Override
	public Optional<AuditReviewReportEntity> saveData(AuditReviewReportEntity applicantShortcomingsReportEntity) {
		if(applicantShortcomingsReportEntity==null)
			  return Optional.empty();
			 try {
				 AuditReviewReportEntity savedApplicantShortcomingsReportEntity = auditReviewReportRepository.save(applicantShortcomingsReportEntity);
		            return Optional.of(savedApplicantShortcomingsReportEntity);
		        } catch (Exception e) {
		            return Optional.empty();
		        }
	}

	@Override
	public List<AuditReviewReportEntity> findByAuditsId(Long shortcomingId) {
		// TODO Auto-generated method stub
		return auditReviewReportRepository.findByAuditsId(shortcomingId);
	}

	@Override
	public Optional<AuditReviewReportEntity> downloadfileBydDocumentName(String fileName) {
		// TODO Auto-generated method stub
		return auditReviewReportRepository.downloadfileBydDocumentName(fileName);
	}
	
	

}
