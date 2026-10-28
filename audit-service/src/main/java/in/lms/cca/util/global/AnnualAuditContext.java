package in.lms.cca.util.global;

public final class AnnualAuditContext {

	private static final ThreadLocal<Long> AUDIT_SCHEDULE_ID = new ThreadLocal<>();

	private AnnualAuditContext() {
	}

	public static void setAuditScheduleId(Long auditScheduleId) {
		if (auditScheduleId == null) {
			AUDIT_SCHEDULE_ID.remove();
		} else {
			AUDIT_SCHEDULE_ID.set(auditScheduleId);
		}
	}

	public static Long getAuditScheduleId() {
		return AUDIT_SCHEDULE_ID.get();
	}

	public static void clear() {
		AUDIT_SCHEDULE_ID.remove();
	}
}
