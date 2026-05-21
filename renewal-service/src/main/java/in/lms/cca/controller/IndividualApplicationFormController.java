package in.lms.cca.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.AppUndertakingDTO;
import in.lms.cca.dto.ApplicationDocumentDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.AppUndertakingEntity;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.BankDetails;
import in.lms.cca.entity.BankDraft;
import in.lms.cca.entity.CPSDocument;
import in.lms.cca.entity.IndivAdditionalDetails;
import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.IndivEmails;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.master.ApplicationTypeMaster;
import in.lms.cca.payload.AdditionalDetailsDTO;
import in.lms.cca.payload.EmailPayload;
import in.lms.cca.payload.IndividualStep1Payload;
import in.lms.cca.payload.LocationDetailsPayload;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.AppUndertakingService;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.BankDetailsService;
import in.lms.cca.service.BankDraftService;
import in.lms.cca.service.IIndividualApplicationFormService;
import in.lms.cca.service.IIndividualEmailsService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.service.IndivAdditionalDetailsService;
import in.lms.cca.util.api.IndividualApplicationFormAPIs;
import in.lms.cca.util.api.RenewLicenseServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(RenewLicenseServiceAPIs.RENEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin(CrossOrigins.Origins)
public class IndividualApplicationFormController {

	@Autowired
	private IIndividualApplicationFormService indAppFormServ;

	@Autowired
	private IIndividualEmailsService indEmailServ;

	@Autowired
	private IIntentApplicationService intentAppServ;
	
	@Autowired
	private AppLocationService appLocationServ;
	
	@Autowired
	private ApplicationDocumentService  applicationDocumentServiceServ;
	
	@Autowired
	private IndivAdditionalDetailsService indivAdditionalDetailsServiceServ;
	
	@Autowired
	private BankDetailsService bankDetailsSev;
	
	@Autowired
	private BankDraftService bankDraftServ;
	
	@Autowired
	private AppUndertakingService appUndertakingServ;
	
	

	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_APPLICATION_FORM)
	public ResponseEntity<?> addApplicationType(@RequestBody IndividualStep1Payload personalDetailsDTO) {
		
		
		
		if (personalDetailsDTO.getSalutation() == null || personalDetailsDTO.getSalutation().trim().isEmpty()) {
			return new ResponseEntity<>("Salutation is required.", HttpStatus.BAD_REQUEST);
		}

		// First Name validation
		if (personalDetailsDTO.getFirstName() == null || personalDetailsDTO.getFirstName().trim().isEmpty()) {
			return new ResponseEntity<>("First Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}else if (personalDetailsDTO.getFirstName().length() < 3 || personalDetailsDTO.getFirstName().length() > 30) {
			return new ResponseEntity<>("The length of First Name must be between 3 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}else if (!personalDetailsDTO.getFirstName().matches("^[A-Za-z]+$")) {
			return new ResponseEntity<>("Please enter a valid First Name.", HttpStatus.BAD_REQUEST);
		}

		// Middle Name validation (optional)

		if(personalDetailsDTO.getMiddleName() == null || personalDetailsDTO.getMiddleName().trim().isEmpty()) {
			
		}else if (personalDetailsDTO.getMiddleName().length() <3 || personalDetailsDTO.getMiddleName().length() > 30) {
			return new ResponseEntity<>("The length of Middle Name must be between 3 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}else if (!personalDetailsDTO.getMiddleName().matches("^[A-Za-z]+$")) {
			return new ResponseEntity<>("Please enter a valid Middle Name.", HttpStatus.BAD_REQUEST);
		}

		// Last Name validation
		if (personalDetailsDTO.getLastName() == null || personalDetailsDTO.getLastName().trim().isEmpty()) {
			return new ResponseEntity<>("Last Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}else if (personalDetailsDTO.getLastName().length() < 3 || personalDetailsDTO.getLastName().length() > 30) {
			return new ResponseEntity<>("The length of Last Name must be between 3 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}else if (!personalDetailsDTO.getLastName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Last Name.", HttpStatus.BAD_REQUEST);
		}

		// Father's First Name validation
		if (personalDetailsDTO.getFatherFirstName() == null
				|| personalDetailsDTO.getFatherFirstName().trim().isEmpty()) {
			return new ResponseEntity<>("Father's First Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		if (personalDetailsDTO.getFatherFirstName().length() < 1
				|| personalDetailsDTO.getFatherFirstName().length() > 30) {
			return new ResponseEntity<>("The length of Father's First Name must be between 1 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getFatherFirstName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Father's First Name.", HttpStatus.BAD_REQUEST);
		}

		// Father's Middle Name validation (optional)
		if (personalDetailsDTO.getFatherMiddleName() != null && !personalDetailsDTO.getFatherMiddleName().isEmpty()) {
			if (personalDetailsDTO.getFatherMiddleName().length() > 30) {
				return new ResponseEntity<>("The length of Father's Middle Name must be between 0 and 30 characters.",
						HttpStatus.BAD_REQUEST);
			}
			if (!personalDetailsDTO.getFatherMiddleName().matches("^[A-Za-z ]+$")) {
				return new ResponseEntity<>("Please enter a valid Father's Middle Name.", HttpStatus.BAD_REQUEST);
			}
		}

		// Father's Last Name validation
		if (personalDetailsDTO.getFatherLastName() == null || personalDetailsDTO.getFatherLastName().trim().isEmpty()) {
			return new ResponseEntity<>("Father's Last Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		if (personalDetailsDTO.getFatherLastName().length() < 1
				|| personalDetailsDTO.getFatherLastName().length() > 30) {
			return new ResponseEntity<>("The length of Father's Last Name must be between 1 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getFatherLastName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Father's Last Name.", HttpStatus.BAD_REQUEST);
		}

		// Other First Name validation
		if (personalDetailsDTO.isKnownByOtherName() == true) {

		if (personalDetailsDTO.getOtherFirstName().length() > 30) {
			return new ResponseEntity<>("The length of Other First Name must be between 0 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getOtherFirstName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Other First Name.", HttpStatus.BAD_REQUEST);
		}

		// Other Middle Name validation (optional)

		if (personalDetailsDTO.getOtherMiddleName().length() > 30) {
			return new ResponseEntity<>("The length of Other Middle Name must be between 0 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getOtherMiddleName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Other Middle Name.", HttpStatus.BAD_REQUEST);
		}

		// Other Last Name validation

		if (personalDetailsDTO.getOtherLastName().length() > 30) {
			return new ResponseEntity<>("The length of Other Last Name must be between 0 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getOtherLastName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Other Last Name.", HttpStatus.BAD_REQUEST);
		}
		}
		// Date of Birth validation
		if (personalDetailsDTO.getDob() == null) {
			return new ResponseEntity<>("Date of Birth is required.", HttpStatus.BAD_REQUEST);
		}

		// Gender validation
		if (personalDetailsDTO.getGender() == null || personalDetailsDTO.getGender().trim().isEmpty()) {
			return new ResponseEntity<>("Gender is required.", HttpStatus.BAD_REQUEST);
		}

		// Nationality validation
		if (personalDetailsDTO.getNationality() == null || personalDetailsDTO.getNationality().trim().isEmpty()) {
			return new ResponseEntity<>("Nationality cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		if (personalDetailsDTO.getNationality().length() < 1 || personalDetailsDTO.getNationality().length() > 50) {
			return new ResponseEntity<>("The length of Nationality must be between 1 and 50 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getNationality().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Nationality.", HttpStatus.BAD_REQUEST);
		}

		// Email validation (as list of email IDs)
		if (personalDetailsDTO.getEmailId() == null || personalDetailsDTO.getEmailId().isEmpty()) {
			return new ResponseEntity<>("Email Address cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		for (EmailPayload email : personalDetailsDTO.getEmailId()) {
			if (email.getEmailId() == null || email.getEmailId().trim().isEmpty()) {
				return new ResponseEntity<>("Email Address cannot be empty.", HttpStatus.BAD_REQUEST);
			}
			if (!email.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
				return new ResponseEntity<>("Please enter a valid Email Address.", HttpStatus.BAD_REQUEST);
			}
		}


		try {
			
			//Intent Application Created During First Step
			IntentApplication intentApp = new IntentApplication();
			intentApp.setAppTypeMasterId(personalDetailsDTO.getAppTypeMasterId());
			intentApp.setUserName(personalDetailsDTO.getUserName());
			intentApp.setApplicationStatus("PENDING-FOR-RENEWAL");
			Optional<IntentApplication> intentApplication = intentAppServ.addIntentApp(intentApp);
			
			
			//Individual Application First Step
			IndivApplication indivApplication = new IndivApplication();
			indivApplication.setSalutation1(personalDetailsDTO.getSalutation());
			indivApplication.setFirstName1(personalDetailsDTO.getFirstName());
			indivApplication.setMiddleName1(personalDetailsDTO.getMiddleName());
			indivApplication.setLastName1(personalDetailsDTO.getLastName());
			indivApplication.setFirstName2(personalDetailsDTO.getOtherFirstName());
			indivApplication.setMiddleName2(personalDetailsDTO.getOtherMiddleName());
			indivApplication.setLastName2(personalDetailsDTO.getOtherLastName());
			indivApplication.setFsalutation(personalDetailsDTO.getFathersSalutation());
			indivApplication.setFfirstName(personalDetailsDTO.getFatherFirstName());
			indivApplication.setFmiddleName(personalDetailsDTO.getFatherMiddleName());
			indivApplication.setFlastName(personalDetailsDTO.getFatherLastName());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			try {
				Date Dob = dateFormat.parse(personalDetailsDTO.getDob());

				indivApplication.setDob(Dob);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			indivApplication.setGender(personalDetailsDTO.getGender());

			indivApplication.setNationality(personalDetailsDTO.getNationality());

			if (personalDetailsDTO.isKnownByOtherName() == true) {
				indivApplication.setFirstName2(personalDetailsDTO.getOtherFirstName());
				indivApplication.setMiddleName2(personalDetailsDTO.getOtherMiddleName());
				indivApplication.setLastName2(personalDetailsDTO.getOtherLastName());
			}
			
			indivApplication.setStatus("Active");
			 IntentApplication intentApps=intentAppServ.findIntentAppById(personalDetailsDTO.getUserName());
			 indivApplication.setIntentAppId(intentApps);
			
			Optional<IndivApplication> result = indAppFormServ.addIndivApplication(indivApplication);

			if (personalDetailsDTO.getEmailId() != null && !personalDetailsDTO.getEmailId().isEmpty()) {

				for (EmailPayload emailPayload : personalDetailsDTO.getEmailId()) {

					IndivEmails indivEmail = new IndivEmails();
					indivEmail.setEmailId(emailPayload.getEmailId());
					indivEmail.setIntentAppId(intentApplication.get());
					indivEmail.setStatus("Active");
					indivEmail.setCreatedBy(personalDetailsDTO.getCreatedBy());
					indivEmail.setCreatedBy(personalDetailsDTO.getUpdatedBy());
					indEmailServ.addIndivEmails(indivEmail);

				}
			}

			if (result.isEmpty()) {
				return new ResponseEntity<>("Error occurred while saving First Step.",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result.get(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the First Step.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_APPLICATIONS_FORM2)
	public ResponseEntity<?> addApplication2(@RequestBody IndivAddressDTO indivAddressDTO) {
	    System.out.println(indivAddressDTO.getResidential().getBlockNo() + " ==== ");

	    try {
	        // Add Residential Address
	        Long resAddId = intentAppServ.addUsers(indivAddressDTO.getResidential());
	        
	        // Add Official Address
	        Long offAddId = intentAppServ.addUsers(indivAddressDTO.getOfficial());
	        
	        // Create Residential AppLocation
	        AppLocation resLocation = new AppLocation();
	        resLocation.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId))); 
	        resLocation.setLocationName("Residential");
	        IntentApplication intentApp = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	        resLocation.setIntentAppId(intentApp); 
	        resLocation.setStatus("Active");
	        resLocation.setCreatedBy(indivAddressDTO.getCreatedBy());
	        resLocation.setUpdatedBy(indivAddressDTO.getUpdaedBy());
	        
	        Optional<AppLocation> resLoc = appLocationServ.addAppLocation(resLocation);

	        // Create Official AppLocation
	        AppLocation offLocation = new AppLocation();
	        offLocation.setAddressId(EncryptionUtil.encrypt(String.valueOf(offAddId))); 
	        offLocation.setLocationName("Official");
	        IntentApplication intentApp1 = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	        offLocation.setIntentAppId(intentApp1); 
	        offLocation.setStatus("Active");
	        offLocation.setCreatedBy(indivAddressDTO.getCreatedBy());
	        offLocation.setUpdatedBy(indivAddressDTO.getUpdaedBy());
	        
	        Optional<AppLocation> offLoc = appLocationServ.addAppLocation(offLocation);

	        // Prepare Response Map
	        Map<String, Object> response = new HashMap<>();
	        response.put("Residential", resLoc);
	        response.put("Official", offLoc);
	        String status = "Edit_Upon_Review".equals(intentApp.getApplicationStatus()) ? "Inactive" : "Active";
	        // Find Intent Application and Individual Application
	        IntentApplication in = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	        IndivApplication inApp = indAppFormServ.findIntentAppById(in.getIntentAppId(),status);

	        // Set Communication Address based on selection
	        if (indivAddressDTO.getCommunicationAddress().equals("Residential")) {
	            // Set Residential address as communication address
	            if (resLoc.isPresent()) {
	                inApp.setCommunicationAddress(resLoc.get());
	            }
	        } else if (indivAddressDTO.getCommunicationAddress().equals("Official")) {
	            // Set Official address as communication address
	            if (offLoc.isPresent()) {
	                inApp.setCommunicationAddress(offLoc.get());
	            }
	        }
	        indAppFormServ.addIndivApplication(inApp);
	        // Return Response
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Failed to create user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	
	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_APPLICATIONS_FORM3)
	public ResponseEntity<?> addApplication3(
	        @RequestParam("passportName") String passportName,
	        @RequestParam("passportNo") String passportNo,
	        @RequestParam("passportIssuingAuthority") String passportIssuingAuthority,
	        @RequestParam("passportExpiryDate") String passportExpiryDate,
	        @RequestParam(value="file1",required = false) MultipartFile file1, // Passport file

	        @RequestParam("voterCardName") String voterCardName,
	        @RequestParam("voterIdNo") String voterIdNo,
	        @RequestParam(value="file2",required = false) MultipartFile file2, // Voter card file

	        @RequestParam("panCardName") String panCardName,
	        @RequestParam("incomeTaxPanNo") String incomeTaxPanNo,
	        @RequestParam(value="file3",required = false) MultipartFile file3, // PAN card file

	        @RequestParam("capitalName") String capitalName,
	        @RequestParam("capital") String capital,
	        @RequestParam("file4") MultipartFile file4, // Capital proof file

	        @RequestParam("creditcardtype") String creditcardtype,
	        @RequestParam("creditCardNo") String creditCardNo,
	        @RequestParam("issuedBy") String issuedBy,
	        @RequestParam("ispName") String ispName,
	        @RequestParam("ispWebsiteAddress") String ispWebsiteAddress,
	        @RequestParam("ispUserName") String ispUserName,
	        @RequestParam("webPageURL") String webPageURL,
	        @RequestParam("useName")String useName
	) {
	    try {
	     
	    	String panCardDocument="panCardDocument";
	    	String idCardDocument="idCardDocument";
	    	String passportDocuments="passportDocument";
	    	String capitalDocuments="capitalDocument";
	    	
	        IndivAdditionalDetails indivAdditionalDetails = new IndivAdditionalDetails();
	        indivAdditionalDetails.setCreditCardType(creditcardtype);
	        indivAdditionalDetails.setCreditCardNo(creditCardNo);
	        indivAdditionalDetails.setCreditCardIssuedBy(issuedBy);
	        indivAdditionalDetails.setWebURL(webPageURL);
	        indivAdditionalDetails.setPassportNo(passportNo);
	        indivAdditionalDetails.setPassportIssuingAuthority(passportIssuingAuthority);

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date publishDateParsed;
	        try {
	            publishDateParsed = dateFormat.parse(passportExpiryDate);
	        } catch (ParseException e) {
	            return new ResponseEntity<>("Invalid  date format.", HttpStatus.BAD_REQUEST);
	        }
	        indivAdditionalDetails.setPassportExpiryDate(publishDateParsed);
	        indivAdditionalDetails.setVoterId(voterIdNo);
	        indivAdditionalDetails.setPan(incomeTaxPanNo);
	        indivAdditionalDetails.setIspName(ispName);
	        indivAdditionalDetails.setIspWebsite(ispWebsiteAddress);
	        indivAdditionalDetails.setIspUsername(ispUserName);
	        indivAdditionalDetails.setPersonalWebPage(webPageURL);
	        indivAdditionalDetails.setIndivCapital(capital);
	        
	        IntentApplication intentApp=intentAppServ.findIntentAppById(useName);
	        indivAdditionalDetails.setIntentAppId(intentApp);
	        indivAdditionalDetails.setStatus("Active");
	        indivAdditionalDetailsServiceServ.addIndivAdditionalDetails(indivAdditionalDetails);  // Assuming a service to save it

	        // Date formatter for current date
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	        String currentDate = LocalDateTime.now().format(formatter);

	     // Handle passport document
	        if (file1 != null && !file1.isEmpty()) {
	            ApplicationDocument passportDocument = new ApplicationDocument();
	            passportDocument.setDocumentTitle(passportDocuments);

	            String passportFileName = saveFile(file1, passportDocuments, currentDate);
	            passportDocument.setFileName(passportFileName);  // e.g., passportName_20230908_153012

	            passportDocument.setIntentAppId(intentApp); 
	            passportDocument.setStatus("ACTIVE");
	            System.out.println(passportName);
	            applicationDocumentServiceServ.addApplicationDocument(passportDocument);
	        }

	        // Handle voter card document
	        if (file2 != null && !file2.isEmpty()) {
	            ApplicationDocument voterDocument = new ApplicationDocument();
	            voterDocument.setDocumentTitle(idCardDocument);

	            String voterFileName = saveFile(file2, idCardDocument, currentDate);
	            voterDocument.setFileName(voterFileName);  // e.g., voterCardName_20230908_153012

	            voterDocument.setIntentAppId(intentApp); 
	            voterDocument.setStatus("ACTIVE");
	            applicationDocumentServiceServ.addApplicationDocument(voterDocument);
	        }

	        // Handle PAN card document
	        if (file3 != null && !file3.isEmpty()) {
	            ApplicationDocument panDocument = new ApplicationDocument();
	            panDocument.setDocumentTitle(panCardDocument);

	            String panFileName = saveFile(file3, panCardDocument, currentDate);
	            panDocument.setFileName(panFileName);  // e.g., panCardName_20230908_153012

	            panDocument.setIntentAppId(intentApp); 
	            panDocument.setStatus("ACTIVE");
	            applicationDocumentServiceServ.addApplicationDocument(panDocument);
	        }
	        // Handle capital proof document
	        ApplicationDocument capitalDocument = new ApplicationDocument();
	        capitalDocument.setDocumentTitle(capitalDocuments);
	        if (!file4.isEmpty()) {
	            String capitalFileName = saveFile(file4, capitalDocuments, currentDate);
	            capitalDocument.setFileName(capitalFileName);  // e.g., capitalName_20230908_153012
	        }
	        capitalDocument.setIntentAppId(intentApp); 
	        capitalDocument.setStatus("ACTIVE");
	        applicationDocumentServiceServ.addApplicationDocument(capitalDocument);
	      
	        // Save all documents to the database
	        
	    
	      
	     

	        return new ResponseEntity<>("Form submitted successfully", HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Error occurred while processing the form", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	// Helper method for saving files to a directory with a unique name
	private String saveFile(MultipartFile file, String documentName, String currentDate) throws IOException {
	    // Check if the file is null or empty
	    if (file == null || file.isEmpty()) {
	        return null; // Return null if no file is provided
	    }

	    String originalFileName = file.getOriginalFilename();
	    String fileExtension = "";

	    // Extract the file extension (if available)
	    if (originalFileName != null && originalFileName.contains(".")) {
	        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	    }

	    // Construct a unique file name using the document name and current date
	    String uniqueFileName = documentName + "_" + currentDate + fileExtension;

	    // Define the file path (replace with your actual upload path)
	    Path directoryPath = Paths.get(Constant.REAL_PATH, "AdditionalDocument", "Document");
	    Path filePath = directoryPath.resolve(uniqueFileName);

	    // Ensure the directory exists
	    Files.createDirectories(directoryPath);

	    // Copy the file to the specified location
	    try (InputStream inputStream = file.getInputStream()) {
	        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	    }

	    return uniqueFileName;
	}
	
	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_APPLICATIONS_FORM4)
	public ResponseEntity<?> addApplication4(@RequestBody AdditionalDetailsDTO additionalDetailsDTO) {

	    // BankDetails validation
	    if (additionalDetailsDTO.getBankDetails() == null) {
	        return new ResponseEntity<>("Bank details cannot be null.", HttpStatus.BAD_REQUEST);
	    }

	    AdditionalDetailsDTO.BankDetails bankDetailsDTO = additionalDetailsDTO.getBankDetails();

	    // Bank Account Number validation
	    if (bankDetailsDTO.getAccountNo() == null || bankDetailsDTO.getAccountNo().trim().isEmpty()) {
	        return new ResponseEntity<>("Bank Account Number cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	   
//	    if (!bankDetailsDTO.getAccountNo().matches("^[0-9]+$")) {
//	        return new ResponseEntity<>("Please enter a valid Bank Account Number.", HttpStatus.BAD_REQUEST);
//	    }

	    // Bank Name validation
	    if (bankDetailsDTO.getBankName() == null || bankDetailsDTO.getBankName().trim().isEmpty()) {
	        return new ResponseEntity<>("Bank Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (!bankDetailsDTO.getBankName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Bank Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Branch Name validation
	    if (bankDetailsDTO.getBranch() == null || bankDetailsDTO.getBranch().trim().isEmpty()) {
	        return new ResponseEntity<>("Branch Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (!bankDetailsDTO.getBranch().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Branch Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Process and save Bank Details
	    BankDetails bankDetails = new BankDetails();
	    bankDetails.setBankAccountNo(bankDetailsDTO.getAccountNo());
	    bankDetails.setBankAccountType(bankDetailsDTO.getAccountType());
	    bankDetails.setBankName(bankDetailsDTO.getBankName());
	    bankDetails.setBranchName(bankDetailsDTO.getBranch());
	    bankDetails.setStatus("Active");
	    IntentApplication intentApp=intentAppServ.findIntentAppById(additionalDetailsDTO.getUserName());
	    bankDetails.setIntentAppId(intentApp); // Assuming getIntentAppId() exists
      
	    Optional<BankDetails> bankDetailsSave = bankDetailsSev.addBankDetails(bankDetails);

	    if (!bankDetailsSave.isPresent()) {
	        return new ResponseEntity<>("Failed to save bank details.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    // If known by other name
	    if (additionalDetailsDTO.isKnownByOtherName()) {
	        // BankDraft validation
	        AdditionalDetailsDTO.BankDraft bankDraftDTO = additionalDetailsDTO.getBankDraft();
	        if (bankDraftDTO == null) {
	            return new ResponseEntity<>("Bank Draft details cannot be null.", HttpStatus.BAD_REQUEST);
	        }

	        // Draft Number validation
	        if (bankDraftDTO.getDraftNo() == null || bankDraftDTO.getDraftNo().trim().isEmpty()) {
	            return new ResponseEntity<>("Draft Number cannot be empty.", HttpStatus.BAD_REQUEST);
	        }
	        if (!bankDraftDTO.getDraftNo().matches("^[0-9]+$")) {
	            return new ResponseEntity<>("Please enter a valid Draft Number.", HttpStatus.BAD_REQUEST);
	        }

	        // Amount validation
	        try {
	            Integer amount = Integer.valueOf(bankDraftDTO.getAmount());
	            if (amount <= 0) {
	                return new ResponseEntity<>("Amount must be greater than zero.", HttpStatus.BAD_REQUEST);
	            }
	        } catch (NumberFormatException e) {
	            return new ResponseEntity<>("Invalid amount format.", HttpStatus.BAD_REQUEST);
	        }

	        // Bank Name validation
	        if (bankDraftDTO.getBankName() == null || bankDraftDTO.getBankName().trim().isEmpty()) {
	            return new ResponseEntity<>("Bank Name cannot be empty.", HttpStatus.BAD_REQUEST);
	        }
	        if (!bankDraftDTO.getBankName().matches("^[A-Za-z ]+$")) {
	            return new ResponseEntity<>("Please enter a valid Bank Name.", HttpStatus.BAD_REQUEST);
	        }

	        // Issue Date validation
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date issueDate;
	        try {
	             issueDate = dateFormat.parse(bankDraftDTO.getIssueDate());
	        } catch (ParseException e) {
	            return new ResponseEntity<>("Invalid date format. Please use yyyy-MM-dd.", HttpStatus.BAD_REQUEST);
	        }

	        // Save Bank Draft
	        BankDraft bankDraft = new BankDraft();
	        bankDraft.setDraftNo(bankDraftDTO.getDraftNo());
	        bankDraft.setAmount(Integer.valueOf(bankDraftDTO.getAmount()));
	        bankDraft.setBankName(bankDraftDTO.getBankName());
	        bankDraft.setIssueDate(issueDate);
	        bankDraft.setStatus("Active");
	        bankDraft.setIntentAppId(intentApp);
	        Optional<BankDraft> savedBankDraft = bankDraftServ.addBankDraft(bankDraft);

	        if (!savedBankDraft.isPresent()) {
	            return new ResponseEntity<>("Failed to save bank draft.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_APPLICATIONS_FORM5)
	public ResponseEntity<?> addApplication6(@RequestBody List<LocationDetailsPayload> locationDetails) {
	    System.out.println("Received payload: " + locationDetails.get(0).getIntentAppId() + " " + locationDetails.get(1).getIntentAppId());

	    try {
	        if (locationDetails == null || locationDetails.isEmpty()) {
	            return new ResponseEntity<>("Location details list cannot be null or empty", HttpStatus.BAD_REQUEST);
	        }

	        String userName = locationDetails.get(0).getUserName();
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);

	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found for user: " + userName, HttpStatus.NOT_FOUND);
	        }

	        // Retrieve active locations first
	        List<AppLocation> appLocationList = appLocationServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());

//	        // If no active locations found, check for inactive locations
//	        if (appLocationList.size()==0) {
//	            appLocationList = appLocationServ.findIntentAppById(intentApp.getIntentAppId(), "Inactive");
//	        }

	     // Iterate through the location details payload to process each one
	        for (LocationDetailsPayload locationDetail : locationDetails) {
	            if (locationDetail == null) {
	                continue; // Skip null location details if any
	            }

	            // Skip "Residential" and "Official" locations
	            if ("Residential".equalsIgnoreCase(locationDetail.getLocationType()) || 
	                "Official".equalsIgnoreCase(locationDetail.getLocationType())) {
	                continue; // Skip processing for these types
	            }

	            // Sort by the created date and process the list
	            List<AppLocation> matchingLocations = appLocationList.stream()
	                    .sorted(Comparator.comparing(AppLocation::getCreated)) // Sort by created date
	                    .collect(Collectors.toList());

	            System.out.println("Found matching locations: " + matchingLocations.size());

	            if (!matchingLocations.isEmpty()) {
	                // Iterate through the sorted locations
	                for (AppLocation appLocation : matchingLocations) {
	                    if ("Inactive".equals(appLocation.getStatus())) {
	                        
	                        
	                        
	                        Long commAddIds = intentAppServ.addUser(locationDetail);

	    	                AppLocation newAppLocation = new AppLocation();
	    	                newAppLocation.setAddressId(String.valueOf(commAddIds));
	    	                newAppLocation.setLocationName(locationDetail.getLocationType());
	    	                newAppLocation.setIntentAppId(intentApp);
	    	                newAppLocation.setStatus("Active");
	    	                Date currentDate = new Date();
	    	                newAppLocation.setCreated(currentDate);
	    	                newAppLocation.setUpdated(currentDate);

	    	                // Save the new location and check if the save was successful
	    	                Optional<AppLocation> result = appLocationServ.addAppLocation(newAppLocation);
	                        break; // Exit the loop once an update is performed
	                    } else {
	    	                // If no matching location is found, create a new AppLocation entry
	    	            	// Update the "Inactive" location
	                        Long commAddId = intentAppServ.updateUser(locationDetail);
	                        Date currentDate = new Date();

	                        appLocation.setLocationName(locationDetail.getLocationType());
	                        appLocation.setAddressId(String.valueOf(commAddId));
	                        appLocation.setStatus("Active"); // Activate the location
	                        appLocation.setCreated(currentDate);
	                        appLocation.setUpdated(currentDate);
	                        appLocation.setLocationId(Long.valueOf(locationDetail.getLocationId()));
	                        appLocation.setIntentAppId(intentApp);

	                        // Update the existing AppLocation instance
	                        appLocationServ.updateAppLocation(appLocation);

	                        System.out.println("Updated inactive location: " + appLocation.getLocationId());
	                        
	                }
	                }
	            }
	               
	        }
	        return new ResponseEntity<>("Locations processed successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Failed to process location details: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}




	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_APPLICATIONS_FORM6)
	public ResponseEntity<String> uploadFiles(
	        @RequestParam(value = "file", required = false) MultipartFile file,
	        @RequestParam("userName") String userName,
	        @RequestParam Map<String, MultipartFile> additionalFiles,
	        @RequestParam Map<String, MultipartFile> additionalsFiles,
	        @RequestParam Map<String, String> additionalFileMetadata, // Metadata for additional files
	        @RequestParam Map<String, String> additionalsFileMetadata) { // Metadata for additionals files
	    try {
	    	
	    	System.out.println("userName===>"+userName);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	        String currentDate = LocalDateTime.now().format(formatter);
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        // Handle the primary file
	        if (file != null && !file.isEmpty()) {
	            
	            String primaryFileName = saveFiles(file, "primaryFile", currentDate);
	            ApplicationDocument additionalDocument = new ApplicationDocument();
                additionalDocument.setFileName(primaryFileName);
                additionalDocument.setDocumentTitle("CPSDocument");
                additionalDocument.setIntentAppId(intentApp);
                additionalDocument.setStatus("Active");
                applicationDocumentServiceServ.addApplicationDocument(additionalDocument);
	        }

	        // Handle additional files
	        for (int i = 0; ; i++) {
	            String fileKey = "additionalFile[" + i + "].file";
	            String fileNameKey = "additionalFile[" + i + "].fileName";
	            String documentTitleKey = "additionalFile[" + i + "].documentTitleName";

	            MultipartFile additionalFile = additionalFiles.get(fileKey);
	            if (additionalFile == null || additionalFile.isEmpty()) {
	                break; // Exit loop if no more files are present
	            }

	            String fileName = additionalFileMetadata.get(fileNameKey) != null ? additionalFileMetadata.get(fileNameKey) : "";
	            String documentTitleName = additionalFileMetadata.get(documentTitleKey) != null ? additionalFileMetadata.get(documentTitleKey) : "";

	            System.out.println("Received additional file: " + additionalFile.getOriginalFilename());
	            String additionalFileName = saveFiles(additionalFile, "additionalFile_" + fileName, currentDate);
	            System.out.println("Additional file saved as: " + additionalFileName);
	            // Process metadata here (fileName, documentTitleName)
	            ApplicationDocument additionalDocument = new ApplicationDocument();
                additionalDocument.setFileName(additionalFileName);
                additionalDocument.setDocumentTitle(documentTitleName);
                additionalDocument.setIntentAppId(intentApp);
                additionalDocument.setStatus("Active");

                applicationDocumentServiceServ.addApplicationDocument(additionalDocument);
	        }

	        // Handle additional files from 'additionalsFile'
	        for (int i = 0; ; i++) {
	            String fileKey = "additionalsFile[" + i + "].file";
	            String fileNameKey = "additionalsFile[" + i + "].fileName";
	            String documentNameKey = "additionalsFile[" + i + "].DocumentName";

	            MultipartFile additionalsFile = additionalsFiles.get(fileKey);
	            if (additionalsFile == null || additionalsFile.isEmpty()) {
	                break; // Exit loop if no more files are present
	            }

	            String fileName = additionalsFileMetadata.get(fileNameKey) != null ? additionalsFileMetadata.get(fileNameKey) : "";
	            String documentName = additionalsFileMetadata.get(documentNameKey) != null ? additionalsFileMetadata.get(documentNameKey) : "";

	            System.out.println("Received additionals file: " + additionalsFile.getOriginalFilename());
	            String additionalsFileName = saveFiles(additionalsFile, "additionalsFile_" + fileName, currentDate);
	            System.out.println("Additionals file saved as: " + additionalsFileName);
	            // Process metadata here (fileName, documentName)
	            ApplicationDocument additionalDocument = new ApplicationDocument();
                additionalDocument.setFileName(additionalsFileName);
                additionalDocument.setDocumentTitle(documentName);
                additionalDocument.setIntentAppId(intentApp);
                additionalDocument.setStatus("Active");
                applicationDocumentServiceServ.addApplicationDocument(additionalDocument);
	        }

	        return ResponseEntity.ok("Files uploaded successfully");
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
	    }
	}

	private String saveFiles(MultipartFile file, String documentName, String currentDate) throws IOException {
	    String originalFileName = file.getOriginalFilename();
	    String fileExtension = "";

	    // Extract the file extension (if available)
	    if (originalFileName != null && originalFileName.contains(".")) {
	        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	    }

	    // Construct the base file name using the document name and current date
	    String baseFileName = documentName + "_" + currentDate;

	    // Define the directory path
	    Path directoryPath = Paths.get(Constant.REAL_PATH, "CPSDocument", "Document");

	    // Ensure the directory exists
	    if (Files.notExists(directoryPath)) {
	        Files.createDirectories(directoryPath);
	    }

	    // Initialize file name with an incremental number
	    String uniqueFileName = baseFileName + fileExtension;
	    Path filePath = directoryPath.resolve(uniqueFileName);
	    int counter = 1;

	    // Check if file already exists and increment the number if necessary
	    while (Files.exists(filePath)) {
	        uniqueFileName = baseFileName + "_" + counter + fileExtension;
	        filePath = directoryPath.resolve(uniqueFileName);
	        counter++;
	    }

	    // Copy the file to the specified location
	    try (InputStream inputStream = file.getInputStream()) {
	        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	    } catch (IOException e) {
	        // Log and rethrow the exception
	        System.err.println("Error saving file: " + e.getMessage());
	        throw new IOException("Error saving file", e);
	    }

	    return uniqueFileName;
	}

	@GetMapping(IndividualApplicationFormAPIs.GET_INDIVIDUAL_APPLICATION_FORM_BY_USERNAME)
	public ResponseEntity<?> getFirstApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        // Fetch the IntentApplication using the provided username
	    	
	    	
	    	
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }
	        
	        Map<String, Object> response = new HashMap<>();
	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        String status = "Edit_Upon_Review".equals(intentApp.getApplicationStatus()) ? "Inactive" : "Active";
	        List<IndivApplication> indivApplication = indAppFormServ.findIntentWithoutStatusAppById(intentAppId);
	        
	        Optional<IndivApplication> indivEmailOptional = indivApplication.stream()
	        	    .sorted((app1, app2) -> app2.getCreated().compareTo(app1.getCreated())) // Sorting in descending order
	        	    .findFirst(); // Get the first email after sorting
	        
	        
	    	if (indivEmailOptional.isPresent()) {
	    		IndivApplication indivApplication1 = indivEmailOptional.get();
        	    // Process the indivEmail object
	    		 response.put("application", indivApplication1);
        	   
        	}
	        if (indivApplication == null) {
	            return new ResponseEntity<>("Individual Application not found.", HttpStatus.NOT_FOUND);
	        }
	        List<IndivEmails> indivEmails = indEmailServ.findByindivApplicationId(intentAppId);
//	        Optional<IndivEmails> indivEmailOptional = indivEmails.stream()
//	        	    .sorted((app1, app2) -> app2.getCreated().compareTo(app1.getCreated())) // Sorting in descending order
//	        	    .findFirst(); // Get the first email after sorting
//
//	        System.out.println("Found the most recent email: " + indivEmailOptional.get().getCreatedBy());
//	        	// Example of how to handle the Optional
//	        	if (indivEmailOptional.isPresent()) {
//	        	    IndivEmails indivEmail = indivEmailOptional.get();
//	        	    // Process the indivEmail object
//	        	    response.put("emails", indivEmail);
//	        	    System.out.println("Found the most recent email: " + indivEmail);
//	        	}
//	   ;
	       
	        response.put("emails", indivEmails);
	        response.put("intentApp", intentApp);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@GetMapping(IndividualApplicationFormAPIs.GET_SECOND_INDIVIDUAL_APPLICATION_FORM_BY_USERNAME)
	public ResponseEntity<?> getSecondApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        // Fetch the IntentApplication by userName
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        Map<String, Object> response = new HashMap<>();

	        // Determine status based on applicationStatus
	        String status = "Edit_Upon_Review".equals(intentApp.getApplicationStatus()) ? "Inactive" : "Active";

	     // Fetch AppLocations by intentAppId and status
	        List<AppLocation> appLocation = appLocationServ.findIntentWithoutStatusAppById(intentAppId);
	        if (appLocation == null || appLocation.isEmpty()) {
	            return new ResponseEntity<>("App Location not found.", HttpStatus.NOT_FOUND);
	        }

	        // Sort AppLocations in descending order by created date
	        List<AppLocation> sortedAppLocations = appLocation.stream()
	            .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	            .collect(Collectors.toList()); // Collect to list

	        // Initialize IndivAddressDTO to populate the response
	        IndivAddressDTO indivAddressDTO = new IndivAddressDTO();

	        // Iterate through the sorted AppLocations to set Residential and Official addresses
	        for (AppLocation location : sortedAppLocations) {
	            String addressIdStr = location.getAddressId();
	            if (addressIdStr != null && !addressIdStr.isEmpty()) {
	                try {
	                    String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	                    AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);

	                    // Set Residential or Official Address
	                    if ("Residential".equals(location.getLocationName())) {
	                        indivAddressDTO.setResidential(addressDTO); // Set residential address
	                    } else if ("Official".equals(location.getLocationName())) {
	                        indivAddressDTO.setOfficial(addressDTO); // Set official address
	                    }
	                } catch (Exception e) {
	                    System.out.println("Error processing Address ID: " + addressIdStr);
	                }
	            } else {
	                System.out.println("Address ID is null or empty for this location.");
	            }
	        }

	        // Handle the Communication Address separately
	        handleCommunicationAddress(intentApp, response, status);

//	        // Add indivAddressDTO to response
//	        response.put("indivAddressDTO", indivAddressDTO);

	        // Add indivAddressDTO to response
	        response.put("indivAddressDTO", indivAddressDTO);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// Separate method for handling communication address
	private void handleCommunicationAddress(IntentApplication intentApp, Map<String, Object> response, String status) {
	    try {
	        // Fetch the IndivApplication to get the Communication Address by locationId
	        IndivApplication inApp = indAppFormServ.findIntentAppById(intentApp.getIntentAppId(),status);

	        // Ensure the communication address is an AppLocation object
	        AppLocation communicationLocation = inApp.getCommunicationAddress();

	        if (communicationLocation != null && communicationLocation.getAddressId() != null) {
	            String commLocationIdStr = communicationLocation.getAddressId();

	            if (commLocationIdStr != null && !commLocationIdStr.isEmpty()) {
	                String decryptedCommLocationId = EncryptionUtil.decrypt(commLocationIdStr);
	                AddressDTO commAddressDTO = intentAppServ.getAllLocationByAddressId(decryptedCommLocationId);

	                // Determine if the communication address is residential or official by locationName
	                String locationName = communicationLocation.getLocationName();
	                if ("Residential".equals(locationName)) {
	                    response.put("communicationAddress", "Residential");
	                } else if ("Official".equals(locationName)) {
	                    response.put("communicationAddress", "Official");
	                }

	                response.put("communicationAddressDetails", commAddressDTO);
	                System.out.println("Communication Address Set: " + commAddressDTO);
	            }
	        } else {
	            System.out.println("Communication Location ID is not set or null.");
	        }
	    } catch (Exception e) {
	        System.out.println("Error processing Communication Location: " + e.getMessage());
	    }
	}




	@GetMapping(IndividualApplicationFormAPIs.GET_THIRD_INDIVIDUAL_APPLICATION_FORM_BY_USERNAME)
	public ResponseEntity<?> getThirdApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        // Fetch the IntentApplication using the provided username
	        System.out.println("userName===>" + userName);
	        
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }
	        System.out.println("intentApp===>" + intentApp.getIntentAppId());

	        // Fetch Individual Additional Details
	        List<IndivAdditionalDetails> indivAdditionalDetails = indivAdditionalDetailsServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
	        if (indivAdditionalDetails == null) {
	            return new ResponseEntity<>("Additional details not found.", HttpStatus.NOT_FOUND);
	        }

	        Optional<IndivAdditionalDetails> mostRecentActive = indivAdditionalDetails.stream()
	                .sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated())) // Sort by most recent created date
	                .findFirst();
	        // Fetch Application Documents associated with the IndivApplication
	        // Check if the method expects the entire IntentApplication or just its ID
	        List<ApplicationDocument> applicationDocument = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId()); // Pass intentApp if method expects it
	        if (applicationDocument == null || applicationDocument.isEmpty()) {
	            return new ResponseEntity<>("No documents found.", HttpStatus.NOT_FOUND);
	        }
System.out.println("applicationDocument=====>"+applicationDocument.size());
List<ApplicationDocument> sortedActiveDocuments = applicationDocument.stream()
.sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated())) // Sort by most recent created date
.collect(Collectors.toList()); // Collect into a list
	        
	        // Extract file names from the application documents
	        List<String> fileNames = applicationDocument.stream()
	            .map(ApplicationDocument::getFileName)  // Assuming ApplicationDocument has a getFileName() method
	            .collect(Collectors.toList());

	        // Create a response object to combine details and file names
	        Map<String, Object> response = new HashMap<>();
	        response.put("indivAdditionalDetails", mostRecentActive.orElse(null));
	        response.put("applicationDocument", sortedActiveDocuments);
	        response.put("fileNames", fileNames);  // Include the list of file names

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Document ID not in format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving document data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping(IndividualApplicationFormAPIs.GET_FOURTH_INDIVIDUAL_APPLICATION_FORM_BY_USERNAME)
	public ResponseEntity<?> getFourthApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        // Fetch the IntentApplication using the provided username
	        System.out.println("userName===>" + userName);

	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);

	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Map<String, Object> response = new HashMap<>();

	        System.out.println("status========>" + intentApp.getApplicationStatus());

	        // Handle case where the status is "pending"
	        if ("Edit_Upon_Review".equals(intentApp.getApplicationStatus())) {
	            // Fetch the most recent active BankDetails
	            List<BankDetails> bankDetailsList = bankDetailsSev.findIntentWithoutAppById(intentApp.getIntentAppId());
	            Optional<BankDetails> mostRecentActiveBankDetail = bankDetailsList.stream()
	                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort by most recent created date
	                .findFirst();

	            // Fetch the most recent active BankDraft
	            List<BankDraft> bankDraftList = bankDraftServ.findIntentWithoutAppById(intentApp.getIntentAppId());
	            Optional<BankDraft> mostRecentActiveBankDraft = bankDraftList.stream()
	                .sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated())) // Sort by most recent created date
	                .findFirst();

	            // Add the most recent active details to the response
	            response.put("bankDetails", mostRecentActiveBankDetail.orElse(null));
	            response.put("bankDraft", mostRecentActiveBankDraft.orElse(null));
	        } else if ("Edit_Upon_Review".equals(intentApp.getApplicationStatus())) {
	            // Fetch the inactive details for "Edit_Upon_Review" status
	            BankDetails bankDetails = bankDetailsSev.findIntentAppWithStatusById(intentApp.getIntentAppId(), "Inactive");
	            BankDraft bankDraft = bankDraftServ.findIntentAppWithStatusById(intentApp.getIntentAppId(), "Inactive");

	            response.put("bankDetails", bankDetails);
	            response.put("bankDraft", bankDraft);
	        } else {
	            // Fetch the default bank details for other statuses
	            BankDetails bankDetails = bankDetailsSev.findIntentAppById(intentApp.getIntentAppId());
	            BankDraft bankDraft = bankDraftServ.findByindivApplicationId(intentApp.getIntentAppId());

	            response.put("bankDetails", bankDetails);
	            response.put("bankDraft", bankDraft);
	        }

	        if (response.isEmpty()) {
	            return new ResponseEntity<>("Document not found.", HttpStatus.NOT_FOUND);
	        }

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Document ID not in format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving Document data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping(IndividualApplicationFormAPIs.GET_FIFTH_INDIVIDUAL_APPLICATION_FORM_BY_USERNAME)
	public ResponseEntity<?> getFifthApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        System.out.println("userName ===> " + userName);

	        // Fetch IntentApplication
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        System.out.println("intentAppId ===> " + intentAppId);

	        // Initialize response map
	        Map<String, Object> response = new HashMap<>();
	       // String status = "Edit_Upon_Review".equals(intentApp.getApplicationStatus()) ? "Inactive" : "Active";
	        List<AppLocation> appLocationList;
	        // If status = Active
	         appLocationList = appLocationServ.findIntentAppById(intentAppId, "Active");
	        
	         
	        System.out.println("vyghjjidf====>"+appLocationList.size());
	        if(appLocationList.size()==0) {
	        	appLocationList = appLocationServ.findIntentAppById(intentAppId, "Inactive");
	        }
	        appLocationList = appLocationServ.findIntentAppById(intentAppId, "Inactive");
	        //Obj = null in case of active then get incative
	        
	        //inactive -- null
	        
	        // Fetch AppLocation by intentAppId
	        //List<AppLocation> appLocationList = appLocationServ.findIntentWithoutStatusAppById(intentAppId);
	        if (appLocationList == null || appLocationList.isEmpty()) {
	            return new ResponseEntity<>("App Location not found.", HttpStatus.NOT_FOUND);
	        }

	        List<AppLocation> sortedActive = appLocationList;
//	        		.stream()
//	                .sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated())) 
//	                .collect(Collectors.toList()); // Collect into a list
	        System.out.println("AppLocation size: " + sortedActive.size());

	        // Initialize a list to store multiple addressDTOs
	        List<AddressDTO> addressDTOList = new ArrayList<>();
	        
	        // Initialize a list to collect non-"Residential" and non-"Official" locations
	        List<AppLocation> otherLocationList = new ArrayList<>();

	        // Process each location
	        for (AppLocation location : sortedActive) {
	            System.out.println("AppLocation object: " + location);
	            String locationName = location.getLocationName();
	            String addressIdStr = location.getAddressId();

	            // Skip processing for "Residential" and "Official" locations
	            if ("Residential".equals(locationName) || "Official".equals(locationName)) {
	                System.out.println("Skipping location: " + locationName);
	                continue;
	            }

	            // Add non-Residential/Official locations to the list
	            otherLocationList.add(location);

	            // Process address ID if available
	            if (addressIdStr != null && !addressIdStr.isEmpty()) {
	                try {
	                    AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(addressIdStr);
	                    addressDTOList.add(addressDTO);  // Add to the list instead of overwriting

	                    System.out.println("Address Details: " + addressDTO.getAddressId());

	                } catch (Exception e) {
	                    System.out.println("Error processing Address ID: " + addressIdStr);
	                }
	            } else {
	                System.out.println("Address ID is null or empty for this location.");
	            }
	        }

	        // Add the list of non-"Residential" and non-"Official" locations to the response
	        if (!otherLocationList.isEmpty()) {
	            response.put("LocationMap", otherLocationList);
	        } else {
	            System.out.println("No other locations found.");
	        }

	        // Add the list of addressDTOs to the response
	        response.put("addressDTOList", addressDTOList);

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Address ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping(IndividualApplicationFormAPIs.GET_SIX_INDIVIDUAL_APPLICATION_FORM_BY_USERNAME)
	public ResponseEntity<?> getSixApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        // Fetch Intent Application by username
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        System.out.println("intentAppId===>" + intentAppId);
	        
	        Map<String, Object> response = new HashMap<>();
	        
	        // Fetch documents by Intent Application ID
	        List<ApplicationDocument> appDocuments = applicationDocumentServiceServ.findIntentAppById(intentAppId);
	        if (appDocuments == null || appDocuments.isEmpty()) {
	            System.out.println("No documents found for intentAppId: " + intentAppId);
	            return new ResponseEntity<>("Document not found.", HttpStatus.NOT_FOUND);
	        }

	        System.out.println("AppDocuments size: " + appDocuments.size());

	        // List to hold the DTOs
	        List<ApplicationDocumentDTO> documentDTOs = new ArrayList<>();

	        // Loop through each document and transform to DTO
	        for (ApplicationDocument document : appDocuments) {
	            System.out.println("Document object: " + document);

	            String documentName = document.getDocumentTitle();

	            // Skip certain documents
	            if ("passportDocument".equals(documentName) || "idCardDocument".equals(documentName) || 
	                "panCardDocument".equals(documentName) || "capitalDocument".equals(documentName)||"firmPanCardDocument".equals(documentName) || "firmNetWorthDocument".equals(documentName) || 
	                 "paidUpCapitalDocument".equals(documentName)) {
	                System.out.println("Skipping addressId processing for documentName: " + documentName);
	                continue;  
	            }

	            // Map entity to DTO
	            ApplicationDocumentDTO appDocumentDTO = new ApplicationDocumentDTO();
	            appDocumentDTO.setAppDocId(document.getAppDocId());
	            appDocumentDTO.setDocumentTitle(document.getDocumentTitle());
	            appDocumentDTO.setIntentAppId(document.getIntentAppId().getIntentAppId());
	            appDocumentDTO.setFileName(document.getFileName());
	            appDocumentDTO.setStatus(document.getStatus());
	            appDocumentDTO.setCreatedBy(document.getCreatedBy());
	            appDocumentDTO.setUpdatedBy(document.getUpdatedBy());

	            // Format date fields
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            appDocumentDTO.setCreated(dateFormat.format(document.getCreated()));
	            appDocumentDTO.setUpdated(dateFormat.format(document.getUpdated()));

	            // Add DTO to the list
	            documentDTOs.add(appDocumentDTO);
	        }

	        // Add the list of document DTOs to the response
	        response.put("documents", documentDTOs);

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Intent ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        e.printStackTrace();  // Add this to get more detailed error messages
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@GetMapping(IndividualApplicationFormAPIs.GET_ALL_INDIVIDUAL_APPLICATION_FORM)
	public ResponseEntity<?> getAllApplicationTypes() {
		List<ApplicationTypeMaster> appTypeList = indAppFormServ.getAllApplicationTypeMaster();
		return new ResponseEntity<>(appTypeList, HttpStatus.OK);
	}

	@GetMapping(IndividualApplicationFormAPIs.GET_INDIVIDUAL_APPLICATION_FORM_BY_ID)
	public ResponseEntity<?> getApplicationTypeById(@RequestParam("id") String appTypeId) {
		try {
			Long id = Long.parseLong(EncryptionUtil.decrypt(appTypeId));
			ApplicationTypeMaster appType = indAppFormServ.getApplicationTypeMasterById(id);

			if (appType == null) {
				return new ResponseEntity<>("Application Type not found.", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(appType, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Application Type ID.", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(IndividualApplicationFormAPIs.UPDATE_INDIVIDUAL_APPLICATION_FORM)
	public ResponseEntity<?> updateApplicationType(@RequestBody IndividualStep1Payload personalDetailsDTO) {
		
		
		System.out.println("===>"+personalDetailsDTO.toString());
	
		if (personalDetailsDTO.getSalutation() == null || personalDetailsDTO.getSalutation().trim().isEmpty()) {
			return new ResponseEntity<>("Salutation is required.", HttpStatus.BAD_REQUEST);
		}

		// First Name validation
		if (personalDetailsDTO.getFirstName() == null || personalDetailsDTO.getFirstName().trim().isEmpty()) {
			return new ResponseEntity<>("First Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}else if (personalDetailsDTO.getFirstName().length() < 3 || personalDetailsDTO.getFirstName().length() > 30) {
			return new ResponseEntity<>("The length of First Name must be between 3 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}else if (!personalDetailsDTO.getFirstName().matches("^[A-Za-z]+$")) {
			return new ResponseEntity<>("Please enter a valid First Name.", HttpStatus.BAD_REQUEST);
		}

		// Middle Name validation (optional)

		if(personalDetailsDTO.getMiddleName() == null || personalDetailsDTO.getMiddleName().trim().isEmpty()) {
			
		}else if (personalDetailsDTO.getMiddleName().length() <3 || personalDetailsDTO.getMiddleName().length() > 30) {
			return new ResponseEntity<>("The length of Middle Name must be between 3 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}else if (!personalDetailsDTO.getMiddleName().matches("^[A-Za-z]+$")) {
			return new ResponseEntity<>("Please enter a valid Middle Name.", HttpStatus.BAD_REQUEST);
		}

		// Last Name validation
		if (personalDetailsDTO.getLastName() == null || personalDetailsDTO.getLastName().trim().isEmpty()) {
			return new ResponseEntity<>("Last Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}else if (personalDetailsDTO.getLastName().length() < 3 || personalDetailsDTO.getLastName().length() > 30) {
			return new ResponseEntity<>("The length of Last Name must be between 3 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}else if (!personalDetailsDTO.getLastName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Last Name.", HttpStatus.BAD_REQUEST);
		}

		// Father's First Name validation
		if (personalDetailsDTO.getFatherFirstName() == null
				|| personalDetailsDTO.getFatherFirstName().trim().isEmpty()) {
			return new ResponseEntity<>("Father's First Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		if (personalDetailsDTO.getFatherFirstName().length() < 1
				|| personalDetailsDTO.getFatherFirstName().length() > 30) {
			return new ResponseEntity<>("The length of Father's First Name must be between 1 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getFatherFirstName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Father's First Name.", HttpStatus.BAD_REQUEST);
		}

		// Father's Middle Name validation (optional)
		if (personalDetailsDTO.getFatherMiddleName() != null && !personalDetailsDTO.getFatherMiddleName().isEmpty()) {
			if (personalDetailsDTO.getFatherMiddleName().length() > 30) {
				return new ResponseEntity<>("The length of Father's Middle Name must be between 0 and 30 characters.",
						HttpStatus.BAD_REQUEST);
			}
			if (!personalDetailsDTO.getFatherMiddleName().matches("^[A-Za-z ]+$")) {
				return new ResponseEntity<>("Please enter a valid Father's Middle Name.", HttpStatus.BAD_REQUEST);
			}
		}

		// Father's Last Name validation
		if (personalDetailsDTO.getFatherLastName() == null || personalDetailsDTO.getFatherLastName().trim().isEmpty()) {
			return new ResponseEntity<>("Father's Last Name cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		if (personalDetailsDTO.getFatherLastName().length() < 1
				|| personalDetailsDTO.getFatherLastName().length() > 30) {
			return new ResponseEntity<>("The length of Father's Last Name must be between 1 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getFatherLastName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Father's Last Name.", HttpStatus.BAD_REQUEST);
		}

		// Other First Name validation
		if (personalDetailsDTO.isKnownByOtherName() == true) {

		if (personalDetailsDTO.getOtherFirstName().length() > 30) {
			return new ResponseEntity<>("The length of Other First Name must be between 0 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getOtherFirstName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Other First Name.", HttpStatus.BAD_REQUEST);
		}

		// Other Middle Name validation (optional)

		if (personalDetailsDTO.getOtherMiddleName().length() > 30) {
			return new ResponseEntity<>("The length of Other Middle Name must be between 0 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getOtherMiddleName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Other Middle Name.", HttpStatus.BAD_REQUEST);
		}

		// Other Last Name validation

		if (personalDetailsDTO.getOtherLastName().length() > 30) {
			return new ResponseEntity<>("The length of Other Last Name must be between 0 and 30 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getOtherLastName().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Other Last Name.", HttpStatus.BAD_REQUEST);
		}
		}
		// Date of Birth validation
		if (personalDetailsDTO.getDob() == null) {
			return new ResponseEntity<>("Date of Birth is required.", HttpStatus.BAD_REQUEST);
		}

		// Gender validation
		if (personalDetailsDTO.getGender() == null || personalDetailsDTO.getGender().trim().isEmpty()) {
			return new ResponseEntity<>("Gender is required.", HttpStatus.BAD_REQUEST);
		}

		// Nationality validation
		if (personalDetailsDTO.getNationality() == null || personalDetailsDTO.getNationality().trim().isEmpty()) {
			return new ResponseEntity<>("Nationality cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		if (personalDetailsDTO.getNationality().length() < 1 || personalDetailsDTO.getNationality().length() > 50) {
			return new ResponseEntity<>("The length of Nationality must be between 1 and 50 characters.",
					HttpStatus.BAD_REQUEST);
		}
		if (!personalDetailsDTO.getNationality().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Please enter a valid Nationality.", HttpStatus.BAD_REQUEST);
		}

		// Email validation (as list of email IDs)
		if (personalDetailsDTO.getEmailId() == null || personalDetailsDTO.getEmailId().isEmpty()) {
			return new ResponseEntity<>("Email Address cannot be empty.", HttpStatus.BAD_REQUEST);
		}

		for (EmailPayload email : personalDetailsDTO.getEmailId()) {
			if (email.getEmailId() == null || email.getEmailId().trim().isEmpty()) {
				return new ResponseEntity<>("Email Address cannot be empty.", HttpStatus.BAD_REQUEST);
			}
			if (!email.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
				return new ResponseEntity<>("Please enter a valid Email Address.", HttpStatus.BAD_REQUEST);
			}
		}

		Optional<IndivApplication> result;
		try {
			
			
			IntentApplication intentApp = intentAppServ.findIntentAppById(personalDetailsDTO.getUserName());
			
			intentApp.setApplicationStatus("PENDING-FOR-RENEWAL");
			intentAppServ.updateIntentApp(intentApp);
			List<IndivApplication> applications = indAppFormServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
			
			// Get the most recent IndivApplication document, sorted by creation date in descending order
			Optional<IndivApplication> optional = applications.stream()
			    .sorted((app1, app2) -> app2.getCreated().compareTo(app1.getCreated())) // Sort by creation date in descending order
			    .findFirst(); // Get the most recent application

			// Process the Optional
			if (optional.isPresent()) {
			    IndivApplication mostRecentApplication = optional.get();
			    
			    System.out.println("Most recent application: " + mostRecentApplication);
			    
			    if("Inactive".equals(mostRecentApplication.getStatus()))
			    {
					
					//Individual Application First Step
					IndivApplication indivApplication = new IndivApplication();
					indivApplication.setSalutation1(personalDetailsDTO.getSalutation());
					indivApplication.setFirstName1(personalDetailsDTO.getFirstName());
					indivApplication.setMiddleName1(personalDetailsDTO.getMiddleName());
					indivApplication.setLastName1(personalDetailsDTO.getLastName());
					indivApplication.setFirstName2(personalDetailsDTO.getOtherFirstName());
					indivApplication.setMiddleName2(personalDetailsDTO.getOtherMiddleName());
					indivApplication.setLastName2(personalDetailsDTO.getOtherLastName());
					indivApplication.setFsalutation(personalDetailsDTO.getFathersSalutation());
					indivApplication.setFfirstName(personalDetailsDTO.getFatherFirstName());
					indivApplication.setFmiddleName(personalDetailsDTO.getFatherMiddleName());
					indivApplication.setFlastName(personalDetailsDTO.getFatherLastName());

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

					try {
						Date Dob = dateFormat.parse(personalDetailsDTO.getDob());

						indivApplication.setDob(Dob);

					} catch (ParseException e) {
						e.printStackTrace();
					}

					indivApplication.setGender(personalDetailsDTO.getGender());

					indivApplication.setNationality(personalDetailsDTO.getNationality());

					if (personalDetailsDTO.isKnownByOtherName() == true) {
						indivApplication.setFirstName2(personalDetailsDTO.getOtherFirstName());
						indivApplication.setMiddleName2(personalDetailsDTO.getOtherMiddleName());
						indivApplication.setLastName2(personalDetailsDTO.getOtherLastName());
					}
					
					indivApplication.setStatus("Active");
					 IntentApplication intentApps=intentAppServ.findIntentAppById(personalDetailsDTO.getUserName());
					 indivApplication.setIntentAppId(intentApps);
					
					 result = indAppFormServ.addIndivApplication(indivApplication);
			    	
			    }else {
			    	
			    	
			    	IndivApplication indivApplication = new IndivApplication();
					indivApplication.setSalutation1(personalDetailsDTO.getSalutation());
					indivApplication.setFirstName1(personalDetailsDTO.getFirstName());
					indivApplication.setMiddleName1(personalDetailsDTO.getMiddleName());
					indivApplication.setLastName1(personalDetailsDTO.getLastName());
					indivApplication.setFirstName2(personalDetailsDTO.getOtherFirstName());
					indivApplication.setMiddleName2(personalDetailsDTO.getOtherMiddleName());
					indivApplication.setLastName2(personalDetailsDTO.getOtherLastName());
					indivApplication.setFsalutation(personalDetailsDTO.getFathersSalutation());
					indivApplication.setFfirstName(personalDetailsDTO.getFatherFirstName());
					indivApplication.setFmiddleName(personalDetailsDTO.getFatherMiddleName());
					indivApplication.setFlastName(personalDetailsDTO.getFatherLastName());
					indivApplication.setIntentAppId(intentApp);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

					try {
						Date Dob = dateFormat.parse(personalDetailsDTO.getDob());

						indivApplication.setDob(Dob);

					} catch (ParseException e) {
						e.printStackTrace();
					}

					indivApplication.setGender(personalDetailsDTO.getGender());

					indivApplication.setNationality(personalDetailsDTO.getNationality());

					if (personalDetailsDTO.isKnownByOtherName() == true) {
						indivApplication.setFirstName2(personalDetailsDTO.getOtherFirstName());
						indivApplication.setMiddleName2(personalDetailsDTO.getOtherMiddleName());
						indivApplication.setLastName2(personalDetailsDTO.getOtherLastName());
					}
					Date date=new Date();
					indivApplication.setCreated(date);
					indivApplication.setUpdated(date);
					indivApplication.setStatus("Active");
					indivApplication.setIndivApplicationId(Long.valueOf(personalDetailsDTO.getIndivAppId()));
				     result = indAppFormServ.updateIndivApplication(indivApplication);

					if (personalDetailsDTO.getEmailId() != null && !personalDetailsDTO.getEmailId().isEmpty()) {

						for (EmailPayload emailPayload : personalDetailsDTO.getEmailId()) {

							IndivEmails indivEmail = new IndivEmails();
		                    indivEmail.setEmailId(emailPayload.getEmailId());
							indivEmail.setCreated(date);
							indivEmail.setUpdated(date);
							indivEmail.setUpdatedBy(personalDetailsDTO.getUpdatedBy());
							indivEmail.setStatus(emailPayload.getStatus());
							indivEmail.setIntentAppId(intentApp);
							
							if(emailPayload.getIndivEmailId() == null || emailPayload.getIndivEmailId().isEmpty() || emailPayload.getIndivEmailId().equals("")) {
								indivEmail.setCreatedBy(personalDetailsDTO.getCreatedBy());
								indivEmail.setStatus("Active");
								indEmailServ.addIndivEmails(indivEmail);
							}
							else
							{
								
								indivEmail.setIndivEmailId(Long.parseLong(emailPayload.getIndivEmailId()));
								indEmailServ.updateIndivEmails(indivEmail);
							}
							


						}
					}

			    }
			    
			}
			
			
		
			return new ResponseEntity<>("form Submitted", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating the First Step.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(IndividualApplicationFormAPIs.UPDATE_INDIVIDUAL_APPLICATIONS_FORM2)
	public ResponseEntity<?> updateApplication2(@RequestBody IndivAddressDTO indivAddressDTO) {

	    System.out.println(indivAddressDTO);

	    try {
	    	 IntentApplication intentApp = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	    	
	    	List<AppLocation>appLocations=appLocationServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
	    	
	    	List<AppLocation> sortedAppLocations = appLocations.stream()
	    		    .filter(location -> "Residential".equals(location.getLocationName()) || "Official".equals(location.getLocationName())) // Filter
	    		    .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	    		    .collect(Collectors.toList()); // Collect to list
	    	 for(AppLocation appLocation3:sortedAppLocations) {
	    	 if("Inactive".equals(appLocation3.getStatus()))
	    	 {
	    		 Long resAddId = intentAppServ.addUsers(indivAddressDTO.getResidential());
	 	        
	 	        // Add Official Address
	 	        Long offAddId = intentAppServ.addUsers(indivAddressDTO.getOfficial());
	 	        
	 	        // Create Residential AppLocation
	 	        AppLocation resLocation = new AppLocation();
	 	        resLocation.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId))); 
	 	        resLocation.setLocationName("Residential");
	 	        resLocation.setIntentAppId(intentApp); 
	 	        resLocation.setStatus("Active");
	 	        resLocation.setCreatedBy(indivAddressDTO.getCreatedBy());
	 	        resLocation.setUpdatedBy(indivAddressDTO.getUpdaedBy());
	 	        
	 	        Optional<AppLocation> resLoc = appLocationServ.addAppLocation(resLocation);

	 	        // Create Official AppLocation
	 	        AppLocation offLocation = new AppLocation();
	 	        offLocation.setAddressId(EncryptionUtil.encrypt(String.valueOf(offAddId))); 
	 	        offLocation.setLocationName("Official");
	 	        IntentApplication intentApp1 = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	 	        offLocation.setIntentAppId(intentApp1); 
	 	        offLocation.setStatus("Active");
	 	        offLocation.setCreatedBy(indivAddressDTO.getCreatedBy());
	 	        offLocation.setUpdatedBy(indivAddressDTO.getUpdaedBy());
	 	        
	 	        Optional<AppLocation> offLoc = appLocationServ.addAppLocation(offLocation);

	 	        // Prepare Response Map
	 	        Map<String, Object> response = new HashMap<>();
	 	        response.put("Residential", resLoc);
	 	        response.put("Official", offLoc);
	 	        String status = "Edit_Upon_Review".equals(intentApp.getApplicationStatus()) ? "Inactive" : "Active";
	 	        // Find Intent Application and Individual Application
	 	        IntentApplication in = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	 	    // Fetch the IndivApplication objects
	 	       List<IndivApplication> inAppList = indAppFormServ.findIntentWithoutStatusAppById(in.getIntentAppId());

	 	       // Sort in descending order based on the creation date
	 	       List<IndivApplication> sortedIndivApplications = inAppList.stream()
	 	               .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	 	               .collect(Collectors.toList()); // Collect to list

	 	       // Assuming you want to update the most recent IndivApplication
	 	       if (!sortedIndivApplications.isEmpty()) {
	 	           IndivApplication inApp = sortedIndivApplications.get(0); // Get the most recent application

	 	           // Set Communication Address based on selection
	 	           if ("Residential".equals(indivAddressDTO.getCommunicationAddress())) {
	 	               // Set Residential address as communication address
	 	               if (resLoc.isPresent()) {
	 	                   inApp.setCommunicationAddress(resLoc.get());
	 	               }
	 	           } else if ("Official".equals(indivAddressDTO.getCommunicationAddress())) {
	 	               // Set Official address as communication address
	 	               if (offLoc.isPresent()) {
	 	                   inApp.setCommunicationAddress(offLoc.get());
	 	               }
	 	           }

	 	           // Save the updated IndivApplication
	 	           indAppFormServ.addIndivApplication(inApp);
	 	       } else {
	 	           // Handle the case where there are no IndivApplications found
	 	           return new ResponseEntity<>("No IndivApplication found for the given ID", HttpStatus.NOT_FOUND);
	 	       }
	    		 
	    		 
	    	 }else {
	    	
	        // Update Residential and Official Addresses
	        Long resAddId = intentAppServ.updateUsers(indivAddressDTO.getResidential());
	        Long offAddId = intentAppServ.updateUsers(indivAddressDTO.getOfficial());

	        System.out.println(resAddId);

	        // Encrypt the address IDs
	        String resApp = EncryptionUtil.encrypt(String.valueOf(resAddId));
	        String offApp = EncryptionUtil.encrypt(String.valueOf(offAddId));
	        // Find the IntentApplication and IndivApplication
	        IntentApplication in = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	     // Fetch the IntentApplication using the user name
	        IntentApplication in1 = intentAppServ.findIntentAppById(indivAddressDTO.getUserName());
	        List<IndivApplication> inAppList = indAppFormServ.findIntentWithoutStatusAppById(in1.getIntentAppId());

	        // Sort in descending order based on the creation date
	        List<IndivApplication> sortedIndivApplications = inAppList.stream()
	                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	                .collect(Collectors.toList()); // Collect to list

	        // Check if there are any IndivApplication instances available
	        if (!sortedIndivApplications.isEmpty()) {
	            // Get the most recent IndivApplication
	            IndivApplication inApp = sortedIndivApplications.get(0); // Fetch the latest application

	            // Set Communication Address based on selection (Residential/Official)
	            if ("Residential".equals(indivAddressDTO.getCommunicationAddress())) {
	                // Find Residential AppLocation using the encrypted addressId (resApp)
	                Optional<AppLocation> resLocation = appLocationServ.findByEncryptedAddressId(resApp);
	                if (resLocation.isPresent()) {
	                    inApp.setCommunicationAddress(resLocation.get());
	                } else {
	                    return new ResponseEntity<>("Failed to update Address: Residential address not found", HttpStatus.BAD_REQUEST);
	                }
	            } else if ("Official".equals(indivAddressDTO.getCommunicationAddress())) {
	                // Find Official AppLocation using the encrypted addressId (offApp)
	                Optional<AppLocation> offLocation = appLocationServ.findByEncryptedAddressId(offApp);
	                if (offLocation.isPresent()) {
	                    inApp.setCommunicationAddress(offLocation.get());
	                } else {
	                    return new ResponseEntity<>("Failed to update Address: Official address not found", HttpStatus.BAD_REQUEST);
	                }
	            } else {
	                return new ResponseEntity<>("Invalid communication address selection", HttpStatus.BAD_REQUEST);
	            }

	            // Save the updated IndivApplication
	            indAppFormServ.updateIndivApplication(inApp);
	        } else {
	            return new ResponseEntity<>("No IndivApplication found for the given IntentApplication ID", HttpStatus.NOT_FOUND);
	        }
	    	 }
	    	 }
	        return new ResponseEntity<>("Address updated successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Failed to update Address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping(IndividualApplicationFormAPIs.UPDATE_INDIVIDUAL_APPLICATIONS_FORM3)
	public ResponseEntity<?> updateApplicationForm(
	        @RequestParam("creditcardtype") String creditcardtype,
	        @RequestParam("creditCardNo") String creditCardNo,
	        @RequestParam("issuedBy") String issuedBy,
	        @RequestParam("ispName") String ispName,
	        @RequestParam("ispWebsiteAddress") String ispWebsiteAddress,
	        @RequestParam("ispUserName") String ispUserName,
	        @RequestParam("webPageURL") String webPageURL,
	        @RequestParam("useName") String useName,
	        @RequestParam("indivAdditionalDetailsId") String indivAdditionalDetailsId,
	        @RequestParam("passportappDocId") String passportappDocId,
	        @RequestParam("passportName") String passportName,
	        @RequestParam("passportNo") String passportNo,
	        @RequestParam("passportIssuingAuthority") String passportIssuingAuthority,
	        @RequestParam("passportExpiryDate") String passportExpiryDate,
	        @RequestParam(value = "file1", required = false) MultipartFile file1,
	        @RequestParam("votarappDocId") String votarappDocId,
	        @RequestParam("voterCardName") String voterCardName,
	        @RequestParam("voterIdNo") String voterIdNo,
	        @RequestParam(value = "file2", required = false) MultipartFile file2,
	        @RequestParam("panappDocId") String panappDocId,
	        @RequestParam("panCardName") String panCardName,
	        @RequestParam("incomeTaxPanNo") String incomeTaxPanNo,
	        @RequestParam(value = "file3", required = false) MultipartFile file3,
	        @RequestParam("capitalappDocId") String capitalappDocId,
	        @RequestParam("capitalName") String capitalName,
	        @RequestParam("userName") String capitalUserName,
	        @RequestParam("capital") String capital,
	        @RequestParam(value = "file4", required = false) MultipartFile file4,
	        @RequestParam(value = "fileName1", required = false) String fileName1,
	        @RequestParam(value="fileName2", required = false) String fileName2,
	        @RequestParam(value="fileName3", required = false) String fileName3,
	        @RequestParam(value="fileName4", required = false) String fileName4) {

	    try {
	    	
	    	 System.out.println("file4====>"+file4);
	    	
	        // Parse and validate the expiry date
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date publishDateParsed;
	        try {
	            publishDateParsed = dateFormat.parse(passportExpiryDate);
	        } catch (ParseException e) {
	            return new ResponseEntity<>("Invalid date format.", HttpStatus.BAD_REQUEST);
	        }  
	        IntentApplication intentApp = intentAppServ.findIntentAppById(useName);
	        
	        List<IndivAdditionalDetails> indivAdditionalDetailsList = indivAdditionalDetailsServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());

	     // Sort the list by the 'Created' date in descending order and get the most recent one
	     Optional<IndivAdditionalDetails> mostRecentDetails = indivAdditionalDetailsList.stream()
	             .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	             .findFirst();

	     // Check if a record is found
	     if (mostRecentDetails.isPresent()) {
	         // Get the most recent IndivAdditionalDetails
	         IndivAdditionalDetails indivAdditionalDetailes = mostRecentDetails.get();
	         
	         // Check if the status is "Inactive"
	         if ("Inactive".equals(indivAdditionalDetailes.getStatus())) {
	             // Perform actions if the status is "Inactive"
	             // For example, update some fields, log info, or handle the inactive state
	             System.out.println("The status is Inactive. Perform your actions here.");
	             
	             // Add any additional logic based on the "Inactive" status
	            
	             String panCardDocument="panCardDocument";
	 	    	String idCardDocument="idCardDocument";
	 	    	String passportDocuments="passportDocument";
	 	    	String capitalDocuments="capitalDocument";
	 	    	
	 	        IndivAdditionalDetails indivAdditionalDetails = new IndivAdditionalDetails();
	 	        indivAdditionalDetails.setCreditCardType(creditcardtype);
	 	        indivAdditionalDetails.setCreditCardNo(creditCardNo);
	 	        indivAdditionalDetails.setCreditCardIssuedBy(issuedBy);
	 	        indivAdditionalDetails.setWebURL(webPageURL);
	 	        indivAdditionalDetails.setPassportNo(passportNo);
	 	        indivAdditionalDetails.setPassportIssuingAuthority(passportIssuingAuthority);

	 	        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	 	        Date publishDateParsed1;
	 	        try {
	 	            publishDateParsed1 = dateFormat1.parse(passportExpiryDate);
	 	        } catch (ParseException e) {
	 	            return new ResponseEntity<>("Invalid  date format.", HttpStatus.BAD_REQUEST);
	 	        }
	 	        indivAdditionalDetails.setPassportExpiryDate(publishDateParsed1);
	 	        indivAdditionalDetails.setVoterId(voterIdNo);
	 	        indivAdditionalDetails.setPan(incomeTaxPanNo);
	 	        indivAdditionalDetails.setIspName(ispName);
	 	        indivAdditionalDetails.setIspWebsite(ispWebsiteAddress);
	 	        indivAdditionalDetails.setIspUsername(ispUserName);
	 	        indivAdditionalDetails.setPersonalWebPage(webPageURL);
	 	        indivAdditionalDetails.setIndivCapital(capital);
	 	        
	 	        indivAdditionalDetails.setIntentAppId(intentApp);
	 	        indivAdditionalDetails.setStatus("Active");
	 	        indivAdditionalDetailsServiceServ.addIndivAdditionalDetails(indivAdditionalDetails);  // Assuming a service to save it

	 	        // Date formatter for current date
	 	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	 	        String currentDate = LocalDateTime.now().format(formatter);

	 	     // Handle passport document
	 	        if (file1 != null && !file1.isEmpty()) {
	 	            ApplicationDocument passportDocument = new ApplicationDocument();
	 	            passportDocument.setDocumentTitle(passportDocuments);

	 	            String passportFileName = saveFile(file1, passportDocuments, currentDate);
	 	            passportDocument.setFileName(passportFileName);  // e.g., passportName_20230908_153012

	 	            passportDocument.setIntentAppId(intentApp); 
	 	            passportDocument.setStatus("Active");
	 	            System.out.println(passportName);
	 	            applicationDocumentServiceServ.addApplicationDocument(passportDocument);
	 	        }

	 	        // Handle voter card document
	 	        if (file2 != null && !file2.isEmpty()) {
	 	            ApplicationDocument voterDocument = new ApplicationDocument();
	 	            voterDocument.setDocumentTitle(idCardDocument);

	 	            String voterFileName = saveFile(file2, idCardDocument, currentDate);
	 	            voterDocument.setFileName(voterFileName);  // e.g., voterCardName_20230908_153012

	 	            voterDocument.setIntentAppId(intentApp); 
	 	            voterDocument.setStatus("ACTIVE");
	 	            applicationDocumentServiceServ.addApplicationDocument(voterDocument);
	 	        }

	 	        // Handle PAN card document
	 	        if (file3 != null && !file3.isEmpty()) {
	 	            ApplicationDocument panDocument = new ApplicationDocument();
	 	            panDocument.setDocumentTitle(panCardDocument);

	 	            String panFileName = saveFile(file3, panCardDocument, currentDate);
	 	            panDocument.setFileName(panFileName);  // e.g., panCardName_20230908_153012

	 	            panDocument.setIntentAppId(intentApp); 
	 	            panDocument.setStatus("ACTIVE");
	 	            applicationDocumentServiceServ.addApplicationDocument(panDocument);
	 	        }
	 	        // Handle capital proof document
	 	        ApplicationDocument capitalDocument = new ApplicationDocument();
	 	        capitalDocument.setDocumentTitle(capitalDocuments);
	 	        if (file3 != null && !file4.isEmpty()) {
	 	            String capitalFileName = saveFile(file4, capitalDocuments, currentDate);
	 	            capitalDocument.setFileName(capitalFileName);  // e.g., capitalName_20230908_153012
	 	        }
	 	        capitalDocument.setIntentAppId(intentApp); 
	 	        capitalDocument.setStatus("ACTIVE");
	 	        applicationDocumentServiceServ.addApplicationDocument(capitalDocument);
	 	      
	 	        // Save all documents to the database
	 	        
	         } else {
	             // Handle the case when the status is not "Inactive"
	        	 
	        	   // Create and update the individual additional details
	 	        IndivAdditionalDetails indivAdditionalDetails = new IndivAdditionalDetails();
	 	        indivAdditionalDetails.setIndivAdditionalDetailsId(Long.valueOf(indivAdditionalDetailsId));
	 	        indivAdditionalDetails.setCreditCardType(creditcardtype);
	 	        indivAdditionalDetails.setCreditCardNo(creditCardNo);
	 	        indivAdditionalDetails.setCreditCardIssuedBy(issuedBy);
	 	        indivAdditionalDetails.setWebURL(webPageURL);
	 	        indivAdditionalDetails.setPassportNo(passportNo);
	 	        indivAdditionalDetails.setPassportIssuingAuthority(passportIssuingAuthority);
	 	        indivAdditionalDetails.setPassportExpiryDate(publishDateParsed);
	 	        indivAdditionalDetails.setVoterId(voterIdNo);
	 	        indivAdditionalDetails.setPan(incomeTaxPanNo);
	 	        indivAdditionalDetails.setIspName(ispName);
	 	        indivAdditionalDetails.setIspWebsite(ispWebsiteAddress);
	 	        indivAdditionalDetails.setIspUsername(ispUserName);
	 	        indivAdditionalDetails.setPersonalWebPage(webPageURL);
	 	        indivAdditionalDetails.setIndivCapital(capital);
	 	        Date date = new Date();
	 	        indivAdditionalDetails.setCreated(date);
	 	        indivAdditionalDetails.setUpdated(date);

	 	     
	 	        indivAdditionalDetails.setIntentAppId(intentApp);
	 	        indivAdditionalDetails.setStatus("Active");
	 	        indivAdditionalDetailsServiceServ.updateIndivAdditionalDetails(indivAdditionalDetails);

	 	        // Date formatter for current date
	 	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	 	        String currentDate = LocalDateTime.now().format(formatter);

	 	        // Save documents
	 	        if (file1 != null && !file1.isEmpty()) {
	 	            saveDocument(file1, "passportDocument", passportappDocId, currentDate, passportName, intentApp.getIntentAppId(), fileName1);
	 	        }
	 	        if (file2 != null && !file2.isEmpty()) {
	 	            saveDocument(file2, "idCardDocument", votarappDocId, currentDate, voterCardName, intentApp.getIntentAppId(), fileName2);
	 	        }
	 	        if (file3 != null && !file3.isEmpty()) {
	 	            saveDocument(file3, "panCardDocument", panappDocId, currentDate, panCardName, intentApp.getIntentAppId(), fileName3);
	 	        }
	 	       
	 	        
	 	        saveDocument(file4, "capitalDocument", capitalappDocId, currentDate, capitalName, intentApp.getIntentAppId(), fileName4);
	 	       

	        	 
	             System.out.println("The status is not Inactive. It is: " + indivAdditionalDetails.getStatus());
	         }
	     }

	     
	        return new ResponseEntity<>("Form updated successfully", HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Error occurred while updating the form", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private void saveDocument(MultipartFile file, String documentTitle, String appDocId, String currentDate, String docName, Long intentAppId, String fileName) throws IOException {
		
	    ApplicationDocument document = new ApplicationDocument();
	    document.setDocumentTitle(documentTitle);
	    document.setAppDocId(Long.valueOf(appDocId));
	    Date date = new Date();
	    document.setCreated(date);
	    document.setUpdated(date);
	    document.setStatus("ACTIVE");
	    document.setIntentAppId(intentAppServ.getIntentByIntentAppId(intentAppId));

	    if (file != null && !file.isEmpty()) {
	        String savedFileName = saveFile(file, documentTitle, currentDate);
	        document.setFileName(savedFileName);
	    } else {
	        document.setFileName(fileName);
	    }

	    applicationDocumentServiceServ.updateApplicationDocument(document);
	    
	}

	@PostMapping(IndividualApplicationFormAPIs.UPDATE_INDIVIDUAL_APPLICATIONS_FORM4)
	public ResponseEntity<?> updateApplication4(@RequestBody AdditionalDetailsDTO additionalDetailsDTO) throws Exception {

	    // Validate BankDetails
	    if (additionalDetailsDTO.getBankDetails() == null) {
	        return new ResponseEntity<>("Bank details cannot be null.", HttpStatus.BAD_REQUEST);
	    }

	    AdditionalDetailsDTO.BankDetails bankDetailsDTO = additionalDetailsDTO.getBankDetails();

	    // Validate Bank Account Number
	    if (bankDetailsDTO.getAccountNo() == null || bankDetailsDTO.getAccountNo().trim().isEmpty()) {
	        return new ResponseEntity<>("Bank Account Number cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	   

	    // Validate Bank Name
	    if (bankDetailsDTO.getBankName() == null || bankDetailsDTO.getBankName().trim().isEmpty()) {
	        return new ResponseEntity<>("Bank Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (!bankDetailsDTO.getBankName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Bank Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Validate Branch Name
	    if (bankDetailsDTO.getBranch() == null || bankDetailsDTO.getBranch().trim().isEmpty()) {
	        return new ResponseEntity<>("Branch Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (!bankDetailsDTO.getBranch().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Branch Name.", HttpStatus.BAD_REQUEST);
	    }

	    IntentApplication intentApp=intentAppServ.findIntentAppById(additionalDetailsDTO.getUserName());		
	    Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	    Optional<BankDetails> bankDetailsSave;
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 // Fetch the list of bank details by intentAppId
	    List<BankDetails> bankDetailsList = bankDetailsSev.findIntentWithoutAppById(intentAppId);
	
	    // Filter the list to include only active bank details and sort by the most recent created date
	    Optional<BankDetails> mostRecentActiveBankDetail = bankDetailsList.stream()
	        .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort by most recent created date
	        .findFirst(); // Get the most recent one

	    // Now use the most recent active bank detail if present
	    if (mostRecentActiveBankDetail.isPresent()) {
	        BankDetails bankDetail = mostRecentActiveBankDetail.get();
	        
	        System.out.println("bankDetails Status=======>"+bankDetail.getStatus());
	        // Perform actions based on the found bankDetail
	        if ("Inactive".equals(bankDetail.getStatus())) {
	            BankDetails newBankDetails = new BankDetails();
	            newBankDetails.setBankAccountNo(bankDetailsDTO.getAccountNo());
	            newBankDetails.setBankAccountType(bankDetailsDTO.getAccountType());
	            newBankDetails.setBankName(bankDetailsDTO.getBankName());
	            newBankDetails.setBranchName(bankDetailsDTO.getBranch());
	            newBankDetails.setStatus("Active");
	            newBankDetails.setIntentAppId(intentApp); // Assuming getIntentAppId() exists

	            bankDetailsSave = bankDetailsSev.addBankDetails(newBankDetails);
	        } else {
	            BankDetails updatedBankDetails = new BankDetails();
	            updatedBankDetails.setBankDetailsId(Long.valueOf(bankDetailsDTO.getBankDetailsId()));
	            updatedBankDetails.setBankAccountNo(bankDetailsDTO.getAccountNo());
	            updatedBankDetails.setBankAccountType(bankDetailsDTO.getAccountType());
	            updatedBankDetails.setBankName(bankDetailsDTO.getBankName());
	            updatedBankDetails.setBranchName(bankDetailsDTO.getBranch());

	            // Parse and set created and updated dates
	            Date createdDate = dateFormat.parse(bankDetailsDTO.getCreated());
	            Date updatedDate = dateFormat.parse(bankDetailsDTO.getUpdated());

	            updatedBankDetails.setCreated(createdDate);
	            updatedBankDetails.setUpdated(updatedDate);
	            updatedBankDetails.setStatus("Active");

	            updatedBankDetails.setIntentAppId(intentApp);

	            bankDetailsSave = bankDetailsSev.updateBankDetails(updatedBankDetails);
	        }
	    } else {
	        // Handle the case when no active bank details are found
	        System.out.println("No active bank details found.");
	    }
	 
//	    if (!bankDetailsSave.isPresent()) {
//	        return new ResponseEntity<>("Failed to save bank details.", HttpStatus.INTERNAL_SERVER_ERROR);
//	    }

	    // Handle Known by Other Name case and Bank Draft
	    if (additionalDetailsDTO.isKnownByOtherName()) {
	        AdditionalDetailsDTO.BankDraft bankDraftDTO = additionalDetailsDTO.getBankDraft();
	        if (bankDraftDTO == null) {
	            return new ResponseEntity<>("Bank Draft details cannot be null.", HttpStatus.BAD_REQUEST);
	        }

	        // Validate Draft Number
	        if (bankDraftDTO.getDraftNo() == null || bankDraftDTO.getDraftNo().trim().isEmpty()) {
	            return new ResponseEntity<>("Draft Number cannot be empty.", HttpStatus.BAD_REQUEST);
	        }
	        if (!bankDraftDTO.getDraftNo().matches("^[0-9]+$")) {
	            return new ResponseEntity<>("Please enter a valid Draft Number.", HttpStatus.BAD_REQUEST);
	        }

	        // Validate Amount
	        try {
	            Integer amount = Integer.valueOf(bankDraftDTO.getAmount());
	            if (amount <= 0) {
	                return new ResponseEntity<>("Amount must be greater than zero.", HttpStatus.BAD_REQUEST);
	            }
	        } catch (NumberFormatException e) {
	            return new ResponseEntity<>("Invalid amount format.", HttpStatus.BAD_REQUEST);
	        }

	        // Validate Bank Draft Bank Name
	        if (bankDraftDTO.getBankName() == null || bankDraftDTO.getBankName().trim().isEmpty()) {
	            return new ResponseEntity<>("Bank Name cannot be empty.", HttpStatus.BAD_REQUEST);
	        }
	        if (!bankDraftDTO.getBankName().matches("^[A-Za-z ]+$")) {
	            return new ResponseEntity<>("Please enter a valid Bank Name.", HttpStatus.BAD_REQUEST);
	        }

	        // Validate Issue Date
	        Date issueDate;
	        try {
	            issueDate = dateFormat.parse(bankDraftDTO.getIssueDate());
	        } catch (ParseException e) {
	            return new ResponseEntity<>("Invalid date format. Please use yyyy-MM-dd.", HttpStatus.BAD_REQUEST);
	        }
	        Optional<BankDraft> savedBankDraft;
	        BankDraft bankDraft = new BankDraft();
	        BankDraft bankDrafts=bankDraftServ.findByindivWithoutApplicationId(intentAppId);
		    if("Inactive".equals(bankDrafts.getStatus())) {
		    	
		    	  bankDraft.setDraftNo(bankDraftDTO.getDraftNo());
			        bankDraft.setAmount(Integer.valueOf(bankDraftDTO.getAmount()));
			        bankDraft.setBankName(bankDraftDTO.getBankName());
			        bankDraft.setIssueDate(issueDate);
			        bankDraft.setStatus("Active");
			        bankDraft.setIntentAppId(intentApp);
			        if( bankDraftDTO.getBankDraftId() != null && !bankDraftDTO.getBankDraftId().trim().isEmpty()) {
			        savedBankDraft=  bankDraftServ.addBankDraft(bankDraft);
			        }
		    }else {
		    	
			        if (bankDraftDTO.getBankDraftId() != null && !bankDraftDTO.getBankDraftId().trim().isEmpty()) {
			            // Update existing Bank Draft
			            Long bankDraftId = Long.valueOf(bankDraftDTO.getBankDraftId());
			            bankDraft.setBankDraftId(bankDraftId);
			            // Parse and set created and updated dates for Bank Draft
				        Date createdDraftDate = dateFormat.parse(bankDraftDTO.getCreated());
				        Date updatedDraftDate = dateFormat.parse(bankDraftDTO.getUpdated());

				        bankDraft.setCreated(createdDraftDate);
				        bankDraft.setUpdated(updatedDraftDate);
			        }
			        // Set common Bank Draft fields
			        bankDraft.setDraftNo(bankDraftDTO.getDraftNo());
			        bankDraft.setAmount(Integer.valueOf(bankDraftDTO.getAmount()));
			        bankDraft.setBankName(bankDraftDTO.getBankName());
			        bankDraft.setIssueDate(issueDate);
			        bankDraft.setStatus("Active");
			        bankDraft.setIntentAppId(intentApp);

			       
		    }
	        
	       
	        if( bankDraftDTO.getBankDraftId() != null && !bankDraftDTO.getBankDraftId().trim().isEmpty()) {
	            // Update Bank Draft
	            savedBankDraft = bankDraftServ.updateBankDraft(bankDraft);
	        } else {
	            // Add new Bank Draft
	            savedBankDraft = bankDraftServ.addBankDraft(bankDraft);
	        }

	        if (!savedBankDraft.isPresent()) {
	            return new ResponseEntity<>("Failed to save or update bank draft.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    return new ResponseEntity<>("Success", HttpStatus.OK);
	}


	@PostMapping(IndividualApplicationFormAPIs.UPDATE_INDIVIDUAL_APPLICATIONS_FORM5)
	public ResponseEntity<?> updateApplication5(@RequestBody List<LocationDetailsPayload> locationDetails) {
	    System.out.println("Received payload: " + locationDetails);

	    try {
	        if (locationDetails == null || locationDetails.isEmpty()) {
	            return new ResponseEntity<>("Location details list cannot be null or empty", HttpStatus.BAD_REQUEST);
	        }

	        for (LocationDetailsPayload locationDetail : locationDetails) {
	            if (locationDetails == null) {
	                continue; 
	            }

	            Long commAddId = intentAppServ.updateUser(locationDetail);
	            if (commAddId == null) {
	                return new ResponseEntity<>("Failed to update user for location details: " + locationDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	            }

	            AppLocation appLocation = new AppLocation();
	            appLocation.setAddressId(String.valueOf(commAddId));
	            appLocation.setLocationName(locationDetail.getLocationType());

	            String userName = locationDetail.getUserName();
	            if (userName == null || userName.isEmpty()) {
	                return new ResponseEntity<>("User name is required for location details: " + locationDetails, HttpStatus.BAD_REQUEST);
	            }

	            IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	            if (intentApp == null) {
	                return new ResponseEntity<>("Intent application not found for user: " + userName, HttpStatus.NOT_FOUND);
	            }

	            appLocation.setIntentAppId(intentApp);
	            appLocation.setLocationId(Long.valueOf(locationDetail.getLocationId()));
	            appLocation.setStatus("Active");
                Date date =new Date();
                appLocation.setCreated(date);
                appLocation.setUpdated(date);
                
	            Optional<AppLocation> result = appLocationServ.updateAppLocation(appLocation);
	            if (!result.isPresent()) {
	                return new ResponseEntity<>("Failed to save location: " + appLocation, HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        }

	        return new ResponseEntity<>("Locations update successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Failed to update the location: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping(IndividualApplicationFormAPIs.UPDATE_INDIVIDUAL_APPLICATIONS_FORM6)
	public ResponseEntity<String> updateUploadFiles(
	        @RequestParam(value = "file", required = false) MultipartFile file,
	        @RequestParam("userName") String userName,
	        @RequestParam("appDocId") String appDocId,
	        @RequestParam("intentId") String intentId,
	        @RequestParam Map<String, MultipartFile> additionalFiles,
	        @RequestParam Map<String, MultipartFile> additionalsFiles,
	        @RequestParam Map<String, String> additionalFileMetadata,
	        @RequestParam Map<String, String> additionalsFileMetadata) throws IOException {

	    System.out.println("userName===>" + userName);
		System.out.println("appDocId===>" + appDocId);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String currentDate = LocalDateTime.now().format(formatter);

		IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
		System.out.println("intentApp Status===>" + intentApp.getIntentAppId());

		List<ApplicationDocument> applicationDocuments = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
		System.out.println("applicationDocuments===>" + applicationDocuments.get(0).getFileName());
		System.out.println("applicationDocumentsSize()===>" + applicationDocuments.size());

		// Get the most recent document
		Optional<ApplicationDocument> mostRecentDocument = applicationDocuments.stream()
		        .filter(doc -> doc.getFileName().startsWith("primaryFile_") ||
		                       doc.getFileName().startsWith("additionalFile_") ||
		                       doc.getFileName().startsWith("additionalsFile_"))
		        .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
		        .findFirst();

		mostRecentDocument.ifPresentOrElse(applicationDocument -> {
		    // Check if the document is inactive
		    System.out.println("applicationDocument Status===>" + applicationDocument.getStatus());
		    if ("Inactive".equals(applicationDocument.getStatus())) {
		        try {
					handleInactiveDocument(file, intentApp, currentDate, applicationDocument, additionalFiles, additionalsFiles, additionalFileMetadata, additionalsFileMetadata);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } else {
		        try {
					handleActiveDocument(intentApp, currentDate, additionalFiles, additionalsFiles, additionalFileMetadata, additionalsFileMetadata);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}, () -> {
		    System.out.println("No matching documents found.");
		});

		return ResponseEntity.ok("Files uploaded successfully");
	}

	// Method to handle inactive document
	private void handleInactiveDocument(MultipartFile file, IntentApplication intentApp, String currentDate,
	                                     ApplicationDocument applicationDocument,
	                                     Map<String, MultipartFile> additionalFiles,
	                                     Map<String, MultipartFile> additionalsFiles,
	                                     Map<String, String> additionalFileMetadata,
	                                     Map<String, String> additionalsFileMetadata) throws Exception {
	    String primaryFileName = saveFiles(file, "primaryFile", currentDate);
	    ApplicationDocument newDocument = new ApplicationDocument();
	    newDocument.setFileName(primaryFileName);
	    newDocument.setDocumentTitle("CPSDocument");
	    newDocument.setIntentAppId(intentApp);
	    newDocument.setStatus("Active");
	    applicationDocumentServiceServ.addApplicationDocument(newDocument);

	    // Handle additional files
	    processAdditionalFiles(additionalFiles, additionalFileMetadata, intentApp, currentDate);
	    processAdditionalFiles(additionalsFiles, additionalsFileMetadata, intentApp, currentDate);
	}

	// Method to handle active document
	private void handleActiveDocument(IntentApplication intentApp, String currentDate,
	                                   Map<String, MultipartFile> additionalFiles,
	                                   Map<String, MultipartFile> additionalsFiles,
	                                   Map<String, String> additionalFileMetadata,
	                                   Map<String, String> additionalsFileMetadata) throws IOException {
	    processAdditionalFiles(additionalFiles, additionalFileMetadata, intentApp, currentDate);
	    processAdditionalFiles(additionalsFiles, additionalsFileMetadata, intentApp, currentDate);
	}

	// Generic method to process additional files
	private void processAdditionalFiles(Map<String, MultipartFile> files, Map<String, String> metadata, IntentApplication intentApp, String currentDate) throws IOException {
	    for (int i = 0; ; i++) {
	        String fileKey = "additionalFile[" + i + "].file"; // or additionalsFile
	        MultipartFile additionalFile = files.get(fileKey);
	        if (additionalFile == null || additionalFile.isEmpty()) {
	            break; // Exit loop if no more files are present
	        }

	        String fileName = metadata.get("fileNameKey"); // Replace with actual metadata key
	        String documentTitle = metadata.get("documentTitleKey"); // Replace with actual metadata key

	        System.out.println("Received file: " + additionalFile.getOriginalFilename());
	        String savedFileName = saveFiles(additionalFile, "file_" + fileName, currentDate);
	        System.out.println("File saved as: " + savedFileName);

	        // Create ApplicationDocument object and save
	        ApplicationDocument additionalDocument = new ApplicationDocument();
	        additionalDocument.setFileName(savedFileName);
	        additionalDocument.setDocumentTitle(documentTitle);
	        additionalDocument.setIntentAppId(intentApp);
	        additionalDocument.setStatus("Active");
	        applicationDocumentServiceServ.addApplicationDocument(additionalDocument);
	    }
	}

	@GetMapping(IndividualApplicationFormAPIs.VIEW_FILE)
	public ResponseEntity<Resource> viewFile(@RequestParam("id") String cpsDocuId) {
	    System.out.println(cpsDocuId);
	    String id = EncryptionUtil.decrypt(cpsDocuId);

	    try {
	        Optional<ApplicationDocument> cpsDocumentOpt = applicationDocumentServiceServ.downloadfile(Long.parseLong(id));

	        if (cpsDocumentOpt.isPresent()) {
	        	ApplicationDocument c = cpsDocumentOpt.get();
                System.out.println(c.getFileName());
                Path filePath = Paths.get(Constant.REAL_PATH + "//AdditionalDocument//Document").resolve(c.getFileName()).normalize();
                System.out.println(filePath);
                Resource resource = new UrlResource(filePath.toUri());
                System.out.println(resource);
                if (resource.exists()) {
                    String contentType = Files.probeContentType(filePath);
                    System.out.println(contentType);
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	
	@GetMapping(IndividualApplicationFormAPIs.GET_APPLICATION_DETAILS_FORM_BY_USERNAME)
	public ResponseEntity<?> getApplicationDetailsByUsername(@RequestParam("userName") String userName) {
	    try {
	        // Fetch the IntentApplication using the provided username
	    	
	    	
	        Map<String, Object> response = new HashMap<>();
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }
	        
	        response.put("intentApp", intentApp);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid userName format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving userName data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@PostMapping(IndividualApplicationFormAPIs.ADD_INDIVIDUAL_DECLARIZATION__FORM2)
	public ResponseEntity<?> addDeclarization(@RequestBody List<AppUndertakingDTO> appUndertakingDTOList) {
		 try {
		        // Validate input list
		        if (appUndertakingDTOList == null || appUndertakingDTOList.isEmpty()) {
		            return new ResponseEntity<>("No undertaking data provided.", HttpStatus.BAD_REQUEST);
		        }

		        // Loop through each AppUndertakingDTO in the list
		        for (AppUndertakingDTO appUndertakingDTO : appUndertakingDTOList) {

		            // Check if necessary fields are provided (e.g., userName)
		            if (appUndertakingDTO.getUserName() == null || appUndertakingDTO.getUserName().isEmpty()) {
		                return new ResponseEntity<>("Username is missing in one of the entries.", HttpStatus.BAD_REQUEST);
		            }

		            // Find the Intent Application by username
		            IntentApplication intentApp = intentAppServ.findIntentAppById(appUndertakingDTO.getUserName());
		            if (intentApp == null) {
		                return new ResponseEntity<>("Intent application not found for username: " + appUndertakingDTO.getUserName(), HttpStatus.NOT_FOUND);
		            }

		          

		            System.out.println("appUndertakingDTO.getAppUndertakingId()=====>"+appUndertakingDTO.getAppUndertakingId());
		            // Check if UndertakingId is null or empty, meaning a new record should be added
		                List<AppUndertakingEntity> appUndertakingList = appUndertakingServ.findByIntentAppId(intentApp.getIntentAppId());

		                // Iterate through each undertaking and check its status
		                for (AppUndertakingEntity existingUndertaking : appUndertakingList) {
		                    // Check if the status is inactive, meaning we need to update or add
		                    if ("Inactive".equals(existingUndertaking.getStatus())) {
		                        // Log the inactive undertaking details
		                        System.out.println("Inactive undertaking found: " + existingUndertaking.getUndertakingId());

		                        // Update or insert the undertaking
		                        AppUndertakingEntity newUndertaking = new AppUndertakingEntity();
	                            newUndertaking.setUndertakingId(Long.valueOf(appUndertakingDTO.getUndertakingId()));
	                            newUndertaking.setIntentAppId(intentApp);
	                            newUndertaking.setCreatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
	                            newUndertaking.setUpdatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
	                            newUndertaking.setStatus("Active");
	                            appUndertakingServ.addData(newUndertaking);
	                            intentApp.setApplicationStatus("SUBMITTED-FOR-RENEWAL");
	        	                intentAppServ.updateIntentApp(intentApp);
		                            System.out.println("Updated inactive undertaking for user: " + appUndertakingDTO.getUserName());
		                        } else {
		                            // If a different undertaking is inactive, create a new entry
		                       
		                        	existingUndertaking.setUndertakingId(Long.valueOf(appUndertakingDTO.getUndertakingId()));
		                        	existingUndertaking.setIntentAppId(intentApp);
		                        	existingUndertaking.setCreatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
		                        	existingUndertaking.setUpdatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
		                        	existingUndertaking.setStatus("Active");
		                            appUndertakingServ.updateData(existingUndertaking);
		                            intentApp.setApplicationStatus("SUBMITTED-FOR-RENEWAL");
		        	                intentAppServ.updateIntentApp(intentApp);
		                            System.out.println("Added new undertaking for user: " + appUndertakingDTO.getUserName());
		                        }
		                    }
		                }
		           
		      

		        // Return success response after processing all the items
		        return new ResponseEntity<>("All undertakings processed successfully.", HttpStatus.OK);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ResponseEntity<>("Failed to process undertakings: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		    }
	}

	

}
