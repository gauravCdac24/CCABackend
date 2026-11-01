package in.lms.cca.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.entity.AuditControlEntity;
import in.lms.cca.repository.AuditControlRepository;
import in.lms.cca.service.AuditControlSelectionService;
import in.lms.cca.service.LicenseeAuditservice;
import in.lms.cca.util.global.AnnualAuditContext;

@Service
@Transactional
public class AuditControlSelectionServiceImpl implements AuditControlSelectionService {

	private static final int MIN_CONTROL_ID = 1;
	private static final int MAX_CONTROL_ID = 11;

	@Autowired
	private AuditControlRepository auditControlRepository;

	@Autowired
	private LicenseeAuditservice licenseeAuditservice;

	@Override
	public String saveControls(List<AuditRequestDTO> requests) {
		if (requests == null || requests.isEmpty()) {
			throw new IllegalArgumentException("Please select at least one audit control.");
		}

		String userName = requireUserName(requests.get(0).getUserName());
		for (AuditRequestDTO request : requests) {
			if (request == null || request.getControlId() == null) {
				throw new IllegalArgumentException("Each selected audit control must include a valid control id.");
			}
			if (!Objects.equals(userName, requireUserName(request.getUserName()))) {
				throw new IllegalArgumentException("All selected audit controls must belong to the same licensee.");
			}
			validateControlId(request.getControlId());
		}

		Long auditScheduleId = resolveScheduleId(userName, requests.get(0).getAuditScheduleId());
		verifyScheduleOwnership(userName, auditScheduleId);

		List<AuditControlEntity> existing = auditControlRepository.findActiveByUserAndSchedule(userName, auditScheduleId);
		Set<Integer> incoming = requests.stream().map(AuditRequestDTO::getControlId).collect(Collectors.toCollection(HashSet::new));
		Set<Integer> current = existing.stream().map(AuditControlEntity::getControlId).collect(Collectors.toCollection(HashSet::new));

		Set<Integer> toAdd = new HashSet<>(incoming);
		toAdd.removeAll(current);

		Set<Integer> toRemove = new HashSet<>(current);
		toRemove.removeAll(incoming);

		Date now = new Date();
		for (Integer controlId : toAdd) {
			AuditControlEntity entity = new AuditControlEntity();
			entity.setControlId(controlId);
			entity.setUserName(userName);
			entity.setAuditScheduleId(auditScheduleId);
			entity.setStatus("Active");
			entity.setCreated(now);
			entity.setUpdated(now);
			auditControlRepository.save(entity);
		}

		for (Integer controlId : toRemove) {
			auditControlRepository.deleteByControlUserAndSchedule(controlId, userName, auditScheduleId);
		}

		return "Audit controls updated successfully.";
	}

	@Override
	public List<AuditControlEntity> getActiveControls(String userName, Long auditScheduleId) {
		String normalized = normalizeUserName(userName);
		if (normalized.isEmpty()) {
			return List.of();
		}
		Long scheduleId = auditScheduleId != null ? auditScheduleId : AnnualAuditContext.getAuditScheduleId();
		if (scheduleId != null) {
			return auditControlRepository.findActiveByUserAndSchedule(normalized, scheduleId);
		}
		return auditControlRepository.findActiveByUserName(normalized);
	}

	@Override
	public List<AuditRequestDTO> getActiveControlDtos(String userName, Long auditScheduleId) {
		List<AuditControlEntity> entities = getActiveControls(userName, auditScheduleId);
		List<AuditRequestDTO> dtos = new ArrayList<>(entities.size());
		for (AuditControlEntity entity : entities) {
			AuditRequestDTO dto = new AuditRequestDTO();
			dto.setId(entity.getId());
			dto.setControlId(entity.getControlId());
			dto.setUserName(entity.getUserName());
			dto.setStatus(entity.getStatus());
			dto.setAuditScheduleId(entity.getAuditScheduleId());
			dto.setCreated(entity.getCreated());
			dto.setUpdated(entity.getUpdated());
			dtos.add(dto);
		}
		return dtos;
	}

	private Long resolveScheduleId(String userName, Long requestScheduleId) {
		Long contextScheduleId = AnnualAuditContext.getAuditScheduleId();
		Long scheduleId = contextScheduleId != null ? contextScheduleId : requestScheduleId;
		if (scheduleId == null) {
			throw new IllegalArgumentException(
					"Annual audit schedule is required. Open the schedule from the annual audit list and try again.");
		}
		return scheduleId;
	}

	private void verifyScheduleOwnership(String userName, Long auditScheduleId) {
		Optional<AnnualAuditScheduleEntity> schedule = licenseeAuditservice.getAnnualAuditScheduleById(auditScheduleId);
		if (schedule.isEmpty()) {
			throw new IllegalArgumentException("Annual audit schedule was not found.");
		}
		AnnualAuditScheduleEntity entity = schedule.get();
		if (entity.getUserName() == null || !userName.equalsIgnoreCase(entity.getUserName().trim())) {
			throw new IllegalArgumentException("Selected audit controls do not match the annual audit schedule licensee.");
		}
	}

	private void validateControlId(Integer controlId) {
		if (controlId < MIN_CONTROL_ID || controlId > MAX_CONTROL_ID) {
			throw new IllegalArgumentException("Invalid audit control selection.");
		}
	}

	private String requireUserName(String userName) {
		String normalized = normalizeUserName(userName);
		if (normalized.isEmpty()) {
			throw new IllegalArgumentException("Licensee user name is required.");
		}
		if (normalized.length() > 25) {
			throw new IllegalArgumentException("Invalid licensee user name.");
		}
		return normalized;
	}

	private String normalizeUserName(String userName) {
		return userName == null ? "" : userName.trim();
	}
}
