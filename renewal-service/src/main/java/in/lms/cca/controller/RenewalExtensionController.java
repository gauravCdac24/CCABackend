package in.lms.cca.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.dto.GrantRenewalExtensionRequest;
import in.lms.cca.dto.RenewalWindowView;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.service.impl.RenewalWindowClient;
import in.lms.cca.service.impl.RenewalWindowGuard;
import in.lms.cca.util.api.RenewLicenseServiceAPIs;
import in.lms.cca.util.api.RenewalExtensionAPIs;
import in.lms.cca.util.global.Constant;

@RestController
@RequestMapping(RenewLicenseServiceAPIs.RENEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin
public class RenewalExtensionController {

	private static final long MAX_FILE_BYTES = 5L * 1024 * 1024;

	@Autowired
	private RenewalWindowClient renewalWindowClient;

	@Autowired
	private RenewalWindowGuard renewalWindowGuard;

	@Autowired
	private IIntentApplicationService intentAppServ;

	@Autowired
	private ApplicationDocumentService applicationDocumentService;

	@GetMapping(RenewalExtensionAPIs.RENEWAL_WINDOW_BY_USERNAME)
	public ResponseEntity<?> renewalWindow(@RequestParam("id") String username) {
		try {
			return new ResponseEntity<>(renewalWindowClient.window(username), HttpStatus.OK);
		} catch (HttpStatusCodeException ex) {
			return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to reach the license service.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(RenewalExtensionAPIs.RENEWAL_EXTENSION_CANDIDATES)
	public ResponseEntity<String> candidates() {
		return forwardGet("/renewal-extension-candidates");
	}

	@GetMapping(RenewalExtensionAPIs.RENEWAL_EXTENSIONS)
	public ResponseEntity<String> extensions() {
		return forwardGet("/renewal-extensions");
	}

	@PostMapping(RenewalExtensionAPIs.GRANT_RENEWAL_EXTENSION)
	public ResponseEntity<String> grant(@RequestBody GrantRenewalExtensionRequest request) {
		try {
			ResponseEntity<String> response = renewalWindowClient.post("/grant-renewal-extension", request);
			return json(response);
		} catch (HttpStatusCodeException ex) {
			return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to grant the renewal extension.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = RenewalExtensionAPIs.UPLOAD_DELAY_OF_RENEWAL_DOCUMENT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadDelayDocument(
			@RequestParam("userName") String userName,
			@RequestParam("file") MultipartFile file) {
		if (userName == null || userName.isBlank()) {
			return new ResponseEntity<>("Username is required.", HttpStatus.BAD_REQUEST);
		}
		if (file == null || file.isEmpty()) {
			return new ResponseEntity<>("Upload the delay of renewal document.", HttpStatus.BAD_REQUEST);
		}
		if (file.getSize() > MAX_FILE_BYTES) {
			return new ResponseEntity<>("The delay document must be 5 MB or smaller.", HttpStatus.BAD_REQUEST);
		}
		String extension = fileExtension(file.getOriginalFilename());
		if (!extension.equalsIgnoreCase(".pdf")
				&& !extension.equalsIgnoreCase(".jpg")
				&& !extension.equalsIgnoreCase(".jpeg")
				&& !extension.equalsIgnoreCase(".png")) {
			return new ResponseEntity<>("Upload the delay document as a PDF, JPG, or PNG file.", HttpStatus.BAD_REQUEST);
		}

		try {
			RenewalWindowView window = renewalWindowClient.window(userName);
			if (window == null || !window.isCanApply()) {
				String message = window != null && window.getMessage() != null
						? window.getMessage()
						: "This CA cannot apply for renewal.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (!window.isDelayDocumentRequired()) {
				return new ResponseEntity<>("A delay of renewal document is not required for this application.",
						HttpStatus.BAD_REQUEST);
			}

			IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			if (intentApp == null) {
				return new ResponseEntity<>("Start the renewal application before uploading the delay document.",
						HttpStatus.BAD_REQUEST);
			}

			List<ApplicationDocument> existing = applicationDocumentService
					.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
			if (existing != null) {
				for (ApplicationDocument document : existing) {
					if (RenewalWindowGuard.isActiveDelayDocument(document)) {
						document.setStatus("Inactive");
						applicationDocumentService.updateApplicationDocument(document);
					}
				}
			}

			String storedName = saveFile(file, extension);
			ApplicationDocument document = new ApplicationDocument();
			document.setDocumentTitle(RenewalWindowGuard.DELAY_DOCUMENT_TITLE);
			document.setFileName(storedName);
			document.setIntentAppId(intentApp);
			document.setStatus("Active");
			applicationDocumentService.addApplicationDocument(document);
			return new ResponseEntity<>("Delay of renewal document uploaded.", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to upload the delay of renewal document.", HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<String> forwardGet(String pathAndQuery) {
		try {
			return json(renewalWindowClient.get(pathAndQuery));
		} catch (HttpStatusCodeException ex) {
			return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to reach the license service.", HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<String> json(ResponseEntity<String> response) {
		return ResponseEntity.status(response.getStatusCode())
				.contentType(MediaType.APPLICATION_JSON)
				.body(response.getBody());
	}

	private String saveFile(MultipartFile file, String extension) throws Exception {
		String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String fileName = "DelayOfRenewal_" + stamp + extension.toLowerCase();
		Path directory = Paths.get(Constant.REAL_PATH, "AdditionalDocument", "Document");
		Files.createDirectories(directory);
		Path target = directory.resolve(fileName);
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
		}
		return fileName;
	}

	private String fileExtension(String originalFilename) {
		if (originalFilename == null || !originalFilename.contains(".")) {
			return "";
		}
		return originalFilename.substring(originalFilename.lastIndexOf('.'));
	}
}
