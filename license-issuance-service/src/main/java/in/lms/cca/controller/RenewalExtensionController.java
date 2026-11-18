package in.lms.cca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.GrantRenewalExtensionRequest;
import in.lms.cca.dto.RenewalExtensionCandidateDTO;
import in.lms.cca.dto.RenewalWindowDTO;
import in.lms.cca.dto.RenewalWindowExtensionDTO;
import in.lms.cca.service.impl.RenewalWindowServiceImpl;
import in.lms.cca.util.api.LicenseIssuanceServiceAPIs;

@RestController
@RequestMapping(LicenseIssuanceServiceAPIs.LICENSE_ISSUANCE_SERVICE_BASE_URL)
@CrossOrigin
public class RenewalExtensionController {

	@Autowired
	private RenewalWindowServiceImpl renewalWindowServ;

	@GetMapping(LicenseIssuanceServiceAPIs.GET_RENEWAL_WINDOW_BY_USERNAME)
	public ResponseEntity<?> renewalWindow(@RequestParam("id") String username) {
		try {
			RenewalWindowDTO window = renewalWindowServ.windowFor(username);
			return new ResponseEntity<>(window, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to read the renewal window.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(LicenseIssuanceServiceAPIs.GET_RENEWAL_EXTENSION_CANDIDATES)
	public ResponseEntity<?> candidates() {
		try {
			List<RenewalExtensionCandidateDTO> candidates = renewalWindowServ.candidates();
			return new ResponseEntity<>(candidates, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to list CAs who need a renewal extension.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(LicenseIssuanceServiceAPIs.GET_RENEWAL_EXTENSIONS)
	public ResponseEntity<?> extensions() {
		try {
			List<RenewalWindowExtensionDTO> extensions = renewalWindowServ.extensions();
			return new ResponseEntity<>(extensions, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to list renewal extensions.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(LicenseIssuanceServiceAPIs.GRANT_RENEWAL_EXTENSION)
	public ResponseEntity<?> grant(@RequestBody GrantRenewalExtensionRequest request) {
		try {
			RenewalWindowExtensionDTO granted = renewalWindowServ.grant(request);
			return new ResponseEntity<>(granted, HttpStatus.OK);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to grant the renewal extension.", HttpStatus.BAD_REQUEST);
		}
	}
}
