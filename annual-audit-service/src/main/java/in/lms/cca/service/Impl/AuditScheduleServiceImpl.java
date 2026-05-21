package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.lms.cca.entity.AuditScheduleEntity;
import in.lms.cca.repository.AuditScheduleRepository;
import in.lms.cca.service.AuditScheduleService;

@Service
@Transactional
public class AuditScheduleServiceImpl implements AuditScheduleService {
	
	@Autowired
	private AuditScheduleRepository auditScheduleRepository;

	@Override
	public Optional<AuditScheduleEntity> saveData(AuditScheduleEntity auditScheduleEntity) {
		 if (auditScheduleEntity == null) {
	            return Optional.empty();
	        }

	        try {
	        	AuditScheduleEntity savedAuditScheduleEntity = auditScheduleRepository.save(auditScheduleEntity);
	            return Optional.of(savedAuditScheduleEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<AuditScheduleEntity> getAllDataByAuditId(Long appAuditId) {
		// TODO Auto-generated method stub
		return auditScheduleRepository.getAllDataByAuditId(appAuditId);
	}

	@Override
	public Optional<AuditScheduleEntity> getAllData(Long id) {
		// TODO Auto-generated method stub
		return auditScheduleRepository.downloadfile(id);
	}

	@Override
	public Optional<AuditScheduleEntity> aprovedData(AuditScheduleEntity auditScheduleEntity) {
		 if (auditScheduleEntity == null) {
	            return Optional.empty();
	        }
		 if (auditScheduleEntity.getAuditScheduleId() == null) {
	            return Optional.empty();
	        }

	        try {
	        	AuditScheduleEntity savedAuditScheduleEntity = auditScheduleRepository.save(auditScheduleEntity);
	            return Optional.of(savedAuditScheduleEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public Optional<AuditScheduleEntity> updateData(AuditScheduleEntity scheduleEntity) {
		if (scheduleEntity == null) {
            return Optional.empty();
        }
		
		if (scheduleEntity.getAuditScheduleId() == null) {
            return Optional.empty();
        }

        try {
        	AuditScheduleEntity savedAuditScheduleEntity = auditScheduleRepository.save(scheduleEntity);
            return Optional.of(savedAuditScheduleEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
	}

}
