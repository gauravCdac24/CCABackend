package in.lms.cca.service.Impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ApplicantActionTakenEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.repository.ApplicationActionTakenRepository;
import in.lms.cca.service.ApplicationActionTakenService;

@Service
@Transactional
public class ApplicationActionTakenServiceImpl implements ApplicationActionTakenService {

	@Autowired
	private ApplicationActionTakenRepository actionTakenRepository;

	@Override
	public Optional<ApplicantActionTakenEntity> saveData(ApplicantActionTakenEntity actionTakenEntity) {
		if (actionTakenEntity == null) {
			return Optional.empty();
		}
		try {
			return Optional.of(actionTakenRepository.save(actionTakenEntity));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public ApplicantActionTakenEntity getAllByReportCriteria(AuditReportCriteriaEntity reportCriteriaEntity) {
		return actionTakenRepository.getAllByReportCriteria(reportCriteriaEntity);
	}

	@Override
	public Optional<ApplicantActionTakenEntity> updateData(ApplicantActionTakenEntity actionTakenEntity) {
		if (actionTakenEntity == null || actionTakenEntity.getActionTakenId() == null) {
			return Optional.empty();
		}
		try {
			return Optional.of(actionTakenRepository.save(actionTakenEntity));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<ApplicantActionTakenEntity> getAllCriteriaData() {
		return actionTakenRepository.findAll();
	}

	@Override
	public List<ApplicantActionTakenEntity> getAllApplicationTaken(String applicantUserName) {
		if (applicantUserName == null || applicantUserName.isBlank()) {
			return Collections.emptyList();
		}
		return actionTakenRepository.getAllApplicationTaken(applicantUserName.trim());
	}

	@Override
	public List<ApplicantActionTakenEntity> getAllApplicationTakenAnyStatus(String applicantUserName) {
		if (applicantUserName == null || applicantUserName.isBlank()) {
			return Collections.emptyList();
		}
		return actionTakenRepository.getAllApplicationTakenAnyStatus(applicantUserName.trim());
	}

	@Override
	public Optional<ApplicantActionTakenEntity> downloadfileBydDocumentName(String documentName) {
		if (documentName == null || documentName.isBlank()) {
			return Optional.empty();
		}
		return actionTakenRepository.downloadfileBydDocumentName(documentName.trim());
	}
}
