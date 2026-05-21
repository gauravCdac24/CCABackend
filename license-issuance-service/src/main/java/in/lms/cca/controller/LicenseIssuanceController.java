package in.lms.cca.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.dto.StatusChangedOfLicense;
import in.lms.cca.entity.BankGuaranteeProof;
import in.lms.cca.entity.LicenseDetails;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.service.IBankGuaranteeProofService;
import in.lms.cca.service.ILicenseDetailsService;
import in.lms.cca.service.INewLicenseClientService;
import in.lms.cca.service.impl.NotificationClientServiceImpl;
import in.lms.cca.util.api.LicenseIssuanceServiceAPIs;
import in.lms.cca.util.golbal.DocumentFileUtil;
import in.lms.cca.util.golbal.EncryptionUtil;
import in.lms.cca.util.golbal.RealPath;

@RestController
@RequestMapping(LicenseIssuanceServiceAPIs.LICENSE_ISSUANCE_SERVICE_BASE_URL)
@CrossOrigin
public class LicenseIssuanceController {

	@Autowired
	private INewLicenseClientService newlicenseClientServ;
	
	@Autowired
	private NotificationClientServiceImpl notificationServ;
	
	@Autowired
	private IBankGuaranteeProofService bankProofServ;
	
	@Autowired
	private ILicenseDetailsService licenseDetailsServ;
	
