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

import in.lms.cca.entity.AgreementTemplate;
import in.lms.cca.service.IAgreementTemplateService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AgreementTemplateAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AgreementTemplateController {

	private static final String TEMPLATE_FOLDER = "agreementtemplate/Document";

	@Autowired
	private IAgreementTemplateService templateService;

	private Path getTemplateDirectory() {
		return Paths.get(Constant.REAL_PATH, TEMPLATE_FOLDER.split("/"));
	}

	private Optional<Path> resolveTemplateFilePath(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return Optional.empty();
		}
		Path[] candidates = new Path[] {
				getTemplateDirectory().resolve(fileName),
				Paths.get(Constant.REAL_PATH, "webapp", "agreementtemplate", "Document", fileName),
				Paths.get(Constant.REAL_PATH + "//webapp//agreementtemplate//Document").resolve(fileName)
		};
		for (Path candidate : candidates) {
			Path normalized = candidate.normalize();
			if (Files.exists(normalized)) {
				return Optional.of(normalized);
			}
		}
		return Optional.empty();
	}

	private Long decryptTemplateId(String encryptedId) {
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

	private String saveTemplateFile(MultipartFile file) throws IOException {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		Path directoryPath = getTemplateDirectory();
		Files.createDirectories(directoryPath);
		SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
		String timestamp = dateFormats.format(new Date());
		String filename = "AGR" + timestamp + "." + extension;
		Files.write(directoryPath.resolve(filename), file.getBytes());
		return filename;
	}

	private void cleanupSavedFile(String filename) {
		if (filename == null || filename.isBlank()) {
			return;
		}
		try {
			Files.deleteIfExists(getTemplateDirectory().resolve(filename));
		} catch (Exception cleanupEx) {
			cleanupEx.printStackTrace();
		}
	}

	private ResponseEntity<?> validateUpload(String version, String publishDate, MultipartFile file) {
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
		return null;
	}

	@PostMapping(AgreementTemplateAPIs.ADD)
	public ResponseEntity<?> addTemplate(
			@RequestParam("file") MultipartFile file,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("version") String version) {

		ResponseEntity<?> validationError = validateUpload(version, publishDate, file);
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
			filename = saveTemplateFile(file);

			AgreementTemplate template = new AgreementTemplate();
			template.setFileName(filename);
			template.setPublishDate(publishDateParsed);
			template.setStatus("Inactive");
			template.setVersion(version);

			Optional<AgreementTemplate> saved = templateService.addTemplate(template);
			if (saved.isEmpty()) {
				cleanupSavedFile(filename);
				return new ResponseEntity<>("An error occurred while saving the agreement template. Please try again.",
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Agreement template successfully added.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			cleanupSavedFile(filename);
			return new ResponseEntity<>("An error occurred while saving the agreement template. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(AgreementTemplateAPIs.UPDATE)
	public ResponseEntity<?> updateTemplate(
			@RequestParam("file") MultipartFile file,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("version") String version,
			@RequestParam("templateId") String templateId,
			@RequestParam("created") String created) {

		ResponseEntity<?> validationError = validateUpload(version, publishDate, file);
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
			filename = saveTemplateFile(file);
			Long docId = decryptTemplateId(templateId);
			AgreementTemplate existing = docId == null ? null : templateService.getByTemplateId(docId);
			if (existing == null) {
				cleanupSavedFile(filename);
				return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
			}

			AgreementTemplate template = new AgreementTemplate();
			template.setTemplateId(docId);
			template.setFileName(filename);
			template.setVersion(version);
			template.setPublishDate(publishDateParsed);
			template.setStatus(existing.getStatus() != null ? existing.getStatus() : "Inactive");
			template.setUpdated(new Date());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			template.setCreated(sdf.parse(created));

			Optional<AgreementTemplate> saved = templateService.updateTemplate(template);
			if (saved.isEmpty()) {
				cleanupSavedFile(filename);
				return new ResponseEntity<>("An error occurred while saving the agreement template. Please try again.",
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Agreement template successfully updated.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			cleanupSavedFile(filename);
			return new ResponseEntity<>("An error occurred while saving the agreement template. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AgreementTemplateAPIs.GET_ALL)
	public ResponseEntity<?> getAllTemplates() {
		return new ResponseEntity<>(templateService.getAllTemplates(), HttpStatus.OK);
	}

	@GetMapping(AgreementTemplateAPIs.GET_ALL_ACTIVE)
	public ResponseEntity<?> getAllActiveTemplates() {
		return new ResponseEntity<>(templateService.getAllActiveTemplates(), HttpStatus.OK);
	}

	@GetMapping(AgreementTemplateAPIs.GET_ALL_INACTIVE)
	public ResponseEntity<?> getAllInactiveTemplates() {
		return new ResponseEntity<>(templateService.getAllInactiveTemplates(), HttpStatus.OK);
	}

	@GetMapping(AgreementTemplateAPIs.CHANGE_STATUS)
	public ResponseEntity<?> changeStatus(@RequestParam("id") String encryptedId) {
		Long docId = decryptTemplateId(encryptedId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
		}
		try {
			AgreementTemplate template = templateService.getByTemplateId(docId);
			if (template == null) {
				return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
			}

			if ("Active".equalsIgnoreCase(template.getStatus())) {
				template.setStatus("Inactive");
			} else {
				List<AgreementTemplate> activeList = templateService.getAllActiveTemplates();
				boolean anotherActiveExists = activeList.stream()
						.anyMatch(active -> active.getTemplateId() != null && !active.getTemplateId().equals(docId));
				if (anotherActiveExists) {
					return new ResponseEntity<>("Active agreement template already present.", HttpStatus.BAD_REQUEST);
				}
				template.setStatus("Active");
			}

			Optional<AgreementTemplate> updated = templateService.updateTemplate(template);
			if (updated.isEmpty()) {
				return new ResponseEntity<>("Unable to change agreement template status. Please try again.",
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Agreement template status changed to " + updated.get().getStatus() + ".",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to change agreement template status. Please try again.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(AgreementTemplateAPIs.DOWNLOAD)
	public ResponseEntity<Resource> downloadFile(
			@RequestParam("id") String encryptedId,
			@RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
		Long templateId = decryptTemplateId(encryptedId);
		if (templateId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		try {
			Optional<AgreementTemplate> templateOpt = templateService.downloadFile(templateId);
			if (templateOpt.isPresent()) {
				AgreementTemplate template = templateOpt.get();
				if (activeOnly && !"Active".equalsIgnoreCase(template.getStatus())) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
				}
				Optional<Path> filePathOpt = resolveTemplateFilePath(template.getFileName());
				if (filePathOpt.isEmpty()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
				Path filePath = filePathOpt.get();
				Resource resource = new UrlResource(filePath.toUri());
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					return ResponseEntity.ok()
							.contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
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

	@GetMapping(AgreementTemplateAPIs.GET_BY_ID)
	public ResponseEntity<?> getById(@RequestParam("id") String encryptedId) {
		Long docId = decryptTemplateId(encryptedId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
		}
		try {
			AgreementTemplate template = templateService.getByTemplateId(docId);
			if (template == null) {
				return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(template, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AgreementTemplateAPIs.DELETE_BY_ID)
	public ResponseEntity<?> deleteById(@RequestParam("id") String encryptedId) {
		Long docId = decryptTemplateId(encryptedId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid agreement template id.", HttpStatus.BAD_REQUEST);
		}
		try {
			boolean deleted = templateService.deleteByTemplateId(docId);
			if (deleted) {
				return new ResponseEntity<>("Agreement template is successfully deleted.", HttpStatus.OK);
			}
			return new ResponseEntity<>("Error in deleting the agreement template.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in deleting the agreement template.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
