package in.lms.cca.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import in.lms.cca.dto.ApplicationReviewDTO;
import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.dto.OrganisationDetailsDTO;
import in.lms.cca.dto.UserDetailsDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.dto.client.ApplicationDTO;
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
import in.lms.cca.service.IndivAdditionalDetailsService;
import in.lms.cca.service.ReviewApplicationService;
import in.lms.cca.service.ReviewDocumentsService;
import in.lms.cca.service.ReviewFieldsService;
import in.lms.cca.util.api.ApplicationReviewAPIs;
import in.lms.cca.util.api.RenewLicenseServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@CrossOrigin
@RequestMapping(RenewLicenseServiceAPIs.RENEW_LICENSE_SERVICE_BASE_URL)
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

	
	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATIONS_IN_REVIEW)
	public ResponseEntity<?> getAllApplications() {

	    List<ApplicationDTO> applicationDTO = new ArrayList<>();

	    // Get all intent applications
	    List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
	    
	   System.out.println(" intentApplications.size()====>"+ intentApplications.size());

	    for (IntentApplication app : intentApplications) {
	    	
	    	

	        // Check if the application status is "underReview"
	    	if (app.getApplicationStatus() != null && 
	    		    (app.getApplicationStatus().equals("SUBMITTED-FOR-RENEWAL"))) {

	            ApplicationDTO application = new ApplicationDTO();

	            application.setAcknowledgementNo(app.getAcknowledgementNo());
	            application.setApplicantUserName(app.getUserName());
	            application.setApplicationCurrentStatus(app.getApplicationStatus());
	            application.setApplicationInitiatedOn(app.getCreated());
	            application.setApplicationType(app.getAppTypeMasterId());
	            application.setIntentAppId(app.getIntentAppId().toString());
	            application.setStatusInitiatedOn(null);
String status="Active";
	         // Check if appTypeMasterId is 1 and fetch full name based on intent ID
	            if (app.getAppTypeMasterId().equals("1")) {
	                System.out.println("abcd---->" + app.getIntentAppId());
	                String fullName = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
	                application.setApplicantName(fullName);
	            } else if (app.getAppTypeMasterId().equals("2") || app.getAppTypeMasterId().equals("3")) {
	                String fullName = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId(),status);
	                application.setApplicantName(fullName);
	            } else if (app.getAppTypeMasterId().equals("4")) {
	                String fullName = governmentAgencyServ.getOrganizationNameByIntentAppId(app.getIntentAppId());
	                application.setApplicantName(fullName);
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

	
	@PostMapping(ApplicationReviewAPIs.ADD_INDIVIDUAL_APPLICATION_REVIEW)
	public ResponseEntity<?> addAllIndividualApplicationReviewData(@RequestBody UserDetailsDTO organizationDetailsDTO) {

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

	    // Set remarks, status, and reviewer details for the new entry
	    reviewApplication.setRemarks(organizationDetailsDTO.getRemarks());
	    reviewApplication.setStatus("RENEW-APPLICATION-REVIEW");  // Mark new entry as Active
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
	    intentApp.setApplicationStatus("Edit_Upon_Review");
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
	    
	    BankDetails bankDetails = bankDetailsSev .findIntentAppById(intentApp.getIntentAppId());
	    if(bankDetails!=null) {
        bankDetails.setStatus("Inactive");
        bankDetailsSev.updateBankDetails(bankDetails);
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

        
        intentApp.setApplicationStatus("Edit_Upon_Review");
        intentAppServ.updateIntentApp(intentApp);
	   
	    return ResponseEntity.ok("Application Review Data Saved Successfully");
	}

	
	
	
	
	
	@PostMapping(ApplicationReviewAPIs.ADD_APPLICATION_REVIEW)
	public ResponseEntity<?> addAllApplicationsReviewData(@RequestBody ApplicationReviewDTO applicationReviewDTO) {

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

	    // Set remarks, status, and reviewer details for the new entry
	    reviewApplication.setRemarks(applicationReviewDTO.getRemarks());
	    reviewApplication.setStatus("RENEW-APPLICATION-REVIEW");  // Mark new entry as Active
	    reviewApplication.setCreatedBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));
	    reviewApplication.setUpdatedBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));
	    reviewApplication.setReviewBy(EncryptionUtil.encrypt(applicationReviewDTO.getReviewerUserName()));

	   
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
	    // Save the new ReviewApplication to the database
	 
	    intentApp.setApplicationStatus("Edit_Upon_Review");
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
	    
	    FirmApplication2 appFirmApplication = firmApplication2Serv.findIntentAppById(intentAppId);
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
	    
	    BankDetails bankDetails = bankDetailsSev .findIntentAppById(intentApp.getIntentAppId());
        bankDetails.setStatus("Inactive");
        bankDetailsSev.updateBankDetails(bankDetails);
	      
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

	   
	    return ResponseEntity.ok("Application Review Data Saved Successfully");
	}

	
	@PostMapping(ApplicationReviewAPIs.ADD_GOVERNMENT_AGENCY_APPLICATION_REVIEW)
	public ResponseEntity<?> addAllGoApplicationsReviewData(@RequestBody OrganisationDetailsDTO organisationDetailsDTO) {

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

	    // Set remarks, status, and reviewer details for the new entry
	    reviewApplication.setRemarks(organisationDetailsDTO.getRemarks());
	    reviewApplication.setStatus("RENEW-APPLICATION-REVIEW");  // Mark new entry as Active
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
	    intentApp.setApplicationStatus("Edit_Upon_Review");
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
	    
	    BankDetails bankDetails = bankDetailsSev .findIntentAppById(intentApp.getIntentAppId());
        bankDetails.setStatus("Inactive");
        bankDetailsSev.updateBankDetails(bankDetails);
	      
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
	                && !fieldName.toLowerCase().contains("firm")) {
	                
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

	            System.out.println("--=----=-====>"+fieldName);
	            // Process fields that start with "firm"
	            if (value != null && fieldName.toLowerCase().startsWith("firm")) {
	            	
	            	  // Check if reviewFieldsList already has data
	                List<ReviewDocuments> existingReviewDocuments = documentsService.getAllDetailsByReviewId(reviewId);
	                
	                if (existingReviewDocuments.isEmpty()) {
	                    // No existing data, add new fields
	                	 addReviewDocument(reviewDocuments, reviewApplication, fieldName, value.toString(), reviewerUserName);
	                } else {
	                    // Existing data found, update status of existing fields to "Inactive"
	                    for (ReviewDocuments existingField : existingReviewDocuments) {
	                        existingField.setStatus("Inactive"); // Assuming there is a setStatus method
	                        documentsService.updateReviewField(existingField); // Save the updated status
	                    }
	                // Add field to ReviewDocuments list
	                addReviewDocument(reviewDocuments, reviewApplication, fieldName, value.toString(), reviewerUserName);
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
	private void addReviewDocument(List<ReviewDocuments> reviewDocuments, ReviewApplication reviewApplication, String fieldName, String value, String reviewerUserName) {
	    ReviewDocuments document = new ReviewDocuments();
	    document.setReviewId(reviewApplication);
	    
	    // Create and set ApplicationDocument based on the fieldName
	    ApplicationDocument applicationDocument = new ApplicationDocument();
	    applicationDocument.setAppDocId(Long.valueOf(value)); // Ensure the fieldName is convertible to Long
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
	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATIONS_IN_AUDT_APPLICATION)
	public ResponseEntity<?> getAllApplicationsByAuditApplication(@RequestParam("userName") String userName) {
	    try {
	       
	        UserLoginDTO userLoginDTO = appTimeLineServ.getAllDetailsByUserName(userName);
	        
	       // System.out.println("userLoginDTO=-=--=->"+userLoginDTO.getUserId());
	        
//	        if (userLoginDTO == null) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                    .body("No user details found for username: " + userName);
//	        }

	        if (userLoginDTO == null) {
	            System.out.println("No user details found for username: " + userName);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("No user details found for username: " + userName);
	        }
	        System.out.println("userLoginDTO=-=--=->"+userLoginDTO.getUserId());
	        System.out.println("userLoginDTO: " + userLoginDTO);

	       
//	        List<AuditAgencySelectionDTO> agencySelectionDTOs = appTimeLineServ.getAllData(userLoginDTO.getUserId());

//	        String decryptedAgencyId = EncryptionUtil.decrypt(userLoginDTO.getUserId());
//	        List<AuditAgencySelectionDTO> agencySelectionDTOs = appTimeLineServ.getAllData(decryptedAgencyId);
//	        if (agencySelectionDTOs == null || agencySelectionDTOs.isEmpty()) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                    .body("No agency selection details found for user: " + userName);
//	        }
	        
	     // ...
	        List<AuditAgencySelectionDTO> agencySelectionDTOs = new ArrayList<>();

	        try {
	            agencySelectionDTOs = appTimeLineServ.getAllData(EncryptionUtil.encrypt(userName));
	        } catch (Exception e) {
	            System.out.println("Audit lookup with encrypted username failed: " + e.getMessage());
	        }

	        if (agencySelectionDTOs == null || agencySelectionDTOs.isEmpty()) {
	            try {
	                String decryptedAgencyId = EncryptionUtil.decrypt(userLoginDTO.getUserId());
	                agencySelectionDTOs = appTimeLineServ.getAllData(decryptedAgencyId);
	            } catch (Exception e) {
	                System.out.println("No assigned applications found for audit agency: " + userName);
	            }
	        }

	        // If list is empty, return OK with empty list (so the Frontend grid shows 0 records instead of Error)
	        if (agencySelectionDTOs == null || agencySelectionDTOs.isEmpty()) {
	            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
	        }
	        // ... Rest of your code handles the data if it exists ...

	        System.out.println("agencySelectionDTO size: " + agencySelectionDTOs.size());

	       
	        List<ApplicationDTO> applicationDTOs = new ArrayList<>();

	        
	        for (AuditAgencySelectionDTO selectionDTO : agencySelectionDTOs) {

	          
	            List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplicationByApplicantUserName(selectionDTO.getAplicantUserName());

	            System.out.println("intentApplications size: " + intentApplications.size());

	          
	            for (IntentApplication app : intentApplications) {

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
	                    String fullName = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
	                    application.setApplicantName(fullName);
	                } else if ("2".equals(app.getAppTypeMasterId()) || "3".equals(app.getAppTypeMasterId())) {
	                    String fullName = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId(), status);
	                    System.out.println(fullName);
	                    System.out.println(app.getAppTypeMasterId());
	                    application.setApplicantName(fullName);
	                } else if ("4".equals(app.getAppTypeMasterId())) {
	                    String fullName = governmentAgencyServ.getOrganizationNameByIntentAppId(app.getIntentAppId());
	                    application.setApplicantName(fullName);
	                }

	               
	                List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	                if (list != null && !list.isEmpty()) {
	                    application.setStatusInitiatedOn(list.get(0).getCreated());
	                } else {
	                    application.setStatusInitiatedOn(app.getUpdated());
	                }

	               
	                applicationDTOs.add(application);
	            }
	        }

	       
	        return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the applications: " + e.getMessage());
	    }
	}

	
	@GetMapping(ApplicationReviewAPIs.GET_ALL_NEW_APPLICATION)
	public ResponseEntity<?> getAllApplicationes() {
		
	    List<ApplicationDTO> applicationDTO = new ArrayList<>();

	    // Get all intent applications
	    List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
	    
	   System.out.println(" intentApplications.size()====>"+ intentApplications.size());

	    for (IntentApplication app : intentApplications) {
	    	
	    	

	   String appStatus = app.getApplicationStatus();
	   // Check for various audit-related statuses (case-insensitive partial match for flexibility)
	   if(appStatus != null && (
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
                    String fullName = indAppFormServ.getFullNameByIntentAppId(app.getIntentAppId());
                    application.setApplicantName(fullName);
                } else if ("2".equals(app.getAppTypeMasterId()) || "3".equals(app.getAppTypeMasterId())) {
                    String fullName = firmApplicationServ.getFirmNameByIntentAppId(app.getIntentAppId(), status);
                    System.out.println(fullName);
                    application.setApplicantName(fullName);
                } else if ("4".equals(app.getAppTypeMasterId())) {
                    String fullName = governmentAgencyServ.getOrganizationNameByIntentAppId(app.getIntentAppId());
                    application.setApplicantName(fullName);
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

	
}