	@GetMapping(LicenseIssuanceServiceAPIs.GRANT_IN_PRINCIPLE_APPROVAL)
	public ResponseEntity<?> getAllRecommendedNewApplication(
								@RequestParam("id")String intentAppId,
								@RequestParam("qid")String approvedBy,
								@RequestParam("pid")String applicantUsername){
		
		String id = intentAppId;
		String appBy = approvedBy;
		String ausername = applicantUsername;
		
		if(EncryptionUtil.decrypt(id) != null)
			id = EncryptionUtil.decrypt(id);
		
		if(EncryptionUtil.decrypt(appBy) != null)
			appBy = EncryptionUtil.decrypt(appBy);
		
		if(EncryptionUtil.decrypt(ausername) != null)
			ausername = EncryptionUtil.decrypt(ausername);
		
		String status = "IN_PRINCIPLE_APPROVAL_GRANTED";
		
		try {
			
			newlicenseClientServ.changeIntentApplicationStatus(id, status, appBy);
			
			
			//Notification to Applicant
			
			String title = "CCA: In-Principle Approval Granted";
			String message = "Dear Applicant, In-Principle Approval has been granted after reviewing your application. Please log in to the portal to submit the bank guarantee, CSR, and agreement.";
			
			NotificationsRequest notification = new NotificationsRequest();
			
			notification.setUsername(ausername);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("All");
			notification.setRole("ROLE_APPLICANT");
			notification.setNotificationDescription("In-Principle Approval Granted");
			
			notificationServ.sendNotification(notification);
			
			
			
			return new ResponseEntity<>("In-principle approval has been granted successfully.", HttpStatus.OK); 
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in granting In-Principle Approval.", HttpStatus.BAD_REQUEST); 
		}
		
		
		
	}
	
	
	@PostMapping(LicenseIssuanceServiceAPIs.UPLOAD_BANK_GUARANTEE_PROOF)
	public ResponseEntity<?> uploadBankGuaranteeProof(
			@RequestParam(value = "intentAppId", required = true) String intentAppId,
			@RequestParam(value = "uploadedBy", required = true) String uploadedBy,
	        @RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "issueDate", required = true) String issueDate,
			@RequestParam(value = "expiryDate", required = true) String expiryDate,
			@RequestParam(value = "transactionNumber", required = true) String transactionNumber
			
	        ){
	        
		
		Date idate = null;
		Date edate = null;
		
		 // Server Side Validation
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            idate = dateFormat.parse(issueDate);
            edate= dateFormat.parse(expiryDate);   
        }catch (ParseException e) {
            return new ResponseEntity<>("Error parsing dates", HttpStatus.BAD_REQUEST);
        }
        
        

	    if (transactionNumber == null || transactionNumber.isEmpty()) {
	        return new ResponseEntity<>("Please enter transaction number.", HttpStatus.BAD_REQUEST);
	    } else if (!transactionNumber.matches("^[A-Za-z0-9]+$")) {
	        return new ResponseEntity<>("Please enter valid transaction number.", HttpStatus.BAD_REQUEST);
	    } else if (transactionNumber.length() < 8 || transactionNumber.length() > 22) {
	        return new ResponseEntity<>("The length must be between 8 and 22 characters.", HttpStatus.BAD_REQUEST);
	    }
	    
	    if (idate == null) {
	        return new ResponseEntity<>("Please select issue date.", HttpStatus.BAD_REQUEST);
	    }
	    
	    if (edate == null) {
	        return new ResponseEntity<>("Please select expiry date.", HttpStatus.BAD_REQUEST);
	    }

	    if(edate.before(idate) || edate.compareTo(idate) == 0) {
	    	return new ResponseEntity<>("Expiry date must be greater than Issue Date.", HttpStatus.BAD_REQUEST);
	    }
	    
	    if(file == null || file.isEmpty()) {
	    	return new ResponseEntity<>("Please upload Bank Guarantee Proof.", HttpStatus.BAD_REQUEST);
	    }
	    
			try {
				
				String appId = intentAppId;
				if(EncryptionUtil.decrypt(appId) != null)
					appId = EncryptionUtil.decrypt(appId);
				
				String uby = uploadedBy;
				if(EncryptionUtil.decrypt(uby) == null)
					uby = EncryptionUtil.encrypt(uby);
				
				
				//Update Status
				newlicenseClientServ.changeIntentApplicationStatus(appId, "BANK_GUARANTEE_PROOF_UPLOADED", uby);

				
				//save the file
				
				String bankGuaranteeProofFileName = DocumentFileUtil.saveFile(file, "BankGuaranteeProof", appId, "BankGuaranteeProof/Document");
				
				BankGuaranteeProof proof = new BankGuaranteeProof();
				
				proof.setConfirmDoc(bankGuaranteeProofFileName);
				proof.setCreatedBy(uby);
				proof.setExpiryDate(edate);
				proof.setIssueDate(idate);
				proof.setIntentAppId(EncryptionUtil.encrypt(appId));
				proof.setStatus("Active");
				proof.setTransactionNumber(transactionNumber);
				proof.setUpdatedBy(uby);

				bankProofServ.addBankGuaranteeProof(proof);
				
								
				return new ResponseEntity<>("You have successfully uploaded the bank guarantee proof.", HttpStatus.OK);
				
			}catch(Exception e) {
				
				e.printStackTrace();
				return new ResponseEntity<>("An error occurred while uploading the documents. Please try again later.", HttpStatus.BAD_REQUEST);
				
			}
	}
	
	
	
	@PostMapping(LicenseIssuanceServiceAPIs.ISSUE_LICENSE_TO_APPLICANT)
	public ResponseEntity<?> issueLicenseToApplicant(
			@RequestParam(value = "intentAppId", required = true) String intentAppId,
			@RequestParam(value = "issuedBy", required = true) String issuedBy,
	        @RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "issueDate", required = true) String issueDate,
			@RequestParam(value = "expiryDate", required = true) String expiryDate,
			@RequestParam(value = "serialNo", required = true) String serialNo,
			@RequestParam(value = "applicantUsername", required = true) String applicantUsername
	        ){
	    
		
		
		 // Server Side Validation
		
		Date idate = null;
		Date edate = null;
		
		 // Server Side Validation
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            idate = dateFormat.parse(issueDate);
            edate= dateFormat.parse(expiryDate);   
        }catch (ParseException e) {
            return new ResponseEntity<>("Error parsing dates", HttpStatus.BAD_REQUEST);
        }

	    if (serialNo == null || serialNo.isEmpty()) {
	        return new ResponseEntity<>("Please enter transaction number.", HttpStatus.BAD_REQUEST);
	    } else if (!serialNo.matches("^[A-Za-z0-9]+$")) {
	        return new ResponseEntity<>("Please enter valid serial number.", HttpStatus.BAD_REQUEST);
	    } else if (serialNo.length() != 24) {
	        return new ResponseEntity<>("Serial Number length must be 24 characters.", HttpStatus.BAD_REQUEST);
	    }
	    
	    if (idate == null) {
	        return new ResponseEntity<>("Please select issue date.", HttpStatus.BAD_REQUEST);
	    }
	    
	    if (edate == null) {
	        return new ResponseEntity<>("Please select expiry date.", HttpStatus.BAD_REQUEST);
	    }

	    if(edate.before(idate) || edate.compareTo(idate) == 0) {
	    	return new ResponseEntity<>("Expiry date must be greater than Issue Date.", HttpStatus.BAD_REQUEST);
	    }

	    if (file == null || file.isEmpty()) {
	    	return new ResponseEntity<>("Please upload license certificate.", HttpStatus.BAD_REQUEST);
	    }

	    if (applicantUsername == null || applicantUsername.isBlank()) {
	    	return new ResponseEntity<>("Applicant username is required.", HttpStatus.BAD_REQUEST);
	    }
		
	
			try {
				
				String appId = resolvePlainParam(intentAppId);
				if (appId == null || appId.isBlank()) {
					return new ResponseEntity<>("Invalid intent application id.", HttpStatus.BAD_REQUEST);
				}

				String uby = resolveEncryptedUserRef(issuedBy);
				if (uby == null || uby.isBlank()) {
					return new ResponseEntity<>("Invalid issued-by user reference.", HttpStatus.BAD_REQUEST);
				}

				String ausername = resolvePlainParam(applicantUsername);
				if (ausername == null || ausername.isBlank()) {
					return new ResponseEntity<>("Invalid applicant username.", HttpStatus.BAD_REQUEST);
				}
				
				
				//Inactive previous license if already active
				
				List<LicenseDetails> activeLicense = licenseDetailsServ.getActiveLicenseDetailsByUsername(ausername);
				if(activeLicense.size()>0) {
					LicenseDetails uplic = activeLicense.get(0);
					uplic.setStatus("Inactive");
					uplic.setUpdatedBy(uby);
					licenseDetailsServ.updateLicenseDetails(uplic);
				}
				
				
				List<LicenseDetails> inactiveLicense = licenseDetailsServ.getInactiveLicenseDetailsByUsername(ausername);
				
				String licStatus = "issued";
				
				if(inactiveLicense.size()>0)
					licStatus = "renewed";
				
				String licenseCertificate = DocumentFileUtil.saveFile(file, "LicenseCertificate", appId,
						"LicenseCertificate/Document");
				if (licenseCertificate == null || licenseCertificate.isBlank()) {
					return new ResponseEntity<>(
							"Failed to save license certificate file. Check server upload path and disk permissions.",
							HttpStatus.BAD_REQUEST);
				}
				licenseCertificate = DocumentFileUtil.truncateFileName(licenseCertificate, 255);

				LicenseDetails license = new LicenseDetails();
				
				String encryptedIntentAppId = EncryptionUtil.encrypt(appId);
				if (encryptedIntentAppId == null || encryptedIntentAppId.isBlank()) {
					return new ResponseEntity<>("Failed to encrypt intent application id for license storage.",
							HttpStatus.BAD_REQUEST);
				}

				license.setCreatedBy(uby);
				license.setExpiryDate(edate);
				license.setIntentAppId(encryptedIntentAppId);
				license.setIssueDate(idate);
				license.setLicenseCertificate(licenseCertificate);
				license.setSerialNo(serialNo);
				license.setStatus("Active");
				license.setUpdatedBy(uby);
				license.setUserName(ausername);
				license.setLicenseName(ausername);

				Optional<LicenseDetails> savedLicense = licenseDetailsServ.addLicenseDetails(license);
				if (savedLicense.isEmpty()) {
					return new ResponseEntity<>("Failed to save license details. Please verify database constraints and try again.",
							HttpStatus.BAD_REQUEST);
				}
				
				//Inactive review/payment records (non-fatal if none exist)
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
					System.err.println("change-applicant-role-to-licensee skipped for " + ausername + ": "
							+ ex.getMessage());
				}

				try {
					newlicenseClientServ.changeAnnexureDetails(ausername);
				} catch (Exception ex) {
					System.err.println("change-annexure-details skipped for " + ausername + ": " + ex.getMessage());
				}

				try {
					newlicenseClientServ.changeIntentApplicationStatus(appId, "LICENSE_ISSUED", uby);
				} catch (Exception ex) {
					ex.printStackTrace();
					return new ResponseEntity<>(
							"License saved but failed to update application status to LICENSE_ISSUED. "
									+ "Ensure newlicense-service is running. Details: " + ex.getMessage(),
							HttpStatus.BAD_REQUEST);
				}

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
					notificationServ.sendNotification(notification);
				} catch (Exception ex) {
					System.err.println("license-issued notification skipped for " + ausername + ": " + ex.getMessage());
				}

				return new ResponseEntity<>("You have successfully issued the license.", HttpStatus.OK);

			} catch (Exception e) {
				e.printStackTrace();
				String detail = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
				return new ResponseEntity<>(
						"An error occurred while issuing the license: " + detail,
						HttpStatus.BAD_REQUEST);
			}
	
	
	
	
	

	}
	
	
	@GetMapping(LicenseIssuanceServiceAPIs.GET_LICENSE_DETAILS_BY_USERNAME)
	public ResponseEntity<?> getLicenseDetailsByUsername(@RequestParam("id")String username)
	{
		
		
		try {
		
		String id = username;
		if(EncryptionUtil.decrypt(id) != null)
			id = EncryptionUtil.decrypt(id);
		
		List<LicenseDetails> li = licenseDetailsServ.getActiveLicenseDetailsByUsername(id);
		
		if(li.size()>0)
			return new ResponseEntity<>(li.get(0), HttpStatus.OK);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while getting the license details.", HttpStatus.BAD_REQUEST);
		}
				

	}
	
	
	
	
	@GetMapping(LicenseIssuanceServiceAPIs.VIEW_LICENSE_DOCUMENT)
	public ResponseEntity<?> viewLicenseDetailsDocument(@RequestParam("id") String licenseId)
	{
		try {
		
		String id = licenseId;
		if(EncryptionUtil.decrypt(id) != null)
			id = EncryptionUtil.decrypt(id);
		
			LicenseDetails li = licenseDetailsServ.getLicenseDetailsById(Long.parseLong(id));
	
		        if (li != null) {
		        	
	                Path filePath = Paths.get(RealPath.REAL_PATH + "//LicenseCertificate//Document").resolve(li.getLicenseCertificate()).normalize();
	                
	                Resource resource = new UrlResource(filePath.toUri());
	                
	                if (resource.exists()) {
	                    String contentType = Files.probeContentType(filePath);
	                   System.out.println(resource);
	                    return ResponseEntity.ok()
	                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
	                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
	                            .body(resource);
	                } else {
	                	
	                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	                }
	            } else {
	            	
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }	
				

	}
	
	
	// Get all active license
	
	@GetMapping(LicenseIssuanceServiceAPIs.GET_ALL_ACTIVE_LICENSE_DETAILS)
	public ResponseEntity<?> getAllActiveLicenseDetails(){
	
		
		try {
			List<LicenseDetails> list = licenseDetailsServ.getAllActiveLicenseDetails();
			return new ResponseEntity<>(list, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(LicenseIssuanceServiceAPIs.GET_ALL_INACTIVE_LICENSE_DETAILS)
	public ResponseEntity<?> getAllInactiveLicenseDetails(){
	
		
		try {
			List<LicenseDetails> list = licenseDetailsServ.getAllInactiveLicenseDetails();
			return new ResponseEntity<>(list, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(LicenseIssuanceServiceAPIs.GET_ALL_LICENSE_DETAILS)
	public ResponseEntity<?> getAllLicenseDetails(){
	
		
		try {
			List<LicenseDetails> list = licenseDetailsServ.getAllLicenseDetails();
			return new ResponseEntity<>(list, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	//Get all license details by username
	
	@GetMapping(LicenseIssuanceServiceAPIs.GET_ALL_LICENSE_DETAILS_BY_USERNAME)
	public ResponseEntity<?> getAllsLicenseDetailsByUsername(@RequestParam("id")String username)
	{
		
		try {
		
		String id = username;
		if(EncryptionUtil.decrypt(id) != null)
			id = EncryptionUtil.decrypt(id);
		
		List<LicenseDetails> li = licenseDetailsServ.getAllLicenseDetailsByUsername(username);
		
		return new ResponseEntity<>(li, HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
				

	}
	

	@GetMapping(LicenseIssuanceServiceAPIs.GET_LICENSE_DETAILS)
	public ResponseEntity<?> getLicenseDetails()
	{
		
		
		try {
		
		
		
		List<LicenseDetails> li = licenseDetailsServ.getActiveLicenseDetails();
		
		if(li.size()>0)
			return new ResponseEntity<>(li, HttpStatus.OK);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while getting the license details.", HttpStatus.BAD_REQUEST);
		}
				

	}

	@PostMapping(LicenseIssuanceServiceAPIs.CHANGE_STATUS_BY_LICENSEID)
	public ResponseEntity<?> getLicenseDetailsByUsername(@RequestBody StatusChangedOfLicense changedOfLicense) {
	    try {
	       
	        LicenseDetails details = new LicenseDetails();
	        details.setLicenseId(Long.valueOf(changedOfLicense.getLicenseId()));
	        details.setStatus(changedOfLicense.getStatus());
	        details.setUpdated(new Date());

	   
	        Optional<LicenseDetails> updatedDetails = licenseDetailsServ.updateLicenseDetails(details);

	     
	        if (updatedDetails.isPresent()) {
	            return ResponseEntity.ok(updatedDetails.get());
	        }

	     
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("License details not found for the provided license ID: " + changedOfLicense.getLicenseId());

	    } catch (Exception e) {
	    
	        e.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("An error occurred while updating the license details: " + e.getMessage());
	    }
	}

	
//	@GetMapping(LicenseIssuanceServiceAPIs.GET_ALL_LICENSE_DETAILS_BY_USERNAME)
//	public ResponseEntity<?> getLatestLicenseDetailsByUsername(@RequestParam("id")String username)
//	{
//		
//		try {
//		
//		String id = username;
//		if(EncryptionUtil.decrypt(id) != null)
//			id = EncryptionUtil.decrypt(id);
//		
//		List<LicenseDetails> li = licenseDetailsServ.getAllLicenseDetailsByUsername(username);
//		
//		return new ResponseEntity<>(li, HttpStatus.OK);
//		
//		}catch(Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.OK);
//		}
//				
//
//	}
	
	

	@GetMapping(LicenseIssuanceServiceAPIs.GET_RENEWAL_START_DATE_BY_USERNAME)
	public ResponseEntity<?> getRenewalStartDateByUsername(@RequestParam("id")String username)
	{
		
		System.out.println(username);
		
		try {
		
		String id = username;
		if(EncryptionUtil.decrypt(id) != null)
			id = EncryptionUtil.decrypt(id);
		System.out.println(id);
		List<LicenseDetails> li = licenseDetailsServ.getAllLicenseDetailsByUsername(id);
		
		System.out.println(li.size());
		
		LicenseDetails liDetails = null;
		
		Date expiryDate = null;
		
		
		
		if(li.size()>0) {
			liDetails = li.get(0);
			expiryDate = liDetails.getExpiryDate();
			
			if (expiryDate != null) {
		        // Convert Date to LocalDate
		        LocalDate localExpiryDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		        // Subtract 45 days
		        LocalDate newDate = localExpiryDate.minus(45, ChronoUnit.DAYS);

		        // Convert back to Date
		        expiryDate = Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		    }
		
		}
		
		
		
		return new ResponseEntity<>(EncryptionUtil.encrypt(expiryDate.toString()), HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
				

	}

	private String resolvePlainParam(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		String decrypted = EncryptionUtil.decrypt(value.trim());
		return decrypted != null ? decrypted.trim() : value.trim();
	}

	private String resolveEncryptedUserRef(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		String decrypted = EncryptionUtil.decrypt(value.trim());
		if (decrypted != null) {
			return EncryptionUtil.encrypt(decrypted.trim());
		}
		return EncryptionUtil.encrypt(value.trim());
	}

}
