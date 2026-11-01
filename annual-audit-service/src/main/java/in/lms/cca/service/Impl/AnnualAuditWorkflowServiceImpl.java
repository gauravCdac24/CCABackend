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
import in.lms.cca.entity.ApplicantActionTakenEntity;
import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.AuditNCSEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.AuditScheduleEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.repository.ApplicationAuditorsRepository;
import in.lms.cca.repository.AuditNCSRepository;
import in.lms.cca.repository.AuditReportDocumentRepository;
import in.lms.cca.repository.AuditScheduleRepository;
import in.lms.cca.service.AnnualAuditWorkflowService;
import in.lms.cca.service.ApplicationActionTakenService;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.AuditAgencySelectionService;
import in.lms.cca.service.AuditReportCriteriaService;
import in.lms.cca.service.LicenseeAuditservice;
import in.lms.cca.util.global.Constant;

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

	@Autowired
	private ApplicationActionTakenService actionTakenService;

	@Autowired
	private AuditReportCriteriaService auditReportCriteriaService;

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
			syncScheduleStatus(schedule, Constant.STATUS_COMPLETED);
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

		// Licensee works one active schedule at a time. Audit agencies (and later
		// reviewers) must still open schedules already assigned to them even when
		// the licensee has another concurrent schedule window.
		if (!isAuditAgencyRole(role) && !isCcaRole(role) && !isReviewCommitteeRole(role)) {
			Long activeScheduleId = resolveActiveScheduleId(schedule.getUserName());
			if (activeScheduleId == null || !activeScheduleId.equals(schedule.getAuditScheduleId())) {
				workflow.setScheduleState(STATE_NOT_STARTED);
				workflow.setCurrentStep(STATE_NOT_STARTED);
				workflow.setReadOnly(true);
				workflow.setAllowedActions(List.of());
				return workflow;
			}
		}

		workflow.setScheduleState(STATE_ACTIVE);
		String ccaReviewStatus = resolveCcaUndertakingReviewStatus(schedule.getAuditScheduleId());
		String currentStep = resolveCurrentStep(schedule.getAuditScheduleId(), ccaReviewStatus);
		workflow.setCurrentStep(currentStep);
		boolean reviewCompleted = CCA_REVIEW_APPROVED.equals(ccaReviewStatus)
				|| CCA_REVIEW_REJECTED.equals(ccaReviewStatus);
		workflow.setReadOnly(reviewCompleted && isCcaRole(role));
		workflow.setAllowedActions(
				resolveAllowedActions(role, currentStep, schedule.getAuditScheduleId(), ccaReviewStatus));
		syncScheduleStatus(schedule, resolveDisplayStatus(currentStep, ccaReviewStatus));
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

	private boolean isAuditAgencyRole(String role) {
		if (role == null) {
			return false;
		}
		String normalizedRole = role.toUpperCase().replace('-', '_');
		return "AUDIT_AGENCY".equals(normalizedRole) || "AUDITAGENCY".equals(normalizedRole);
	}

	private boolean isReviewCommitteeRole(String role) {
		if (role == null) {
			return false;
		}
		String normalizedRole = role.toUpperCase().replace('-', '_');
		return "REVIEW_COMMITTEE".equals(normalizedRole) || "REVIEWCOMMITTEE".equals(normalizedRole);
	}

	private String resolveCurrentStep(Long auditScheduleId, String ccaReviewStatus) {
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

		// Mirror fresh audit: agency fills NC only after CCA approves undertaking.
		if (!CCA_REVIEW_APPROVED.equals(ccaReviewStatus)) {
			return STEP_SCOPE_SCHEDULE;
		}

		OptionalScheduleStatus scheduleStatus = readScheduleStatus(auditScheduleId);
		if (scheduleStatus.isNcActionTakenOrReview()) {
			return STEP_NC_VERIFICATION;
		}

		AuditNCSEntity ncs = auditNCSRepository.findByAuditId(activeAudit.getLicenseeAuditId());
		AuditReportDocumentEntity report =
				auditReportDocumentRepository.getByAuditId(activeAudit);

		// Applicant responded via per-control action_taken (fresh parity) or legacy audit_ncs.
		if (hasApplicantNcActionTaken(activeAudit) || ncs != null) {
			return STEP_NC_VERIFICATION;
		}

		// NC already issued to licensee — agency waits / views until applicant responds.
		if (scheduleStatus.isNcIssued() || hasAuditorNcCriteria(activeAudit)) {
			return STEP_NC_ACTION;
		}

		// CCA approved, no NC fill yet → agency conducts audit / fills NCs.
		if (report == null) {
			return STEP_AUDIT_REPORT;
		}

		return STEP_NC_ACTION;
	}

	private OptionalScheduleStatus readScheduleStatus(Long auditScheduleId) {
		if (auditScheduleId == null) {
			return OptionalScheduleStatus.empty();
		}
		return licenseeAuditservice.getAnnualAuditScheduleById(auditScheduleId)
				.map(OptionalScheduleStatus::from)
				.orElseGet(OptionalScheduleStatus::empty);
	}

	private boolean hasApplicantNcActionTaken(LicenseeAuditEntity activeAudit) {
		if (activeAudit == null) {
			return false;
		}
		String userName = activeAudit.getApplicantUserName();
		if (userName != null && !userName.isBlank()) {
			List<ApplicantActionTakenEntity> byUser =
					actionTakenService.getAllApplicationTakenAnyStatus(userName.trim());
			if (byUser != null && !byUser.isEmpty()) {
				return true;
			}
		}
		List<AuditReportCriteriaEntity> criteria =
				auditReportCriteriaService.findByAuditId(activeAudit.getLicenseeAuditId());
		if (criteria == null || criteria.isEmpty()) {
			return false;
		}
		for (AuditReportCriteriaEntity row : criteria) {
			if (row == null) {
				continue;
			}
			ApplicantActionTakenEntity action = actionTakenService.getAllByReportCriteria(row);
			if (action != null) {
				return true;
			}
		}
		return false;
	}

	private boolean hasAuditorNcCriteria(LicenseeAuditEntity activeAudit) {
		if (activeAudit == null || activeAudit.getLicenseeAuditId() == null) {
			return false;
		}
		List<AuditReportCriteriaEntity> criteria =
				auditReportCriteriaService.findByAuditId(activeAudit.getLicenseeAuditId());
		if (criteria == null || criteria.isEmpty()) {
			return false;
		}
		for (AuditReportCriteriaEntity row : criteria) {
			if (row == null) {
				continue;
			}
			boolean hasPayload = (row.getCompliance() != null && !row.getCompliance().isBlank())
					|| (row.getDocument() != null && !row.getDocument().isBlank())
					|| (row.getRemarks() != null && !row.getRemarks().isBlank());
			if (hasPayload) {
				return true;
			}
		}
		return false;
	}

	private static final class OptionalScheduleStatus {
		private final String status;

		private OptionalScheduleStatus(String status) {
			this.status = status;
		}

		static OptionalScheduleStatus empty() {
			return new OptionalScheduleStatus(null);
		}

		static OptionalScheduleStatus from(AnnualAuditScheduleEntity schedule) {
			return new OptionalScheduleStatus(schedule == null ? null : schedule.getStatus());
		}

		boolean isNcActionTakenOrReview() {
			return Constant.STATUS_NC_ACTION_TAKEN.equalsIgnoreCase(status)
					|| Constant.STATUS_REVIEW_NC_REPORT.equalsIgnoreCase(status);
		}

		boolean isNcIssued() {
			return Constant.STATUS_NC_ISSUED.equalsIgnoreCase(status);
		}
	}

	private String resolveDisplayStatus(String currentStep, String ccaReviewStatus) {
		switch (currentStep) {
		case STEP_AGENCY_SELECTION:
			return Constant.STATUS_AGENCY_SELECTION;
		case STEP_AUDIT_FORM:
		case STEP_SCOPE_SCHEDULE:
			if (CCA_REVIEW_PENDING.equals(ccaReviewStatus)) {
				return Constant.STATUS_RECOMMENDED_FOR_AUDIT_AGENCY;
			}
			if (CCA_REVIEW_REJECTED.equals(ccaReviewStatus)) {
				return Constant.STATUS_AGENCY_SELECTION;
			}
			return Constant.STATUS_AGENCY_SELECTION;
		case STEP_AUDIT_REPORT:
		case STEP_NC_ACTION:
			return Constant.STATUS_APPROVE_FROM_CCA;
		case STEP_NC_VERIFICATION:
		case STEP_REVIEW:
			return Constant.STATUS_NC_ACTION_TAKEN;
		case STEP_COMPLETED:
			return Constant.STATUS_COMPLETED;
		default:
			return Constant.STATUS_INITIATED;
		}
	}

	private void syncScheduleStatus(AnnualAuditScheduleEntity schedule, String targetStatus) {
		if (schedule == null || schedule.getAuditScheduleId() == null
				|| targetStatus == null || targetStatus.isBlank()) {
			return;
		}
		String currentStatus = schedule.getStatus();
		if (targetStatus.equals(currentStatus)) {
			return;
		}
		// Never downgrade an explicitly advanced status (e.g. NC Issued → Approve From CCA).
		if (statusRank(currentStatus) > statusRank(targetStatus)) {
			return;
		}
		licenseeAuditservice.updateScheduleStatus(schedule.getAuditScheduleId(), targetStatus);
		schedule.setStatus(targetStatus);
	}

	private int statusRank(String status) {
		if (status == null || status.isBlank()) {
			return 0;
		}
		if (Constant.STATUS_INITIATED.equalsIgnoreCase(status)) {
			return 1;
		}
		if (Constant.STATUS_AGENCY_SELECTION.equalsIgnoreCase(status)) {
			return 2;
		}
		if (Constant.STATUS_RECOMMENDED_FOR_AUDIT_AGENCY.equalsIgnoreCase(status)) {
			return 3;
		}
		if (Constant.STATUS_APPROVE_FROM_CCA.equalsIgnoreCase(status)) {
			return 4;
		}
		if (Constant.STATUS_NC_ISSUED.equalsIgnoreCase(status)) {
			return 5;
		}
		if (Constant.STATUS_NC_ACTION_TAKEN.equalsIgnoreCase(status)
				|| Constant.STATUS_REVIEW_NC_REPORT.equalsIgnoreCase(status)) {
			return 6;
		}
		if (Constant.STATUS_COMPLETED.equalsIgnoreCase(status)
				|| "Closed".equalsIgnoreCase(status)
				|| "Complete".equalsIgnoreCase(status)) {
			return 7;
		}
		return 0;
	}

	private List<String> resolveAllowedActions(
			String role, String currentStep, Long auditScheduleId, String ccaReviewStatus) {
		List<String> actions = new ArrayList<>();
		String normalizedRole = role == null ? "LICENSEE" : role.toUpperCase();

		switch (normalizedRole) {
		case "LICENSEE":
			addLicenseeActions(actions, currentStep, auditScheduleId);
			break;
		case "AUDIT_AGENCY":
		case "AUDITAGENCY":
			addAuditAgencyActions(actions, currentStep, ccaReviewStatus);
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

	private void addAuditAgencyActions(List<String> actions, String currentStep, String ccaReviewStatus) {
		switch (currentStep) {
		case STEP_AUDIT_FORM:
			actions.add("SUBMIT_UNDERTAKING");
			break;
		case STEP_SCOPE_SCHEDULE:
			// Undertaking already submitted; wait for CCA unless rejected.
			if (CCA_REVIEW_REJECTED.equals(ccaReviewStatus) || CCA_REVIEW_NOT_SUBMITTED.equals(ccaReviewStatus)) {
				actions.add("SUBMIT_UNDERTAKING");
			} else {
				actions.add("VIEW");
			}
			break;
		case STEP_AUDIT_REPORT:
			// Fresh parity: after CCA approve → Fill NC / Conduct audit.
			actions.add("FILL_NC");
			break;
		case STEP_NC_ACTION:
			// NC issued / waiting on licensee response — keep visible as view-only here.
			// Fill NC remains on the Conduct Audit list while still filling.
			actions.add("VIEW");
			break;
		case STEP_NC_VERIFICATION:
			// Fresh parity: after licensee NC action taken → Review / verify NC report.
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
		if (STEP_NC_ACTION.equals(currentStep) || STEP_NC_VERIFICATION.equals(currentStep)
				|| STEP_REVIEW.equals(currentStep)) {
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
