package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AuditControlEntity;
import in.lms.cca.repository.AuditRepository;
import in.lms.cca.service.AuditService;
import in.lms.cca.util.global.AnnualAuditContext;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {
	
	@Autowired
	private AuditRepository auditRepository;

	@Override
	public Optional<AuditControlEntity> save(AuditControlEntity control) {
		 if (control == null) {
	            return Optional.empty();
	        }

		 if (control.getAuditScheduleId() == null) {
			 control.setAuditScheduleId(AnnualAuditContext.getAuditScheduleId());
		 }

	        try {
	        	AuditControlEntity savedAuditControlEntity = auditRepository.save(control);
	            return Optional.of(savedAuditControlEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<AuditControlEntity> getAuditControlsByUser(String userName) {
		return auditRepository.getAuditControlsByUser(userName, AnnualAuditContext.getAuditScheduleId());
	}

	@Override
	public List<AuditControlEntity> getActiveAuditControlsByUser(String userName) {
		if (userName == null || userName.isBlank()) {
			return List.of();
		}
		List<AuditControlEntity> controls = auditRepository.findActiveByUserName(userName.trim());
		return controls == null ? List.of() : controls;
	}

	@Override
	public void deleteByControlId(int idToRemove, String userName) {
		 auditRepository.deleteByControlIdAndUser(idToRemove, userName, AnnualAuditContext.getAuditScheduleId());
	}
	

}
