package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.repository.LicenseeAuditRepository;
import in.lms.cca.service.LicenseeAuditservice;

@Service
@Transactional
public class LicenseeAuditServiceImpl implements LicenseeAuditservice {
	
	@Autowired
	private LicenseeAuditRepository licenseeAuditRepository;

	@Override
	public Optional<AnnualAuditScheduleEntity> addAnnualAudit(AnnualAuditScheduleEntity newAnnualAuditScheduleEntity) {
		 if (newAnnualAuditScheduleEntity == null) {
	            return Optional.empty();
	        }

	        try {
	        	AnnualAuditScheduleEntity savedAnnualAuditScheduleEntity = licenseeAuditRepository.save(newAnnualAuditScheduleEntity);
	            return Optional.of(savedAnnualAuditScheduleEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<AnnualAuditScheduleEntity> getAllAnnualAuditSchedule() {
		
		return licenseeAuditRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<AnnualAuditScheduleEntity> getAnnualAuditDetailsByUsername(String id) {
		
		return licenseeAuditRepository.findByUsername(id);
	}


}
