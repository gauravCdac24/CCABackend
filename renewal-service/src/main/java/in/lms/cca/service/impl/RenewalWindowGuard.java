package in.lms.cca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import in.lms.cca.dto.RenewalWindowView;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.service.ApplicationDocumentService;

@Component
public class RenewalWindowGuard {

	public static final String DELAY_DOCUMENT_TITLE = "Delay of Renewal Application";

	@Autowired
	private RenewalWindowClient renewalWindowClient;

	@Autowired
	private ApplicationDocumentService applicationDocumentService;

	public ResponseEntity<String> blockIfCannotApply(String username) {
		RenewalWindowView window = readWindow(username);
		if (window == null) {
			return unavailable();
		}
		if (!window.isCanApply()) {
			return new ResponseEntity<>(message(window), HttpStatus.BAD_REQUEST);
		}
		return null;
	}

	public ResponseEntity<String> blockIfCannotSubmit(String username, Long intentAppId) {
		RenewalWindowView window = readWindow(username);
		if (window == null) {
			return unavailable();
		}
		if (!window.isCanApply()) {
			return new ResponseEntity<>(message(window), HttpStatus.BAD_REQUEST);
		}
		if (window.isDelayDocumentRequired() && !hasDelayDocument(intentAppId)) {
			return new ResponseEntity<>(
					"Upload the delay of renewal document before submitting the renewal application.",
					HttpStatus.BAD_REQUEST);
		}
		return null;
	}

	public boolean hasDelayDocument(Long intentAppId) {
		if (intentAppId == null) {
			return false;
		}
		List<ApplicationDocument> documents = applicationDocumentService.findIntentWithoutStatusAppById(intentAppId);
		if (documents == null) {
			return false;
		}
		for (ApplicationDocument document : documents) {
			if (isActiveDelayDocument(document)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isActiveDelayDocument(ApplicationDocument document) {
		return document != null
				&& document.getDocumentTitle() != null
				&& DELAY_DOCUMENT_TITLE.equalsIgnoreCase(document.getDocumentTitle().trim())
				&& document.getStatus() != null
				&& "Active".equalsIgnoreCase(document.getStatus());
	}

	private RenewalWindowView readWindow(String username) {
		try {
			return renewalWindowClient.window(username);
		} catch (Exception e) {
			System.err.println("Renewal window lookup failed: " + e.getMessage());
			return null;
		}
	}

	private ResponseEntity<String> unavailable() {
		return new ResponseEntity<>("Unable to verify the renewal window. Please try again.", HttpStatus.BAD_REQUEST);
	}

	private String message(RenewalWindowView window) {
		if (window.getMessage() != null && !window.getMessage().isBlank()) {
			return window.getMessage();
		}
		return "This CA cannot apply for renewal.";
	}
}
