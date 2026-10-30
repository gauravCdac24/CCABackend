package in.lms.cca.util.golbal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Renewal application window for an issued CA license.
 * More than 45 days left: apply with no extra document.
 * 45 days left down to 1 day left: apply with a delay document.
 * Expiry day and after: online apply is closed unless CCA has an extension still in force.
 * An extension stays open through the extension date and closes at 24:00, the start of the next day.
 */
public final class RenewalWindowRules {

	public static final int LATE_THRESHOLD_DAYS = 45;
	public static final int EXTENSION_LENGTH_DAYS = 45;

	public static final String NORMAL = "NORMAL";
	public static final String LATE = "LATE";
	public static final String BLOCKED = "BLOCKED";
	public static final String EXTENDED = "EXTENDED";
	public static final String NO_LICENSE = "NO_LICENSE";

	private RenewalWindowRules() {
	}

	public static Decision classify(LocalDate expiryDate, LocalDate today, LocalDate extensionUntil) {
		if (expiryDate == null || today == null) {
			return new Decision(NO_LICENSE, false, false, 0);
		}
		long daysLeft = ChronoUnit.DAYS.between(today, expiryDate);
		if (daysLeft > LATE_THRESHOLD_DAYS) {
			return new Decision(NORMAL, true, false, daysLeft);
		}
		if (daysLeft >= 1) {
			return new Decision(LATE, true, true, daysLeft);
		}
		if (extensionUntil != null && !today.isAfter(extensionUntil)) {
			return new Decision(EXTENDED, true, true, daysLeft);
		}
		return new Decision(BLOCKED, false, false, daysLeft);
	}

	public static final class Decision {
		private final String window;
		private final boolean canApply;
		private final boolean delayDocumentRequired;
		private final long daysLeft;

		public Decision(String window, boolean canApply, boolean delayDocumentRequired, long daysLeft) {
			this.window = window;
			this.canApply = canApply;
			this.delayDocumentRequired = delayDocumentRequired;
			this.daysLeft = daysLeft;
		}

		public String getWindow() {
			return window;
		}

		public boolean isCanApply() {
			return canApply;
		}

		public boolean isDelayDocumentRequired() {
			return delayDocumentRequired;
		}

		public long getDaysLeft() {
			return daysLeft;
		}
	}
}
