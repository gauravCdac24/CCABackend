package in.lms.cca.service.Impl;

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
	        	ApplicantActionTakenEntity savedApplicantActionTakenEntity = actionTakenRepository.save(actionTakenEntity);
	            return Optional.of(savedApplicantActionTakenEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}


	@Override
	public ApplicantActionTakenEntity getAllByReportCriteria(AuditReportCriteriaEntity reportCriteriaEntity) {
		return actionTakenRepository. getAllByReportCriteria(reportCriteriaEntity);
	}


	@Override
	public Optional<ApplicantActionTakenEntity> updateData(ApplicantActionTakenEntity actionTakenEntity) {
		 if (actionTakenEntity == null) {
	            return Optional.empty();
	        }
		 if (actionTakenEntity.getActionTakenId() == null) {
	            return Optional.empty();
	        }

	        try {
	        	ApplicantActionTakenEntity savedApplicantActionTakenEntity = actionTakenRepository.save(actionTakenEntity);
	            return Optional.of(savedApplicantActionTakenEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}


	@Override
	public List<ApplicantActionTakenEntity> getAllCriteriaData() {
		// TODO Auto-generated method stub
		return actionTakenRepository.findAll();
	}


	@Override
	public List<ApplicantActionTakenEntity> getAllApplicationTaken(String applicantUserName) {
		// TODO Auto-generated method stub
		return actionTakenRepository.getAllApplicationTaken(applicantUserName);
	}


	@Override
	public Optional<ApplicantActionTakenEntity> downloadfileBydDocumentName(String documentName) {
		// TODO Auto-generated method stub
		return actionTakenRepository.downloadfileBydDocumentName(documentName);
	}

}
