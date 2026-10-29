package in.lms.cca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import in.lms.cca.util.golbal.LicenseSerialNumber;

class LicenseSerialNumberTest {

	@Test
	void buildsTwentyFourDigitSerialForAFreshLicence() {
		String serial = LicenseSerialNumber.build(
				LocalDate.of(2026, 10, 29),
				LocalDate.of(2031, 10, 29),
				25,
				0);

		assertEquals(24, serial.length());
		assertEquals("202600252910310000000000", serial);
		assertEquals(25, LicenseSerialNumber.caSerial(serial));
		assertEquals(0, LicenseSerialNumber.nnn(serial));
	}

	@Test
	void freshAllotmentStartsAt0025() {
		assertEquals(25, LicenseSerialNumber.nextCaSerial(0));
		assertEquals(25, LicenseSerialNumber.nextCaSerial(10));
		assertEquals(31, LicenseSerialNumber.nextCaSerial(30));
	}

	@Test
	void renewalIncrementsNnnByRootCaKeys() {
		assertEquals(2, LicenseSerialNumber.renewalNnn(0, 2));
		assertEquals(4, LicenseSerialNumber.renewalNnn(2, 2));
		assertEquals(1, LicenseSerialNumber.renewalNnn(0, 0));
	}

	@Test
	void ignoresSerialsThatAreNot24Digits() {
		assertNull(LicenseSerialNumber.caSerial("ABC"));
		assertNull(LicenseSerialNumber.nnn(null));
	}
}
