package in.lms.cca.controller;

import java.io.ByteArrayOutputStream;
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.utils.PdfMerger;
import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.AppUndertakingDTO;
import in.lms.cca.dto.ApplicationDocumentDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.dto.PaymentProofDTO;
import in.lms.cca.dto.PaymentVerificationDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.AppUndertakingEntity;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.BankDetails;
import in.lms.cca.entity.BankDraft;
import in.lms.cca.entity.ESignedDocumentsEntity;
import in.lms.cca.entity.IndivAdditionalDetails;
import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.IndivEmails;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.PaymentProof;
import in.lms.cca.entity.PaymentVerification;
import in.lms.cca.entity.master.ApplicationTypeMaster;
import in.lms.cca.payload.AdditionalDetailsDTO;
import in.lms.cca.payload.EmailPayload;
import in.lms.cca.payload.IndividualStep1Payload;
import in.lms.cca.payload.LocationDTO;
import in.lms.cca.payload.LocationDetailsPayload;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.AppUndertakingService;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.BankDetailsService;
import in.lms.cca.service.BankDraftService;
import in.lms.cca.service.EsignedDocumentsService;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IIndividualApplicationFormService;
import in.lms.cca.service.IIndividualEmailsService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.service.IPaymentProof;
import in.lms.cca.service.IPaymentVerficationService;
import in.lms.cca.service.IndivAdditionalDetailsService;
import in.lms.cca.util.api.IndividualApplicationFormAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.ApplicationStatusUtil;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
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
	
	@Autowired
	private IPaymentProof iPaymentProof;
	
	@Autowired
	private IPaymentVerficationService iPaymentVerficationService;
	
	@Autowired
	private IApplicationTimeLineService appTimeLineServ;
	
	@Autowired
	private EsignedDocumentsService documentsService;
	
	

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
			intentApp.setUniqueCode(personalDetailsDTO.getUniqueCode());
			intentApp.setApplicationStatus("pending");
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

			   ApplicationTimeLine obj1 = new ApplicationTimeLine();
				obj1.setApplicationStatus("pending");
				obj1.setInitiatedBy(EncryptionUtil.encrypt(personalDetailsDTO.getCreatedBy()));
				obj1.setIntentAppId(intentApplication.get());
				
				appTimeLineServ.addApplicationTimeLine(obj1);
			
			
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
	            passportDocument.setFileName(passportFileName);  

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
	
	// download only 3 step document
	@GetMapping(IndividualApplicationFormAPIs.DOWLOAD_THREE_STEP_FILE)
	public ResponseEntity<Resource> downloadThreeStepFile(@RequestParam("id") String cpsDocuId) {
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
	public ResponseEntity<?> addApplication6(@ModelAttribute LocationDTO locationDTO) {

	    try {
	        List<LocationDetailsPayload> locationDetails = locationDTO.getLocationDetails();

	        System.out.println("size=-=--->" + locationDetails.size());
	        
	        

	        // Ensure locationDetails is not null or empty
	        if (locationDetails == null || locationDetails.isEmpty()) {
	            return new ResponseEntity<>("Location details list cannot be null or empty", HttpStatus.BAD_REQUEST);
	        }

	        // Assuming locationDetails is a list of LocationDetailsPayload objects
	        String userName = locationDetails.get(0).getUserName();
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);

	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found for user: " + userName, HttpStatus.NOT_FOUND);
	        }

	        // Track which locations we have already processed based on their addressId or locationId, not just locationType
	        Set<String> processedLocationIds = new HashSet<>();

	        // Iterate over each location detail in the provided list
	        for (LocationDetailsPayload locationDetail : locationDetails) {
	          
//	            // Skip locations if we've already processed them based on locationId or addressId
//	            if (processedLocationIds.contains(locationDetail.getLocationId()) || processedLocationIds.contains(locationDetail.getAddressId())) {
//	                continue; // Skip already processed locations by locationId or addressId
//	            }

	            // Skip certain location types if necessary (e.g., "Residential" or "Official")
	            if ("Residential".equalsIgnoreCase(locationDetail.getLocationType()) ||
	                "Official".equalsIgnoreCase(locationDetail.getLocationType())) {
	                continue;
	            }

	            // If locationId is null or empty, insert a new location
	            if (locationDetail.getLocationId() == null || locationDetail.getLocationId().isEmpty()) {

	            	  System.out.println("details=-=--->" + locationDetail.toString());
	            	
	                AddressDTO addressDTO = new AddressDTO();
	                addressDTO.setBlockNo(locationDetail.getBlockNo());
	                addressDTO.setVillage(locationDetail.getVillage());
	                addressDTO.setPostOffice(locationDetail.getPostOffice());
	                addressDTO.setSubDivision(locationDetail.getSubDivision());
	                addressDTO.setCountry(locationDetail.getCountry());
	                addressDTO.setCity(locationDetail.getCity());
	                addressDTO.setState(locationDetail.getState());
	                addressDTO.setPin(locationDetail.getPin());
	                addressDTO.setAddressId(locationDetail.getAddressId());

	                Long commAddIds = intentAppServ.addUser(addressDTO);

	                
	                System.out.println("type=--->"+locationDetail.getLocationType());
	                AppLocation newAppLocation = new AppLocation();
	                newAppLocation.setAddressId(String.valueOf(commAddIds));
	                newAppLocation.setLocationName(locationDetail.getLocationType());
	                newAppLocation.setIntentAppId(intentApp);
	                newAppLocation.setStatus("Active"); // Mark new location as active
	                Date currentDate = new Date();
	                newAppLocation.setCreated(currentDate);
	                newAppLocation.setUpdated(currentDate);

	                if (locationDetail.getCertificationDocument() != null) {
	                    String fileName = DocumentFileUtil.saveFile(locationDetail.getCertificationDocument(), "certificateDocument", String.valueOf(commAddIds), "CertificateDocument/Document");
	                    newAppLocation.setCertificateFile(fileName);
	                }

	                newAppLocation.setCertificateLevel(locationDetail.getCertificationLevel());
	                newAppLocation.setCompanyName(locationDetail.getCompanyName());

	                // Save the new location
	                Optional<AppLocation> result = appLocationServ.addAppLocation(newAppLocation);

	                // Mark this location as processed
	                processedLocationIds.add(locationDetail.getLocationId() != null ? locationDetail.getLocationId() : locationDetail.getAddressId());
	            } else {
	                // If locationId is not null, check if we need to update existing locations
	                List<AppLocation> matchingLocations = appLocationServ.findIntentById(intentApp.getIntentAppId());
	                System.out.println("type1=--->"+locationDetail.getLocationType());
	                System.out.println("Number of matching locations: " + matchingLocations.size());

	                boolean locationUpdated = false;

	                // Iterate through existing locations and update if necessary
	                for (AppLocation appLocation : matchingLocations) {
	                    if (locationDetail.getLocationType().equals(appLocation.getLocationName())) {
	                        if ("Inactive".equals(appLocation.getStatus())) {
	                            // If inactive, insert a new entry
	                            AddressDTO addressDTO = new AddressDTO();
	                            addressDTO.setBlockNo(locationDetail.getBlockNo());
	                            addressDTO.setVillage(locationDetail.getVillage());
	                            addressDTO.setPostOffice(locationDetail.getPostOffice());
	                            addressDTO.setSubDivision(locationDetail.getSubDivision());
	                            addressDTO.setCountry(locationDetail.getCountry());
	                            addressDTO.setCity(locationDetail.getCity());
	                            addressDTO.setState(locationDetail.getState());
	                            addressDTO.setPin(locationDetail.getPin());
	                            addressDTO.setAddressId(locationDetail.getAddressId());

	                            Long commAddIds = intentAppServ.addUser(addressDTO);
	                            System.out.println("type2=--->"+locationDetail.getLocationType());
	                            AppLocation newAppLocation = new AppLocation();
	                            newAppLocation.setAddressId(String.valueOf(commAddIds));
	                            newAppLocation.setLocationName(locationDetail.getLocationType());
	                            newAppLocation.setIntentAppId(intentApp);
	                            newAppLocation.setStatus("Active"); // Mark as active
	                            Date currentDate = new Date();
	                            newAppLocation.setCreated(currentDate);
	                            newAppLocation.setUpdated(currentDate);

	                            if (locationDetail.getCertificationDocument() == null) {
	                               // String fileName = DocumentFileUtil.saveFile(locationDetail.getCertificationDocument(), "certificateDocument", String.valueOf(commAddIds), "CertificateDocument/Document");
	                                newAppLocation.setCertificateFile(locationDetail.getCertificateName());
	                                newAppLocation.setCertificateLevel(locationDetail.getCertificationLevel());
		                            newAppLocation.setCompanyName(locationDetail.getCompanyName());
	                            }

	                            

	                            // Insert the new location
	                            Optional<AppLocation> result = appLocationServ.addAppLocation(newAppLocation);
	                            System.out.println("Inserted new location for: " + locationDetail.getLocationType());
	                        } else {
	                            // If active, just update the existing location
	                            Long commAddId = intentAppServ.updateUser(locationDetail);
	                            appLocation.setAddressId(String.valueOf(commAddId));
	                            appLocation.setStatus("Active"); // Ensure it's active
	                            Date currentDate = new Date();
	                            appLocation.setCreated(currentDate);
	                            appLocation.setUpdated(currentDate);

	                            // Update the location
	                            appLocationServ.updateAppLocation(appLocation);
	                            System.out.println("Updated existing location: " + appLocation.getLocationId());
	                        }
	                        locationUpdated = true;
	                        processedLocationIds.add(locationDetail.getLocationId() != null ? locationDetail.getLocationId() : locationDetail.getAddressId());
	                        break; // Stop further iteration after handling the location
	                    }
	                }

	                // If no matching location was updated, handle it separately
	                if (!locationUpdated) {
	                    AddressDTO addressDTO = new AddressDTO();
	                    addressDTO.setBlockNo(locationDetail.getBlockNo());
	                    addressDTO.setVillage(locationDetail.getVillage());
	                    addressDTO.setPostOffice(locationDetail.getPostOffice());
	                    addressDTO.setSubDivision(locationDetail.getSubDivision());
	                    addressDTO.setCountry(locationDetail.getCountry());
	                    addressDTO.setCity(locationDetail.getCity());
	                    addressDTO.setState(locationDetail.getState());
	                    addressDTO.setPin(locationDetail.getPin());
	                    addressDTO.setAddressId(locationDetail.getAddressId());

	                    Long commAddIds = intentAppServ.addUser(addressDTO);
	                    System.out.println("type4=--->"+locationDetail.getLocationType());
	                    AppLocation newAppLocation = new AppLocation();
	                    newAppLocation.setAddressId(String.valueOf(commAddIds));
	                    newAppLocation.setLocationName(locationDetail.getLocationType());
	                    newAppLocation.setIntentAppId(intentApp);
	                    newAppLocation.setStatus("Active"); // Mark as active
	                    Date currentDate = new Date();
	                    newAppLocation.setCreated(currentDate);
	                    newAppLocation.setUpdated(currentDate);

//	                    if (locationDetail.getCertificationDocument() != null) {
//	                        String fileName = DocumentFileUtil.saveFile(locationDetail.getCertificationDocument(), "certificateDocument", String.valueOf(commAddIds), "CertificateDocument/Document");
//	                        newAppLocation.setCertificateFile(fileName);
	                        newAppLocation.setCertificateLevel(locationDetail.getCertificationLevel());
		                    newAppLocation.setCompanyName(locationDetail.getCompanyName());
	                    //}

	                   

	                    // Insert the new location
	                    Optional<AppLocation> result = appLocationServ.addAppLocation(newAppLocation);
	                    processedLocationIds.add(locationDetail.getLocationId() != null ? locationDetail.getLocationId() : locationDetail.getAddressId());
	                    System.out.println("Inserted new location as fallback for: " + locationDetail.getLocationType());
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

	@GetMapping(IndividualApplicationFormAPIs.DOWLOAD_SIX_STEP_FILE)
	public ResponseEntity<Resource> downloadSixStepFile(@RequestParam("id") String cpsDocuId) {
	    System.out.println(cpsDocuId);
	    String id = EncryptionUtil.decrypt(cpsDocuId);

	    try {
	        Optional<ApplicationDocument> cpsDocumentOpt = applicationDocumentServiceServ.downloadfile(Long.parseLong(id));

	        if (cpsDocumentOpt.isPresent()) {
	        	ApplicationDocument c = cpsDocumentOpt.get();
                System.out.println(c.getFileName());
                Path filePath = Paths.get(Constant.REAL_PATH + "//CPSDocument//Document").resolve(c.getFileName()).normalize();
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
	        List<IndivApplication> indivApplication = indAppFormServ.findIntentWithoutStatusAppById(intentAppId);

	        if (indivApplication == null || indivApplication.isEmpty()) {
	            return new ResponseEntity<>("Individual Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Optional<IndivApplication> indivEmailOptional = indivApplication.stream()
	        	    .sorted((app1, app2) -> app2.getCreated().compareTo(app1.getCreated()))
	        	    .findFirst();

	    	if (indivEmailOptional.isPresent()) {
	    		response.put("application", indivEmailOptional.get());
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
            System.out.println("userName===>" + userName);

            // Fetch IntentApplication by username
            IntentApplication intentApp = intentAppServ.findIntentAppById(userName); // Assuming corrected method name
            if (intentApp == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Intent Application not found.");
            }

            Map<String, Object> response = new HashMap<>();
            String status = intentApp.getApplicationStatus();
            System.out.println("status========>" + status);

            // Fetch data based on status
            switch (status) {
                case "Edit Upon Review": // Assuming this is the intended status
                    // Fetch most recent active BankDetails and BankDraft
                    List<BankDetails> bankDetailsList = bankDetailsSev.findIntentWithoutAppById(intentApp.getIntentAppId());
                    Optional<BankDetails> mostRecentBankDetail = bankDetailsList.stream()
                            .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
                            .findFirst();

                    List<BankDraft> bankDraftList = bankDraftServ.findIntentWithoutAppById(intentApp.getIntentAppId());
                    Optional<BankDraft> mostRecentBankDraft = bankDraftList.stream()
                            .sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated()))
                            .findFirst();

                    response.put("bankDetails", mostRecentBankDetail.orElse(null));
                    response.put("bankDraft", mostRecentBankDraft.orElse(null));
                    break;

                case "Edit_Upon_Review": // Assuming this is intentional
                    // Fetch inactive details
                    BankDetails inactiveBankDetails = bankDetailsSev.findIntentAppWithStatusById(intentApp.getIntentAppId(), "Active");
                    BankDraft inactiveBankDraft = bankDraftServ.findIntentAppWithStatusById(intentApp.getIntentAppId(), "Active");

                    response.put("bankDetails", inactiveBankDetails);
                    response.put("bankDraft", inactiveBankDraft);
                    break;

                default:
                    // Fetch default details
                	List<BankDetails> bankDetailsList1 = bankDetailsSev.findIntentWithoutAppById(intentApp.getIntentAppId());
                    Optional<BankDetails> mostRecentBankDetail1 = bankDetailsList1.stream()
                            .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
                            .findFirst();

                    List<BankDraft> bankDraftList1 = bankDraftServ.findIntentWithoutAppById(intentApp.getIntentAppId());
                    Optional<BankDraft> mostRecentBankDraft1 = bankDraftList1.stream()
                            .sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated()))
                            .findFirst();

                    response.put("bankDetails", mostRecentBankDetail1.orElse(null));
                    response.put("bankDraft", mostRecentBankDraft1.orElse(null));
                    break;
            }

            // Check if response has meaningful data
            if (response.values().stream().allMatch(value -> value == null)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found.");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving Document data: " + e.getMessage());
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
	        //appLocationList = appLocationServ.findIntentAppById(intentAppId, "Inactive");
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

	    // 1. Get IntentApplication by intentId or userName (adjust as per your logic)
		IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
		System.out.println("intentApp Status===>" + intentApp.getIntentAppId());

		List<ApplicationDocument> existingDocuments = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
	    Long intentAppId = intentApp.getIntentAppId();

	    // 3. Create map for existing docs by appDocId for fast lookup
	    Map<Long, ApplicationDocument> existingDocsMap = existingDocuments.stream()
	            .filter(doc -> doc.getAppDocId() != null)
	            .collect(Collectors.toMap(ApplicationDocument::getAppDocId, Function.identity()));
	    
	    
	    System.out.println("existingDocsMap....-->"+existingDocsMap.toString());

	    // 4. Process files with existing documents for additionalFiles
	    processFilesWithExistingDocuments(additionalFiles, additionalFileMetadata, intentApp, existingDocsMap);

	    // 5. Process files with existing documents for additionalsFiles
	    processFilesWithExistingDocuments(additionalsFiles, additionalsFileMetadata, intentApp, existingDocsMap);

	    // 6. Mark any documents NOT present in incoming metadata as Inactive
	    processAndSyncDocuments(
	    	    existingDocsMap,          // Map<Long, ApplicationDocument>
	    	    additionalsFiles,                    // Map<String, MultipartFile>
	    	    intentApp,                // IntentApplication
	    	    additionalsFileMetadata   // Map<String, String>
	    	);

	    return ResponseEntity.ok("Files uploaded and documents updated successfully");
	}
	
	private void processAndSyncDocuments(Map<Long, ApplicationDocument> existingDocs,Map<String, MultipartFile> files,         
	        IntentApplication intentApp,Map<String, String>        metadata  ) {

	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	    String ts = LocalDateTime.now().format(fmt);

	    System.out.println("Starting syncDocumentsDynamically...");
	    System.out.println("Metadata keys: " + metadata.keySet());
	    System.out.println("File keys: " + files.keySet());
	    System.out.println("Existing Docs IDs: " + existingDocs.keySet());

	    // 1️⃣ discover record prefixes on the fly
	    Set<String> prefixes = metadata.keySet().stream()
	            .filter(k -> k.endsWith(".fileName"))
	            .map(k -> k.substring(0, k.indexOf(".fileName")))
	            .collect(Collectors.toSet());

	    System.out.println("Discovered prefixes: " + prefixes);

	    for (String prefix : prefixes) {
	        // ----- read fields with this prefix -----
	        String fileName      = metadata.get(prefix + ".fileName");
	        String title         = metadata.getOrDefault(prefix + ".documentTitleName",
	                                                     metadata.get(prefix + ".DocumentName"));
	        String appDocIdStr   = metadata.get(prefix + ".appDocId");
	        MultipartFile file   = files.get(prefix + ".file");   // key must match your FormData

	        System.out.println("\nProcessing prefix: " + prefix);
	        System.out.println("FileName: " + fileName);
	        System.out.println("Title: " + title);
	        System.out.println("AppDocId string: " + appDocIdStr);
	        System.out.println("MultipartFile: " + (file != null ? file.getOriginalFilename() : "null"));

	        if (file == null || file.isEmpty()) {
	            System.out.println("No file found or file is empty, skipping this prefix.");
	            continue;   // skip if no file sent
	        }

	        // ----- save physical file -----
	        String saved;
	        try { 
	            saved = saveFiles(file, "additionalFile_" + fileName, ts);
	            System.out.println("Saved file name: " + saved);
	        }
	        catch (IOException ex) { 
	            ex.printStackTrace(); 
	            continue; 
	        }

	        // ----- update or insert -----
	        if (appDocIdStr != null && !appDocIdStr.isBlank()) {
	            try {
	                Long id = Long.parseLong(appDocIdStr.trim());
	                ApplicationDocument doc = existingDocs.get(id);
	                if (doc != null) {                 // ✅ update
	                    System.out.println("Updating existing document with ID: " + id);
	                    doc.setFileName(saved);
	                    doc.setDocumentTitle(title);
	                    doc.setStatus("Active");
	                    doc.setUpdated(new Date());
	                    doc.setUpdatedBy(intentApp.getUpdatedBy());
	                    applicationDocumentServiceServ.updateApplicationDocument(doc);
	                    existingDocs.remove(id);       // mark as processed
	                    continue;
	                } else {
	                    System.out.println("No existing document found for ID: " + id + ", will insert new.");
	                }
	            } catch (NumberFormatException ignored) { 
	                System.out.println("Invalid appDocId format: " + appDocIdStr + ", will insert new.");
	            }
	        } else {
	            System.out.println("No appDocId provided, will insert new document.");
	        }

	        // ➕ insert new
	        System.out.println("Inserting new document.");
	        ApplicationDocument newDoc = new ApplicationDocument();
	        newDoc.setFileName(saved);
	        newDoc.setDocumentTitle(title);
	        newDoc.setIntentAppId(intentApp);
	        newDoc.setStatus("Active");
	        newDoc.setCreated(new Date());
	        newDoc.setCreatedBy(intentApp.getCreatedBy());
	        applicationDocumentServiceServ.addApplicationDocument(newDoc);
	    }

	    // 3️⃣ any leftover docs in existingDocs were NOT in the request → mark Inactive
	    System.out.println("\nMarking absent documents as Inactive:");
	    existingDocs.values().forEach(d -> {
	        System.out.println("Marking doc ID " + d.getAppDocId() + " inactive.");
	        d.setStatus("Inactive");
	        d.setUpdated(new Date());
	        applicationDocumentServiceServ.updateApplicationDocument(d);
	    });

	    System.out.println("syncDocumentsDynamically completed.");
	}

	
	

	private void processFilesWithExistingDocuments(
	        Map<String, MultipartFile> files,
	        Map<String, String> metadata,
	        IntentApplication intentApp,
	        Map<Long, ApplicationDocument> existingDocsMap) {

	    for (int i = 0; ; i++) {
	        String fileKey = "additionalFile[" + i + "].file";
	        MultipartFile file = files.get(fileKey);
	        if (file == null || file.isEmpty()) {
	            // No more files found in this pattern
	            break;
	        }

	        String baseKey = "additionalFile[" + i + "]";
	        String fileNameKey = baseKey + ".fileName";
	        String documentTitleKey = baseKey + ".documentTitleName";
	        String appDocIdKey = baseKey + ".appDocId";

	        String fileName = metadata.get(fileNameKey);
	        String documentTitle = metadata.get(documentTitleKey);
	        String appDocIdStr = metadata.get(appDocIdKey);

	        String savedFileName;
	        try {
	            savedFileName = saveFiles(file, "file_" + fileName, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
	        } catch (IOException e) {
	            e.printStackTrace();
	            continue; // skip this file if error
	        }

	        if (appDocIdStr != null && !appDocIdStr.trim().isEmpty()) {
	            try {
	                Long appDocId = Long.parseLong(appDocIdStr);
	                if (existingDocsMap.containsKey(appDocId)) {
	                    // Update existing document
	                    ApplicationDocument existingDoc = existingDocsMap.get(appDocId);
	                    existingDoc.setFileName(savedFileName);
	                    existingDoc.setDocumentTitle(documentTitle);
	                    existingDoc.setStatus("Active");
	                    existingDoc.setUpdated(new Date());
	                    existingDoc.setUpdatedBy(intentApp.getUpdatedBy());
	                    applicationDocumentServiceServ.updateApplicationDocument(existingDoc);

	                    // Remove updated doc from map to mark as processed
	                    existingDocsMap.remove(appDocId);
	                    continue;
	                }
	            } catch (NumberFormatException e) {
	                // Log and treat it as a new document if appDocId is not a number
	                e.printStackTrace();
	            }
	        }

	        // Insert new document if appDocId is blank or not matched
	        ApplicationDocument newDoc = new ApplicationDocument();
	        newDoc.setFileName(savedFileName);
	        newDoc.setDocumentTitle(documentTitle);
	        newDoc.setIntentAppId(intentApp);
	        newDoc.setStatus("Active");
	        newDoc.setCreated(new Date());
	        newDoc.setCreatedBy(intentApp.getCreatedBy());
	        applicationDocumentServiceServ.addApplicationDocument(newDoc);
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

	            // Define the paths for all three directories
	            Path additionalDocPath = Paths.get(Constant.REAL_PATH + "//AdditionalDocument//Document").resolve(c.getFileName()).normalize();
	            Path cpsDocPath = Paths.get(Constant.REAL_PATH + "//CPSDocument//Document").resolve(c.getFileName()).normalize();
	            Path firmDocPath = Paths.get(Constant.REAL_PATH + "//FirmDocuments//Documents").resolve(c.getFileName()).normalize();

	            System.out.println("Checking Additional Document Path: " + additionalDocPath);
	            System.out.println("Checking CPS Document Path: " + cpsDocPath);
	            System.out.println("Checking Firm Document Path: " + firmDocPath);

	            // Check the file path in the three directories
	            Path filePath = null;
	            if (Files.exists(additionalDocPath)) {
	                filePath = additionalDocPath;  // If file exists in the first path
	            } else if (Files.exists(cpsDocPath)) {
	                filePath = cpsDocPath;  // If file exists in the second path
	            } else if (Files.exists(firmDocPath)) {
	                filePath = firmDocPath;  // If file exists in the third path
	            }

	            if (filePath != null) {
	                // If file exists in one of the directories, load and return it
	                Resource resource = new UrlResource(filePath.toUri());
	                System.out.println("Resource found at: " + resource);

	                if (resource.exists()) {
	                    // Get the content type of the file
	                    String contentType = Files.probeContentType(filePath);
	                    System.out.println("Content Type: " + contentType);

	                    return ResponseEntity.ok()
	                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
	                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
	                            .body(resource);
	                } else {
	                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // File exists but unable to load
	                }
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // File not found in any of the three paths
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Document not found in the database
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Handle server error
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
	            if (appUndertakingDTO.getAppUndertakingId() == null || appUndertakingDTO.getAppUndertakingId().isEmpty()) {
	                // Create a new AppUndertakingEntity and map fields
	                AppUndertakingEntity newAppUndertakingEntity = new AppUndertakingEntity();
	                newAppUndertakingEntity.setIntentAppId(intentApp);
	                newAppUndertakingEntity.setUndertakingId(Long.valueOf(appUndertakingDTO.getUndertakingId()));
	                newAppUndertakingEntity.setCreatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
	                newAppUndertakingEntity.setUpdatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
	                newAppUndertakingEntity.setStatus("Active");
	              //add application timeline
					ApplicationTimeLine obj = new ApplicationTimeLine();
					obj.setApplicationStatus("Submitted");
					obj.setInitiatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
					obj.setIntentAppId(intentApp);
					
					appTimeLineServ.addApplicationTimeLine(obj);
	                // Add the new undertaking to the database
	                appUndertakingServ.addData(newAppUndertakingEntity);
	                
	                // Set application status and update it
	                
	                
	                PaymentVerification paymentVerification = iPaymentVerficationService.getByIntentId(intentApp);

	                if (paymentVerification == null) {
	                    intentApp.setApplicationStatus("Submitted");
	                } else {
	                    intentApp.setApplicationStatus("Edit_Upon_Review");
	                    ApplicationTimeLine obj1 = new ApplicationTimeLine();
						obj1.setApplicationStatus("Edit_Upon_Review");
						obj1.setInitiatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
						obj1.setIntentAppId(intentApp);
						
						appTimeLineServ.addApplicationTimeLine(obj1);
	                    
	                }

	                intentAppServ.updateIntentApp(intentApp);
	                System.out.println("Added new undertaking for user: " + appUndertakingDTO.getUserName());
	            } else {
	                // UndertakingId is provided, so we need to fetch all associated undertakings by intentAppId
	                List<AppUndertakingEntity> appUndertakingList = appUndertakingServ.findByIntentAppId(intentApp.getIntentAppId());
//	                if (appUndertakingList == null || appUndertakingList.isEmpty()) {
//	                    return new ResponseEntity<>("No undertakings found for intent application ID: " + intentApp.getIntentAppId(), HttpStatus.NOT_FOUND);
//	                }

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
                            intentApp.setApplicationStatus("Edit_Upon_Review");
                            ApplicationTimeLine obj1 = new ApplicationTimeLine();
    						obj1.setApplicationStatus("Edit_Upon_Review");
    						obj1.setInitiatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
    						obj1.setIntentAppId(intentApp);
    						
    						appTimeLineServ.addApplicationTimeLine(obj1);
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
	                            intentApp.setApplicationStatus("Edit_Upon_Review");
	                            ApplicationTimeLine obj1 = new ApplicationTimeLine();
	    						obj1.setApplicationStatus("Edit_Upon_Review");
	    						obj1.setInitiatedBy(EncryptionUtil.encrypt(appUndertakingDTO.getUserName()));
	    						obj1.setIntentAppId(intentApp);
	    						
	    						appTimeLineServ.addApplicationTimeLine(obj1);
	        	                intentAppServ.updateIntentApp(intentApp);
	                            System.out.println("Added new undertaking for user: " + appUndertakingDTO.getUserName());
	                        }
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

	
	  @GetMapping(IndividualApplicationFormAPIs.GET_ALL_INTENT_DETAILS)
	    public ResponseEntity<?> getAllServices() {
	        List<IntentApplication> intentApplications = intentAppServ.getAllServiceMaster();
	        return new ResponseEntity<>(intentApplications, HttpStatus.OK);
	    }

	
	  @GetMapping(IndividualApplicationFormAPIs.APPLICATION_FORM_GENERATE_FOR_INDIVIDUAL)
		public ResponseEntity<?> generatePdf(@RequestParam("intentId") String intentId) {
			
			
		 
		   Long intentAppId = Long.valueOf(intentId);
		   IntentApplication intentApplication=intentAppServ.getIntentByIntentAppId(intentAppId);
		  
		   List<IndivApplication> indivApplication = indAppFormServ.findIntentWithoutStatusAppById(intentAppId);
	        
	        Optional<IndivApplication> indivEmailOptional = indivApplication.stream()
	        	    .sorted((app1, app2) -> app2.getCreated().compareTo(app1.getCreated())) // Sorting in descending order
	        	    .findFirst(); 
	    		
	    		  List<IndivEmails> indivEmails = indEmailServ.findByindivApplicationId(intentAppId);
	    		  IndivApplication indivApplication1 = indivEmailOptional.get();
	    		  
	    		  
	    		  List<AppLocation> appLocation = appLocationServ.findIntentWithoutStatusAppById(intentAppId);
	  	        if (appLocation == null || appLocation.isEmpty()) {
	  	            return new ResponseEntity<>("App Location not found.", HttpStatus.NOT_FOUND);
	  	        }

	  	        
//	  	        //esigned Document
	  	         ESignedDocumentsEntity documentsEntity = new ESignedDocumentsEntity();
				documentsEntity.setUserName(intentApplication.getUserName());
				documentsEntity.setStatus("Active");
				Optional<ESignedDocumentsEntity> optional = documentsService.addEsignDocument(documentsEntity);
				if (!optional.isPresent()) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save eSigned document");
				}
	  	       
	  	        List<AppLocation> sortedAppLocations = appLocation.stream()
	  	            .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) 
	  	            .collect(Collectors.toList()); 

	  	      
	  	        IndivAddressDTO indivAddressDTO = new IndivAddressDTO();

	  	       
	  	        for (AppLocation location : sortedAppLocations) {
	  	            String addressIdStr = location.getAddressId();
	  	            if (addressIdStr != null && !addressIdStr.isEmpty()) {
	  	                try {
	  	                    String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	  	                    AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);
	  	                    if ("Residential".equals(location.getLocationName())) {
	  	                        indivAddressDTO.setResidential(addressDTO); 
	  	                    } else if ("Official".equals(location.getLocationName())) {
	  	                        indivAddressDTO.setOfficial(addressDTO); 
	  	                    }
	  	                } catch (Exception e) {
	  	                    System.out.println("Error processing Address ID: " + addressIdStr);
	  	                }
	  	            } else {
	  	                System.out.println("Address ID is null or empty for this location.");
	  	            }
	  	        }

	  	        System.out.println("indivAddressDTO-=-=-=--->"+indivAddressDTO.getOfficial());
	  	        
	  	   // Fetch Individual Additional Details
		        List<IndivAdditionalDetails> indivAdditionalDetails = indivAdditionalDetailsServiceServ.findIntentWithoutStatusAppById(intentAppId);
		        if (indivAdditionalDetails == null) {
		            return new ResponseEntity<>("Additional details not found.", HttpStatus.NOT_FOUND);
		        }

		        Optional<IndivAdditionalDetails> mostRecentActive = indivAdditionalDetails.stream()
		                .sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated())) // Sort by most recent created date
		                .findFirst();
		        // Fetch Application Documents associated with the IndivApplication
		        // Check if the method expects the entire IntentApplication or just its ID
		        List<ApplicationDocument> applicationDocument = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentAppId); // Pass intentApp if method expects it
		        if (applicationDocument == null || applicationDocument.isEmpty()) {
		            return new ResponseEntity<>("No documents found.", HttpStatus.NOT_FOUND);
		        }
	System.out.println("applicationDocument=====>"+applicationDocument.get(0).getDocumentTitle());
	List<ApplicationDocument> sortedActiveDocuments = applicationDocument.stream()
	.sorted((d1, d2) -> d2.getCreated().compareTo(d1.getCreated())) // Sort by most recent created date
	.collect(Collectors.toList()); // Collect into a list
		        
		        // Extract file names from the application documents
		        List<String> fileNames = applicationDocument.stream()
		            .map(ApplicationDocument::getFileName)  // Assuming ApplicationDocument has a getFileName() method
		            .collect(Collectors.toList());

	  	        
		        IndivAdditionalDetails additionalDetails =mostRecentActive.get();
	  	        
	  	        
	  	        
			
		    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

		       
		        StringBuilder contentBuilder = new StringBuilder();
		        contentBuilder.append("<html>");
		        contentBuilder.append("<head>");
		        contentBuilder.append("<style>");
		        contentBuilder.append("@page { margin: 60pt;}"); // Page margin
		        contentBuilder.append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; } ");
		        contentBuilder.append("p {text-align: justify; text-justify: inter-word;} ");
		        contentBuilder.append("div {text-align: justify; text-justify: inter-word;} ");
		        contentBuilder.append("table{border-collapse: collapse;} ");
		        contentBuilder.append("td{padding-left: 5px;} ");
		        contentBuilder.append("</style>");
		        contentBuilder.append("</head>");
		        contentBuilder.append("<body>");

		        String prefix = "CCA/CA";
		        Long serialNumber = intentApplication.getIntentAppId();
		        String formattedsDate = new SimpleDateFormat("yyyyMMdd").format(new Date()); // Format only date

		        String formattedDates = new SimpleDateFormat("yyyy-MM-dd").format(intentApplication.getUpdated());
		        contentBuilder.append("<span style='font-weight: bold;'>Date:</span> ")
		                      .append(formattedDates) 
		                      .append("<br>");

		        contentBuilder.append("<span style='font-weight: bold;'>Intent Code:</span> ")
		                      .append(intentApplication.getUniqueCode())
		                      .append("<br>");

		        contentBuilder.append("<span style='font-weight: bold;'>Application Number:</span> ")
		                      .append(prefix)
		                      .append("/")
		                      .append(formattedsDate)
		                      .append("/")
		                      .append(serialNumber)
		                      .append("<br>");

		        
		        
		        // Step 2: Header for the document
		        contentBuilder.append("<h3>Form for Application for Grant of Licence to be a Certifying Authority</h3>");
		        contentBuilder.append("<h4 style='text-decoration: underline;'>For Individual</h4>");
		        
		        
		        // Add form fields (Full Name, Address, etc.)
		        //contentBuilder.append("<p>1. Full Name: " + indivApplication1.getFirstName1() +""+indivApplication1.getMiddleName1()+""+ indivApplication1.getLastName1() + "</p>");
		        
		        
		        contentBuilder.append("<p><b>1. Full Name *</b></p>");
		        contentBuilder.append("<p>Last Name/Surname: " +  indivApplication1.getLastName1() + "</p>");
		        contentBuilder.append("<p>First Name: " + indivApplication1.getFirstName1() + "</p>");
		        contentBuilder.append("<p>Middle Name: " + indivApplication1.getMiddleName1()+ "</p>");
		        
		        String firstName2 = indivApplication1.getFirstName2(); // Get the value of firstName2

		     // Check if firstName2 is null
		     if (firstName2 == null) {
		         contentBuilder.append("<p><b>2. Have you ever been known by any other name?</b> No</p>");
		         contentBuilder.append("<p>Last Name/Surname:  </p>");
			        contentBuilder.append("<p>First Name:</p>");
			        contentBuilder.append("<p>Middle Name:</p>");
		         
		     } else {
		         contentBuilder.append("<p><b>2. Have you ever been known by any other name?</b> Yes</p>");
		         contentBuilder.append("<p>Last Name/Surname: " +  indivApplication1.getLastName2() + "</p>");
			        contentBuilder.append("<p>First Name: " + indivApplication1.getFirstName2() + "</p>");
			        contentBuilder.append("<p>Middle Name: " + indivApplication1.getMiddleName2()+ "</p>");
		         
		     }

		     contentBuilder.append("<p><b>3. Address</b></p>");
		     contentBuilder.append("<p><b>A. Residential Address *</b></p>");
		     contentBuilder.append("<p>Flat/Door/Block No:"+indivAddressDTO.getResidential().getBlockNo() +"</p>");
		     contentBuilder.append("<p>Name of Premises/Building/Village:"+indivAddressDTO.getResidential().getVillage() +"</p>");
		     contentBuilder.append("<p>Road/Street/Lane/Post Office: "+indivAddressDTO.getResidential().getPostOffice() +"</p>");
		     contentBuilder.append("<p>Area/Locality/Taluka/Sub-Division: "+indivAddressDTO.getResidential().getSubDivision() +"</p>");
		     contentBuilder.append("<p>Town/City/District: "+indivAddressDTO.getResidential().getCity() +"</p>");
		     contentBuilder.append("<p>State/Union Territory: "+indivAddressDTO.getResidential().getState() +"</p>");
		     contentBuilder.append("<p>Pin: "+indivAddressDTO.getResidential().getPin() +"</p>");
		     contentBuilder.append("<p>Telephone No:"+indivAddressDTO.getResidential().getTelephoneNo() +" </p>");
		     contentBuilder.append("<p>Fax: "+indivAddressDTO.getResidential().getFax() +"</p>");
		     contentBuilder.append("<p>Mobile Phone No: "+indivAddressDTO.getResidential().getMobile() +"</p>");

		     contentBuilder.append("<p><b>B. Office Address *</b></p>");
		     contentBuilder.append("<p>Flat/Door/Block No:"+indivAddressDTO.getOfficial().getBlockNo() +"</p>");
		     contentBuilder.append("<p>Name of Premises/Building/Village:"+indivAddressDTO.getOfficial().getVillage() +"</p>");
		     contentBuilder.append("<p>Road/Street/Lane/Post Office: "+indivAddressDTO.getOfficial().getPostOffice() +"</p>");
		     contentBuilder.append("<p>Area/Locality/Taluka/Sub-Division: "+indivAddressDTO.getOfficial().getSubDivision() +"</p>");
		     contentBuilder.append("<p>Town/City/District: "+indivAddressDTO.getOfficial().getCity() +"</p>");
		     contentBuilder.append("<p>State/Union Territory: "+indivAddressDTO.getOfficial().getState() +"</p>");
		     contentBuilder.append("<p>Pin: "+indivAddressDTO.getOfficial().getPin() +"</p>");
		     contentBuilder.append("<p>Telephone No:"+indivAddressDTO.getOfficial().getTelephoneNo() +" </p>");
		     contentBuilder.append("<p>Fax: "+indivAddressDTO.getOfficial().getFax() +"</p>");
		     contentBuilder.append("<p>Mobile Phone No: "+indivAddressDTO.getOfficial().getMobile() +"</p>");

		     contentBuilder.append("<p><b>4. Address for Communication *</b></p>");
		     contentBuilder.append("<p>Tick ✓ as applicable: ");
//		     contentBuilder.append("<input type='checkbox' " + ("A ? checked) + ))";
//		     contentBuilder.append("<input type='checkbox' " + ("B".equals(formData.get("communicationAddress")) ? "checked" : "") + "> B. Office Address </p>");

		     
		     
		        contentBuilder.append("<p><b>5. Father's Name *</b></p>");
		        contentBuilder.append("<p>Last Name/Surname: " +  indivApplication1.getFlastName() + "</p>");
		        contentBuilder.append("<p>First Name: " + indivApplication1.getFfirstName() + "</p>");
		        contentBuilder.append("<p>Middle Name: " + indivApplication1.getFmiddleName()+ "</p>");
		     
		        contentBuilder.append("<p><b>6. Sex (For Individual Applicant Only)*</b>:"+indivApplication1.getGender()+"</p>");
		        contentBuilder.append("<p><b>7. Date Of Birth (dd/mm/yyyy)*</b>:"+indivApplication1.getDob()+"</p>");
		        contentBuilder.append("<p><b>8. Nationality*</b>:"+indivApplication1.getNationality()+"</p>");
		        
		        
		        contentBuilder.append("<p><b>9. Credit Card Details</b></p>");
		        contentBuilder.append("<p>Credit Card Type: " + additionalDetails.getCreditCardType() + "</p>");
		        contentBuilder.append("<p>Credit Card No.: " + additionalDetails.getCreditCardNo() + "</p>");
		        contentBuilder.append("<p>Issued By: " + additionalDetails.getCreditCardIssuedBy() + "</p>");
		        
		    
		        StringBuilder emailAddresses = new StringBuilder();

		      
		        for (IndivEmails indivEmail : indivEmails) {
		            if (emailAddresses.length() > 0) {
		                emailAddresses.append(", "); 
		            }
		            emailAddresses.append(indivEmail.getEmailId()); // Append the email address
		        }

		        contentBuilder.append("<p><b>10. E-mail Address</b>: " + emailAddresses.toString() + "</p>");
		        contentBuilder.append("<p><b>11. Web URL Address</b></p>");
		        contentBuilder.append("<p>URL: " +additionalDetails.getWebURL() + "</p>");

		        contentBuilder.append("<p><b>12. Passport Details</b></p>");
		        contentBuilder.append("<p>Passport No.: " + additionalDetails.getPassportNo() + "</p>");
		        contentBuilder.append("<p>Passport Issuing Authority: " + additionalDetails.getPassportIssuingAuthority() + "</p>");
		        contentBuilder.append("<p>Passport Expiry Date (dd/mm/yyyy): " + additionalDetails.getPassportExpiryDate() + "</p>");

		        contentBuilder.append("<p><b>13. Voter's Identity Card No.</b></p>");
		        contentBuilder.append("<p>Voter ID: " + additionalDetails.getVoterId() + "</p>");

		        contentBuilder.append("<p><b>14. Income Tax PAN No.</b></p>");
		        contentBuilder.append("<p>PAN: " + additionalDetails.getPan()+"</p>");

		        contentBuilder.append("<p><b>15. ISP Details</b></p>");
		        contentBuilder.append("<p>ISP Name: " + additionalDetails.getIspName() + "</p>");
		        contentBuilder.append("<p>ISP's Website Address, if any: " + additionalDetails.getIspWebsite() + "</p>");
		        contentBuilder.append("<p>Your User Name at ISP, if any: " + additionalDetails.getIspUsername() + "</p>");

		        contentBuilder.append("<p><b>16. Personal Web Page URL Address, if any</b></p>");
		        contentBuilder.append("<p>Web Page URL: " + additionalDetails.getWebURL() + "</p>");

		        contentBuilder.append("<p><b>17. Capital in the Business or Profession</b></p>");
		        contentBuilder.append("<p>Capital (in Rs): " + additionalDetails.getIndivCapital() + "</p>");
		        
		        contentBuilder.append("</body>");
		        contentBuilder.append("</html>");

		        
		        byte[] pdfBytes = generatePdfFromHtml(contentBuilder.toString());
		        ByteArrayOutputStream finalOutputStream = mergeWithExistingPdf(pdfBytes, applicationDocument);

		     // Return PDF
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=" + optional.get().geteSignedId() + "")
						.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
						.contentType(MediaType.APPLICATION_PDF).body(finalOutputStream.toByteArray());

		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                .body("Error in downloading cover letter.");
		    }
		}

		private byte[] generatePdfFromHtml(String htmlContent) {
		    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		        ConverterProperties converterProperties = new ConverterProperties();
		        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
		        return outputStream.toByteArray();
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("Error converting HTML to PDF");
		    }
		}

		private ByteArrayOutputStream mergeWithExistingPdf(byte[] generatedPdfBytes, List<ApplicationDocument> applicationDocument) {
		    List<String> fileNames = applicationDocument.stream()
		        .map(ApplicationDocument::getFileName)
		        .collect(Collectors.toList());

		    HashMap<String, String> index = new LinkedHashMap<>();
		    int currentPageNum = 2;

		    try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {

		        // Open generated PDF
		        PdfDocument generatedPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(generatedPdfBytes)));
		        List<PdfDocument> pdfDocsToMerge = new ArrayList<>();
		        pdfDocsToMerge.add(generatedPdf);
		        index.put("Main Document", currentPageNum + "-" + (currentPageNum + generatedPdf.getNumberOfPages() - 1));
		        currentPageNum += generatedPdf.getNumberOfPages();

		        String[] basePaths = {
		            // Constant.REAL_PATH + "//FirmDocuments//Documents",
		            Constant.REAL_PATH + "//CPSDocument//Document"
		        };

		        // We'll create a list of placeholders (blank pages) for missing files
		        List<String> placeholderTitles = new ArrayList<>();

		        // Loop over each document
		        for (int i = 0; i < applicationDocument.size(); i++) {
		            String fileName = fileNames.get(i);
		            String docTitle = applicationDocument.get(i).getDocumentTitle();
		            if (docTitle == null || docTitle.isEmpty()) {
		                docTitle = fileName;
		            }

		            Path filePath = null;
		            boolean fileFound = false;

		            for (String basePath : basePaths) {
		                filePath = Paths.get(basePath).resolve(fileName).normalize();
		                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
		                    fileFound = true;
		                    break;
		                }
		            }

		            if (fileFound) {
		                PdfDocument pdfInput = new PdfDocument(new PdfReader(filePath.toString()));
		                pdfDocsToMerge.add(pdfInput);

		                placeholderTitles.add(docTitle);
		                
		                index.put(docTitle, currentPageNum + "-" + (currentPageNum + pdfInput.getNumberOfPages() - 1));
		                currentPageNum += pdfInput.getNumberOfPages();

		            } else {
		                // File missing: add placeholder page with title
		                placeholderTitles.add(docTitle);
		                // Add 1 page for the placeholder
		                index.put(docTitle, String.valueOf(currentPageNum));
		                currentPageNum += 1;
		            }
		        }

		        // Generate TOC PDF bytes
		        StringBuilder contentBuilder = new StringBuilder();
		        contentBuilder.append("<h2 style='text-align:center;'>Table of Contents</h2>");
		        contentBuilder.append("<table border='1' cellspacing='0' cellpadding='5' width='100%'>");
		        contentBuilder.append("<thead><tr><th>Section</th><th>Page(s)</th></tr></thead>");
		        contentBuilder.append("<tbody>");

		        int tocIndex = 1;
		        for (Map.Entry<String, String> entry : index.entrySet()) {
		            contentBuilder.append("<tr>")
		                .append("<td>").append(tocIndex).append(". ").append(entry.getKey()).append("</td>")
		                .append("<td>").append(entry.getValue()).append("</td>")
		                .append("</tr>");
		            tocIndex++;
		        }

		        contentBuilder.append("</tbody></table>");

		        byte[] tocPdfBytes = generatePdfFromHtml(contentBuilder.toString());
		        PdfDocument tocPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(tocPdfBytes)));

		        // Create final PDF document
		        PdfDocument finalPdfDocument = new PdfDocument(new PdfWriter(finalOutputStream));
		        PdfMerger pdfMerger = new PdfMerger(finalPdfDocument);

		        // Merge TOC first
		        pdfMerger.merge(tocPdf, 1, tocPdf.getNumberOfPages());

		        // Merge generatedPdf and other actual PDFs
		        for (PdfDocument pdfDoc : pdfDocsToMerge) {
		            pdfMerger.merge(pdfDoc, 1, pdfDoc.getNumberOfPages());
		        }

		        // Now add placeholder pages for missing files with just the title
		        for (String title : placeholderTitles) {
		        	
		        	System.out.println("------>"+title);
		        	
		            PdfPage blankPage = finalPdfDocument.addNewPage();
		            PdfCanvas canvas = new PdfCanvas(blankPage);
		            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		            canvas.beginText().setFontAndSize(font, 12).moveText(20, blankPage.getPageSize().getTop() - 15) // Position	
                    .showText(title).endText();
		        }

		        tocPdf.close();
		        for (PdfDocument pdfDoc : pdfDocsToMerge) {
		            pdfDoc.close();
		        }
		        finalPdfDocument.close();

		        byte[] mergedPdfBytes = finalOutputStream.toByteArray();
		        // Add page numbers and watermark
		        return addPageNumber(mergedPdfBytes);

		    } catch (IOException e) {
		        e.printStackTrace();
		        throw new RuntimeException("Error merging PDFs", e);
		    }
		}
	 
		private ByteArrayOutputStream addPageNumber(byte[] generatedPdfBytes) throws IOException {
		    try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {
		        if (generatedPdfBytes == null || generatedPdfBytes.length == 0) {
		            throw new IOException("Generated PDF is empty or corrupted");
		        }

		        try (PdfReader reader = new PdfReader(new ByteArrayInputStream(generatedPdfBytes));
		             PdfWriter writer = new PdfWriter(finalOutputStream);
		             PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

		            int totalPages = pdfDoc.getNumberOfPages();
		            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		            float fontSize = 8f;

		            for (int i = 1; i <= totalPages; i++) {
		                PdfPage page = pdfDoc.getPage(i);
		                PdfCanvas canvas = new PdfCanvas(page);

		             // Add page number
						canvas.beginText().setFontAndSize(font, fontSize).moveText(page.getPageSize().getRight() - 100, 20)
								.showText("Page " + i + " of " + totalPages).endText();

		                // Add watermark diagonally (light gray, semi-transparent)
		                PdfCanvas watermarkCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

		                AffineTransform transform = AffineTransform.getRotateInstance(
							    Math.toRadians(45),
							    26,
							    150
							);

		             
		                watermarkCanvas.saveState().setFillColorGray(0.75f) // light gray color
						.setExtGState(new PdfExtGState().setFillOpacity(0.3f)) // opacity
						.beginText().setFontAndSize(font, 50).setTextMatrix(transform).moveText(0, 0)
		                        .showText("Individual Application Report")
		                        .endText()
		                        .restoreState();
		            }

		            return finalOutputStream;
		        }
		    }
		}

		@GetMapping(IndividualApplicationFormAPIs.GET_ALL_ACTIVE_APPLOCATION) 
		public ResponseEntity<List<AppLocation>> getAllAppLocation() {
			List<AppLocation> appLocations = appLocationServ.getAllActiveAppLocation(); 
			return ResponseEntity.ok(appLocations); 
			}
		
		@PostMapping(IndividualApplicationFormAPIs.GET_PAYMENT_APPLICATION_FORM)
		public ResponseEntity<String> getPaymentApplicationForm(@RequestBody AdditionalDetailsDTO additionalDetailsDTO) {
		    try {
		      
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
		        IntentApplication intentApplication = intentAppServ.getIntentByIntentAppId(Long.valueOf(bankDraftDTO.getIntentId()));
		        bankDraft.setIntentAppId(intentApplication);
		        Optional<BankDraft> savedBankDraft = bankDraftServ.addBankDraft(bankDraft);
		        
		        // Fetch the intent application
		        IntentApplication intentApplication1 = intentAppServ.getIntentByIntentAppId(Long.valueOf(bankDraftDTO.getIntentId()));
		        intentApplication1.setApplicationStatus("Fee Payment");
		        intentAppServ.updateIntentApp(intentApplication1);
		        
		        if (intentApplication1 == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                    .body("Intent Application not found for ID: " + intentApplication1.getIntentAppId());
		        }
		        

		        if (!savedBankDraft.isPresent()) {
		            return new ResponseEntity<>("Failed to save bank draft.", HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		        
		        return ResponseEntity.ok("Your application form has been submitted. Payment is subject to verification. Please upload the payment receipt.");
		        
		    } catch (NumberFormatException e) {
		        return ResponseEntity.badRequest().body("Invalid Intent ID format.");
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("An error occurred while fetching the payment application form.");
		    }
		}

		
		@PostMapping(IndividualApplicationFormAPIs.GET_PAYMENT_PROOF_FORM)
		public ResponseEntity<?> getPaymentProofApplicationForm(@ModelAttribute PaymentProofDTO paymentProofDTO) {
		    try {
		      
		    	System.out.println(paymentProofDTO);
		        Long decryptedIntentId = Long.valueOf(paymentProofDTO.getIntentAppId());
		        System.out.println(decryptedIntentId);
		        
		       
		        IntentApplication intentApplication = intentAppServ.getIntentByIntentAppId(decryptedIntentId);
		        
		        PaymentProof paymentProof=new PaymentProof();
		        paymentProof.setAmount(Integer.valueOf(paymentProofDTO.getAmount()));
		        paymentProof.setIndivApplicationId(intentApplication);
		        String FileName=DocumentFileUtil.saveFile(paymentProofDTO.getFile(), "paymentProof", String.valueOf(decryptedIntentId), "paymentProof");
		        paymentProof.setPaymentProofDoc(FileName);
		        paymentProof.setTransactionID(paymentProofDTO.getTransactionNumber());
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	            Date parsedDate = dateFormat.parse(paymentProofDTO.getSelectedDate());
	            paymentProof.setDateOfPayment(parsedDate);
	            paymentProof.setStatus("Active");
		        Optional<PaymentProof>optional=iPaymentProof.addPaymentProof(paymentProof);
		        if (optional.isEmpty()) {
		        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		        			.body("Failed to save payment proof. Please try again.");
		        }
		        intentApplication.setApplicationStatus(ApplicationStatusUtil.PAYMENT_PROOF_RECEIVED);
		        Optional<IntentApplication> updatedIntent = intentAppServ.updateIntentApp(intentApplication);
		        if (updatedIntent.isEmpty()) {
		        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		        			.body("Payment proof saved but application status could not be updated. Please contact support.");
		        }
		        ApplicationTimeLine obj1 = new ApplicationTimeLine();
				obj1.setApplicationStatus(ApplicationStatusUtil.PAYMENT_PROOF_RECEIVED);
				obj1.setInitiatedBy(EncryptionUtil.encrypt(intentApplication.getUserName()));
				obj1.setIntentAppId(intentApplication);
				
				appTimeLineServ.addApplicationTimeLine(obj1);
		        
		        if (intentApplication == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                    .body("Intent Application not found for ID: " + decryptedIntentId);
		        }

		        return ResponseEntity.ok("Payment Proof upload successfully.");
		        
		    } catch (NumberFormatException e) {
		        return ResponseEntity.badRequest().body("Invalid Intent ID format.");
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("An error occurred while fetching the payment application form.");
		    }
		}

		@GetMapping(IndividualApplicationFormAPIs.GET_ALL_PAYMENT_PROOF)
		public ResponseEntity<?> getAllApplications() {

		    Map<Long, IntentApplication> pendingByIntentId = new LinkedHashMap<>();

		    // Primary: intents with an active payment proof awaiting CCA verification
		    for (IntentApplication app : intentAppServ.getPaymentProofPendingVerification()) {
		        if (app != null && app.getIntentAppId() != null) {
		            pendingByIntentId.put(app.getIntentAppId(), app);
		        }
		    }

		    // Fallback: legacy rows matched only by status text
		    for (IntentApplication app : intentAppServ.getAllIntentApplication()) {
		        if (app != null && app.getIntentAppId() != null
		                && ApplicationStatusUtil.isPaymentProofReceived(app.getApplicationStatus())) {
		            pendingByIntentId.putIfAbsent(app.getIntentAppId(), app);
		        }
		    }

		    return new ResponseEntity<>(new ArrayList<>(pendingByIntentId.values()), HttpStatus.OK);
		}

		
		@PostMapping(IndividualApplicationFormAPIs.GET_PAYMENT_VERIFICATION_FORM)
		public ResponseEntity<?> getPaymentVarificationApplicationForm(@ModelAttribute PaymentVerificationDTO paymentVerificationDTO) {
		    try {
		      
		    	System.out.println(paymentVerificationDTO);
		        Long decryptedIntentId = Long.valueOf(paymentVerificationDTO.getIntentAppId());
		        System.out.println(decryptedIntentId);
		        
		       
		        IntentApplication intentApplication = intentAppServ.getIntentByIntentAppId(decryptedIntentId);
		        
		        PaymentVerification paymentVerification=new PaymentVerification();
		        paymentVerification.setAmount(Integer.valueOf(paymentVerificationDTO.getAmount()));
		        paymentVerification.setIndivApplicationId(intentApplication);
		        String FileName=DocumentFileUtil.saveFile(paymentVerificationDTO.getFile(), "paymentVerification", String.valueOf(decryptedIntentId), "paymentVerification");
		        paymentVerification.setPaymentVerificationProof(FileName);
		        paymentVerification.setTransactionID(paymentVerificationDTO.getTransactionNumber());
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	            Date parsedDate = dateFormat.parse(paymentVerificationDTO.getSelectedDate());
	            paymentVerification.setDateOfPayment(parsedDate);
	            paymentVerification.setVerifiedBy(paymentVerificationDTO.getUserName());
	            paymentVerification.setStatus("Active");
		        Optional<PaymentVerification>optional=iPaymentVerficationService.addPaymentProof(paymentVerification);
		        intentApplication.setApplicationStatus("Edit_Upon_Review");
		        intentAppServ.updateIntentApp(intentApplication);
		        
		        ApplicationTimeLine obj1 = new ApplicationTimeLine();
				obj1.setApplicationStatus("Edit_Upon_Review");
				obj1.setInitiatedBy(EncryptionUtil.encrypt(paymentVerificationDTO.getUserName()));
				obj1.setIntentAppId(intentApplication);
				
				appTimeLineServ.addApplicationTimeLine(obj1);
		        if (intentApplication == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                    .body("Intent Application not found for ID: " + decryptedIntentId);
		        }

		        return ResponseEntity.ok("Payment Verfication upload successfully.");
		        
		    } catch (NumberFormatException e) {
		        return ResponseEntity.badRequest().body("Invalid Intent ID format.");
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("An error occurred while fetching the payment application form.");
		    }
		}

		@GetMapping(IndividualApplicationFormAPIs.GET_ALL_PAYMENT_PROOF_BY_INTENT_ID)
		public ResponseEntity<?> getAllApplicationsByIntentId(@RequestParam("id")String id) {
			
			 Long decryptedIntentId = Long.valueOf(EncryptionUtil.decrypt(id));
		 

		    // Get all intent applications
			 PaymentProof paymentVerification = iPaymentProof.getAllIntentApplicationByIntentId(decryptedIntentId);

		    

		    if (paymentVerification==null) {
		        return new ResponseEntity<>("No applications found with 'PaymentProof Recieved' status.", HttpStatus.NOT_FOUND);
		    }

		    return new ResponseEntity<>(paymentVerification, HttpStatus.OK);
		}
		@GetMapping("/download-final-esigned-report")
		public ResponseEntity<?> eSignCoverLetter(@RequestParam("id") String id, @RequestParam("did") String did) {

			try {

				String eSignDocId = id;
				String signedDocument = did;

				if (EncryptionUtil.decrypt(eSignDocId) != null)
					eSignDocId = EncryptionUtil.decrypt(eSignDocId);

				if (EncryptionUtil.decrypt(signedDocument) != null)
					signedDocument = EncryptionUtil.decrypt(signedDocument);

				ESignedDocumentsEntity edoc = documentsService.getEsignDocumentById(Long.parseLong(eSignDocId));

				if (edoc != null) {

					edoc.setSignedDocId(EncryptionUtil.encrypt(signedDocument));
					documentsService.addEsignDocument(edoc);

				}

				return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		}
		
		
		@GetMapping("/get-esign-document-id")
		public ResponseEntity<?> eSignDocumentId(@RequestParam("userName") String userName) {

			try {

				String eSignDocId = userName;
				List<ESignedDocumentsEntity> edoc = documentsService.getEsignDocumentByUserName(eSignDocId);

				return new ResponseEntity<>(edoc.get(0), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		}

		
		@GetMapping("/get-all-application-tumeline")
		public ResponseEntity<?> getAllTimeLine(@RequestParam("userName") String userName) {

			try {

				String eSignDocId = EncryptionUtil.decrypt(userName);
				IntentApplication intentApplication=intentAppServ.findIntentAppById(eSignDocId);
				List<ApplicationTimeLine>appTimeLines=appTimeLineServ.getByIntentApplicationId(intentApplication.getIntentAppId());
				return new ResponseEntity<>(appTimeLines, HttpStatus.OK);
				
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		}
		
		
}


