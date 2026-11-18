package in.lms.cca.certificate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.LicenseDetails;
import in.lms.cca.util.golbal.EncryptionUtil;

@Service
public class LicenseCertificateDataService {

	private static final List<String> LOCATION_ORDER = List.of(
			"organizationaddress",
			"firmaddress",
			"official",
			"residential",
			"communicationaddress",
			"communication");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public LicenseCertificateContent load(LicenseDetails license) {
		LicenseCertificateContent content = new LicenseCertificateContent();
		content.setIssueDate(license.getIssueDate());
		content.setPublicKey(clean(license.getPublicKey()));

		Long intentId = resolveIntentId(license);
		if (intentId != null) {
			fillOrganisation(content, intentId);
			if (content.getLocation() == null || content.getLocation().isBlank()) {
				content.setLocation(loadLocation(intentId));
			}
		}

		if (content.getAuthorityName() == null || content.getAuthorityName().isBlank()) {
			String licenseName = clean(license.getLicenseName());
			String username = clean(license.getUserName());
			if (!licenseName.isBlank() && !licenseName.equalsIgnoreCase(username)) {
				content.setAuthorityName(licenseName);
			} else if (!username.isBlank()) {
				content.setAuthorityName(username);
			}
		}
		if (content.getLocation() == null) {
			content.setLocation("");
		}
		if (content.getPublicKey() == null) {
			content.setPublicKey("");
		}
		return content;
	}

	private void fillOrganisation(LicenseCertificateContent content, Long intentId) {
		List<Map<String, Object>> govt = query(
				"SELECT org_name, public_key FROM new_license_management.govt_organization_application "
						+ "WHERE intent_app_id = ? ORDER BY org_application_id DESC LIMIT 1",
				intentId);
		if (!govt.isEmpty()) {
			content.setAuthorityName(value(govt.get(0), "org_name"));
			if (content.getPublicKey().isBlank()) {
				content.setPublicKey(value(govt.get(0), "public_key"));
			}
		}

		if (content.getAuthorityName() == null || content.getAuthorityName().isBlank()) {
			List<Map<String, Object>> firm = query(
					"SELECT office_name FROM new_license_management.firm_application "
							+ "WHERE intent_app_id = ? ORDER BY firm_application_id DESC LIMIT 1",
					intentId);
			if (!firm.isEmpty()) {
				content.setAuthorityName(value(firm.get(0), "office_name"));
			}
		}

		if (content.getAuthorityName() == null || content.getAuthorityName().isBlank()) {
			List<Map<String, Object>> individual = query(
					"SELECT first_name1, middle_name1, last_name1 FROM new_license_management.indiv_application "
							+ "WHERE intent_app_id = ? ORDER BY indiv_application_id DESC LIMIT 1",
					intentId);
			if (!individual.isEmpty()) {
				Map<String, Object> row = individual.get(0);
				content.setAuthorityName(joinName(value(row, "first_name1"), value(row, "middle_name1"),
						value(row, "last_name1")));
			}
		}
	}

	private String loadLocation(Long intentId) {
		List<Map<String, Object>> locations = query(
				"SELECT location_name, address_id FROM new_license_management.app_location "
						+ "WHERE intent_app_id = ? ORDER BY location_id DESC",
				intentId);
		locations.sort(Comparator.comparingInt(row -> locationRank(value(row, "location_name"))));
		for (Map<String, Object> location : locations) {
			Long addressId = parseAddressId(value(location, "address_id"));
			if (addressId == null) {
				continue;
			}
			String formatted = formatAddress(addressId);
			if (!formatted.isBlank()) {
				return formatted;
			}
		}
		return "";
	}

	private String formatAddress(Long addressId) {
		List<Map<String, Object>> rows = query(
				"SELECT a.blockno, a.village, a.post_office, a.sub_division, a.pin_code, "
						+ "c.city_name, s.state_name, co.country_name "
						+ "FROM admin_user_management.address a "
						+ "LEFT JOIN admin_user_management.city c ON c.city_id = a.city_id "
						+ "LEFT JOIN admin_user_management.state s ON s.state_id = a.state_id "
						+ "LEFT JOIN admin_user_management.country co ON co.country_id = a.country_id "
						+ "WHERE a.address_id = ?",
				addressId);
		if (rows.isEmpty()) {
			return "";
		}
		Map<String, Object> row = rows.get(0);
		return joinParts(value(row, "blockno"), value(row, "village"), value(row, "post_office"),
				value(row, "sub_division"), value(row, "city_name"), value(row, "state_name"),
				value(row, "pin_code"), value(row, "country_name"));
	}

	private Long resolveIntentId(LicenseDetails license) {
		Long fromLicense = parseLong(decrypt(license.getIntentAppId()));
		if (fromLicense != null) {
			return fromLicense;
		}
		String username = clean(license.getUserName());
		if (username.isBlank()) {
			return null;
		}
		List<Map<String, Object>> rows = query(
				"SELECT intent_app_id FROM new_license_management.intent_application "
						+ "WHERE user_name = ? ORDER BY intent_app_id DESC LIMIT 1",
				username);
		if (rows.isEmpty()) {
			return null;
		}
		return parseLong(value(rows.get(0), "intent_app_id"));
	}

	private List<Map<String, Object>> query(String sql, Object... args) {
		try {
			return jdbcTemplate.queryForList(sql, args);
		} catch (Exception ex) {
			System.err.println("License certificate lookup skipped: " + ex.getMessage());
			return List.of();
		}
	}

	private static int locationRank(String name) {
		String key = name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
		int index = LOCATION_ORDER.indexOf(key);
		return index < 0 ? LOCATION_ORDER.size() : index;
	}

	private static Long parseAddressId(String raw) {
		if (raw == null || raw.isBlank()) {
			return null;
		}
		String decrypted = EncryptionUtil.decrypt(raw.trim());
		return parseLong(decrypted != null ? decrypted : raw);
	}

	private static Long parseLong(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return Long.parseLong(value.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private static String decrypt(String value) {
		if (value == null || value.isBlank()) {
			return "";
		}
		String decrypted = EncryptionUtil.decrypt(value.trim());
		return decrypted != null ? decrypted.trim() : value.trim();
	}

	private static String value(Map<String, Object> row, String key) {
		Object found = row.get(key);
		if (found == null) {
			for (Map.Entry<String, Object> entry : row.entrySet()) {
				if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(key)) {
					found = entry.getValue();
					break;
				}
			}
		}
		return found == null ? "" : found.toString().trim();
	}

	private static String joinName(String first, String middle, String last) {
		return joinParts(first, middle, last).replace(", ", " ");
	}

	private static String joinParts(String... parts) {
		List<String> kept = new ArrayList<>();
		for (String part : parts) {
			if (part != null && !part.isBlank()) {
				kept.add(part.trim());
			}
		}
		return String.join(", ", kept);
	}

	private static String clean(String value) {
		return value == null ? "" : value.trim();
	}
}
