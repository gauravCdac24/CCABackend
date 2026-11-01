package in.lms.cca.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.certificate.LicenseCertificateDataService;
import in.lms.cca.certificate.LicenseCertificatePdfGenerator;
import in.lms.cca.dto.LicenseFormContextDTO;
import in.lms.cca.dto.LicenseFormDefaultsDTO;
import in.lms.cca.entity.LicenseDetails;
import in.lms.cca.entity.LicenseReview;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.repository.LicenseReviewRepository;
import in.lms.cca.service.ILicenseDetailsService;
import in.lms.cca.service.ILicenseReviewService;
import in.lms.cca.service.INewLicenseClientService;
import in.lms.cca.util.global.SmsTemplateKey;
import in.lms.cca.util.golbal.DocumentFileUtil;
import in.lms.cca.util.golbal.EncryptionUtil;
import in.lms.cca.util.golbal.LicenseSerialNumber;
import in.lms.cca.util.golbal.PublicKeyCertificate;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LicenseReviewServiceImpl implements ILicenseReviewService {

	private static final String PENDING_CCA_STATUS = "LICENSE_DETAILS_PENDING_CCA_REVIEW";
	private static final String SENT_TO_ADMIN_STATUS = "LICENSE_DETAILS_SENT_TO_ADMIN";

	@Autowired
	private LicenseReviewRepository reviewRepository;

	@Autowired
	private ILicenseDetailsService licenseDetailsServ;

	@Autowired
	private INewLicenseClientService newlicenseClientServ;

	@Autowired
	private NotificationClientServiceImpl notificationServ;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LicenseCertificatePdfGenerator licenseCertificatePdf;

	@Autowired
	private LicenseCertificateDataService licenseCertificateData;

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;

	@Override
	public LicenseFormDefaultsDTO getFormDefaults(String intentAppId, String applicantUsername) {
		String plainIntentId = requirePlain(intentAppId, "Invalid intent application id.");
		String username = requirePlain(applicantUsername, "Invalid applicant username.");
		LicenseFormContextDTO context = fetchContext(plainIntentId);
		Allocation allocation = allocate(plainIntentId, username, context);
		LicenseReview open = findOpenReview(plainIntentId);

		LicenseFormDefaultsDTO dto = new LicenseFormDefaultsDTO();
		dto.setApplicationKind(allocation.kind);
		dto.setCaSerial(allocation.caSerial);
		dto.setKeySequence(allocation.keySequence);
		dto.setPreviousSerialNo(allocation.previousSerialNo);
		dto.setRootCaKeyCount(context.getRootCaKeyCount() == null ? 0 : context.getRootCaKeyCount());
		dto.setIssueDateEditable(LicenseReview.RENEWAL.equals(allocation.kind));
		dto.setPublicKey("");
		dto.setCcaRemark("");

		if (open != null) {
			dto.setReviewId(open.getLicenseReviewId());
			dto.setReviewStatus(open.getStatus());
			dto.setPublicKey(open.getPublicKey());
			dto.setCcaRemark(open.getCcaRemark() == null ? "" : open.getCcaRemark());
			dto.setCaSerial(open.getCaSerial());
			dto.setKeySequence(open.getKeySequence());
			dto.setSerialNo(open.getSerialNo());
			dto.setIssueDate(formatDate(open.getIssueDate()));
			dto.setExpiryDate(formatDate(open.getExpiryDate()));
			return dto;
		}

		LocalDate issueDate = parseDate(context.getInPrincipleApprovalDate());
		if (LicenseReview.FRESH.equals(allocation.kind) && issueDate != null) {
			LocalDate expiryDate = issueDate.plusYears(5);
			dto.setIssueDate(issueDate.toString());
			dto.setExpiryDate(expiryDate.toString());
			dto.setSerialNo(LicenseSerialNumber.build(issueDate, expiryDate, allocation.caSerial, allocation.keySequence));
		} else if (LicenseReview.RENEWAL.equals(allocation.kind)) {
			// Admin enters the renewal serial; do not pre-fill previous or generated values.
			dto.setSerialNo("");
		}
		return dto;
	}

	@Override
	public void submitForReview(String intentAppId, String applicantUsername, String submittedBy, String serialNo,
			String issueDate, String expiryDate, String publicKey) {
		String plainIntentId = requirePlain(intentAppId, "Invalid intent application id.");
		String username = requirePlain(applicantUsername, "Invalid applicant username.");
		String submittedByRef = requireEncryptedUser(submittedBy, "Invalid submitted-by user reference.");
		String key = publicKey == null ? "" : publicKey.trim();
		if (key.isEmpty()) {
			throw new IllegalArgumentException("Please enter the public key.");
		}
		if (key.length() > 20000) {
			throw new IllegalArgumentException("Public key is too long.");
		}
		PublicKeyCertificate.requireEnvelope(key);

		LocalDate issue = parseDate(issueDate);
		LocalDate expiry = parseDate(expiryDate);
		if (issue == null) {
			throw new IllegalArgumentException("Please select issue date.");
		}
		if (expiry == null || !expiry.equals(issue.plusYears(5))) {
			throw new IllegalArgumentException("Expiry date must be 5 years from the issue date.");
		}

		LicenseFormContextDTO context = fetchContext(plainIntentId);
		Allocation allocation = allocate(plainIntentId, username, context);
		LicenseReview open = findOpenReview(plainIntentId);
		if (open != null && LicenseReview.PENDING_CCA_REVIEW.equals(open.getStatus())) {
			throw new IllegalArgumentException("These license details are already pending CCA review.");
		}

		int caSerial;
		int keySequence;
		String resolvedSerial;
		if (LicenseReview.FRESH.equals(allocation.kind)) {
			LocalDate approvalDate = parseDate(context.getInPrincipleApprovalDate());
			if (approvalDate == null) {
				throw new IllegalArgumentException("In-principle approval date is not available for this application.");
			}
			if (!issue.equals(approvalDate)) {
				throw new IllegalArgumentException("Issue date must be the in-principle approval date.");
			}
			caSerial = open != null && open.getCaSerial() != null ? open.getCaSerial() : allocation.caSerial;
			keySequence = open != null && open.getKeySequence() != null ? open.getKeySequence() : allocation.keySequence;
			resolvedSerial = LicenseSerialNumber.build(issue, expiry, caSerial, keySequence);
			if (serialNo == null || !resolvedSerial.equals(serialNo.trim())) {
				throw new IllegalArgumentException(
						"License serial number does not match the required 24-character format. Expected "
								+ resolvedSerial + ".");
			}
		} else {
			// Renewal: admin-supplied serial (not auto-generated like fresh applications).
			resolvedSerial = serialNo == null ? "" : serialNo.trim();
			if (!LicenseSerialNumber.isNumeric24(resolvedSerial)) {
				throw new IllegalArgumentException("Please enter a valid 24-digit license serial number.");
			}
			String expectedFromDates = LicenseSerialNumber.build(issue, expiry,
					LicenseSerialNumber.caSerial(resolvedSerial), LicenseSerialNumber.nnn(resolvedSerial));
			if (!resolvedSerial.equals(expectedFromDates)) {
				throw new IllegalArgumentException(
						"License serial number must use the selected issue year and expiry date (DDMMYY).");
			}
			caSerial = LicenseSerialNumber.caSerial(resolvedSerial);
			keySequence = LicenseSerialNumber.nnn(resolvedSerial);
		}
		assertSerialAvailable(resolvedSerial, EncryptionUtil.encrypt(plainIntentId));

		LicenseReview review = open == null ? new LicenseReview() : open;
		review.setIntentAppId(EncryptionUtil.encrypt(plainIntentId));
		review.setUserName(username);
		review.setSerialNo(resolvedSerial);
		review.setIssueDate(toDate(issue));
		review.setExpiryDate(toDate(expiry));
		review.setPublicKey(key);
		review.setApplicationKind(allocation.kind);
		review.setStatus(LicenseReview.PENDING_CCA_REVIEW);
		review.setCaSerial(caSerial);
		review.setKeySequence(keySequence);
		review.setSubmittedBy(submittedByRef);
		reviewRepository.save(review);

		newlicenseClientServ.changeIntentApplicationStatus(plainIntentId, PENDING_CCA_STATUS, submittedByRef);
	}

	@Override
	public void approve(Long reviewId, String remark, String reviewedBy) {
		LicenseReview review = requirePendingReview(reviewId);
		String reviewer = requireEncryptedUser(reviewedBy, "Invalid reviewer reference.");
		review.setCcaRemark(remark == null ? "" : remark.trim());
		review.setReviewedBy(reviewer);
		review.setStatus(LicenseReview.APPROVED);
		reviewRepository.save(review);
		grantLicense(review, reviewer);
	}

	@Override
	public void sendToAdmin(Long reviewId, String remark, String reviewedBy) {
		String note = remark == null ? "" : remark.trim();
		if (note.isEmpty()) {
			throw new IllegalArgumentException("Please enter a remark before sending the form back to admin.");
		}
		LicenseReview review = requirePendingReview(reviewId);
		String reviewer = requireEncryptedUser(reviewedBy, "Invalid reviewer reference.");
		review.setCcaRemark(note);
		review.setReviewedBy(reviewer);
		review.setStatus(LicenseReview.SENT_TO_ADMIN);
		reviewRepository.save(review);

		String plainIntentId = EncryptionUtil.decrypt(review.getIntentAppId());
		if (plainIntentId == null || plainIntentId.isBlank()) {
			throw new IllegalArgumentException("Invalid intent application id on the license review.");
		}
		newlicenseClientServ.changeIntentApplicationStatus(plainIntentId, SENT_TO_ADMIN_STATUS, reviewer);
	}

	private void grantLicense(LicenseReview review, String reviewedBy) {
		String appId = EncryptionUtil.decrypt(review.getIntentAppId());
		if (appId == null || appId.isBlank()) {
			throw new IllegalStateException("Invalid intent application id on the license review.");
		}
		String ausername = review.getUserName();

		List<LicenseDetails> activeLicense = licenseDetailsServ.getActiveLicenseDetailsByUsername(ausername);
		if (activeLicense != null && !activeLicense.isEmpty()) {
			LicenseDetails uplic = activeLicense.get(0);
			uplic.setStatus("Inactive");
			uplic.setUpdatedBy(reviewedBy);
			licenseDetailsServ.updateLicenseDetails(uplic);
		}

		String licStatus = LicenseReview.RENEWAL.equals(review.getApplicationKind()) ? "renewed" : "issued";

		LicenseDetails license = new LicenseDetails();
		license.setCreatedBy(reviewedBy);
		license.setExpiryDate(review.getExpiryDate());
		license.setIntentAppId(review.getIntentAppId());
		license.setIssueDate(review.getIssueDate());
		license.setPublicKey(review.getPublicKey());
		license.setSerialNo(review.getSerialNo());
		license.setStatus("Active");
		license.setUpdatedBy(reviewedBy);
		license.setUserName(ausername);
		license.setLicenseName(ausername);

		String licenseCertificate;
		try {
			byte[] pdf = licenseCertificatePdf.render(licenseCertificateData.load(license));
			licenseCertificate = DocumentFileUtil.saveBytes(pdf, "LicenseCertificate", appId,
					"LicenseCertificate/Document", ".pdf");
		} catch (Exception ex) {
			throw new IllegalStateException(
					"Failed to save license certificate file. Check server upload path and disk permissions.");
		}
		if (licenseCertificate == null || licenseCertificate.isBlank()) {
			throw new IllegalStateException(
					"Failed to save license certificate file. Check server upload path and disk permissions.");
		}
		licenseCertificate = DocumentFileUtil.truncateFileName(licenseCertificate, 255);
		license.setLicenseCertificate(licenseCertificate);

		Optional<LicenseDetails> savedLicense = licenseDetailsServ.addLicenseDetails(license);
		if (savedLicense.isEmpty()) {
			throw new IllegalStateException("Failed to save license details. Please verify database constraints and try again.");
		}

		try {
			licenseDetailsServ.changedTheReviewStatus(ausername);
		} catch (Exception ex) {
			System.err.println("change-review-status skipped for " + ausername + ": " + ex.getMessage());
		}
		try {
			licenseDetailsServ.changedThePaymentStatus(ausername);
		} catch (Exception ex) {
			System.err.println("change-payment-status skipped for " + ausername + ": " + ex.getMessage());
		}
		try {
			newlicenseClientServ.changeApplicantRoleToLicensee(ausername);
		} catch (Exception ex) {
			System.err.println("change-applicant-role-to-licensee skipped for " + ausername + ": " + ex.getMessage());
		}
		try {
			newlicenseClientServ.changeAnnexureDetails(ausername);
		} catch (Exception ex) {
			System.err.println("change-annexure-details skipped for " + ausername + ": " + ex.getMessage());
		}

		newlicenseClientServ.changeIntentApplicationStatus(appId, "LICENSE_ISSUED", reviewedBy);

		try {
			String title = "CCA: License " + licStatus;
			String message = "Dear Applicant, Your CA license has " + licStatus
					+ ". You can view your license details after signin to the portal.";
			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(ausername);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("All");
			notification.setRole("ROLE_LICENSEE");
			notification.setNotificationDescription("CA License Issued");
			if ("renewed".equals(licStatus)) {
				notification.setSmsTemplateKey(SmsTemplateKey.LICENSE_RENEWAL);
				notification.setSmsVars(List.of("Applicant", appId, review.getSerialNo()));
			} else {
				notification.setSmsTemplateKey(SmsTemplateKey.LICENSE_ISSUANCE);
				notification.setSmsVars(List.of("Applicant", review.getSerialNo(), appId));
			}
			notificationServ.sendNotification(notification);
		} catch (Exception ex) {
			System.err.println("license-issued notification skipped for " + ausername + ": " + ex.getMessage());
		}
	}

	private LicenseReview requirePendingReview(Long reviewId) {
		if (reviewId == null) {
			throw new IllegalArgumentException("License review id is required.");
		}
		LicenseReview review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("License review was not found."));
		if (!LicenseReview.PENDING_CCA_REVIEW.equals(review.getStatus())) {
			throw new IllegalArgumentException("This license form is not waiting for CCA review.");
		}
		return review;
	}

	private Allocation allocate(String plainIntentId, String username, LicenseFormContextDTO context) {
		List<LicenseDetails> existing = licenseDetailsServ.getAllLicenseDetailsByUsername(username);
		boolean renewal = existing != null && !existing.isEmpty();
		String previousSerial = renewal ? existing.get(0).getSerialNo() : null;
		LicenseReview open = findOpenReview(plainIntentId);

		Allocation allocation = new Allocation();
		allocation.kind = renewal ? LicenseReview.RENEWAL : LicenseReview.FRESH;
		allocation.previousSerialNo = previousSerial;

		if (open != null && open.getCaSerial() != null && open.getKeySequence() != null) {
			allocation.caSerial = open.getCaSerial();
			allocation.keySequence = open.getKeySequence();
			return allocation;
		}

		if (renewal) {
			Integer previousCa = LicenseSerialNumber.caSerial(previousSerial);
			Integer previousNnn = LicenseSerialNumber.nnn(previousSerial);
			allocation.caSerial = previousCa != null ? previousCa : nextFreshCaSerial(plainIntentId);
			int baseNnn = previousNnn != null ? previousNnn : 0;
			int rootKeys = context.getRootCaKeyCount() == null ? 0 : context.getRootCaKeyCount();
			allocation.keySequence = LicenseSerialNumber.renewalNnn(baseNnn, rootKeys);
			return allocation;
		}

		allocation.caSerial = nextFreshCaSerial(plainIntentId);
		allocation.keySequence = 0;
		return allocation;
	}

	private int nextFreshCaSerial(String plainIntentId) {
		int highest = 0;
		String encryptedSelf = EncryptionUtil.encrypt(plainIntentId);
		List<LicenseDetails> all = licenseDetailsServ.getAllLicenseDetails();
		if (all != null) {
			for (LicenseDetails license : all) {
				Integer serial = LicenseSerialNumber.caSerial(license.getSerialNo());
				if (serial != null && serial > highest) {
					highest = serial;
				}
			}
		}
		List<LicenseReview> reviews = reviewRepository.findAll();
		for (LicenseReview review : reviews) {
			if (encryptedSelf != null && encryptedSelf.equals(review.getIntentAppId())) {
				continue;
			}
			if (LicenseReview.APPROVED.equals(review.getStatus())) {
				continue;
			}
			Integer serial = review.getCaSerial() != null ? review.getCaSerial()
					: LicenseSerialNumber.caSerial(review.getSerialNo());
			if (serial != null && serial > highest) {
				highest = serial;
			}
		}
		return LicenseSerialNumber.nextCaSerial(highest);
	}

	private void assertSerialAvailable(String serial, String encryptedIntent) {
		List<LicenseDetails> licenses = licenseDetailsServ.getAllLicenseDetails();
		if (licenses != null) {
			for (LicenseDetails license : licenses) {
				if (serial.equals(license.getSerialNo())) {
					throw new IllegalArgumentException(
							"This license serial number is already in use. Close the form and open it again.");
				}
			}
		}
		for (LicenseReview review : reviewRepository.findAll()) {
			if (encryptedIntent != null && encryptedIntent.equals(review.getIntentAppId())) {
				continue;
			}
			if (serial.equals(review.getSerialNo()) && !LicenseReview.APPROVED.equals(review.getStatus())) {
				throw new IllegalArgumentException(
						"This license serial number is already reserved. Close the form and open it again.");
			}
		}
	}

	private LicenseReview findOpenReview(String plainIntentId) {
		String encrypted = EncryptionUtil.encrypt(plainIntentId);
		if (encrypted == null) {
			return null;
		}
		List<LicenseReview> rows = reviewRepository.findByIntentAppIdAndStatusNotOrderByCreatedDesc(encrypted,
				LicenseReview.APPROVED);
		return rows == null || rows.isEmpty() ? null : rows.get(0);
	}

	private LicenseFormContextDTO fetchContext(String plainIntentId) {
		try {
			String uriTemplate = apigatewayServiceUrl + "/api/newlicense-service/license-form-context";
			var uri = UriComponentsBuilder.fromUriString(uriTemplate).queryParam("id", plainIntentId).build().toUri();
			LicenseFormContextDTO context = restTemplate.getForObject(uri, LicenseFormContextDTO.class);
			if (context == null) {
				context = new LicenseFormContextDTO();
			}
			if (context.getRootCaKeyCount() == null) {
				context.setRootCaKeyCount(0);
			}
			return context;
		} catch (Exception ex) {
			System.err.println("license-form-context skipped for " + plainIntentId + ": " + ex.getMessage());
			LicenseFormContextDTO empty = new LicenseFormContextDTO();
			empty.setRootCaKeyCount(0);
			return empty;
		}
	}

	private String requirePlain(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		String decrypted = EncryptionUtil.decrypt(value.trim());
		String plain = decrypted != null ? decrypted.trim() : value.trim();
		if (plain.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return plain;
	}

	private String requireEncryptedUser(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		String decrypted = EncryptionUtil.decrypt(value.trim());
		String encrypted = decrypted != null ? EncryptionUtil.encrypt(decrypted.trim()) : EncryptionUtil.encrypt(value.trim());
		if (encrypted == null || encrypted.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return encrypted;
	}

	private LocalDate parseDate(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return LocalDate.parse(value.trim());
		} catch (Exception ex) {
			return null;
		}
	}

	private Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
	}

	private static final class Allocation {
		private String kind;
		private int caSerial;
		private int keySequence;
		private String previousSerialNo;
	}
}
