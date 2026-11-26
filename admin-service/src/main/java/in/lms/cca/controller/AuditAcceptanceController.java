package in.lms.cca.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import in.lms.cca.repository.AuditAcceptanceRepository;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditAcceptanceServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import in.lms.cca.util.global.EncryptionUtil;

/**
 * Admin master for the Audit Acceptance letter template (Word document) that
 * applicants download, sign and upload during the "Pending Audit Acceptance"
 * stage. Only one record may be Active at a time.
 */
@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditAcceptanceController {

	private static final String FOLDER = "AuditAcceptance/Document";
	private static final String ACTIVE = "Active";
	private static final String INACTIVE = "Inactive";
	private static final long MAX_FILE_SIZE = 4L * 1024 * 1024;
	private static final String ALREADY_ACTIVE_MSG = "Active audit acceptance already present.";

	@Autowired
	private AuditAcceptanceRepository repository;

	@GetMapping(AuditAcceptanceServiceAPIs.GET_ALL_AUDIT_ACCEPTANCE)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAllByOrderByAuditAcceptanceIdDesc(), HttpStatus.OK);
	}

	@GetMapping(AuditAcceptanceServiceAPIs.GET_ALL_ACTIVE_AUDIT_ACCEPTANCE)
	public ResponseEntity<?> getAllActive() {
		List<AuditAcceptance> list = repository.findByStatusOrderByAuditAcceptanceIdDesc(ACTIVE);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping(AuditAcceptanceServiceAPIs.GET_AUDIT_ACCEPTANCE_BY_ID)
	public ResponseEntity<?> getById(@RequestParam("id") String id) {
		Long pk = decryptId(id);
		if (pk == null) {
			return new ResponseEntity<>("Invalid Audit Acceptance Id.", HttpStatus.BAD_REQUEST);
		}
		Optional<AuditAcceptance> obj = repository.findById(pk);
		if (obj.isEmpty()) {
			return new ResponseEntity<>("Audit acceptance not found.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(obj.get(), HttpStatus.OK);
	}

	@PostMapping(AuditAcceptanceServiceAPIs.ADD_AUDIT_ACCEPTANCE)
	public ResponseEntity<?> add(@RequestParam("file") MultipartFile file,
			@RequestParam("publishDate") String publishDate, @RequestParam("version") String version) {
		try {
			String error = validateFile(file, true);
			if (error == null) {
				error = validateVersion(version);
			}
			Date parsedDate = parseDate(publishDate);
			if (error == null && parsedDate == null) {
				error = "Please enter Publish Date.";
			}
			if (error != null) {
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			String ver = version.trim();
			if (repository.existsByVersionIgnoreCase(ver)) {
				return new ResponseEntity<>("Duplicate version is not allowed.", HttpStatus.BAD_REQUEST);
			}

			String savedName = DocumentFileUtil.saveFile(file, "AA", FOLDER);
			if (savedName == null) {
				return new ResponseEntity<>("An error occurred while saving the file. Please try again.",
						HttpStatus.BAD_REQUEST);
			}

			AuditAcceptance obj = new AuditAcceptance();
			obj.setVersion(ver);
			obj.setPublishDate(parsedDate);
			obj.setFileName(savedName);
			// Only one active letter template at a time.
			boolean activePresent = !repository.findByStatusOrderByAuditAcceptanceIdDesc(ACTIVE).isEmpty();
			obj.setStatus(activePresent ? INACTIVE : ACTIVE);
			repository.save(obj);

			return new ResponseEntity<>("Audit acceptance has been successfully added.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(AuditAcceptanceServiceAPIs.UPDATE_AUDIT_ACCEPTANCE)
	public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("publishDate") String publishDate, @RequestParam("version") String version,
			@RequestParam("auditAcceptanceId") String auditAcceptanceId) {
		try {
			Long pk = decryptId(auditAcceptanceId);
			if (pk == null) {
				return new ResponseEntity<>("Invalid Audit Acceptance Id.", HttpStatus.BAD_REQUEST);
			}
			Optional<AuditAcceptance> existing = repository.findById(pk);
			if (existing.isEmpty()) {
				return new ResponseEntity<>("Audit acceptance not found.", HttpStatus.NOT_FOUND);
			}

			boolean hasFile = file != null && !file.isEmpty();
			String error = validateFile(file, false);
			if (error == null) {
				error = validateVersion(version);
			}
			Date parsedDate = parseDate(publishDate);
			if (error == null && parsedDate == null) {
				error = "Please enter Publish Date.";
			}
			if (error != null) {
				return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
			}

			String ver = version.trim();
			if (repository.existsByVersionIgnoreCaseAndAuditAcceptanceIdNot(ver, pk)) {
				return new ResponseEntity<>("Duplicate version is not allowed.", HttpStatus.BAD_REQUEST);
			}

			AuditAcceptance obj = existing.get();
			if (hasFile) {
				String savedName = DocumentFileUtil.saveFile(file, "AA", FOLDER);
				if (savedName == null) {
					return new ResponseEntity<>("An error occurred while saving the file. Please try again.",
							HttpStatus.BAD_REQUEST);
				}
				if (obj.getFileName() != null) {
					DocumentFileUtil.deleteFile(obj.getFileName(), FOLDER);
				}
				obj.setFileName(savedName);
			}
			obj.setVersion(ver);
			obj.setPublishDate(parsedDate);
			repository.save(obj);

			return new ResponseEntity<>("Audit acceptance has been successfully updated.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AuditAcceptanceServiceAPIs.CHANGE_AUDIT_ACCEPTANCE_STATUS)
	public ResponseEntity<?> changeStatus(@RequestParam("id") String id) {
		try {
			Long pk = decryptId(id);
			if (pk == null) {
				return new ResponseEntity<>("Invalid Audit Acceptance Id.", HttpStatus.BAD_REQUEST);
			}
			Optional<AuditAcceptance> existing = repository.findById(pk);
			if (existing.isEmpty()) {
				return new ResponseEntity<>("Audit acceptance not found.", HttpStatus.NOT_FOUND);
			}
			AuditAcceptance obj = existing.get();
			if (ACTIVE.equals(obj.getStatus())) {
				obj.setStatus(INACTIVE);
			} else {
				if (repository.existsByStatusAndAuditAcceptanceIdNot(ACTIVE, pk)) {
					return new ResponseEntity<>(ALREADY_ACTIVE_MSG, HttpStatus.BAD_REQUEST);
				}
				obj.setStatus(ACTIVE);
			}
			repository.save(obj);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to change audit acceptance status.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AuditAcceptanceServiceAPIs.DELETE_AUDIT_ACCEPTANCE_BY_ID)
	public ResponseEntity<?> deleteById(@RequestParam("id") String id) {
		try {
			Long pk = decryptId(id);
			if (pk == null) {
				return new ResponseEntity<>("Invalid Audit Acceptance Id.", HttpStatus.BAD_REQUEST);
			}
			Optional<AuditAcceptance> existing = repository.findById(pk);
			if (existing.isEmpty()) {
				return new ResponseEntity<>("Audit acceptance not found.", HttpStatus.NOT_FOUND);
			}
			AuditAcceptance obj = existing.get();
			if (ACTIVE.equals(obj.getStatus())) {
				return new ResponseEntity<>("Active audit acceptance cannot be deleted. Please deactivate it first.",
						HttpStatus.BAD_REQUEST);
			}
			if (obj.getFileName() != null) {
				DocumentFileUtil.deleteFile(obj.getFileName(), FOLDER);
			}
			repository.delete(obj);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in deleting audit acceptance.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AuditAcceptanceServiceAPIs.DOWNLOAD_AUDIT_ACCEPTANCE)
	public ResponseEntity<?> download(@RequestParam("id") String id,
			@RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
		try {
			Long pk = decryptId(id);
			if (pk == null) {
				return new ResponseEntity<>("Invalid Audit Acceptance Id.", HttpStatus.BAD_REQUEST);
			}
			Optional<AuditAcceptance> existing = repository.findById(pk);
			if (existing.isEmpty() || existing.get().getFileName() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			AuditAcceptance obj = existing.get();
			if (activeOnly && !ACTIVE.equals(obj.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			Path filePath = Paths.get(Constant.REAL_PATH, FOLDER).resolve(obj.getFileName()).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			String lower = obj.getFileName().toLowerCase();
			String contentType = lower.endsWith(".doc") ? "application/msword"
					: "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + obj.getFileName() + "\"")
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		}
	}

	// ---------------------------------------------------------------- helpers

	private Long decryptId(String id) {
		try {
			String plain = EncryptionUtil.decrypt(id);
			if (plain == null) {
				plain = id;
			}
			return Long.parseLong(plain.trim());
		} catch (Exception e) {
			return null;
		}
	}

	private String validateFile(MultipartFile file, boolean required) {
		if (file == null || file.isEmpty()) {
			return required ? "Please select file." : null;
		}
		String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
		if (!(name.endsWith(".doc") || name.endsWith(".docx"))) {
			return "Only .docx or .doc file is allowed.";
		}
		if (file.getSize() > MAX_FILE_SIZE) {
			return "The size of file must be less than 4MB.";
		}
		return null;
	}

	private String validateVersion(String version) {
		if (version == null || version.trim().isEmpty()) {
			return "Please enter version.";
		}
		String v = version.trim();
		if (v.length() < 3 || v.length() > 7) {
			return "The length must be between 3 and 7 characters.";
		}
		if (!v.matches("^[0-9.]+$")) {
			return "Only numbers and dots are allowed.";
		}
		return null;
	}

	private Date parseDate(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			return sdf.parse(value.trim());
		} catch (Exception e) {
			return null;
		}
	}
}
