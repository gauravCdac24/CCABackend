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

import in.lms.cca.entity.CSRDocument;
import in.lms.cca.service.ICSRDocumentService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CSRDocServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class CSRDocumentController {

	private static final String CSR_DOCUMENT_FOLDER = "csr/Document";

	@Autowired
	private ICSRDocumentService csrDocServ;

	private Path getCsrDocumentDirectory() {
		return Paths.get(Constant.REAL_PATH, CSR_DOCUMENT_FOLDER.split("/"));
	}

	private Optional<Path> resolveCsrFilePath(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return Optional.empty();
		}
		Path[] candidates = new Path[] {
				getCsrDocumentDirectory().resolve(fileName),
				Paths.get(Constant.REAL_PATH, "webapp", "csr", "Document", fileName),
				Paths.get(Constant.REAL_PATH + "//webapp//csr//Document").resolve(fileName)
		};
		for (Path candidate : candidates) {
			Path normalized = candidate.normalize();
			if (Files.exists(normalized)) {
				return Optional.of(normalized);
			}
		}
		return Optional.empty();
	}

	private Long decryptCsrDocId(String encryptedId) {
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

	private String saveCsrDocumentFile(MultipartFile file) throws IOException {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		Path directoryPath = getCsrDocumentDirectory();
		Files.createDirectories(directoryPath);
		SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
		String timestamp = dateFormats.format(new Date());
		String filename = "CSR" + timestamp + "." + extension;
		Files.write(directoryPath.resolve(filename), file.getBytes());
		return filename;
	}

	private void cleanupSavedCsrFile(String filename) {
		if (filename == null || filename.isBlank()) {
			return;
		}
		try {
			Path filePath = getCsrDocumentDirectory().resolve(filename);
			Files.deleteIfExists(filePath);
		} catch (Exception cleanupEx) {
			cleanupEx.printStackTrace();
		}
	}

	@PostMapping(CSRDocServiceAPIs.ADD_CSR_DOCUMENT)
	public ResponseEntity<?> addNewCSRDoc(
			@RequestParam("file") MultipartFile file,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("version") String version) {

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

		if (file == null || file.isEmpty()) {
			return new ResponseEntity<>("Please enter file name.", HttpStatus.BAD_REQUEST);
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
			filename = saveCsrDocumentFile(file);

			CSRDocument newCSRDocument = new CSRDocument();
			newCSRDocument.setFileName(filename);
			newCSRDocument.setPublishDate(publishDateParsed);
			newCSRDocument.setStatus("Inactive");
			newCSRDocument.setVersion(version);

			Optional<CSRDocument> saved = csrDocServ.addCSRDocument(newCSRDocument);
			if (saved.isEmpty()) {
				cleanupSavedCsrFile(filename);
				return new ResponseEntity<>("An error occurred while saving the CSR Document. Please try again.",
						HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>("CSR Document successfully added.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			cleanupSavedCsrFile(filename);
			return new ResponseEntity<>("An error occurred while saving the CSR Document. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(CSRDocServiceAPIs.UPDATE_CSR_DOCUMENT)
	public ResponseEntity<?> updateCSRDoc(
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("version") String version,
			@RequestParam("csrDocId") String csrDocId,
			@RequestParam("created") String created) {

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

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date publishDateParsed;
		try {
			publishDateParsed = dateFormat.parse(publishDate);
		} catch (ParseException e) {
			return new ResponseEntity<>("Invalid publish date format.", HttpStatus.BAD_REQUEST);
		}

		String filename = null;
		try {
			Long docId = decryptCsrDocId(csrDocId);
			if (docId == null) {
				return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
			}
			CSRDocument existing = csrDocServ.getCSRDocById(docId);
			if (existing == null) {
				return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
			}

			CSRDocument newCSRDocument = new CSRDocument();
			Date now = new Date();
			newCSRDocument.setCsrDocId(docId);
			if (file != null && !file.isEmpty()) {
				filename = saveCsrDocumentFile(file);
				newCSRDocument.setFileName(filename);
			} else {
				newCSRDocument.setFileName(existing.getFileName());
			}
			newCSRDocument.setVersion(version);
			newCSRDocument.setPublishDate(publishDateParsed);
			newCSRDocument.setStatus(existing.getStatus() != null ? existing.getStatus() : "Inactive");
			newCSRDocument.setUpdated(now);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(created);
			newCSRDocument.setCreated(cdate);

			Optional<CSRDocument> saved = csrDocServ.updateCSRDocument(newCSRDocument);
			if (saved.isEmpty()) {
				cleanupSavedCsrFile(filename);
				return new ResponseEntity<>("An error occurred while saving the CSR Document. Please try again.",
						HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>("CSR Document successfully updated.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			cleanupSavedCsrFile(filename);
			return new ResponseEntity<>("An error occurred while saving the CSR Document. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(CSRDocServiceAPIs.GET_ALL_CSR_DOCUMENT)
	public ResponseEntity<?> getAllCSRDocument() {
		List<CSRDocument> list = csrDocServ.getAllCSRDocument();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping(CSRDocServiceAPIs.GET_ALL_ACTIVE_CSR_DOCUMENT)
	public ResponseEntity<?> getAllActiveCSRDocument() {
		List<CSRDocument> list = csrDocServ.getAllActiveCSRDocument();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping(CSRDocServiceAPIs.GET_ALL_INACTIVE_CSR_DOCUMENT)
	public ResponseEntity<?> getAllInactiveCSRDocument() {
		List<CSRDocument> list = csrDocServ.getAllInactiveCSRDocument();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping(CSRDocServiceAPIs.CHANGE_CSRDOC_STATUS)
	public ResponseEntity<?> changeCSRDOCStatus(@RequestParam("id") String csrDocuId) {
		Long docId = decryptCsrDocId(csrDocuId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
		}

		try {
			CSRDocument c = csrDocServ.getCSRDocById(docId);
			if (c == null) {
				return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
			}

			String previousStatus = c.getStatus();
			if ("Active".equalsIgnoreCase(previousStatus)) {
				c.setStatus("Inactive");
			} else {
				List<CSRDocument> activeList = csrDocServ.getAllActiveCSRDocument();
				boolean anotherActiveExists = activeList.stream()
						.anyMatch(active -> active.getCsrDocId() != null
								&& !active.getCsrDocId().equals(docId));
				if (anotherActiveExists) {
					return new ResponseEntity<>("Active csr already present.", HttpStatus.BAD_REQUEST);
				}
				c.setStatus("Active");
			}

			Optional<CSRDocument> updated = csrDocServ.updateCSRDocument(c);
			if (updated.isEmpty()) {
				return new ResponseEntity<>("Unable to change CSR document status. Please try again.",
						HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(
					"CSR document status changed to " + updated.get().getStatus() + ".",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to change CSR document status. Please try again.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(CSRDocServiceAPIs.DOWNLOAD_FILE)
	public ResponseEntity<Resource> downloadFile(
			@RequestParam("id") String csrDocuId,
			@RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
		Long csrDocId = decryptCsrDocId(csrDocuId);
		if (csrDocId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		try {
			Optional<CSRDocument> csrDocumentOpt = csrDocServ.downloadfile(csrDocId);

			if (csrDocumentOpt.isPresent()) {
				CSRDocument c = csrDocumentOpt.get();
				if (activeOnly && !"Active".equalsIgnoreCase(c.getStatus())) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
				}
				Optional<Path> filePathOpt = resolveCsrFilePath(c.getFileName());
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
							.header("Content-Disposition",
									"attachment; filename=\"" + resource.getFilename() + "\"")
							.body(resource);
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping(CSRDocServiceAPIs.GET_CSR_DOCUMENT_BY_ID)
	public ResponseEntity<?> getCSRDocumentByID(@RequestParam("id") String csrDocId) {
		Long docId = decryptCsrDocId(csrDocId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
		}

		try {
			CSRDocument c = csrDocServ.getCSRDocById(docId);
			if (c == null) {
				return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(c, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(CSRDocServiceAPIs.DELETE_CSR_DOCUMENT_BY_ID)
	public ResponseEntity<?> deleteCSRById(@RequestParam("id") String csrDocId) {
		Long docId = decryptCsrDocId(csrDocId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid CSR Document Id.", HttpStatus.BAD_REQUEST);
		}

		try {
			boolean res = csrDocServ.deleteBycsrDocId(docId);
			if (res) {
				return new ResponseEntity<>("CSR Document is successfully deleted.", HttpStatus.OK);
			}
			return new ResponseEntity<>("Error in deleting the CSR Document.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in deleting the CSR Document.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
