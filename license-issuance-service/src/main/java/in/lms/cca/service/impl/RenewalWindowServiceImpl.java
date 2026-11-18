package in.lms.cca.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import in.lms.cca.certificate.LicenseCertificateContent;
import in.lms.cca.certificate.LicenseCertificateDataService;
import in.lms.cca.dto.GrantRenewalExtensionRequest;
import in.lms.cca.dto.RenewalExtensionCandidateDTO;
import in.lms.cca.dto.RenewalWindowDTO;
import in.lms.cca.dto.RenewalWindowExtensionDTO;
import in.lms.cca.entity.LicenseDetails;
import in.lms.cca.entity.RenewalWindowExtension;
import in.lms.cca.repository.RenewalWindowExtensionRepository;
import in.lms.cca.service.ILicenseDetailsService;
import in.lms.cca.util.golbal.EncryptionUtil;
import in.lms.cca.util.golbal.RenewalWindowRules;
import in.lms.cca.util.golbal.RenewalWindowRules.Decision;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RenewalWindowServiceImpl {

	private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
	private static final DateTimeFormatter EXTENSION_DATE = DateTimeFormatter.ofPattern("dd-MM-uuuu")
			.withResolverStyle(ResolverStyle.STRICT);
	private static final String ACTIVE = "Active";
	private static final String EXPIRED = "Expired";

	@Autowired
	private ILicenseDetailsService licenseDetailsServ;

	@Autowired
	private RenewalWindowExtensionRepository extensionRepo;

	@Autowired
	private LicenseCertificateDataService certificateDataService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public RenewalWindowDTO windowFor(String username) {
		String plain = plainUsername(username);
		LicenseDetails license = latestLicense(plain);
		LocalDate today = LocalDate.now();
		RenewalWindowExtension extension = plain == null ? null : activeExtension(plain, today);
		return toWindow(plain, license, extension, today);
	}

	public Date legacyStartDate(String username) {
		RenewalWindowDTO window = windowFor(username);
		LocalDate start;
		if (window == null || !window.isCanApply()) {
			start = LocalDate.of(9999, 12, 31);
		} else if (RenewalWindowRules.EXTENDED.equals(window.getWindow()) && window.getExtensionGrantedOn() != null) {
			start = window.getExtensionGrantedOn();
		} else if (window.getIssueDate() != null) {
			start = window.getIssueDate();
		} else {
			start = LocalDate.of(1970, 1, 1);
		}
		return Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public List<RenewalExtensionCandidateDTO> candidates() {
		LocalDate today = LocalDate.now();
		List<RenewalExtensionCandidateDTO> result = new ArrayList<>();
		for (LicenseDetails license : latestLicensePerUser()) {
			LocalDate expiry = toLocalDate(license.getExpiryDate());
			if (expiry == null) {
				continue;
			}
			long daysLeft = ChronoUnit.DAYS.between(today, expiry);
			if (daysLeft > 0) {
				continue;
			}
			if (activeExtension(license.getUserName(), today) != null) {
				continue;
			}
			RenewalExtensionCandidateDTO dto = new RenewalExtensionCandidateDTO();
			String caName = caName(license);
			String applicantName = applicantName(license.getUserName());
			if (applicantName.isBlank()) {
				applicantName = caName;
			}
			dto.setLicenseId(license.getLicenseId());
			dto.setUserName(license.getUserName());
			dto.setApplicantName(applicantName);
			dto.setCaName(caName);
			dto.setLicenseName(license.getLicenseName());
			dto.setSerialNo(license.getSerialNo());
			dto.setIssueDate(toLocalDate(license.getIssueDate()));
			dto.setExpiryDate(expiry);
			dto.setDaysLeft(daysLeft);
			result.add(dto);
		}
		return result;
	}

	public List<RenewalWindowExtensionDTO> extensions() {
		LocalDate today = LocalDate.now();
		List<RenewalWindowExtension> rows = extensionRepo.findAllByOrderByCreatedDesc();
		List<RenewalWindowExtensionDTO> result = new ArrayList<>();
		for (RenewalWindowExtension row : rows) {
			expireIfNeeded(row, today);
			result.add(toExtensionDto(row));
		}
		return result;
	}

	public RenewalWindowExtensionDTO grant(GrantRenewalExtensionRequest request) {
		if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
			throw new IllegalArgumentException("Username is required.");
		}
		if (request.getGrantedBy() == null || request.getGrantedBy().isBlank()) {
			throw new IllegalArgumentException("Granted by is required.");
		}
		String remarks = request.getRemarks() == null ? null : request.getRemarks().trim();
		if (remarks != null && remarks.length() > 1000) {
			throw new IllegalArgumentException("Remarks must be 1000 characters or fewer.");
		}
		LocalDate extensionUntil = parseExtensionDate(request.getExtensionDate());
		if (extensionUntil == null) {
			throw new IllegalArgumentException(
					"Enter a valid renewal application extension date in DD-MM-YYYY format.");
		}

		String plain = plainUsername(request.getUsername());
		LicenseDetails license = latestLicense(plain);
		if (license == null || license.getExpiryDate() == null) {
			throw new IllegalArgumentException("No license was found for this CA.");
		}

		LocalDate today = LocalDate.now();
		if (extensionUntil.isBefore(today)) {
			throw new IllegalArgumentException("Renewal application extension date cannot be before today.");
		}
		LocalDate expiry = toLocalDate(license.getExpiryDate());
		long daysLeft = ChronoUnit.DAYS.between(today, expiry);
		if (daysLeft > 0) {
			throw new IllegalStateException(
					"A renewal extension can be granted only after the license has expired.");
		}
		RenewalWindowExtension existing = activeExtension(plain, today);
		if (existing != null) {
			throw new IllegalStateException(
					"An extension is already active until " + existing.getExtensionUntil().format(DISPLAY_DATE) + ".");
		}

		RenewalWindowExtension extension = new RenewalWindowExtension();
		extension.setLicenseId(license.getLicenseId());
		extension.setUserName(plain);
		extension.setLicenseName(license.getLicenseName());
		extension.setSerialNo(license.getSerialNo());
		extension.setGrantedOn(today);
		extension.setExtensionUntil(extensionUntil);
		extension.setGrantedBy(plainUsername(request.getGrantedBy()));
		extension.setRemarks(remarks == null || remarks.isEmpty() ? null : remarks);
		extension.setStatus(ACTIVE);
		extension = extensionRepo.save(extension);
		return toExtensionDto(extension);
	}

	private RenewalWindowDTO toWindow(String username, LicenseDetails license, RenewalWindowExtension extension,
			LocalDate today) {
		RenewalWindowDTO dto = new RenewalWindowDTO();
		dto.setUserName(username);
		if (license == null || license.getExpiryDate() == null) {
			dto.setWindow(RenewalWindowRules.NO_LICENSE);
			dto.setCanApply(false);
			dto.setDelayDocumentRequired(false);
			dto.setMessage("No license was found for this CA.");
			return dto;
		}

		LocalDate expiry = toLocalDate(license.getExpiryDate());
		LocalDate extensionUntil = extension == null ? null : extension.getExtensionUntil();
		Decision decision = RenewalWindowRules.classify(expiry, today, extensionUntil);
		dto.setWindow(decision.getWindow());
		dto.setCanApply(decision.isCanApply());
		dto.setDelayDocumentRequired(decision.isDelayDocumentRequired());
		dto.setDaysLeft(decision.getDaysLeft());
		dto.setIssueDate(toLocalDate(license.getIssueDate()));
		dto.setExpiryDate(expiry);
		dto.setLicenseId(license.getLicenseId());
		dto.setSerialNo(license.getSerialNo());
		dto.setLicenseName(license.getLicenseName());
		if (extension != null) {
			dto.setExtensionGrantedOn(extension.getGrantedOn());
			dto.setExtensionUntil(extension.getExtensionUntil());
		}
		dto.setMessage(messageFor(decision, extension));
		return dto;
	}

	private String messageFor(Decision decision, RenewalWindowExtension extension) {
		if (RenewalWindowRules.NORMAL.equals(decision.getWindow())) {
			return "Renewal can be submitted until 45 days remain on the license.";
		}
		if (RenewalWindowRules.LATE.equals(decision.getWindow())) {
			return "45 days or fewer remain before the license expires. Upload the delay of renewal document before submitting.";
		}
		if (RenewalWindowRules.EXTENDED.equals(decision.getWindow())) {
			String until = extension != null && extension.getExtensionUntil() != null
					? extension.getExtensionUntil().format(DISPLAY_DATE)
					: "";
			return "CCA has extended the renewal window until " + until
					+ ". Upload the delay of renewal document before submitting.";
		}
		if (RenewalWindowRules.BLOCKED.equals(decision.getWindow())) {
			return "The license has expired. Visit the CCA office to request a renewal application extension.";
		}
		return "No license was found for this CA.";
	}

	private RenewalWindowExtension activeExtension(String username, LocalDate today) {
		List<RenewalWindowExtension> rows = extensionRepo.findByUserNameAndStatusOrderByExtensionUntilDesc(username, ACTIVE);
		RenewalWindowExtension active = null;
		for (RenewalWindowExtension row : rows) {
			if (expireIfNeeded(row, today)) {
				continue;
			}
			if (active == null) {
				active = row;
			} else {
				row.setStatus(EXPIRED);
				extensionRepo.save(row);
			}
		}
		return active;
	}

	private boolean expireIfNeeded(RenewalWindowExtension row, LocalDate today) {
		if (row == null || !ACTIVE.equals(row.getStatus())) {
			return false;
		}
		if (row.getExtensionUntil() != null && today.isAfter(row.getExtensionUntil())) {
			row.setStatus(EXPIRED);
			extensionRepo.save(row);
			return true;
		}
		return false;
	}

	private List<LicenseDetails> latestLicensePerUser() {
		List<LicenseDetails> all = licenseDetailsServ.getAllLicenseDetails();
		Map<String, LicenseDetails> latest = new LinkedHashMap<>();
		if (all == null) {
			return List.of();
		}
		for (LicenseDetails license : all) {
			if (license.getUserName() != null && !latest.containsKey(license.getUserName())) {
				latest.put(license.getUserName(), license);
			}
		}
		return new ArrayList<>(latest.values());
	}

	private LicenseDetails latestLicense(String username) {
		if (username == null || username.isBlank()) {
			return null;
		}
		List<LicenseDetails> active = licenseDetailsServ.getActiveLicenseDetailsByUsername(username);
		if (active != null && !active.isEmpty()) {
			return active.get(0);
		}
		List<LicenseDetails> all = licenseDetailsServ.getAllLicenseDetailsByUsername(username);
		if (all != null && !all.isEmpty()) {
			return all.get(0);
		}
		return null;
	}

	private RenewalWindowExtensionDTO toExtensionDto(RenewalWindowExtension row) {
		RenewalWindowExtensionDTO dto = new RenewalWindowExtensionDTO();
		dto.setExtensionId(row.getExtensionId());
		dto.setLicenseId(row.getLicenseId());
		dto.setUserName(row.getUserName());
		dto.setLicenseName(row.getLicenseName());
		dto.setSerialNo(row.getSerialNo());
		dto.setGrantedOn(row.getGrantedOn());
		dto.setExtensionUntil(row.getExtensionUntil());
		dto.setGrantedBy(row.getGrantedBy());
		dto.setRemarks(row.getRemarks());
		dto.setStatus(row.getStatus());
		return dto;
	}

	private String caName(LicenseDetails license) {
		try {
			LicenseCertificateContent content = certificateDataService.load(license);
			if (content != null && content.getAuthorityName() != null && !content.getAuthorityName().isBlank()) {
				return content.getAuthorityName().trim();
			}
		} catch (Exception ex) {
			System.err.println("CA name lookup skipped: " + ex.getMessage());
		}
		return license.getLicenseName() == null ? "" : license.getLicenseName().trim();
	}

	private String applicantName(String username) {
		if (username == null || username.isBlank()) {
			return "";
		}
		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					"SELECT first_name, middle_name, last_name FROM auth_users_management.user_login WHERE username = ? LIMIT 1",
					username.trim());
			if (rows.isEmpty()) {
				return "";
			}
			Map<String, Object> row = rows.get(0);
			return joinName(text(row.get("first_name")), text(row.get("middle_name")), text(row.get("last_name")));
		} catch (Exception ex) {
			System.err.println("Applicant name lookup skipped: " + ex.getMessage());
			return "";
		}
	}

	private LocalDate parseExtensionDate(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		String text = value.trim();
		try {
			if (text.matches("\\d{2}-\\d{2}-\\d{4}")) {
				return LocalDate.parse(text, EXTENSION_DATE);
			}
			return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
		} catch (DateTimeParseException ex) {
			return null;
		}
	}

	private static String text(Object value) {
		return value == null ? "" : value.toString().trim();
	}

	private static String joinName(String first, String middle, String last) {
		StringBuilder name = new StringBuilder();
		for (String part : new String[] { first, middle, last }) {
			if (part == null || part.isBlank()) {
				continue;
			}
			if (name.length() > 0) {
				name.append(' ');
			}
			name.append(part.trim());
		}
		return name.toString();
	}

	private LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private String plainUsername(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		String trimmed = value.trim();
		String decrypted = EncryptionUtil.decrypt(trimmed);
		return decrypted != null ? decrypted.trim() : trimmed;
	}
}
