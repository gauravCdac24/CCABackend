package in.lms.cca.service.Impl;

import java.util.Collections;
import java.util.Date;
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

	@Override
	public List<AnnualAuditScheduleEntity> getStartedAnnualAuditDetailsByUsername(String id) {
		return licenseeAuditRepository.findStartedByUsername(id, new Date());
	}

	@Override
	public List<AnnualAuditScheduleEntity> getAllStartedAnnualAuditSchedules() {
		return licenseeAuditRepository.findAllStarted(new Date());
	}

	@Override
	public List<AnnualAuditScheduleEntity> getStartedAnnualAuditSchedulesByAuditAgencyId(String auditAgencyId) {
		if (auditAgencyId == null || auditAgencyId.isBlank()) {
			return Collections.emptyList();
		}
		return licenseeAuditRepository.findStartedByAuditAgencyId(auditAgencyId, new Date());
	}

	@Override
	public Optional<AnnualAuditScheduleEntity> getAnnualAuditScheduleById(Long auditScheduleId) {
		if (auditScheduleId == null) {
			return Optional.empty();
		}
		return licenseeAuditRepository.findById(auditScheduleId);
	}

	@Override
	public Optional<AnnualAuditScheduleEntity> updateScheduleStatus(Long auditScheduleId, String status) {
		if (auditScheduleId == null || status == null || status.isBlank()) {
			return Optional.empty();
		}
		Optional<AnnualAuditScheduleEntity> scheduleOpt = licenseeAuditRepository.findById(auditScheduleId);
		if (scheduleOpt.isEmpty()) {
			return Optional.empty();
		}
		AnnualAuditScheduleEntity schedule = scheduleOpt.get();
		if (status.equals(schedule.getStatus())) {
			return scheduleOpt;
		}
		schedule.setStatus(status.trim());
		schedule.setUpdated(new Date());
		return Optional.of(licenseeAuditRepository.save(schedule));
	}

	@Override
	public Optional<AnnualAuditScheduleEntity> updateScheduleStatusByUserName(String userName, String status) {
		if (userName == null || userName.isBlank() || status == null || status.isBlank()) {
			return Optional.empty();
		}
		List<AnnualAuditScheduleEntity> schedules =
				licenseeAuditRepository.findStartedByUsername(userName.trim(), new Date());
		if (schedules == null || schedules.isEmpty()) {
			schedules = licenseeAuditRepository.findByUsername(userName.trim());
		}
		if (schedules == null || schedules.isEmpty()) {
			return Optional.empty();
		}
		AnnualAuditScheduleEntity schedule = schedules.get(0);
		if (schedules.size() > 1) {
			// Prefer active (non-completed) schedule with latest start date.
			schedule = schedules.stream()
					.filter(item -> item.getDateOfCompletion() == null)
					.sorted((a, b) -> {
						Date aDate = a.getScheduleStartDate();
						Date bDate = b.getScheduleStartDate();
						if (aDate == null && bDate == null) {
							return 0;
						}
						if (aDate == null) {
							return 1;
						}
						if (bDate == null) {
							return -1;
						}
						return bDate.compareTo(aDate);
					})
					.findFirst()
					.orElse(schedules.get(0));
		}
		return updateScheduleStatus(schedule.getAuditScheduleId(), status);
	}

}
