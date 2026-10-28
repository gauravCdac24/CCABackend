package in.lms.cca.service.Impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.AnnualAuditWorkflowDTO;
import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.AuditNCSEntity;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.AuditScheduleEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.repository.ApplicationAuditorsRepository;
import in.lms.cca.repository.AuditNCSRepository;
import in.lms.cca.repository.AuditReportDocumentRepository;
import in.lms.cca.repository.AuditScheduleRepository;
import in.lms.cca.service.AnnualAuditWorkflowService;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.AuditAgencySelectionService;
import in.lms.cca.service.LicenseeAuditservice;

@Service
public class AnnualAuditWorkflowServiceImpl implements AnnualAuditWorkflowService {

	public static final String STATE_NOT_STARTED = "NOT_STARTED";
	public static final String STATE_ACTIVE = "ACTIVE";
	public static final String STATE_COMPLETED = "COMPLETED";

	public static final String STEP_AGENCY_SELECTION = "AGENCY_SELECTION";
	public static final String STEP_AUDIT_FORM = "AUDIT_FORM_AND_ANNEXURE";
	public static final String STEP_SCOPE_SCHEDULE = "AUDIT_SCOPE_SCHEDULE";
	public static final String STEP_AUDIT_REPORT = "AUDIT_REPORT";
	public static final String STEP_NC_ACTION = "NC_ACTION";
	public static final String STEP_NC_VERIFICATION = "NC_VERIFICATION";
	public static final String STEP_REVIEW = "REVIEW_COMMITTEE";
	public static final String STEP_COMPLETED = "COMPLETED";

	public static final String CCA_REVIEW_NOT_SUBMITTED = "NOT_SUBMITTED";
	public static final String CCA_REVIEW_PENDING = "PENDING";
	public static final String CCA_REVIEW_APPROVED = "APPROVED";
	public static final String CCA_REVIEW_REJECTED = "REJECTED";

	@Autowired
	private LicenseeAuditservice licenseeAuditservice;

	@Autowired
	private ApplicationAuditService applicationAuditService;

	@Autowired
	private AuditAgencySelectionService auditAgencySelectionService;

	@Autowired
	private ApplicationAuditorsRepository applicationAuditorsRepository;

	@Autowired
	private AuditScheduleRepository auditScheduleRepository;

	@Autowired
	private AuditReportDocumentRepository auditReportDocumentRepository;

	@Autowired
	private AuditNCSRepository auditNCSRepository;

	@Override
	public Long resolveActiveScheduleId(String userName) {
		List<AnnualAuditScheduleEntity> schedules =
				licenseeAuditservice.getStartedAnnualAuditDetailsByUsername(userName);
		schedules.sort(Comparator.comparing(
				AnnualAuditScheduleEntity::getScheduleStartDate,
				Comparator.nullsLast(Comparator.naturalOrder())));
		Date today = stripTime(new Date());
		for (AnnualAuditScheduleEntity schedule : schedules) {
			if (!isScheduleCompleted(schedule)
					&& schedule.getScheduleStartDate() != null
					&& !stripTime(schedule.getScheduleStartDate()).after(today)) {
				return schedule.getAuditScheduleId();
			}
		}
		return null;
	}

	@Override
	public AnnualAuditWorkflowDTO buildWorkflow(AnnualAuditScheduleEntity schedule, String role) {
		AnnualAuditWorkflowDTO workflow = new AnnualAuditWorkflowDTO();
		workflow.setAuditScheduleId(String.valueOf(schedule.getAuditScheduleId()));

		if (isScheduleCompleted(schedule)) {
			workflow.setScheduleState(STATE_COMPLETED);
			workflow.setCurrentStep(STEP_COMPLETED);
			workflow.setReadOnly(true);
			workflow.setAllowedActions(List.of("VIEW"));
			return workflow;
		}

		Date today = stripTime(new Date());
		if (schedule.getScheduleStartDate() == null
				|| stripTime(schedule.getScheduleStartDate()).after(today)) {
			workflow.setScheduleState(STATE_NOT_STARTED);
			workflow.setCurrentStep(STATE_NOT_STARTED);
			workflow.setReadOnly(true);
			workflow.setAllowedActions(List.of());
			return workflow;
		}

		Long activeScheduleId = resolveActiveScheduleId(schedule.getUserName());
		if (activeScheduleId == null || !activeScheduleId.equals(schedule.getAuditScheduleId())) {
			workflow.setScheduleState(STATE_NOT_STARTED);
			workflow.setCurrentStep(STATE_NOT_STARTED);
			workflow.setReadOnly(true);
			workflow.setAllowedActions(List.of());
			return workflow;
		}

		workflow.setScheduleState(STATE_ACTIVE);
		String currentStep = resolveCurrentStep(schedule.getAuditScheduleId());
		workflow.setCurrentStep(currentStep);
		String ccaReviewStatus = resolveCcaUndertakingReviewStatus(schedule.getAuditScheduleId());
		boolean reviewCompleted = CCA_REVIEW_APPROVED.equals(ccaReviewStatus)
				|| CCA_REVIEW_REJECTED.equals(ccaReviewStatus);
		workflow.setReadOnly(reviewCompleted && isCcaRole(role));
		workflow.setAllowedActions(resolveAllowedActions(role, currentStep, schedule.getAuditScheduleId()));
		return workflow;
	}

