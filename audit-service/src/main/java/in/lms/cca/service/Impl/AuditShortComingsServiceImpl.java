package in.lms.cca.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.repository.AuditShortComingsRepository;
import in.lms.cca.service.AuditShortComingsService;

@Service
@Transactional
public class AuditShortComingsServiceImpl implements AuditShortComingsService {
	
	@Autowired
	private AuditShortComingsRepository auditShortComingsRepository;

	@Override
	public Optional<AuditShortComingsEntity> saveData(AuditShortComingsEntity auditShortComingsEntity) {
		 if (auditShortComingsEntity == null) {
	            return Optional.empty();
	        }

	        try {
	        	AuditShortComingsEntity savedAuditShortComingsEntity = auditShortComingsRepository.save(auditShortComingsEntity);
	            return Optional.of(savedAuditShortComingsEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public AuditShortComingsEntity getByAuditId(ApplicationAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return null;
		}
		return getActiveByAppAuditId(applicationAuditEntity.getAppAuditId());
	}

	@Override
	public AuditShortComingsEntity getActiveByAppAuditId(Long appAuditId) {
		if (appAuditId == null) {
			return null;
		}
		return auditShortComingsRepository.getActiveByAppAuditId(appAuditId);
	}

	@Override
	public Optional<AuditShortComingsEntity> updateData(AuditShortComingsEntity documentEntity) {
		if (documentEntity == null) {
            return Optional.empty();
        }
		if (documentEntity.getShortcomingId() == null) {
            return Optional.empty();
        }

        try {
        	AuditShortComingsEntity savedAuditShortComingsEntity = auditShortComingsRepository.save(documentEntity);
            return Optional.of(savedAuditShortComingsEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}

	@Override
	public List<AuditShortComingsEntity> findByAuditsId(Long appAuditId) {
		return auditShortComingsRepository.findByAuditsId(appAuditId);
	}

	@Override
	public long countCommitteeRejections(Long appAuditId) {
		if (appAuditId == null) {
			return 0;
		}
		return auditShortComingsRepository.findByAuditsId(appAuditId).stream()
				.filter(this::isCommitteeRejectionRecord)
				.count();
	}

	@Override
	public void resetCommitteeRejections(Long appAuditId) {
		if (appAuditId == null) {
			return;
		}
		Date now = new Date();
		auditShortComingsRepository.findByAuditsId(appAuditId).stream()
				.filter(this::isCommitteeRejectionRecord)
				.forEach(record -> {
					record.setShortcomings("CCA_RESET");
					record.setStatus("Inactive");
					record.setUpdated(now);
					auditShortComingsRepository.save(record);
				});
	}

	private boolean isCommitteeRejectionRecord(AuditShortComingsEntity record) {
		if (record == null) {
			return false;
		}
		String type = record.getShortcomings();
		if ("COMMITTEE_REJECTION".equals(type)) {
			return true;
		}
		// Legacy rows created before rejection tagging
		return type == null || type.isBlank()
				|| (!"COMMITTEE_APPROVAL".equals(type) && !"CCA_RESET".equals(type));
	}

}
