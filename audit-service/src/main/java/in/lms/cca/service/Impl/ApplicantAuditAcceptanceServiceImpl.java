package in.lms.cca.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ApplicantAuditAcceptanceEntity;
import in.lms.cca.repository.ApplicantAuditAcceptanceRepository;
import in.lms.cca.service.ApplicantAuditAcceptanceService;

@Service
public class ApplicantAuditAcceptanceServiceImpl implements ApplicantAuditAcceptanceService {

	@Autowired
	private ApplicantAuditAcceptanceRepository repository;

	@Override
	public Optional<ApplicantAuditAcceptanceEntity> findActiveByApplicantUserName(String applicantUserName) {
		return repository.findActiveByApplicantUserName(applicantUserName);
	}

	@Override
	public ApplicantAuditAcceptanceEntity save(ApplicantAuditAcceptanceEntity entity) {
		return repository.save(entity);
	}

	@Override
	public ApplicantAuditAcceptanceEntity getOrCreate(String applicantUserName) {
		return repository.findActiveByApplicantUserName(applicantUserName).orElseGet(() -> {
			ApplicantAuditAcceptanceEntity created = new ApplicantAuditAcceptanceEntity();
			created.setApplicantUserName(applicantUserName);
			created.setLetterDownloaded(Boolean.FALSE);
			created.setEsignStatus("NONE");
			created.setStatus("Active");
			return repository.save(created);
		});
	}
}