	private boolean isCcaRole(String role) {
		if (role == null) {
			return false;
		}
		String normalizedRole = role.toUpperCase().replace('-', '_');
		return "CCA".equals(normalizedRole)
				|| "CCA_OFFICER".equals(normalizedRole)
				|| "CCAOFFICER".equals(normalizedRole);
	}

	private String resolveCurrentStep(Long auditScheduleId) {
		LicenseeAuditEntity activeAudit =
				applicationAuditService.findActiveByAuditScheduleId(auditScheduleId);
		if (activeAudit == null) {
			return STEP_AGENCY_SELECTION;
		}

		AuditAgencySelectionEntity selection =
				auditAgencySelectionService.findbyAuditId(activeAudit);
		if (selection == null || !"Active".equalsIgnoreCase(selection.getStatus())) {
			return STEP_AGENCY_SELECTION;
		}

		List<ApplicationAuditorsEntity> auditors =
				applicationAuditorsRepository.getAllDataByAuditId(activeAudit.getLicenseeAuditId());
		if (auditors == null || auditors.isEmpty()) {
			return STEP_AUDIT_FORM;
		}

		List<AuditScheduleEntity> scopeSchedules =
				auditScheduleRepository.getAllDataByAuditId(activeAudit.getLicenseeAuditId());
		if (scopeSchedules == null || scopeSchedules.isEmpty()) {
			return STEP_SCOPE_SCHEDULE;
		}

		AuditReportDocumentEntity report =
				auditReportDocumentRepository.getByAuditId(activeAudit);
		if (report == null) {
			return STEP_AUDIT_REPORT;
		}

		AuditNCSEntity ncs = auditNCSRepository.findByAuditId(activeAudit.getLicenseeAuditId());
		if (ncs == null) {
			return STEP_AUDIT_REPORT;
		}

		return STEP_NC_ACTION;
	}

	private List<String> resolveAllowedActions(String role, String currentStep, Long auditScheduleId) {
		List<String> actions = new ArrayList<>();
		String normalizedRole = role == null ? "LICENSEE" : role.toUpperCase();

		switch (normalizedRole) {
		case "LICENSEE":
			addLicenseeActions(actions, currentStep, auditScheduleId);
			break;
		case "AUDIT_AGENCY":
		case "AUDITAGENCY":
			addAuditAgencyActions(actions, currentStep);
			break;
		case "CCA":
		case "CCA_OFFICER":
		case "CCAOFFICER":
			addCcaActions(actions, currentStep, auditScheduleId);
			break;
		case "REVIEW_COMMITTEE":
		case "REVIEWCOMMITTEE":
			addReviewCommitteeActions(actions, currentStep);
			break;
		default:
			addLicenseeActions(actions, currentStep, auditScheduleId);
			break;
		}

		if (actions.isEmpty() && !STEP_COMPLETED.equals(currentStep)) {
			actions.add("VIEW");
		}
		return actions;
	}

