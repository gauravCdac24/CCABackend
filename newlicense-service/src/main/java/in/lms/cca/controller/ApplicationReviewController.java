package in.lms.cca.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.ApplicationReviewDTO;
import in.lms.cca.dto.Approveddata;
import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.dto.Ccadata;
import in.lms.cca.dto.OrganisationDetailsDTO;
import in.lms.cca.dto.OrganizationDetailsDTO;
import in.lms.cca.dto.UserDetailsDTO;
import in.lms.cca.dto.client.ApplicationDTO;
import in.lms.cca.dto.client.ReviewApplicationDTO;
import in.lms.cca.dto.client.ReviewDocumentDTO;
import in.lms.cca.dto.client.ReviewFieldsDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.BankDetails;
import in.lms.cca.entity.BankDraft;
import in.lms.cca.entity.FirmApplication;
import in.lms.cca.entity.FirmApplication2;
import in.lms.cca.entity.FirmAuthorizedRepresentative;
import in.lms.cca.entity.FirmPartnerDetails;
import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.IndivEmails;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.PaymentProof;
import in.lms.cca.entity.PaymentVerification;
import in.lms.cca.entity.ReviewApplication;
import in.lms.cca.entity.ReviewDocuments;
import in.lms.cca.entity.ReviewFields;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.AppUndertakingService;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.BankDetailsService;
import in.lms.cca.service.BankDraftService;
import in.lms.cca.service.FirmApplication2service;
import in.lms.cca.service.FirmApplicationService;
import in.lms.cca.service.FirmAuthorizedRepresentiveService;
import in.lms.cca.service.FirmPartnerService;
import in.lms.cca.service.GovernmentAgencyService;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IIndividualApplicationFormService;
import in.lms.cca.service.IIndividualEmailsService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.service.IPaymentProof;
import in.lms.cca.service.IPaymentVerficationService;
import in.lms.cca.service.IndivAdditionalDetailsService;
import in.lms.cca.service.ReviewApplicationService;
import in.lms.cca.service.ReviewDocumentsService;
import in.lms.cca.service.ReviewFieldsService;
import in.lms.cca.util.api.ApplicationReviewAPIs;
import in.lms.cca.util.api.IndividualApplicationFormAPIs;
import in.lms.cca.util.api.NewLicenseClientServiceAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@CrossOrigin
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
public class ApplicationReviewController {
	
	@Autowired
	private IIntentApplicationService intentAppServ;
	
	@Autowired
	private IIndividualEmailsService indEmailServ;
	
	@Autowired
	private IApplicationTimeLineService appTimeLineServ;
	
	@Autowired
	private IIndividualApplicationFormService indAppFormServ;

	@Autowired
	private FirmApplicationService firmApplicationServ;
	
	@Autowired
	private GovernmentAgencyService governmentAgencyServ;
	
	@Autowired
	private ApplicationDocumentService applicationDocumentService;
	
	@Autowired
	private ReviewFieldsService reviewFieldsService;
	
	@Autowired
	private ReviewApplicationService reviewApplicationService;
	
	
	@Autowired
	private ReviewDocumentsService documentsService;
	
	@Autowired
	private AppLocationService appLocationServ;
	
	@Autowired
	private ApplicationDocumentService  applicationDocumentServiceServ;
	
	@Autowired
	private FirmApplication2service firmApplication2Serv;
	
	@Autowired
	private FirmPartnerService firmPartnerServ;
	
