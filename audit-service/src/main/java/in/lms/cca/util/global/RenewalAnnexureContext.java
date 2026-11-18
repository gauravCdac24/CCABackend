package in.lms.cca.util.global;

public final class RenewalAnnexureContext {

	private static final ThreadLocal<Long> INTENT_APP_ID = new ThreadLocal<>();

	private RenewalAnnexureContext() {
	}

	public static void setIntentAppId(Long intentAppId) {
		if (intentAppId == null) {
			INTENT_APP_ID.remove();
		} else {
			INTENT_APP_ID.set(intentAppId);
		}
	}

	public static Long getIntentAppId() {
		return INTENT_APP_ID.get();
	}

	public static void clear() {
		INTENT_APP_ID.remove();
	}
}