	private void addLicenseeActions(List<String> actions, String currentStep, Long auditScheduleId) {
		switch (currentStep) {
		case STEP_AGENCY_SELECTION:
			if (hasActiveAgencySelection(auditScheduleId)) {
				actions.add("UPDATE_AGENCY");
			} else {
				actions.add("SELECT_AGENCY");
			}
			break;
		case STEP_AUDIT_FORM:
			actions.add("UPDATE_AGENCY");
			actions.add("VIEW");
			break;
		case STEP_SCOPE_SCHEDULE:
		case STEP_AUDIT_REPORT:
		case STEP_NC_ACTION:
		case STEP_NC_VERIFICATION:
			actions.add("VIEW");
			break;
		default:
			actions.add("VIEW");
			break;
		}
	}

	private void addAuditAgencyActions(List<String> actions, String currentStep) {
		switch (currentStep) {
		case STEP_AUDIT_FORM:
		case STEP_SCOPE_SCHEDULE:
			actions.add("SUBMIT_UNDERTAKING");
			break;
		case STEP_AUDIT_REPORT:
			actions.add("SUBMIT_REPORT");
			break;
		case STEP_NC_VERIFICATION:
			actions.add("VERIFY_NC");
			break;
		default:
			if (!STEP_AGENCY_SELECTION.equals(currentStep) && !STATE_NOT_STARTED.equals(currentStep)) {
				actions.add("VIEW");
			}
			break;
		}
	}

	private void addCcaActions(List<String> actions, String currentStep, Long auditScheduleId) {
		String reviewStatus = resolveCcaUndertakingReviewStatus(auditScheduleId);
		if (CCA_REVIEW_APPROVED.equals(reviewStatus) || CCA_REVIEW_REJECTED.equals(reviewStatus)) {
			actions.add("VIEW");
			return;
		}
		if (STEP_SCOPE_SCHEDULE.equals(currentStep) && CCA_REVIEW_PENDING.equals(reviewStatus)) {
			actions.add("APPROVE_SCOPE");
			return;
		}
		if (CCA_REVIEW_PENDING.equals(reviewStatus)) {
			actions.add("APPROVE_SCOPE");
			return;
		}
		actions.add("VIEW");
	}

	@Override
	public String resolveCcaUndertakingReviewStatus(Long auditScheduleId) {
		if (auditScheduleId == null) {
			return CCA_REVIEW_NOT_SUBMITTED;
		}
		LicenseeAuditEntity activeAudit =
				applicationAuditService.findActiveByAuditScheduleId(auditScheduleId);
		if (activeAudit == null) {
			return CCA_REVIEW_NOT_SUBMITTED;
		}

		List<ApplicationAuditorsEntity> allAuditors =
				applicationAuditorsRepository.findAllByLicenseeAuditId(activeAudit.getLicenseeAuditId());
		if (allAuditors == null || allAuditors.isEmpty()) {
			return CCA_REVIEW_NOT_SUBMITTED;
		}

		List<ApplicationAuditorsEntity> activeAuditors =
				applicationAuditorsRepository.getAllDataByAuditId(activeAudit.getLicenseeAuditId());
		if (activeAuditors == null || activeAuditors.isEmpty()) {
			return CCA_REVIEW_REJECTED;
		}

		boolean approved = activeAuditors.stream()
				.anyMatch(auditor -> auditor.getRemarks() != null && !auditor.getRemarks().isBlank());
		return approved ? CCA_REVIEW_APPROVED : CCA_REVIEW_PENDING;
	}

	private void addReviewCommitteeActions(List<String> actions, String currentStep) {
		if (STEP_NC_ACTION.equals(currentStep) || STEP_NC_VERIFICATION.equals(currentStep)) {
			actions.add("REVIEW");
		} else {
			actions.add("VIEW");
		}
	}

	private boolean hasActiveAgencySelection(Long auditScheduleId) {
		LicenseeAuditEntity activeAudit =
				applicationAuditService.findActiveByAuditScheduleId(auditScheduleId);
		if (activeAudit == null) {
			return false;
		}
		AuditAgencySelectionEntity selection =
				auditAgencySelectionService.findbyAuditId(activeAudit);
		return selection != null && "Active".equalsIgnoreCase(selection.getStatus());
	}

	private boolean isScheduleCompleted(AnnualAuditScheduleEntity schedule) {
		if (schedule.getDateOfCompletion() != null) {
			return true;
		}
		String status = schedule.getStatus();
		return status != null
				&& (status.equalsIgnoreCase("Completed")
						|| status.equalsIgnoreCase("Closed")
						|| status.equalsIgnoreCase("Complete"));
	}

	private Date stripTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
