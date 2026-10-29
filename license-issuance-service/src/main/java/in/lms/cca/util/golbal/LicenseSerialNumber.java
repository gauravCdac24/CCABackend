package in.lms.cca.util.golbal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * License serial: YYYYXXXXDDMMYYNNNMMMMZZZ (24 digits).
 * YYYY year of issue, XXXX CA serial (new allotments start at 0025),
 * DDMMYY valid-until date, NNN 000 for a fresh licence and incremented on renewal
 * by the number of root CA keys, MMMM 0000, ZZZ 000.
 */
public final class LicenseSerialNumber {

	public static final int LENGTH = 24;
	public static final int FIRST_CA_SERIAL = 25;

	private static final DateTimeFormatter YEAR = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter VALID_UNTIL = DateTimeFormatter.ofPattern("ddMMyy");

	private LicenseSerialNumber() {
	}

	public static String build(LocalDate issueDate, LocalDate expiryDate, int caSerial, int nnn) {
		if (issueDate == null || expiryDate == null) {
			throw new IllegalArgumentException("Issue date and expiry date are required to build the license serial number.");
		}
		if (caSerial < 0 || caSerial > 9999) {
			throw new IllegalArgumentException("CA serial must be 4 digits.");
		}
		if (nnn < 0 || nnn > 999) {
			throw new IllegalArgumentException("License key sequence cannot exceed 999.");
		}
		return issueDate.format(YEAR)
				+ String.format("%04d", caSerial)
				+ expiryDate.format(VALID_UNTIL)
				+ String.format("%03d", nnn)
				+ "0000"
				+ "000";
	}

	public static boolean isNumeric24(String serial) {
		return serial != null && serial.length() == LENGTH && serial.chars().allMatch(Character::isDigit);
	}

	public static Integer caSerial(String serial) {
		if (!isNumeric24(serial)) {
			return null;
		}
		return Integer.parseInt(serial.substring(4, 8));
	}

	public static Integer nnn(String serial) {
		if (!isNumeric24(serial)) {
			return null;
		}
		return Integer.parseInt(serial.substring(14, 17));
	}

	/** Next XXXX for a fresh licence. Existing numbers below 0025 do not pull the sequence backwards. */
	public static int nextCaSerial(int highestExisting) {
		int base = Math.max(highestExisting, FIRST_CA_SERIAL - 1);
		return base + 1;
	}

	/** Renewal NNN is the previous NNN plus the number of root CA keys, and always moves forward. */
	public static int renewalNnn(int previousNnn, int rootCaKeyCount) {
		int increment = Math.max(rootCaKeyCount, 1);
		int next = previousNnn + increment;
		if (next > 999) {
			throw new IllegalArgumentException("License key sequence cannot exceed 999.");
		}
		return next;
	}
}
