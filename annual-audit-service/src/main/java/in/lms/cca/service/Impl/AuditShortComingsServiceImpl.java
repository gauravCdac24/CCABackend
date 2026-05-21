package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
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
	public AuditShortComingsEntity getByAuditId(LicenseeAuditEntity applicationAuditEntity) {
		// TODO Auto-generated method stub
		return auditShortComingsRepository.getByAuditId(applicationAuditEntity);
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
		// TODO Auto-generated method stub
		return auditShortComingsRepository.findByAuditsId(appAuditId);
	}

}
