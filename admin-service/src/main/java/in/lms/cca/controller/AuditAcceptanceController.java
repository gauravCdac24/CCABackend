package in.lms.cca.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.entity.AuditAcceptance;
import in.lms.cca.service.IAuditAcceptanceService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditAcceptanceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditAcceptanceController {

	private static final String DOCUMENT_FOLDER = "auditacceptance/Document";

	@Autowired
	private IAuditAcceptanceService documentService;

	private Path getDocumentDirectory() {
		return Paths.get(Constant.REAL_PATH, DOCUMENT_FOLDER.split("/"));
	}

	private Optional<Path> resolveFilePath(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return Optional.empty();
		}
		Path[] candidates = new Path[] {
				getDocumentDirectory().resolve(fileName),
				Paths.get(Constant.REAL_PATH, "webapp", "auditacceptance", "Document", fileName),
				Paths.get(Constant.REAL_PATH + "//webapp//auditacceptance//Document").resolve(fileName)
		};
		for (Path candidate : candidates) {
			Path normalized = candidate.normalize();
			if (Files.exists(normalized)) {
				return Optional.of(normalized);
			}
		}
		return Optional.empty();
	}

	private Long decryptId(String encryptedId) {
		if (encryptedId == null || encryptedId.isBlank()) {
			return null;
		}
		String decrypted = EncryptionUtil.decrypt(encryptedId);
		if (decrypted == null || decrypted.isBlank()) {
			return null;
		}
		try {
			return Long.parseLong(decrypted.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private String saveFile(MultipartFile file) throws IOException {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		Path directoryPath = getDocumentDirectory();
		Files.createDirectories(directoryPath);
		SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
		String timestamp = dateFormats.format(new Date());
		String filename = "AA" + timestamp + "." + extension;
		Files.write(directoryPath.resolve(filename), file.getBytes());
		return filename;
	}

	private void cleanupSavedFile(String filename) {
		if (filename == null || filename.isBlank()) {
			return;
		}
		try {
			Files.deleteIfExists(getDocumentDirectory().resolve(filename));
		} catch (Exception cleanupEx) {
			cleanupEx.printStackTrace();
		}
	}

	private ResponseEntity<?> validateFields(String version, String publishDate, MultipartFile file, boolean fileRequired) {
		if (version == null || version.trim().isEmpty()) {
			return new ResponseEntity<>("Please enter version.", HttpStatus.BAD_REQUEST);
		} else if (version.length() > 10) {
			return new ResponseEntity<>("Version cannot exceed 10 characters.", HttpStatus.BAD_REQUEST);
		}
		if (publishDate == null || publishDate.trim().isEmpty()) {
			return new ResponseEntity<>("Please enter publish date.", HttpStatus.BAD_REQUEST);
		} else if (!publishDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return new ResponseEntity<>("Publish date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
		}
		if (fileRequired && (file == null || file.isEmpty())) {
			return new ResponseEntity<>("Please enter file name.", HttpStatus.BAD_REQUEST);
		}
		return null;
	}

	private Date parseCreated(String created, Date fallback) {
		if (created == null || created.isBlank()) {
			return fallback != null ? fallback : new Date();
		}
		String value = created.trim();
		if (value.matches("\\d+")) {
			try {
				return new Date(Long.parseLong(value));
			} catch (NumberFormatException ex) {
				return fallback != null ? fallback : new Date();
			}
		}
		String[] patterns = {
				"yyyy-MM-dd'T'HH:mm:ss.SSS",
				"yyyy-MM-dd'T'HH:mm:ss.SSSX",
				"yyyy-MM-dd'T'HH:mm:ss",
				"yyyy-MM-dd HH:mm:ss.SSS",
				"yyyy-MM-dd HH:mm:ss"
		};
		for (String pattern : patterns) {
			try {
				return new SimpleDateFormat(pattern).parse(value);
			} catch (ParseException ignored) {
				// try the next stored date shape
			}
		}
		return fallback != null ? fallback : new Date();
	}

	@PostMapping(AuditAcceptanceAPIs.ADD)
	public ResponseEntity<?> addDocument(
			@RequestParam("file") MultipartFile file,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("version") String version) {

		ResponseEntity<?> validationError = validateFields(version, publishDate, file, true);
		if (validationError != null) {
			return validationError;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date publishDateParsed;
		try {
			publishDateParsed = dateFormat.parse(publishDate);
		} catch (ParseException e) {
			return new ResponseEntity<>("Invalid publish date format.", HttpStatus.BAD_REQUEST);
		}

		String filename = null;
		try {
			filename = saveFile(file);

			AuditAcceptance document = new AuditAcceptance();
			document.setFileName(filename);
			document.setPublishDate(publishDateParsed);
			document.setStatus("Inactive");
			document.setVersion(version.trim());

			Optional<AuditAcceptance> saved = documentService.addDocument(document);
			if (saved.isEmpty()) {
				cleanupSavedFile(filename);
				return new ResponseEntity<>("An error occurred while saving the audit acceptance. Please try again.",
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Audit acceptance successfully added.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			cleanupSavedFile(filename);
			return new ResponseEntity<>("An error occurred while saving the audit acceptance. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(AuditAcceptanceAPIs.UPDATE)
	public ResponseEntity<?> updateDocument(
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("version") String version,
			@RequestParam("auditAcceptanceId") String auditAcceptanceId,
			@RequestParam("created") String created) {

		boolean fileProvided = file != null && !file.isEmpty();
		ResponseEntity<?> validationError = validateFields(version, publishDate, file, false);
		if (validationError != null) {
			return validationError;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date publishDateParsed;
		try {
			publishDateParsed = dateFormat.parse(publishDate);
		} catch (ParseException e) {
			return new ResponseEntity<>("Invalid publish date format.", HttpStatus.BAD_REQUEST);
		}

		String filename = null;
		try {
			Long docId = decryptId(auditAcceptanceId);
			AuditAcceptance existing = docId == null ? null : documentService.getById(docId);
			if (existing == null) {
				return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
			}

			if (fileProvided) {
				filename = saveFile(file);
			}

			AuditAcceptance document = new AuditAcceptance();
			document.setAuditAcceptanceId(docId);
			document.setFileName(fileProvided ? filename : existing.getFileName());
			document.setVersion(version.trim());
			document.setPublishDate(publishDateParsed);
			document.setStatus(existing.getStatus() != null ? existing.getStatus() : "Inactive");
			document.setCreatedBy(existing.getCreatedBy());
			document.setUpdatedBy(existing.getUpdatedBy());
			document.setUpdated(new Date());
			document.setCreated(parseCreated(created, existing.getCreated()));

			Optional<AuditAcceptance> saved = documentService.updateDocument(document);
			if (saved.isEmpty()) {
				cleanupSavedFile(filename);
				return new ResponseEntity<>("An error occurred while saving the audit acceptance. Please try again.",
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Audit acceptance successfully updated.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			cleanupSavedFile(filename);
			return new ResponseEntity<>("An error occurred while saving the audit acceptance. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AuditAcceptanceAPIs.GET_ALL)
	public ResponseEntity<?> getAllDocuments() {
		return new ResponseEntity<>(documentService.getAllDocuments(), HttpStatus.OK);
	}

	@GetMapping(AuditAcceptanceAPIs.GET_ALL_ACTIVE)
	public ResponseEntity<?> getAllActiveDocuments() {
		return new ResponseEntity<>(documentService.getAllActiveDocuments(), HttpStatus.OK);
	}

	@GetMapping(AuditAcceptanceAPIs.GET_ALL_INACTIVE)
	public ResponseEntity<?> getAllInactiveDocuments() {
		return new ResponseEntity<>(documentService.getAllInactiveDocuments(), HttpStatus.OK);
	}

	@GetMapping(AuditAcceptanceAPIs.CHANGE_STATUS)
	public ResponseEntity<?> changeStatus(@RequestParam("id") String encryptedId) {
		Long docId = decryptId(encryptedId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
		}
		try {
			AuditAcceptance document = documentService.getById(docId);
			if (document == null) {
				return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
			}

			if ("Active".equalsIgnoreCase(document.getStatus())) {
				document.setStatus("Inactive");
			} else {
				List<AuditAcceptance> activeList = documentService.getAllActiveDocuments();
				boolean anotherActiveExists = activeList.stream()
						.anyMatch(active -> active.getAuditAcceptanceId() != null
								&& !active.getAuditAcceptanceId().equals(docId));
				if (anotherActiveExists) {
					return new ResponseEntity<>("Active audit acceptance already present.", HttpStatus.BAD_REQUEST);
				}
				document.setStatus("Active");
			}

			Optional<AuditAcceptance> updated = documentService.updateDocument(document);
			if (updated.isEmpty()) {
				return new ResponseEntity<>("Unable to change audit acceptance status. Please try again.",
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Audit acceptance status changed to " + updated.get().getStatus() + ".",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to change audit acceptance status. Please try again.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(AuditAcceptanceAPIs.DOWNLOAD)
	public ResponseEntity<Resource> downloadFile(
			@RequestParam("id") String encryptedId,
			@RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
		Long docId = decryptId(encryptedId);
		if (docId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		try {
			Optional<AuditAcceptance> documentOpt = documentService.downloadFile(docId);
			if (documentOpt.isPresent()) {
				AuditAcceptance document = documentOpt.get();
				if (activeOnly && !"Active".equalsIgnoreCase(document.getStatus())) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
				}
				Optional<Path> filePathOpt = resolveFilePath(document.getFileName());
				if (filePathOpt.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
				Path filePath = filePathOpt.get();
				Resource resource = new UrlResource(filePath.toUri());
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					return ResponseEntity.ok()
							.contentType(MediaType.parseMediaType(
									contentType != null ? contentType : "application/octet-stream"))
							.header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
							.body(resource);
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping(AuditAcceptanceAPIs.GET_BY_ID)
	public ResponseEntity<?> getById(@RequestParam("id") String encryptedId) {
		Long docId = decryptId(encryptedId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
		}
		try {
			AuditAcceptance document = documentService.getById(docId);
			if (document == null) {
				return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(document, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AuditAcceptanceAPIs.DELETE_BY_ID)
	public ResponseEntity<?> deleteById(@RequestParam("id") String encryptedId) {
		Long docId = decryptId(encryptedId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid audit acceptance id.", HttpStatus.BAD_REQUEST);
		}
		try {
			boolean deleted = documentService.deleteById(docId);
			if (deleted) {
				return new ResponseEntity<>("Audit acceptance is successfully deleted.", HttpStatus.OK);
			}
			return new ResponseEntity<>("Error in deleting the audit acceptance.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in deleting the audit acceptance.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
