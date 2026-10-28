package in.lms.cca.util.global;

import in.lms.cca.entity.City;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.State;

public final class LocationMapping {

	private LocationMapping() {
	}

	public static Long parseId(String raw) {
		if (raw == null || raw.trim().isEmpty()) {
			throw new IllegalArgumentException("Location id is missing.");
		}
		String decrypted = EncryptionUtil.decrypt(raw.trim());
		String id = decrypted != null ? decrypted.trim() : raw.trim();
		return Long.valueOf(id);
	}

	public static void assertMapped(Country country, State state, City city) {
		if (country == null) {
			throw new IllegalArgumentException("Invalid country.");
		}
		if (state == null || state.getCountryId() == null
				|| !country.getCountryId().equals(state.getCountryId().getCountryId())) {
			throw new IllegalArgumentException("Selected state does not belong to the selected country.");
		}
		if (city == null || city.getStateId() == null
				|| !state.getStateId().equals(city.getStateId().getStateId())) {
			throw new IllegalArgumentException("Selected city does not belong to the selected state.");
		}
	}
}