	@Autowired
	private	FirmAuthorizedRepresentiveService firmAuthorizedRepresentiveService;
	
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
	
	
	Random rand = new Random();
	Integer rnum = rand.nextInt(1000);
	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATIONS_IN_REVIEW)
	public ResponseEntity<?> getAllApplications() {

	    List<ApplicationDTO> applicationDTO = new ArrayList<>();

	    // Get all intent applications
	    List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
	    
	   System.out.println(" intentApplications.size()====>"+ intentApplications.size());

	    for (IntentApplication app : intentApplications) {
	    	
	    	

	        // Check if the application status is "underReview"
	    	if (app.getApplicationStatus() != null && 
	    		    (app.getApplicationStatus().equals("underReview") || app.getApplicationStatus().equals("Edit_Upon_Review"))) {

	            ApplicationDTO application = new ApplicationDTO();

	            application.setAcknowledgementNo(app.getAcknowledgementNo());
	            application.setApplicantUserName(app.getUserName());
	            application.setApplicationCurrentStatus(app.getApplicationStatus());
	            application.setApplicationInitiatedOn(app.getCreated());
	            application.setApplicationType(app.getAppTypeMasterId());
	            application.setIntentAppId(app.getIntentAppId().toString());
	            application.setStatusInitiatedOn(null);

	         // Check if appTypeMasterId is 1 and fetch full name based on intent ID
	            if (app.getAppTypeMasterId().equals("1")) {
	                System.out.println("abcd---->" + app.getIntentAppId());
	                List<String> fullNames = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
	                if (fullNames != null && !fullNames.isEmpty()) {
	                    application.setApplicantName(fullNames.get(0));  // Set the first name
	                } else {
	                    application.setApplicantName("No name found");
	                }
	            } else if (app.getAppTypeMasterId().equals("2") || app.getAppTypeMasterId().equals("3")) {
	                List<String> firmNames = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId());
	                if (firmNames != null && !firmNames.isEmpty()) {
	                    application.setApplicantName(firmNames.get(0));  // Set the first firm name
	                } else {
	                    application.setApplicantName("No firm name found");
	                }
	            } else if (app.getAppTypeMasterId().equals("4")) {
	                List<String> organizationNames = governmentAgencyServ.getOrganizationNameByIntentAppId(app.getIntentAppId());
	                if (organizationNames != null && !organizationNames.isEmpty()) {
	                    application.setApplicantName(organizationNames.get(0));  // Set the first organization name
	                } else {
	                    application.setApplicantName("No organization name found");
	                }
	            }


	            // Set the status initiated date
	            List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	            if (list.size() > 0) {
	                application.setStatusInitiatedOn(list.get(0).getCreated());
	            } else {
	                application.setStatusInitiatedOn(app.getUpdated());
	            }

	            // Add the application to the DTO list
	            applicationDTO.add(application);
	        }
	    }

	    return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
	}

	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATIONS_IN_REVIEW_BY_CCA)
	public ResponseEntity<?> getAllApplication() {

	    List<ApplicationDTO> applicationDTO = new ArrayList<>();

	    // Get all intent applications
	    List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
	    
	   System.out.println(" intentApplications.size()====>"+ intentApplications.size());

	    for (IntentApplication app : intentApplications) {
	    	
	    	

	        // Check if the application status is "underReview"
	    	if (app.getApplicationStatus() != null && 
	    		    (app.getApplicationStatus().equals("Recommended Reject For Review") || 
	    		     app.getApplicationStatus().equals("Approved From Review"))) {

	            ApplicationDTO application = new ApplicationDTO();

	            application.setAcknowledgementNo(app.getAcknowledgementNo());
	            application.setApplicantUserName(app.getUserName());
	            application.setApplicationCurrentStatus(app.getApplicationStatus());
	            application.setApplicationInitiatedOn(app.getCreated());
	            application.setApplicationType(app.getAppTypeMasterId());
	            application.setIntentAppId(app.getIntentAppId().toString());
	            application.setStatusInitiatedOn(null);

	         // Check if appTypeMasterId is 1 and fetch full name based on intent ID
	            if (app.getAppTypeMasterId().equals("1")) {
	                System.out.println("abcd---->" + app.getIntentAppId());
	                List<String> fullNames = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
	                if (fullNames != null && !fullNames.isEmpty()) {
	                    application.setApplicantName(fullNames.get(0));  // Set the first name
	                } else {
	                    application.setApplicantName("No name found");
	                }
	            } else if (app.getAppTypeMasterId().equals("2") || app.getAppTypeMasterId().equals("3")) {
	                List<String> firmNames = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId());
	                if (firmNames != null && !firmNames.isEmpty()) {
	                    application.setApplicantName(firmNames.get(0));  // Set the first firm name
	                } else {
	                    application.setApplicantName("No firm name found");
	                }
	            } else if (app.getAppTypeMasterId().equals("4")) {
	                List<String> organizationNames = governmentAgencyServ.getOrganizationNameByIntentAppId(app.getIntentAppId());
	                if (organizationNames != null && !organizationNames.isEmpty()) {
	                    application.setApplicantName(organizationNames.get(0));  // Set the first organization name
	                } else {
	                    application.setApplicantName("No organization name found");
	                }
	            }


	            // Set the status initiated date
	            List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	            if (list.size() > 0) {
	                application.setStatusInitiatedOn(list.get(0).getCreated());
	            } else {
	                application.setStatusInitiatedOn(app.getUpdated());
	            }

	            // Add the application to the DTO list
	            applicationDTO.add(application);
	        }
	    }

	    return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
	}

	
	
	
	@PostMapping(ApplicationReviewAPIs.ADD_APPLICATION_REVIEW)
	public ResponseEntity<?> addAllApplicationsReviewData(@ModelAttribute ApplicationReviewDTO applicationReviewDTO) throws IOException, ParseException {

	    // Log the received DTO
	    System.out.println("Received DTO: " + applicationReviewDTO.toString());

	    // Find the IntentApplication entity using the provided userName from the DTO
	    IntentApplication intentApp = intentAppServ.findIntentAppById(applicationReviewDTO.getUserName());
 
	   
	    // Find existing review applications for the IntentApplication
	    List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
	    System.out.println("Existing Review Remarks: " + (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));

	    // Check if reviewApplications is not null or empty
	    if (reviewApplications != null && !reviewApplications.isEmpty()) {
	        // If data found, set the status of the existing applications to "Inactive"
	        for (ReviewApplication existingReviewApp : reviewApplications) {
	            existingReviewApp.setStatus("Inactive");
	            existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));
	            reviewApplicationService.updateReview(existingReviewApp);  // Save the updated ReviewApplication
	        }
	    }

	    // Create a new ReviewApplication entity for saving new data
	    ReviewApplication reviewApplication = new ReviewApplication();
	    reviewApplication.setIntentAppId(intentApp);  // Set IntentApplication
	    String fileName = DocumentFileUtil.saveFile(applicationReviewDTO.getFile1(), "MOMFile", rnum.toString(), "MOMFile");
	    reviewApplication.setMomFile(fileName);
        String reviewDateStr = applicationReviewDTO.getReviewDate();
        if (reviewDateStr != null && !reviewDateStr.isEmpty()) {
            // Assuming the date string is in "yyyy-MM-dd" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date reviewDate = dateFormat.parse(reviewDateStr);
            reviewApplication.setReviewDate(reviewDate);
        } else {
        	reviewApplication.setReviewDate(null); // Handle null or empty string case
        }
	    // Set remarks, status, and reviewer details for the new entry
	    reviewApplication.setRemarks(applicationReviewDTO.getRemarks());
	    reviewApplication.setStatus("Active");  // Mark new entry as Active
	    reviewApplication.setCreatedBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));
	    reviewApplication.setUpdatedBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));
	    reviewApplication.setReviewBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));

	    // Save the new ReviewApplication to the database
	    reviewApplication = reviewApplicationService.addReview(reviewApplication);

	    // Create lists to hold ReviewFields and ReviewDocuments
	    List<ReviewFields> reviewFieldsList = new ArrayList<>();
	    List<ReviewDocuments> reviewDocuments = new ArrayList<>();

	    // Process fields and documents dynamically, using the last existing review ID if available
	    Long reviewId = reviewApplications.isEmpty() ? null : reviewApplications.get(0).getReviewId();
	    processFields(applicationReviewDTO, reviewFieldsList, reviewApplication, applicationReviewDTO.getReviewerUserName(), reviewId);
	    processDocuments(applicationReviewDTO, reviewDocuments, reviewApplication, applicationReviewDTO.getReviewerUserName(), reviewId);

	    // Save all ReviewFields and ReviewDocuments to the database
	    reviewFieldsService.addAll(reviewFieldsList);
	    documentsService.addAllDocument(reviewDocuments);
	    if (applicationReviewDTO.getIsRejected() == true) {
	        intentApp.setApplicationStatus("Recommended Reject For Review");
	    } else {
	        intentApp.setApplicationStatus("Edit Upon Review");
	    }
	    Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	    String status="Active";
	    
	    List<AppLocation> appLocationList = appLocationServ.findIntentAppById(intentAppId,status);
	    if (appLocationList != null && !appLocationList.isEmpty()) {
	      
	        for (AppLocation appLocation : appLocationList) {
	            appLocation.setStatus("Inactive");
	            appLocationServ.updateAppLocation(appLocation);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	    FirmApplication firmApplication=firmApplicationServ.findIntentAppById(intentAppId,status);
	    firmApplication.setStatus("Inactive");
	    firmApplicationServ.updateData(firmApplication);
	    
	    FirmApplication2 appFirmApplication = firmApplication2Serv.findIntentAppByIdWithStatus(intentAppId,status);
	    appFirmApplication.setStatus("Inactive");
	    firmApplication2Serv.updateData(appFirmApplication);
	    
	    List<AppLocation> appLocations = appLocationServ.findIntentAppById(intentAppId,status);
	    
	    if (appLocationList != null && !appLocationList.isEmpty()) {
		      
	        for (AppLocation appLocation : appLocations) {
	            appLocation.setStatus("Inactive");
	            appLocationServ.updateAppLocation(appLocation);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	    List<FirmPartnerDetails> appFirmPartnerDetails = firmPartnerServ.findIntentAppById(intentAppId);
	    
	    if (appFirmPartnerDetails != null && !appFirmPartnerDetails.isEmpty()) {
		      
	        for (FirmPartnerDetails firmPartnerDetails : appFirmPartnerDetails) {
	        	firmPartnerDetails.setStatus("Inactive");
	            firmPartnerServ.updateData(firmPartnerDetails);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    List<FirmAuthorizedRepresentative> firmAuthorizedRepresentatives=firmAuthorizedRepresentiveService.findIntentAppById(intentAppId,status);
	    
	    if (firmAuthorizedRepresentatives != null && !firmAuthorizedRepresentatives.isEmpty()) {
		      
	        for (FirmAuthorizedRepresentative authorizedRepresentative : firmAuthorizedRepresentatives) {
	        	authorizedRepresentative.setStatus("Inactive");
	        	firmAuthorizedRepresentiveService.updateData(authorizedRepresentative);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	    List<ApplicationDocument> appDocuments = applicationDocumentServiceServ.findIntentAppById(intentAppId);
	    
	    if (appDocuments != null && !appDocuments.isEmpty()) {
		      
	        for (ApplicationDocument applicationDocument : appDocuments) {
	        	applicationDocument.setStatus("Inactive");
	        	applicationDocumentServiceServ.updateApplicationDocument(applicationDocument);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	    intentAppServ.updateIntentApp(intentApp);
	    
	    List<BankDetails> bankDetailsList = bankDetailsSev.findIntentAppById(intentApp.getIntentAppId());
        if (bankDetailsList != null && !bankDetailsList.isEmpty()) {
            for (BankDetails bankDetails : bankDetailsList) {
                bankDetails.setStatus("Inactive");
                bankDetailsSev.updateBankDetails(bankDetails);
            }
        } else {
            System.out.println("No BankDetails found for intentAppId: " + intentApp.getIntentAppId());
        }
	      
     // Find BankDraft by the IntentApplication's ID
        BankDraft bankDraft = bankDraftServ.findByindivApplicationId(intentApp.getIntentAppId());

        if (bankDraft != null) {
            // Set the BankDraft status to "Inactive"
            bankDraft.setStatus("Inactive");
            
            // Update the BankDraft status in the database
            bankDraftServ.updateBankDraft(bankDraft);
        } else {
            // Optionally log or handle the case where no BankDraft is found
            System.out.println("No BankDraft found for intentAppId: " + intentApp.getIntentAppId());
        }
        
       // intentApp.setApplicationStatus("Edit_Upon_Review");
        intentAppServ.updateIntentApp(intentApp);
	   
	    return ResponseEntity.ok("Application Review Data Saved Successfully");
	}

	
	@PostMapping(ApplicationReviewAPIs.ADD_GOVERNMENT_AGENCY_APPLICATION_REVIEW)
	public ResponseEntity<?> addAllGoApplicationsReviewData(@ModelAttribute OrganisationDetailsDTO organisationDetailsDTO) throws IOException, ParseException {

	    // Log the received DTO
	    System.out.println("Received DTO: " + organisationDetailsDTO.toString());

	    // Find the IntentApplication entity using the provided userName from the DTO
	    IntentApplication intentApp = intentAppServ.findIntentAppById(organisationDetailsDTO.getUserName());
 
	    // Find existing review applications for the IntentApplication
	    List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
	    System.out.println("Existing Review Remarks: " + (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));

	    // Check if reviewApplications is not null or empty
	    if (reviewApplications != null && !reviewApplications.isEmpty()) {
	        // If data found, set the status of the existing applications to "Inactive"
	        for (ReviewApplication existingReviewApp : reviewApplications) {
	            existingReviewApp.setStatus("Inactive");
	            existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(organisationDetailsDTO.getReviewerUserName()));
	            reviewApplicationService.updateReview(existingReviewApp);  // Save the updated ReviewApplication
	        }
	    }

	    // Create a new ReviewApplication entity for saving new data
	    ReviewApplication reviewApplication = new ReviewApplication();
	    reviewApplication.setIntentAppId(intentApp);  // Set IntentApplication

	    String fileName = DocumentFileUtil.saveFile(organisationDetailsDTO.getFile1(), "MOMFile", rnum.toString(), "MOMFile");
	    reviewApplication.setMomFile(fileName);
        String reviewDateStr = organisationDetailsDTO.getReviewDate();
        if (reviewDateStr != null && !reviewDateStr.isEmpty()) {
            // Assuming the date string is in "yyyy-MM-dd" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date reviewDate = dateFormat.parse(reviewDateStr);
            reviewApplication.setReviewDate(reviewDate);
        } else {
        	reviewApplication.setReviewDate(null); // Handle null or empty string case
        }
	    // Set remarks, status, and reviewer details for the new entry
	    reviewApplication.setRemarks(organisationDetailsDTO.getRemarks());
	    reviewApplication.setStatus("Active");  // Mark new entry as Active
	    reviewApplication.setCreatedBy(EncryptionUtil.encrypt(organisationDetailsDTO.getReviewerUserName()));
	    reviewApplication.setUpdatedBy(EncryptionUtil.encrypt(organisationDetailsDTO.getReviewerUserName()));
	    reviewApplication.setReviewBy(EncryptionUtil.encrypt(organisationDetailsDTO.getReviewerUserName()));

	    // Save the new ReviewApplication to the database
	    reviewApplication = reviewApplicationService.addReview(reviewApplication);

	    // Create lists to hold ReviewFields and ReviewDocuments
	    List<ReviewFields> reviewFieldsList = new ArrayList<>();
	    List<ReviewDocuments> reviewDocuments = new ArrayList<>();

	    // Process fields and documents dynamically, using the last existing review ID if available
	    Long reviewId = reviewApplications.isEmpty() ? null : reviewApplications.get(0).getReviewId();
	    processFields(organisationDetailsDTO, reviewFieldsList, reviewApplication, organisationDetailsDTO.getReviewerUserName(), reviewId);
	    processDocuments(organisationDetailsDTO, reviewDocuments, reviewApplication, organisationDetailsDTO.getReviewerUserName(), reviewId);

	    // Save all ReviewFields and ReviewDocuments to the database
	    reviewFieldsService.addAll(reviewFieldsList);
	    documentsService.addAllDocument(reviewDocuments);
	    if (organisationDetailsDTO.getIsRejected() == true) {
	        intentApp.setApplicationStatus("Recommended Reject For Review");
	    } else {
	        intentApp.setApplicationStatus("Edit Upon Review");
	    }
	    Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	    String status="Active";
	    
	    List<AppLocation> appLocationList = appLocationServ.findIntentAppById(intentAppId,status);
	    if (appLocationList != null && !appLocationList.isEmpty()) {
	      
	        for (AppLocation appLocation : appLocationList) {
	            appLocation.setStatus("Inactive");
	            appLocationServ.updateAppLocation(appLocation);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	
	    
	    List<AppLocation> appLocations = appLocationServ.findIntentAppById(intentAppId,status);
	    
	    if (appLocationList != null && !appLocationList.isEmpty()) {
		      
	        for (AppLocation appLocation : appLocations) {
	            appLocation.setStatus("Inactive");
	            appLocationServ.updateAppLocation(appLocation);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    

	    
	    List<ApplicationDocument> appDocuments = applicationDocumentServiceServ.findIntentAppById(intentAppId);
	    
	    if (appDocuments != null && !appDocuments.isEmpty()) {
		      
	        for (ApplicationDocument applicationDocument : appDocuments) {
	        	applicationDocument.setStatus("Inactive");
	        	applicationDocumentServiceServ.updateApplicationDocument(applicationDocument);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	    intentAppServ.updateIntentApp(intentApp);
	    
	    List<BankDetails> bankDetailsList = bankDetailsSev.findIntentAppById(intentApp.getIntentAppId());
        if (bankDetailsList != null && !bankDetailsList.isEmpty()) {
            for (BankDetails bankDetails : bankDetailsList) {
                bankDetails.setStatus("Inactive");
                bankDetailsSev.updateBankDetails(bankDetails);
            }
        } else {
            System.out.println("No BankDetails found for intentAppId: " + intentApp.getIntentAppId());
        }
	      
     // Find BankDraft by the IntentApplication's ID
        BankDraft bankDraft = bankDraftServ.findByindivApplicationId(intentApp.getIntentAppId());

        if (bankDraft != null) {
            // Set the BankDraft status to "Inactive"
            bankDraft.setStatus("Inactive");
            
            // Update the BankDraft status in the database
            bankDraftServ.updateBankDraft(bankDraft);
        } else {
            // Optionally log or handle the case where no BankDraft is found
            System.out.println("No BankDraft found for intentAppId: " + intentApp.getIntentAppId());
        }

        
        //intentApp.setApplicationStatus("Edit Upon Review");
        intentAppServ.updateIntentApp(intentApp);
	   
	    return ResponseEntity.ok("Application Review Data Saved Successfully");
	}

	
	@PostMapping(ApplicationReviewAPIs.ADD_INDIVIDUAL_APPLICATION_REVIEW)
	public ResponseEntity<?> addAllIndividualApplicationReviewData(@ModelAttribute UserDetailsDTO organizationDetailsDTO) throws IOException, ParseException {

	    // Log the received DTO
	    System.out.println("Received DTO: " + organizationDetailsDTO.toString());

	    // Find the IntentApplication entity using the provided userName from the DTO
	    IntentApplication intentApp = intentAppServ.findIntentAppById(organizationDetailsDTO.getUserName());
 
	    // Find existing review applications for the IntentApplication
	    List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
	    System.out.println("Existing Review Remarks: " + (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));

	    // Check if reviewApplications is not null or empty
	    if (reviewApplications != null && !reviewApplications.isEmpty()) {
	        // If data found, set the status of the existing applications to "Inactive"
	        for (ReviewApplication existingReviewApp : reviewApplications) {
	            existingReviewApp.setStatus("Inactive");
	            existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
	            reviewApplicationService.updateReview(existingReviewApp);  // Save the updated ReviewApplication
	        }
	    }

	    // Create a new ReviewApplication entity for saving new data
	    ReviewApplication reviewApplication = new ReviewApplication();
	    reviewApplication.setIntentAppId(intentApp);  // Set IntentApplication
	    
	    
	    if (organizationDetailsDTO.getFile1() != null && !organizationDetailsDTO.getFile1().isEmpty()) {
	    String fileName = DocumentFileUtil.saveFile(organizationDetailsDTO.getFile1(), "MOMFile", rnum.toString(), "MOMFile");
	    reviewApplication.setMomFile(fileName);
	    } else {
	        reviewApplication.setMomFile(null); 
	    }
	    
        String reviewDateStr = organizationDetailsDTO.getReviewDate();
        if (reviewDateStr != null && !reviewDateStr.isEmpty()) {
            // Assuming the date string is in "yyyy-MM-dd" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date reviewDate = dateFormat.parse(reviewDateStr);
            reviewApplication.setReviewDate(reviewDate);
        } else {
        	reviewApplication.setReviewDate(null); // Handle null or empty string case
        }
	    // Set remarks, status, and reviewer details for the new entry
	    reviewApplication.setRemarks(organizationDetailsDTO.getRemarks());
	    reviewApplication.setStatus("Active");  // Mark new entry as Active
	    reviewApplication.setCreatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
	    reviewApplication.setUpdatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
	    reviewApplication.setReviewBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));

	    // Save the new ReviewApplication to the database
	    reviewApplication = reviewApplicationService.addReview(reviewApplication);

	    // Create lists to hold ReviewFields and ReviewDocuments
	    List<ReviewFields> reviewFieldsList = new ArrayList<>();
	    List<ReviewDocuments> reviewDocuments = new ArrayList<>();

	    // Process fields and documents dynamically, using the last existing review ID if available
	    Long reviewId = reviewApplications.isEmpty() ? null : reviewApplications.get(0).getReviewId();
	    processFields(organizationDetailsDTO, reviewFieldsList, reviewApplication, organizationDetailsDTO.getReviewerUserName(), reviewId);
	   processDocuments(organizationDetailsDTO, reviewDocuments, reviewApplication, organizationDetailsDTO.getReviewerUserName(), reviewId);

	    // Save all ReviewFields and ReviewDocuments to the database
	    reviewFieldsService.addAll(reviewFieldsList);
	    documentsService.addAllDocument(reviewDocuments);
	    if (organizationDetailsDTO.getIsRejected() == true) {
	        intentApp.setApplicationStatus("Recommended Reject For Review");
	    } else {
	        intentApp.setApplicationStatus("Edit Upon Review");
	    }

	    Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	    
	    List<IndivApplication> indivApplications = indAppFormServ.findIntentWithoutStatusAppById(intentAppId);

	 // Iterate through the list and set the status to "Inactive" for each application
	 for (IndivApplication indivApplication : indivApplications) {
	     indivApplication.setStatus("Inactive");
	     indAppFormServ.updateIndivApplication(indivApplication); // Update each application individually
	 }		
	 List<IndivEmails> indivEmails = indEmailServ.findByindivApplicationId(intentAppId);

		if (indivEmails != null && !indivEmails.isEmpty()) {
		    for (IndivEmails indivEmail : indivEmails) {
		        indivEmail.setStatus("Inactive"); 
		        indEmailServ.updateIndivEmails(indivEmail);
		    }
		} else {
		    System.out.println("No emails found for the given intentAppId: " + intentAppId);
		}
		
	    String status="Active";
	    
	    List<AppLocation> appLocationList = appLocationServ.findIntentAppById(intentAppId,status);
	    if (appLocationList != null && !appLocationList.isEmpty()) {
	      
	        for (AppLocation appLocation : appLocationList) {
	            appLocation.setStatus("Inactive");
	            appLocationServ.updateAppLocation(appLocation);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	
	    
	    List<AppLocation> appLocations = appLocationServ.findIntentAppById(intentAppId,status);
	    
	    if (appLocationList != null && !appLocationList.isEmpty()) {
		      
	        for (AppLocation appLocation : appLocations) {
	            appLocation.setStatus("Inactive");
	            appLocationServ.updateAppLocation(appLocation);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    

	    
	    List<ApplicationDocument> appDocuments = applicationDocumentServiceServ.findIntentAppById(intentAppId);
	    
	    if (appDocuments != null && !appDocuments.isEmpty()) {
		      
	        for (ApplicationDocument applicationDocument : appDocuments) {
	        	applicationDocument.setStatus("Inactive");
	        	applicationDocumentServiceServ.updateApplicationDocument(applicationDocument);
	        }
	    } else {
	        System.out.println("No AppLocation found for the given intentAppId: " + intentAppId);
	    }
	    
	    intentAppServ.updateIntentApp(intentApp);
	    
	    List<BankDetails> bankDetailsList = bankDetailsSev.findIntentAppById(intentApp.getIntentAppId());
	    if(bankDetailsList!=null && !bankDetailsList.isEmpty()) {
	        for (BankDetails bankDetails : bankDetailsList) {
	            bankDetails.setStatus("Inactive");
	            bankDetailsSev.updateBankDetails(bankDetails);
	        }
	    }else {
	    	  System.out.println("No bankDetails found for intentAppId: " + intentApp.getIntentAppId());
	    }
     // Find BankDraft by the IntentApplication's ID
        BankDraft bankDraft = bankDraftServ.findByindivApplicationId(intentApp.getIntentAppId());

        if (bankDraft != null) {
            // Set the BankDraft status to "Inactive"
            bankDraft.setStatus("Inactive");
            
            // Update the BankDraft status in the database
            bankDraftServ.updateBankDraft(bankDraft);
        } else {
            // Optionally log or handle the case where no BankDraft is found
            System.out.println("No BankDraft found for intentAppId: " + intentApp.getIntentAppId());
        }

        
       // intentApp.setApplicationStatus("Edit_Upon_Review");
        intentAppServ.updateIntentApp(intentApp);
	   
	    return ResponseEntity.ok("Application Review Data Saved Successfully");
	}

	
	
	private void processFields(Object dtoObject, List<ReviewFields> reviewFieldsList, ReviewApplication reviewApplication, String reviewerUserName, Long reviewId) {
	    System.out.println("dtoObject=====>" + dtoObject);
	    Field[] fields = dtoObject.getClass().getDeclaredFields();

	    for (Field field : fields) {
	        field.setAccessible(true);
	        try {
	            Object value = field.get(dtoObject);  // Get the value of the field
	            String fieldName = field.getName();
	            System.out.println("--=----=-====>" + fieldName);
	            // If value is not a simple string, handle complex objects or arrays
	            String valueString = null;

	            if (value instanceof String) {
	                valueString = (String) value; // Cast directly if it's a string
	            }
	            System.out.println("gfgvdhbg-=-=-=-->" + valueString);

	            // Exclude specific fields and any firm-related fields
	            if (value != null 
	                && !fieldName.equals("userName") 
	                && !fieldName.equals("remarks") 
	                && !fieldName.equals("reviewerUserName") 
	                && !fieldName.endsWith("Document")) {
	                
	                // Check if reviewFieldsList already has data
	                List<ReviewFields> existingReviewFields = reviewFieldsService.getAllDetailsByReviewId(reviewId);
	                
	                if (existingReviewFields.isEmpty()) {
	                    // No existing data, add new fields
	                    addReviewField(reviewFieldsList, reviewApplication, fieldName, valueString, reviewerUserName);
	                } else {
	                    // Existing data found, update status of existing fields to "Inactive"
	                    for (ReviewFields existingField : existingReviewFields) {
	                        existingField.setStatus("Inactive"); // Assuming there is a setStatus method
	                        reviewFieldsService.updateReviewField(existingField); // Save the updated status
	                    }
	                    
	                    // Now add the new field
	                    addReviewField(reviewFieldsList, reviewApplication, fieldName, valueString, reviewerUserName);
	                }
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
	}


	private void processDocuments(Object dtoObject, List<ReviewDocuments> reviewDocuments, ReviewApplication reviewApplication, String reviewerUserName,Long reviewId) {
	    Field[] fields = dtoObject.getClass().getDeclaredFields();

	    for (Field field : fields) {
	        field.setAccessible(true);
	        try {
	            Object value = field.get(dtoObject);  // Get the value of the field
	            String fieldName = field.getName();

	            System.out.println("--=----=-====>"+value);
	            
	            String valueString = null;

	            if (value instanceof String) {
	                valueString = (String) value; // Cast directly if it's a string
	                System.out.println("gfgvdhbg valueStrings-=-=-=-->" + valueString);
	            }
	            System.out.println("gfgvdhbg valueString-=-=-=-->" + valueString);

	            
	            // Process fields that start with "firm"
	            if (value != null  
		                && fieldName.endsWith("Document")) {
	            	
	            	
	            	  // Check if reviewFieldsList already has data
	                List<ReviewDocuments> existingReviewDocuments = documentsService.getAllDetailsByReviewId(reviewId);
	                
	                System.err.println("existingReviewDocuments.size()====>"+existingReviewDocuments.size());
	                if (existingReviewDocuments.size()== 0) {
	                    // No existing data, add new fields
	                	 addReviewDocument(reviewDocuments, reviewApplication, fieldName, valueString, reviewerUserName);
	                } else {
	                    // Existing data found, update status of existing fields to "Inactive"
	                    for (ReviewDocuments existingField : existingReviewDocuments) {
	                        existingField.setStatus("Inactive"); // Assuming there is a setStatus method
	                        documentsService.updateReviewField(existingField); // Save the updated status
	                    }
	               
	            }
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
	}

	// Helper method to add a ReviewField object to the list
	private void addReviewField(List<ReviewFields> reviewFieldsList, ReviewApplication reviewApplication, String fieldName, String value, String reviewerUserName) {
	    ReviewFields field = new ReviewFields();
	    field.setReviewId(reviewApplication);
	    field.setFieldName(value);
	    field.setStatus("Active");
	    field.setCreatedBy(EncryptionUtil.encrypt(reviewerUserName));
	    field.setUpdatedBy(EncryptionUtil.encrypt(reviewerUserName));
	    reviewFieldsList.add(field);
	}

	// Helper method to add a ReviewDocument object to the list
	private void addReviewDocument(List<ReviewDocuments> reviewDocuments, ReviewApplication reviewApplication, String fieldName, String valueString, String reviewerUserName) {
	    ReviewDocuments document = new ReviewDocuments();
	    document.setReviewId(reviewApplication);
	    
	    // Create and set ApplicationDocument based on the fieldName
	    ApplicationDocument applicationDocument = new ApplicationDocument();
	    applicationDocument.setAppDocId(Long.valueOf(valueString)); // Ensure the fieldName is convertible to Long
	    document.setApplicationDocument(applicationDocument);
	    
	    document.setStatus("Active");
	    document.setCreatedBy(EncryptionUtil.encrypt(reviewerUserName));
	    document.setUpdatedBy(EncryptionUtil.encrypt(reviewerUserName));
	    reviewDocuments.add(document);
	}

	
	
//	@GetMapping(ApplicationReviewAPIs.ADD_APPLICATION_REVIEW)
//	public ResponseEntity<?> getAllApplicationsByIntentId(@RequestParam("userName") String userName) {
//
//	    // Log the received username
//	    System.out.println("Received username: " + userName);
//
//	    // Find the IntentApplication entity using the provided userName from the DTO
//	    IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
//	    
//	    // Check if intentApp is null
//	    if (intentApp == null) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                .body("IntentApplication not found for user: " + userName);
//	    }
//
//	    // Find existing review applications for the IntentApplication
//	    List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
//	    System.out.println("Existing Review Remarks: " + (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));
//
//	    // Return response based on reviewApplications
//	    if (reviewApplications.isEmpty()) {
//	        return ResponseEntity.ok("No review applications found for IntentApplication ID: " + intentApp.getIntentAppId());
//	    } else {
//	        return ResponseEntity.ok(reviewApplications); // Return the list of review applications
//	    }
//	}
	
	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_APPLICATION_REVIEW)
	public ResponseEntity<?> getAllApplicationsByIntentId() {

	   
	    // Find existing review applications for the IntentApplication
	    List<ReviewApplication> reviewApplications = reviewApplicationService.findAllData();
	    System.out.println("Existing Review Remarks: " + (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));

	    // Return response based on reviewApplications
	    if (reviewApplications.isEmpty()) {
	        return ResponseEntity.ok("No review applications found for IntentApplication ID ");
	    } else {
	        return ResponseEntity.ok(reviewApplications); // Return the list of review applications
	    }
	}
	
	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_APPLICATION_REVIEW_BY_USERNAME)
	public ResponseEntity<?> getAllApplicationsByUserName(@RequestParam("userName") String userName) {
	    try {
	       
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);

	       
	        if (intentApp == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No Intent Application found for the provided username.");
	        }

	       
	        ReviewApplication reviewApplications = reviewApplicationService.findByIntentsId(intentApp);

	       
	        if (reviewApplications == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No Review Application found for the Intent Application ID.");
	        }

	       
	        List<ReviewFields> reviewFields = reviewFieldsService.finByReviewFieldsByApplicationId(reviewApplications.getReviewId());

	       
	        if (reviewFields == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No Review Fields found for the Review Application ID.");
	        }

	      
	        return ResponseEntity.ok(reviewFields);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body("An error occurred while retrieving the review applications: " + e.getMessage());
	    }
	}
	
	
	
	@GetMapping("change-intent-status/{intentAppId}")
	public ResponseEntity<?> changeIntentApplicationStatus(
			@PathVariable String intentAppId
			  ) {
		
		String id = intentAppId;
		
		
		
		try {
			IntentApplication inApp = intentAppServ.getIntentByIntentAppId(Long.parseLong(id));
			
			
			
			//update Intent Application Status;
			inApp.setApplicationStatus("Agency Selection");
			inApp.setUpdated(new Date());
			
			intentAppServ.updateIntentApp(inApp);
			
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
		
		
		
	}
	
	
	
	@GetMapping("/change-intent-status-by-username/{applicantUserName}")
	public ResponseEntity<?> changeIntentApplicationStatusByApplicantUserName(
			@PathVariable String applicantUserName
			  ) {
		
		String userName = applicantUserName;
		
		 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);

		if (intentApp == null) {
			return new ResponseEntity<>("Intent application not found for user: " + userName, HttpStatus.NOT_FOUND);
		}
		
		try {
			IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
			
			
			
			//update Intent Application Status;
			inApp.setApplicationStatus("Recomanded For Audit Agency");
			inApp.setUpdated(new Date());
			
			intentAppServ.updateIntentApp(inApp);
			
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
		
		
		
	}
	
	
	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATIONS_IN_AUDT_APPLICATION)
	public ResponseEntity<?> getAllApplicationsByAuditApplication(@RequestParam("userName") String userName) {
		try {
			List<AuditAgencySelectionDTO> agencySelectionDTOs = new ArrayList<>();

			try {
				agencySelectionDTOs = appTimeLineServ.getAllData(EncryptionUtil.encrypt(userName));
			} catch (Exception e) {
				System.out.println("Audit lookup with encrypted username failed: " + e.getMessage());
			}

			if (agencySelectionDTOs == null || agencySelectionDTOs.isEmpty()) {
				try {
					agencySelectionDTOs = appTimeLineServ.getAllData(userName);
				} catch (Exception e) {
					System.out.println("Audit lookup with plain username failed: " + e.getMessage());
				}
			}

			if (agencySelectionDTOs == null) {
				agencySelectionDTOs = new ArrayList<>();
			}

			if (agencySelectionDTOs.isEmpty()) {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			}

			List<ApplicationDTO> applicationDTOs = new ArrayList<>();

			for (AuditAgencySelectionDTO selectionDTO : agencySelectionDTOs) {
				String applicantUserName = selectionDTO.getAplicantUserName();
				if (applicantUserName == null || applicantUserName.trim().isEmpty()) {
					continue;
				}

				IntentApplication app = intentAppServ.findIntentAppById(applicantUserName.trim());
				if (app == null) {
					continue;
				}

				ApplicationDTO application = new ApplicationDTO();
				application.setAcknowledgementNo(app.getAcknowledgementNo());
				application.setApplicantUserName(app.getUserName());
				application.setApplicationCurrentStatus(app.getApplicationStatus());
				application.setApplicationInitiatedOn(app.getCreated());
				application.setApplicationType(app.getAppTypeMasterId());
				application.setIntentAppId(app.getIntentAppId().toString());

				if ("1".equals(app.getAppTypeMasterId())) {
					List<String> fullNames = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
					application.setApplicantName(
							fullNames != null && !fullNames.isEmpty() ? fullNames.get(0) : "No name found");
				} else if ("2".equals(app.getAppTypeMasterId()) || "3".equals(app.getAppTypeMasterId())) {
					List<String> firmNames = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId());
					application.setApplicantName(
							firmNames != null && !firmNames.isEmpty() ? firmNames.get(0) : "No firm name found");
				} else if ("4".equals(app.getAppTypeMasterId())) {
					List<String> organizationNames = governmentAgencyServ
							.getOrganizationNameByIntentAppId(app.getIntentAppId());
					application.setApplicantName(organizationNames != null && !organizationNames.isEmpty()
							? organizationNames.get(0)
							: "No organization name found");
				} else {
					application.setApplicantName(applicantUserName);
				}

				List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
				if (list != null && !list.isEmpty()) {
					application.setStatusInitiatedOn(list.get(0).getCreated());
				} else {
					application.setStatusInitiatedOn(app.getUpdated());
				}

				applicationDTOs.add(application);
			}

			return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the applications: " + e.getMessage());
		}
	}

	
	@GetMapping("/approve-by-username/{applicantUserName}")
	public ResponseEntity<?> approvedStatusByApplicantUserName(
			@PathVariable String applicantUserName
			  ) {
		
		String userName = applicantUserName;
		
		 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
		
		try {
			IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
			
			
			
			//update Intent Application Status;
			inApp.setApplicationStatus("Approve From CCA");
			inApp.setUpdated(new Date());
			
			intentAppServ.updateIntentApp(inApp);
			
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
	}
		
		@GetMapping("/reject-by-username/{applicantUserName}")
		public ResponseEntity<?> rejectedStatusByApplicantUserName(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			
			try {
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				
				
				//update Intent Application Status;
				inApp.setApplicationStatus("RECOMANDED_FOR_AUDIT");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		
		
	}
	
		@GetMapping("/change-status-by-username/{applicantUserName}")
		public ResponseEntity<?> changeStatusByAuditor(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			
			try {
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				
				
				//update Intent Application Status;
				inApp.setApplicationStatus("NC Issued By Auditor");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		
		
	}
	

		@GetMapping("/change-applicant-status-by-username/{applicantUserName}")
		public ResponseEntity<?> changeStatusByApplicant(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			
			try {
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				
				
				//update Intent Application Status;
				inApp.setApplicationStatus("NC Action taken By Applicant");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		
		
	}
		
		
		
		@GetMapping("/change-applicants-status-by-username/{applicantUserName}")
		public ResponseEntity<?> changeApplicantsStatusByUserName(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			System.out.println("--------->"+userName);
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			
			try {
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				
				System.out.println("abc------->"+inApp.getApplicationStatus());
				//update Intent Application Status;
				inApp.setApplicationStatus("Review Audit Report");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		
		
	}

		
		
		@GetMapping("/change-auditor-status-by-username/{applicantUserName}")
		public ResponseEntity<?> changeApplicantStatusByAuditor(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			
			try {
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				
				
				//update Intent Application Status;
				inApp.setApplicationStatus("Review NC Report");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		
		
	}

		// New endpoint to accept NC report and move to next workflow step (NC Closure)
		@GetMapping("/accept-auditor-status-by-username/{applicantUserName}")
		public ResponseEntity<?> acceptApplicantStatusByAuditor(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
			
			try {
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				// Accept NC Report and move to In Principle Approval queue for CCA
				inApp.setApplicationStatus("RECOMMENDED_IN_PRINCIPLE_APPROVAL");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
			}
		
		
	}

		
		
		
		@GetMapping("/change-applicant-status/{applicantUserName}")
		public ResponseEntity<?> changeStatus(
				@PathVariable String applicantUserName
				  ) {
			
			String userName = applicantUserName;
			
			try {
				IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
				
				// Check if application exists
				if (intentApp == null) {
					System.out.println("IntentApplication not found for username: " + userName);
					return new ResponseEntity<>("Application not found for username: " + userName, HttpStatus.NOT_FOUND);
				}
				
				IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
				
				if (inApp == null) {
					System.out.println("IntentApplication not found for intentAppId: " + intentApp.getIntentAppId());
					return new ResponseEntity<>("Application details not found", HttpStatus.NOT_FOUND);
				}
				
				//update Intent Application Status;
				inApp.setApplicationStatus("Review NC Report");
				inApp.setUpdated(new Date());
				
				intentAppServ.updateIntentApp(inApp);
				
				return new ResponseEntity<>("Success", HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
		
			@GetMapping("/change-applicant-approve-for-rejection/{applicantUserName}")
			public ResponseEntity<?> changeStatusApproveForRejection(
					@PathVariable String applicantUserName
					  ) {
				
				String userName = applicantUserName;
				
				try {
					IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
					
					// Check if application exists
					if (intentApp == null) {
						System.out.println("IntentApplication not found for username: " + userName);
						return new ResponseEntity<>("Application not found for username: " + userName, HttpStatus.NOT_FOUND);
					}
					
					IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
					
					if (inApp == null) {
						System.out.println("IntentApplication not found for intentAppId: " + intentApp.getIntentAppId());
						return new ResponseEntity<>("Application details not found", HttpStatus.NOT_FOUND);
					}
					
					//update Intent Application Status;
					inApp.setApplicationStatus("Send To Rejection");
					inApp.setUpdated(new Date());
					
					intentAppServ.updateIntentApp(inApp);
					
					return new ResponseEntity<>("Success", HttpStatus.OK);
				}
				catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
				}

	}
		

			@GetMapping("/change-applicant-approve/{applicantUserName}")
			public ResponseEntity<?> changeStatusApprove(
					@PathVariable String applicantUserName
					  ) {
				
				String userName = applicantUserName;
				
				try {
					IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
					
					// Check if application exists
					if (intentApp == null) {
						System.out.println("IntentApplication not found for username: " + userName);
						return new ResponseEntity<>("Application not found for username: " + userName, HttpStatus.NOT_FOUND);
					}
					
					IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
					
					if (inApp == null) {
						System.out.println("IntentApplication not found for intentAppId: " + intentApp.getIntentAppId());
						return new ResponseEntity<>("Application details not found", HttpStatus.NOT_FOUND);
					}
					
					//update Intent Application Status;
					inApp.setApplicationStatus("RECOMMENDED_IN_PRINCIPLE_APPROVAL");
					inApp.setUpdated(new Date());
					
					intentAppServ.updateIntentApp(inApp);
					
					return new ResponseEntity<>("Success", HttpStatus.OK);
				}
				catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
				}

	}
	
			
			@GetMapping("/change-payment-status/{applicantUserName}")
			public ResponseEntity<?> changedThePaymentStatus(
					@PathVariable String applicantUserName
					  ) {
				
				String userName = applicantUserName;
				
				 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
				
				if (intentApp == null) {
					return new ResponseEntity<>("Intent application not found for user: " + userName, HttpStatus.NOT_FOUND);
				}
				
				try {
					PaymentProof paymentProof = iPaymentProof.getChangedThePaymentStatus(intentApp.getIntentAppId());
					if (paymentProof != null) {
						paymentProof.setStatus("Inactive");
						paymentProof.setUpdated(new Date());
						iPaymentProof.updateIntentApp(paymentProof);
					}
					
					PaymentVerification paymentVerification = iPaymentVerficationService
							.getChangedThePaymentStatus(intentApp.getIntentAppId());
					if (paymentVerification != null) {
						paymentVerification.setStatus("Inactive");
						paymentVerification.setUpdated(new Date());
						iPaymentVerficationService.updatePymentVerification(paymentVerification);
					}
					
					return new ResponseEntity<>("Success", HttpStatus.OK);
				}
				catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
				}

	}

			@GetMapping("/change-review-status/{applicantUserName}")
			public ResponseEntity<?> changedTheReviewStatus(
					@PathVariable String applicantUserName
					  ) {
				
				String userName = applicantUserName;
				
				 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
				
				if (intentApp == null) {
					return new ResponseEntity<>("Intent application not found for user: " + userName, HttpStatus.NOT_FOUND);
				}
				
				try {
					 List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
					 

					    // Check if reviewApplications is not null or empty
					    if (reviewApplications != null && !reviewApplications.isEmpty()) {
					        // If data found, set the status of the existing applications to "Inactive"
					        for (ReviewApplication existingReviewApp : reviewApplications) {
					            existingReviewApp.setStatus("Inactive");
					            existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(applicantUserName));
					            reviewApplicationService.updateReview(existingReviewApp);  // Save the updated ReviewApplication
					        }
					    }
					
					return new ResponseEntity<>("Success", HttpStatus.OK);
				}
				catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
				}

	}

			
			

			@GetMapping("/change-applicant-cca-rejection/{applicantUserName}")
			public ResponseEntity<?> changeStatusCCARejection(
					@PathVariable String applicantUserName
					  ) {
				
				String userName = applicantUserName;
				
				 IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
				
				try {
					IntentApplication inApp = intentAppServ.getIntentByIntentAppId(intentApp.getIntentAppId());
					
					
					
					//update Intent Application Status;
					inApp.setApplicationStatus("Rejected");
					inApp.setUpdated(new Date());
					
					intentAppServ.updateIntentApp(inApp);
					
					return new ResponseEntity<>("Success", HttpStatus.OK);
				}
				catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
				}

	}
	
			
			@GetMapping(ApplicationReviewAPIs.GET_ALL_APPLICATION_DATA_BY_USERNAME)
			public ResponseEntity<?> getAllApplicationsByUserName1(@RequestParam("userName") String userName) {
			    try {
			      
			        IntentApplication intentApp = intentAppServ.findIntentAppById((userName));
			        if (intentApp == null) {
			            return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                .body("No Intent Application found for the provided username.");
			        }

			       
			        List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentesId(intentApp);
			        if (reviewApplications == null) {
			            return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                .body("No Review Application found for the Intent Application ID.");
			        }

			        List<ReviewFields> allReviewFields = new ArrayList<>();
			        List<ReviewDocuments> allReviewDocuments = new ArrayList<>();

			       
			        for (ReviewApplication reviewApplication : reviewApplications) {
			            List<ReviewFields> reviewFields = reviewFieldsService.reviewDatafields(reviewApplication.getReviewId());
			            List<ReviewDocuments> reviewDocuments = documentsService.reviewDataDocument(reviewApplication.getReviewId());

			            System.out.println("reviewApplication-=-=->"+reviewApplication.getReviewId());
			            System.out.println("reviewFields-=-=->"+reviewFields.toString());
			            System.out.println("reviewDocuments-=-=->"+reviewDocuments.toString());
			            if (reviewFields != null) {
			                allReviewFields.addAll(reviewFields);
			            }
			            if (reviewDocuments != null) {
			                allReviewDocuments.addAll(reviewDocuments);
			            }
			        }

			        Map<String, Object> response = new HashMap<>();
			        response.put("reviewApplications", reviewApplications);
			        response.put("reviewFields", allReviewFields);
			        response.put("reviewDocuments", allReviewDocuments);

			      
			        
			        return ResponseEntity.ok(response);

			    } catch (Exception e) {
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			            .body("An error occurred while retrieving the review applications: " + e.getMessage());
			    }
			}


			@GetMapping(ApplicationReviewAPIs.GET_ALL_APPLICATION_DATA_BY_USERNAMES)
			public ResponseEntity<?> getAllApplicationsByUserName2(@RequestParam("userName") String userName) {
			    try {
			       
			        IntentApplication intentApp = intentAppServ.findIntentAppById((userName));
			        if (intentApp == null) {
			            return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                .body("No Intent Application found for the provided username.");
			        }

			        
			        List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentesId(intentApp);
			        if (reviewApplications == null || reviewApplications.isEmpty()) {
			            return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                .body("No Review Application found for the Intent Application ID.");
			        }

			       
			        List<ReviewApplicationDTO> dtoList = new ArrayList<>();

			        for (ReviewApplication reviewApplication : reviewApplications) {
			            Long reviewId = reviewApplication.getReviewId();

			           
			            List<ReviewFields> reviewFields = reviewFieldsService.reviewDatafields(reviewId);
			            List<ReviewDocuments> reviewDocuments = documentsService.reviewDataDocument(reviewId);

			            
			            List<ReviewFieldsDTO> fieldDTOs = new ArrayList<>();
			            if (reviewFields != null) {
			                for (ReviewFields field : reviewFields) {
			                    fieldDTOs.add(new ReviewFieldsDTO(String.valueOf(field.getReviewFieldId()), field.getFieldName(), field.getFieldName()));
			                }
			            }

			           
			            List<ReviewDocumentDTO> documentDTOs = new ArrayList<>();
			            if (reviewDocuments != null) {
			                for (ReviewDocuments document : reviewDocuments) {
			                    documentDTOs.add(new ReviewDocumentDTO(String.valueOf(document.getReviewDocId()), null, null));
			                }
			            }

			           
			            ReviewApplicationDTO dto = new ReviewApplicationDTO(String.valueOf(reviewId), documentDTOs, fieldDTOs);
			            dtoList.add(dto);
			        }

			       
			        return ResponseEntity.ok(dtoList);

			    } catch (Exception e) {
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			            .body("An error occurred while retrieving the review applications: " + e.getMessage());
			    }
			}

			@PostMapping(ApplicationReviewAPIs.CCA_REJECTED_DATA)
		    public ResponseEntity<?> addAllRejectedAData(@RequestBody Ccadata organizationDetailsDTO) {
		      

		        // Validate input
		        if (organizationDetailsDTO.getUserName() == null || organizationDetailsDTO.getUserName().isEmpty()) {
		            return ResponseEntity.badRequest().body("UserName is required");
		        }

		        // Fetch IntentApplication
		        IntentApplication intentApp = intentAppServ.findIntentAppById(organizationDetailsDTO.getUserName());
		        if (intentApp == null) {
		            return ResponseEntity.badRequest().body("IntentApplication not found");
		        }

		        // Fetch existing review applications
		        List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
		        System.out.printf("Existing Review Remarks: {}", (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));

		        // Update existing active review applications
		        if (!reviewApplications.isEmpty()) {
		            for (ReviewApplication existingReviewApp : reviewApplications) {
		                if ("Active".equals(existingReviewApp.getStatus())) {  // Corrected string comparison
		                    existingReviewApp.setCcaRemarks(organizationDetailsDTO.getRemarks());
		                    existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
		                    reviewApplicationService.updateReview(existingReviewApp);  
		                }
		            }
		        }

		        // Update application status
		        intentApp.setApplicationStatus("pending");
		        intentAppServ.updateIntentApp(intentApp);

		        return ResponseEntity.ok("CCA Data Saved Successfully");
		    }
			
			@PostMapping(ApplicationReviewAPIs.CCA_APPROVED_DATA)
		    public ResponseEntity<?> addAllAprovedAData(@RequestBody Ccadata organizationDetailsDTO) {
		      

		        // Validate input
		        if (organizationDetailsDTO.getUserName() == null || organizationDetailsDTO.getUserName().isEmpty()) {
		            return ResponseEntity.badRequest().body("UserName is required");
		        }

		        // Fetch IntentApplication
		        IntentApplication intentApp = intentAppServ.findIntentAppById(organizationDetailsDTO.getUserName());
		        if (intentApp == null) {
		            return ResponseEntity.badRequest().body("Intent Application not found");
		        }

		        // Fetch existing review applications
		        List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
		        System.out.printf("Existing Review Remarks: {}", (reviewApplications.isEmpty() ? "No Remarks" : reviewApplications.get(0).getRemarks()));

		        // Update existing active review applications
		        if (!reviewApplications.isEmpty()) {
		            for (ReviewApplication existingReviewApp : reviewApplications) {
		                if ("Active".equals(existingReviewApp.getStatus())) {  // Corrected string comparison
		                    existingReviewApp.setCcaRemarks(organizationDetailsDTO.getRemarks());
		                    existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
		                    reviewApplicationService.updateReview(existingReviewApp);  
		                }
		            }
		        }

		        // Update application status
		        intentApp.setApplicationStatus("Application Rejected");
		        intentAppServ.updateIntentApp(intentApp);

		        return ResponseEntity.ok("CCA Data Saved Successfully");
		    }
	
			
			@PostMapping(ApplicationReviewAPIs.APPROVED_DATA)
			public ResponseEntity<?> addAllApproved(@ModelAttribute Approveddata organizationDetailsDTO) {
			    try {
			        // Validate input
			        if (organizationDetailsDTO.getUserName() == null || organizationDetailsDTO.getUserName().isEmpty()) {
			            return ResponseEntity.badRequest().body("UserName is required");
			        }

			        // Fetch IntentApplication
			        IntentApplication intentApp = intentAppServ.findIntentAppById(organizationDetailsDTO.getUserName());
			        if (intentApp == null) {
			            return ResponseEntity.badRequest().body("Intent Application not found");
			        }
			        System.out.println("Fetched IntentApplication: " + intentApp);

			        // Fetch existing review applications
			        List<ReviewApplication> reviewApplications = reviewApplicationService.findByIntentId(intentApp);
			        System.out.println("Review Applications found: " + reviewApplications.size());

			        // Update existing active review applications or create new ones
			        if (!reviewApplications.isEmpty()) {
			            // Handle update for existing review applications
			            for (ReviewApplication existingReviewApp : reviewApplications) {
			                if (Boolean.FALSE.equals(organizationDetailsDTO.getIsRejected())) {
			                    existingReviewApp.setRemarks(organizationDetailsDTO.getRemarks());
			                    existingReviewApp.setUpdatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
			                    String fileName = DocumentFileUtil.saveFile(organizationDetailsDTO.getFile1(), "MOMFile", rnum.toString(), "MOMFile");
			                    existingReviewApp.setMomFile(fileName);
			                    String reviewDateStr = organizationDetailsDTO.getReviewDate();
			                    if (reviewDateStr != null && !reviewDateStr.isEmpty()) {
			                        // Assuming the date string is in "yyyy-MM-dd" format
			                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			                        Date reviewDate = dateFormat.parse(reviewDateStr);
			                        existingReviewApp.setReviewDate(reviewDate);
			                    } else {
			                        existingReviewApp.setReviewDate(null); // Handle null or empty string case
			                    }
			                    reviewApplicationService.updateReview(existingReviewApp);
			                    System.out.println("Updated review application: " + existingReviewApp);
			                }
			            }
			        } else {
			            // If no review applications exist, create a new one
			            if (Boolean.FALSE.equals(organizationDetailsDTO.getIsRejected())) {
			                ReviewApplication newReviewApp = new ReviewApplication();
			                newReviewApp.setIntentAppId(intentApp);
			                newReviewApp.setStatus("Active");
			                newReviewApp.setRemarks(organizationDetailsDTO.getRemarks());
			                newReviewApp.setUpdatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
			                String fileName = DocumentFileUtil.saveFile(organizationDetailsDTO.getFile1(), "MOMFile", rnum.toString(), "MOMFile");
			                newReviewApp.setMomFile(fileName);
			                String reviewDateStr = organizationDetailsDTO.getReviewDate();
			                if (reviewDateStr != null && !reviewDateStr.isEmpty()) {
			                    // Assuming the date string is in "yyyy-MM-dd" format
			                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			                    Date reviewDate = dateFormat.parse(reviewDateStr);
			                    newReviewApp.setReviewDate(reviewDate);
			                } else {
			                    newReviewApp.setReviewDate(null);
			                }
			                reviewApplicationService.addReview(newReviewApp);
			                System.out.println("Created new review application: " + newReviewApp);
			            }
			        }

			        // Update application status based on rejection status
			        if (Boolean.TRUE.equals(organizationDetailsDTO.getIsRejected())) {
			            intentApp.setApplicationStatus("RECOMANDED_FOR_AUDIT");
			            ApplicationTimeLine obj1 = new ApplicationTimeLine();
						obj1.setApplicationStatus("RECOMANDED_FOR_AUDIT");
						obj1.setInitiatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
						obj1.setIntentAppId(intentApp);
						
						appTimeLineServ.addApplicationTimeLine(obj1);
			            
			        } else {
			            intentApp.setApplicationStatus("Approved From Review");
			            
			            ApplicationTimeLine obj1 = new ApplicationTimeLine();
						obj1.setApplicationStatus("Approved From Review");
						obj1.setInitiatedBy(EncryptionUtil.encrypt(organizationDetailsDTO.getReviewerUserName()));
						obj1.setIntentAppId(intentApp);
						
						appTimeLineServ.addApplicationTimeLine(obj1);
			        }

			        // Save updated IntentApplication
			        intentAppServ.updateIntentApp(intentApp);
			        System.out.println("Updated Application Status: " + intentApp.getApplicationStatus());

			        return ResponseEntity.ok("CCA Data Saved Successfully");
			    } catch (Exception e) {
			        e.printStackTrace();
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                .body("An error occurred while processing the request.");
			    }
			}

			
			
			@GetMapping(ApplicationReviewAPIs.VIEWS_FILE)
			public ResponseEntity<Resource> viewFile(@RequestParam("id") String fileName) {
			    System.out.println(fileName);
			    String id = EncryptionUtil.decrypt(fileName);

			    try {
			        Optional<ReviewApplication> applicationMoMDocument = reviewApplicationService.downloadfile(id);

			        if (applicationMoMDocument.isPresent()) {
			        	ReviewApplication c = applicationMoMDocument.get();
			            System.out.println(c.getMomFile());

			            // Define the paths for all three directories
			            Path additionalDocPath = Paths.get(Constant.REAL_PATH + "//MOMFile").resolve(c.getMomFile()).normalize();
			            System.out.println("Checking Additional Document Path: " + additionalDocPath);
			          

			            // Check the file path in the three directories
			            Path filePath = null;
			            if (Files.exists(additionalDocPath)) {
			                filePath = additionalDocPath;  // If file exists in the first path
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

	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATION)
	public ResponseEntity<?> getAllApplicationes() {

	    List<ApplicationDTO> applicationDTO = new ArrayList<>();
	    List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();

	    for (IntentApplication app : intentApplications) {
	        String appStatus = app.getApplicationStatus();
	        if (appStatus != null && (
	            appStatus.toUpperCase().contains("RECOMANDED") ||
	            appStatus.toUpperCase().contains("RECOMMENDED") ||
	            appStatus.toUpperCase().contains("AUDIT") ||
	            appStatus.equalsIgnoreCase("Review NC Report") ||
	            appStatus.equalsIgnoreCase("Send To Rejection") ||
	            appStatus.equalsIgnoreCase("Review Audit Report") ||
	            appStatus.equalsIgnoreCase("Agency Selection"))) {

	            ApplicationDTO application = new ApplicationDTO();
	            application.setAcknowledgementNo(app.getAcknowledgementNo());
	            application.setApplicantUserName(app.getUserName());
	            application.setApplicationCurrentStatus(app.getApplicationStatus());
	            application.setApplicationInitiatedOn(app.getCreated());
	            application.setApplicationType(app.getAppTypeMasterId());
	            application.setIntentAppId(app.getIntentAppId().toString());
	            application.setStatusInitiatedOn(null);

	            String status = "Active";
	            if ("1".equals(app.getAppTypeMasterId())) {
	                List<String> fullNames = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
	                application.setApplicantName(fullNames != null && !fullNames.isEmpty() ? fullNames.get(0) : "No name found");
	            } else if ("2".equals(app.getAppTypeMasterId()) || "3".equals(app.getAppTypeMasterId())) {
	                List<String> firmNames = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId());
	                application.setApplicantName(firmNames != null && !firmNames.isEmpty() ? firmNames.get(0) : "No firm name found");
	            } else if ("4".equals(app.getAppTypeMasterId())) {
	                List<String> organizationNames = governmentAgencyServ.getOrganizationNameByIntentAppId(app.getIntentAppId());
	                application.setApplicantName(organizationNames != null && !organizationNames.isEmpty() ? organizationNames.get(0) : "No organization name found");
	            }

	            List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	            if (!list.isEmpty()) {
	                application.setStatusInitiatedOn(list.get(0).getCreated());
	            } else {
	                application.setStatusInitiatedOn(app.getUpdated());
	            }

	            applicationDTO.add(application);
	        }
	    }

	    return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
	}

}
