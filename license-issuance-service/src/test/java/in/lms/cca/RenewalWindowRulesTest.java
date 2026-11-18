package in.lms.cca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import in.lms.cca.util.golbal.RenewalWindowRules;
import in.lms.cca.util.golbal.RenewalWindowRules.Decision;

class RenewalWindowRulesTest {

	private final LocalDate today = LocalDate.of(2026, 10, 30);

	@Test
	void moreThan45DaysLeftIsANormalApplication() {
		Decision decision = RenewalWindowRules.classify(today.plusDays(46), today, null);
		assertEquals(RenewalWindowRules.NORMAL, decision.getWindow());
		assertTrue(decision.isCanApply());
		assertFalse(decision.isDelayDocumentRequired());
		assertEquals(46, decision.getDaysLeft());
	}

	@Test
	void last45DaysRequireADelayDocument() {
		Decision at45 = RenewalWindowRules.classify(today.plusDays(45), today, null);
		Decision at1 = RenewalWindowRules.classify(today.plusDays(1), today, null);
		assertEquals(RenewalWindowRules.LATE, at45.getWindow());
		assertTrue(at45.isDelayDocumentRequired());
		assertEquals(RenewalWindowRules.LATE, at1.getWindow());
		assertTrue(at1.isCanApply());
	}

	@Test
	void expiryDayAndAfterAreClosedWithoutAnExtension() {
		Decision expiryDay = RenewalWindowRules.classify(today, today, null);
		Decision expired = RenewalWindowRules.classify(today.minusDays(3), today, null);
		assertEquals(RenewalWindowRules.BLOCKED, expiryDay.getWindow());
		assertFalse(expiryDay.isCanApply());
		assertEquals(0, expiryDay.getDaysLeft());
		assertEquals(RenewalWindowRules.BLOCKED, expired.getWindow());
		assertEquals(-3, expired.getDaysLeft());
	}

	@Test
	void activeExtensionReopensAClosedWindow() {
		Decision open = RenewalWindowRules.classify(today.minusDays(1), today, today.plusDays(45));
		Decision ended = RenewalWindowRules.classify(today.minusDays(1), today, today.minusDays(1));
		assertEquals(RenewalWindowRules.EXTENDED, open.getWindow());
		assertTrue(open.isCanApply());
		assertTrue(open.isDelayDocumentRequired());
		assertEquals(RenewalWindowRules.BLOCKED, ended.getWindow());
	}

	@Test
	void extensionStaysOpenThroughTheExtensionDateAndClosesAtTheNextDay() {
		Decision onExtensionDate = RenewalWindowRules.classify(today.minusDays(10), today, today);
		Decision afterExtensionDate = RenewalWindowRules.classify(today.minusDays(10), today.plusDays(1), today);
		assertEquals(RenewalWindowRules.EXTENDED, onExtensionDate.getWindow());
		assertTrue(onExtensionDate.isCanApply());
		assertEquals(RenewalWindowRules.BLOCKED, afterExtensionDate.getWindow());
		assertFalse(afterExtensionDate.isCanApply());
	}

	@Test
	void extensionDoesNotReplaceAnOpenLicenseWindow() {
		Decision decision = RenewalWindowRules.classify(today.plusDays(10), today, today.plusDays(45));
		assertEquals(RenewalWindowRules.LATE, decision.getWindow());
	}

	@Test
	void missingExpiryCannotApply() {
		Decision decision = RenewalWindowRules.classify(null, today, null);
		assertEquals(RenewalWindowRules.NO_LICENSE, decision.getWindow());
		assertFalse(decision.isCanApply());
	}
}
