package in.lms.cca.controller;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.entity.ApplicantAuditAcceptanceEntity;
import in.lms.cca.entity.ESignedDocumentsEntity;
import in.lms.cca.repository.ApplicantAuditAcceptanceRepository;
import in.lms.cca.service.EsignedDocumentsService;
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.golbal.DocumentFileUtil;

/**
 * Audit Acceptance letter workflow (applicant side) and eSigned audit report
 * lookups.
 *
 * eSign rows in {@code audit_management.esigned_documents} are keyed as:
 * <ul>
 * <li>{@code UCCA...} (legacy single signer; {@code signer_key} may hold
 * {@code AUDITOR:id} or {@code ACCEPTANCE_REPORT})</li>
 * <li>{@code UCCA...|AUDITOR:id} (multi-auditor)</li>
 * <li>{@code UCCA...|ACCEPTANCE} (applicant acceptance letter)</li>
 * </ul>
 */
@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class AuditAcceptanceLetterController {

	private static final String LETTER_FOLDER = "AcceptanceLetter";
	private static final String ACCEPTANCE_SUFFIX = "|ACCEPTANCE";
	private static final String ACCEPTANCE_SIGNER_KEY = "ACCEPTANCE";
	private static final long MAX_LETTER_SIZE = 15L * 1024 * 1024;

	@Autowired
	private EsignedDocumentsService documentsService;

	@Autowired
	private ApplicantAuditAcceptanceRepository acceptanceRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;

	// ------------------------------------------------------------------
	// eSign document lookup
	// ------------------------------------------------------------------

	@GetMapping("/get-esign-document-id")
	public ResponseEntity<?> eSignDocumentId(@RequestParam("userName") String userName) {
		try {
			String applicant = normalizeUserName(userName);
			if (applicant.isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			List<ESignedDocumentsEntity> all = documentsService.getAllForApplicant(applicant);
			if (all == null || all.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No eSign document found for username: " + applicant);
			}

			ESignedDocumentsEntity latestAuditorSigned = latest(all, applicant, false, true);
			ESignedDocumentsEntity latestAuditorAny = latest(all, applicant, false, false);
			ESignedDocumentsEntity latestAcceptanceSigned = latest(all, applicant, true, true);

			ESignedDocumentsEntity primary = latestAuditorSigned != null ? latestAuditorSigned : latestAuditorAny;
			if (primary == null) {
				// Only acceptance rows exist
				primary = latest(all, applicant, true, false);
			}

			Map<String, Object> res = new HashMap<>();
			res.put("eSignedId", primary.geteSignedId());
			res.put("userName", primary.getUserName());
			res.put("status", primary.getStatus());
			res.put("signerKey", effectiveSignerKey(primary));
			res.put("created", primary.getCreated());
			res.put("updated", primary.getUpdated());

			String auditorSignedDocId = latestAuditorSigned != null ? latestAuditorSigned.getSignedDocId() : null;
			res.put("signedDocId", auditorSignedDocId);
			res.put("auditorSignedDocId", auditorSignedDocId);
			res.put("auditorESignedId", latestAuditorSigned != null ? latestAuditorSigned.geteSignedId() : null);

			String acceptanceSignedDocId = latestAcceptanceSigned != null ? latestAcceptanceSigned.getSignedDocId()
					: null;
			if (isBlank(acceptanceSignedDocId)) {
				Optional<ApplicantAuditAcceptanceEntity> row = acceptanceRepository
						.findFirstByApplicantUserNameOrderByAcceptanceIdDesc(applicant);
				if (row.isPresent() && !isBlank(row.get().getSignedDocId())) {
					acceptanceSignedDocId = row.get().getSignedDocId();
				}
			}
			res.put("acceptanceSignedDocId", isBlank(acceptanceSignedDocId) ? null : acceptanceSignedDocId);
			res.put("acceptanceESignedId", latestAcceptanceSigned != null ? latestAcceptanceSigned.geteSignedId() : null);
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/download-auditor-signed-report")
	public ResponseEntity<?> downloadAuditorSignedReport(@RequestParam("userName") String userName) {
		try {
			String applicant = normalizeUserName(userName);
			if (applicant.isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			ESignedDocumentsEntity signed = latest(documentsService.getAllForApplicant(applicant), applicant, false,
					true);
			if (signed == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No auditor-signed report found for username: " + applicant);
			}

			String id = URLEncoder.encode(signed.getSignedDocId(), StandardCharsets.UTF_8);
			URI uri = URI.create(apigatewayServiceUrl + "/api/esign-service/get-digitally-signed-document?id=" + id);
			ResponseEntity<byte[]> upstream;
			try {
				upstream = restTemplate.exchange(uri, HttpMethod.GET, null, byte[].class);
			} catch (Exception ex) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Signed audit report file is not available: " + ex.getMessage());
			}
			byte[] body = upstream.getBody();
			if (body == null || body.length == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Signed audit report file is empty.");
			}
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"AuditReport.pdf\"")
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
					.body(body);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to download signed audit report: " + e.getMessage());
		}
	}

	// ------------------------------------------------------------------
	// Acceptance letter
	// ------------------------------------------------------------------

	@GetMapping("/acceptance-letter-status")
	public ResponseEntity<?> acceptanceLetterStatus(@RequestParam("userName") String userName) {
		try {
			String applicant = normalizeUserName(userName);
			if (applicant.isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			return ResponseEntity.ok(buildStatus(applicant,
					acceptanceRepository.findFirstByApplicantUserNameOrderByAcceptanceIdDesc(applicant).orElse(null)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch acceptance letter status: " + e.getMessage());
		}
	}

	@PostMapping("/mark-acceptance-downloaded")
	public ResponseEntity<?> markAcceptanceDownloaded(@RequestParam("userName") String userName) {
		try {
			String applicant = normalizeUserName(userName);
			if (applicant.isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			ApplicantAuditAcceptanceEntity row = getOrCreateRow(applicant);
			row.setLetterDownloaded(Boolean.TRUE);
			row = acceptanceRepository.save(row);
			return ResponseEntity.ok(buildStatus(applicant, row));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to mark acceptance letter as downloaded: " + e.getMessage());
		}
	}

	@PostMapping("/upload-acceptance-letter")
	public ResponseEntity<?> uploadAcceptanceLetter(@RequestParam("userName") String userName,
			@RequestParam("file") MultipartFile file) {
		try {
			String applicant = normalizeUserName(userName);
			if (applicant.isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			if (file == null || file.isEmpty()) {
				return ResponseEntity.badRequest().body("Please select the Audit acceptance letter (PDF).");
			}
			String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
			if (!original.endsWith(".pdf")) {
				return ResponseEntity.badRequest().body("Please upload a PDF Audit acceptance letter.");
			}
			if (file.getSize() > MAX_LETTER_SIZE) {
				return ResponseEntity.badRequest().body("The size of file must be less than 15MB.");
			}
			byte[] bytes = file.getBytes();
			if (bytes.length < 5 || bytes[0] != '%' || bytes[1] != 'P' || bytes[2] != 'D' || bytes[3] != 'F') {
				return ResponseEntity.badRequest().body("The uploaded file is not a valid PDF.");
			}

			ApplicantAuditAcceptanceEntity row = getOrCreateRow(applicant);
			if (isSigned(applicant, row)) {
				return ResponseEntity.badRequest().body("Audit acceptance letter has already been eSigned.");
			}

			String savedName = DocumentFileUtil.saveFile(file, "AL", applicant.replaceAll("[^A-Za-z0-9]", ""),
					LETTER_FOLDER);
			if (savedName == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Unable to save the Audit acceptance letter.");
			}
			if (!isBlank(row.getUploadedFileName())) {
				DocumentFileUtil.deleteFile(row.getUploadedFileName(), LETTER_FOLDER);
			}
			row.setUploadedFileName(savedName);
			row.setEsignStatus("NONE");
			row = acceptanceRepository.save(row);
			return ResponseEntity.ok(buildStatus(applicant, row));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to upload acceptance letter: " + e.getMessage());
		}
	}

	@GetMapping("/download-uploaded-acceptance-letter")
	public ResponseEntity<?> downloadUploadedAcceptanceLetter(@RequestParam("userName") String userName) {
		try {
			String applicant = normalizeUserName(userName);
			Optional<ApplicantAuditAcceptanceEntity> row = acceptanceRepository
					.findFirstByApplicantUserNameOrderByAcceptanceIdDesc(applicant);
			if (row.isEmpty() || isBlank(row.get().getUploadedFileName())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No uploaded acceptance letter found.");
			}
			Path filePath = Paths.get(Constant.REAL_PATH, LETTER_FOLDER).resolve(row.get().getUploadedFileName())
					.normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Uploaded acceptance letter file is not available.");
			}
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"AcceptanceLetter.pdf\"")
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to download acceptance letter: " + e.getMessage());
		}
	}

	@PostMapping("/prepare-acceptance-esign")
	public ResponseEntity<?> prepareAcceptanceEsign(@RequestParam("userName") String userName) {
		try {
			String applicant = normalizeUserName(userName);
			if (applicant.isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			Optional<ApplicantAuditAcceptanceEntity> rowOpt = acceptanceRepository
					.findFirstByApplicantUserNameOrderByAcceptanceIdDesc(applicant);
			if (rowOpt.isEmpty() || isBlank(rowOpt.get().getUploadedFileName())) {
				return ResponseEntity.badRequest().body("Please upload the Audit acceptance letter before eSign.");
			}
			ApplicantAuditAcceptanceEntity row = rowOpt.get();
			if (isSigned(applicant, row)) {
				return ResponseEntity.badRequest().body("Audit acceptance letter has already been eSigned.");
			}

			String acceptanceUserName = applicant + ACCEPTANCE_SUFFIX;
			ESignedDocumentsEntity pending = findPending(acceptanceUserName);
			if (pending == null) {
				pending = new ESignedDocumentsEntity();
				pending.setUserName(acceptanceUserName);
				pending.setSignerKey(ACCEPTANCE_SIGNER_KEY);
			}
			pending.setStatus("Active");
			pending.setUpdated(new Date());
			Optional<ESignedDocumentsEntity> saved = documentsService.addEsignDocument(pending);
			if (saved.isEmpty()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failed to prepare eSign document for acceptance letter.");
			}

			row.setEsignStatus("PENDING");
			acceptanceRepository.save(row);

			ESignedDocumentsEntity previous = latest(documentsService.getAllForApplicant(applicant), applicant, false,
					true);
			String previousSignedDocId = previous != null ? previous.getSignedDocId() : null;

			Map<String, Object> res = new HashMap<>();
			res.put("eSignedId", saved.get().geteSignedId());
			res.put("needsFullReport", !isBlank(previousSignedDocId));
			res.put("previousSignedDocId", isBlank(previousSignedDocId) ? null : previousSignedDocId);
			res.put("signerKey", ACCEPTANCE_SIGNER_KEY);
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to prepare acceptance eSign: " + e.getMessage());
		}
	}

	// ------------------------------------------------------------------
	// Helpers
	// ------------------------------------------------------------------

	private Map<String, Object> buildStatus(String applicant, ApplicantAuditAcceptanceEntity row) {
		String status = "NONE";
		if (isSigned(applicant, row)) {
			status = "SIGNED";
		} else if (row != null && !isBlank(row.getUploadedFileName())) {
			status = "UPLOADED";
		} else if (row != null && Boolean.TRUE.equals(row.getLetterDownloaded())) {
			status = "DOWNLOADED";
		}
		Map<String, Object> res = new HashMap<>();
		res.put("acceptanceLetterStatus", status);
		res.put("letterDownloaded", row != null && Boolean.TRUE.equals(row.getLetterDownloaded()));
		res.put("uploaded", row != null && !isBlank(row.getUploadedFileName()));
		res.put("esignStatus", row != null && row.getEsignStatus() != null ? row.getEsignStatus() : "NONE");
		return res;
	}

	private boolean isSigned(String applicant, ApplicantAuditAcceptanceEntity row) {
		if (row != null && ("SIGNED".equalsIgnoreCase(row.getEsignStatus()) || !isBlank(row.getSignedDocId()))) {
			return true;
		}
		if (row == null) {
			return false;
		}
		return latest(documentsService.getAllForApplicant(applicant), applicant, true, true) != null;
	}

	private ApplicantAuditAcceptanceEntity getOrCreateRow(String applicant) {
		Optional<ApplicantAuditAcceptanceEntity> existing = acceptanceRepository
				.findFirstByApplicantUserNameOrderByAcceptanceIdDesc(applicant);
		if (existing.isPresent()) {
			return existing.get();
		}
		ApplicantAuditAcceptanceEntity row = new ApplicantAuditAcceptanceEntity();
		row.setApplicantUserName(applicant);
		row.setLetterDownloaded(Boolean.FALSE);
		row.setEsignStatus("NONE");
		row.setStatus("Active");
		return row;
	}

	private ESignedDocumentsEntity findPending(String userName) {
		List<ESignedDocumentsEntity> docs = documentsService.getEsignDocumentByUserName(userName);
		if (docs == null) {
			return null;
		}
		return docs.stream().filter(d -> isBlank(d.getSignedDocId()))
				.filter(d -> !"Failed".equalsIgnoreCase(d.getStatus()))
				.max(Comparator.comparing(ESignedDocumentsEntity::geteSignedId)).orElse(null);
	}

	/**
	 * Latest eSign record for an applicant.
	 *
	 * @param acceptance true to look at acceptance-letter rows, false for auditor
	 *                   (report) rows
	 * @param signedOnly true to require a non-blank signedDocId
	 */
	private ESignedDocumentsEntity latest(List<ESignedDocumentsEntity> all, String applicant, boolean acceptance,
			boolean signedOnly) {
		if (all == null) {
			return null;
		}
		return all.stream().filter(d -> d != null && d.geteSignedId() != null)
				.filter(d -> isAcceptanceRecord(d, applicant) == acceptance)
				.filter(d -> !signedOnly || !isBlank(d.getSignedDocId()))
				.max(Comparator.comparing(ESignedDocumentsEntity::geteSignedId)).orElse(null);
	}

	private boolean isAcceptanceRecord(ESignedDocumentsEntity d, String applicant) {
		String name = d.getUserName() == null ? "" : d.getUserName();
		String key = d.getSignerKey() == null ? "" : d.getSignerKey().trim().toUpperCase();
		// Exact ACCEPTANCE only — do NOT treat ACCEPTANCE_REPORT (auditor package) as letter.
		return name.equalsIgnoreCase(applicant + ACCEPTANCE_SUFFIX)
				|| key.equals(ACCEPTANCE_SIGNER_KEY)
				|| key.startsWith(ACCEPTANCE_SIGNER_KEY + ":");
	}

	private String effectiveSignerKey(ESignedDocumentsEntity d) {
		if (!isBlank(d.getSignerKey())) {
			return d.getSignerKey();
		}
		String name = d.getUserName();
		int idx = name == null ? -1 : name.indexOf('|');
		return idx >= 0 ? name.substring(idx + 1) : null;
	}

	private String normalizeUserName(String userName) {
		if (userName == null) {
			return "";
		}
		String normalized = userName.replace("\"", "").trim();
		String decrypted = EncryptionUtil.decrypt(normalized);
		if (decrypted != null) {
			normalized = decrypted;
		}
		return normalized.replace("\"", "").trim();
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
