package in.lms.cca.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.AnnualAuditScheduleListDTO;
import in.lms.cca.dto.AnnualAuditWorkflowDTO;
import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.service.AnnualAuditScheduleListService;
import in.lms.cca.service.AnnualAuditWorkflowService;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.AuditAgencySelectionService;
import in.lms.cca.service.LicenseeAuditservice;

@Service
public class AnnualAuditScheduleListServiceImpl implements AnnualAuditScheduleListService {

	@Autowired
	private LicenseeAuditservice licenseeAuditservice;

	@Autowired
	private AnnualAuditWorkflowService annualAuditWorkflowService;

	@Autowired
	private ApplicationAuditService applicationAuditService;

	@Autowired
	private AuditAgencySelectionService auditAgencySelectionService;

	@Override
	public List<AnnualAuditScheduleListDTO> getScheduleList(String id, String role) {
		String normalizedRole = role == null ? "LICENSEE" : role.toUpperCase().replace('-', '_');
		List<AnnualAuditScheduleEntity> schedules = resolveSchedules(id, normalizedRole);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<AnnualAuditScheduleListDTO> response = new ArrayList<>();

		for (AnnualAuditScheduleEntity schedule : schedules) {
			AnnualAuditScheduleListDTO dto = new AnnualAuditScheduleListDTO();
			dto.setAuditScheduleId(String.valueOf(schedule.getAuditScheduleId()));
			dto.setLicenseId(schedule.getLicenseId());
			dto.setUserName(schedule.getUserName());
			dto.setStatus(schedule.getStatus());
			if (schedule.getScheduleStartDate() != null) {
				dto.setScheduleStartDate(dateFormat.format(schedule.getScheduleStartDate()));
			}

			boolean agencySelected = false;
			LicenseeAuditEntity activeAudit =
					applicationAuditService.findActiveByAuditScheduleId(schedule.getAuditScheduleId());
			if (activeAudit != null) {
				AuditAgencySelectionEntity selectionEntity =
						auditAgencySelectionService.findbyAuditId(activeAudit);
				agencySelected = selectionEntity != null
						&& "Active".equalsIgnoreCase(selectionEntity.getStatus());
			}
			dto.setAgencySelected(agencySelected);

			AnnualAuditWorkflowDTO workflow =
					annualAuditWorkflowService.buildWorkflow(schedule, normalizedRole);
			dto.setScheduleState(workflow.getScheduleState());
			dto.setCurrentStep(workflow.getCurrentStep());
			dto.setReadOnly(workflow.isReadOnly());
			dto.setAllowedActions(workflow.getAllowedActions());
			response.add(dto);
		}

		return response;
	}

	private List<AnnualAuditScheduleEntity> resolveSchedules(String id, String role) {
		switch (role) {
		case "LICENSEE":
			return licenseeAuditservice.getStartedAnnualAuditDetailsByUsername(id);
		case "AUDIT_AGENCY":
		case "AUDITAGENCY":
			return licenseeAuditservice.getStartedAnnualAuditSchedulesByAuditAgencyId(id);
		case "CCA":
		case "CCA_OFFICER":
		case "CCAOFFICER":
		case "REVIEW_COMMITTEE":
		case "REVIEWCOMMITTEE":
			return licenseeAuditservice.getAllStartedAnnualAuditSchedules();
		default:
			return licenseeAuditservice.getStartedAnnualAuditDetailsByUsername(id);
		}
	}
}
