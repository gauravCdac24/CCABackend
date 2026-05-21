package in.lms.cca.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;


import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.dto.OrganizationDetailsDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.BankDetails;
import in.lms.cca.entity.BankDraft;
import in.lms.cca.entity.FirmApplication;
import in.lms.cca.entity.FirmAuthorizedRepresentative;
import in.lms.cca.entity.FirmPartnerDetails;
import in.lms.cca.entity.GovtOrganizationApplication;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.BankDetailsService;
import in.lms.cca.service.BankDraftService;
import in.lms.cca.service.GovernmentAgencyService;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.util.api.FirmApplicationFormAPIs;
import in.lms.cca.util.api.GovernmentAgencyApplicationFormAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin(CrossOrigins.Origins)
public class GovernmentAgencyFormController {
	
	
	@Autowired
	private GovernmentAgencyService governmentAgencyServ;
	
	@Autowired
	private IIntentApplicationService intentAppServ;
	
	@Autowired
	private AppLocationService appLocationServ;
	
	@Autowired
	private ApplicationDocumentService  applicationDocumentServiceServ;
	
	@Autowired
	private BankDetailsService bankDetailsSev;
	
	@Autowired
	private BankDraftService bankDraftServ;

	@Autowired
	private IApplicationTimeLineService appTimeLineServ;

	
	
	@PostMapping(GovernmentAgencyApplicationFormAPIs.ADD_GOVERNMENT_AGENCY_APPLICATION_FORM)
	public ResponseEntity<?> addGovernmentAgency(@RequestBody OrganizationDetailsDTO organizationalDTO) {
		
		 // Organization Name validation
	    if (organizationalDTO.getOrganizationName() == null || organizationalDTO.getOrganizationName().trim().isEmpty()) {
	        return new ResponseEntity<>("Organization Name is required.", HttpStatus.BAD_REQUEST);
	    }

	    // Department validation
	    if (organizationalDTO.getDepartment() == null || organizationalDTO.getDepartment().trim().isEmpty()) {
	        return new ResponseEntity<>("Department is required.", HttpStatus.BAD_REQUEST);
	    }

	    // OrgType validation
	    System.out.println("abc===>"+organizationalDTO.getOrgType());
	    if (organizationalDTO.getOrgType() == null || organizationalDTO.getOrgType().isEmpty()) {
	        return new ResponseEntity<>("Organization Type is required.", HttpStatus.BAD_REQUEST);
	    }

	    // Block No validation
	    if (organizationalDTO.getBlockNo() != null && organizationalDTO.getBlockNo().length() > 30) {
	        return new ResponseEntity<>("The length of Block No must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Village validation
	    if (organizationalDTO.getVillage() != null && organizationalDTO.getVillage().length() > 30) {
	        return new ResponseEntity<>("The length of Village must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Post Office validation
	    if (organizationalDTO.getPostOffice() != null && organizationalDTO.getPostOffice().length() > 30) {
	        return new ResponseEntity<>("The length of Post Office must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Subdivision validation
	    if (organizationalDTO.getSubDivision() != null && organizationalDTO.getSubDivision().length() > 30) {
	        return new ResponseEntity<>("The length of Subdivision must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Country validation
	    if (organizationalDTO.getCountry() == null || organizationalDTO.getCountry().trim().isEmpty()) {
	        return new ResponseEntity<>("Country is required.", HttpStatus.BAD_REQUEST);
	    }

	    // State validation
	    if (organizationalDTO.getState() == null || organizationalDTO.getState().trim().isEmpty()) {
	        return new ResponseEntity<>("State is required.", HttpStatus.BAD_REQUEST);
	    }

	    // City validation
	    if (organizationalDTO.getCity() == null || organizationalDTO.getCity().trim().isEmpty()) {
	        return new ResponseEntity<>("City is required.", HttpStatus.BAD_REQUEST);
	    }

	    // PIN validation
	    if (organizationalDTO.getPin() == null || organizationalDTO.getPin().trim().isEmpty()) {
	        return new ResponseEntity<>("PIN is required.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getPin().matches("\\d{6}")) {
	        return new ResponseEntity<>("Please enter a valid 6-digit PIN code.", HttpStatus.BAD_REQUEST);
	    }

	    // Fax validation (optional)
	    if (organizationalDTO.getFax() != null && organizationalDTO.getFax().length() > 15) {
	        return new ResponseEntity<>("The length of Fax must be between 0 and 15 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Telephone No validation
	    if (organizationalDTO.getTelephoneNo() != null && !organizationalDTO.getTelephoneNo().matches("\\d{10,15}")) {
	        return new ResponseEntity<>("Please enter a valid telephone number.", HttpStatus.BAD_REQUEST);
	    }

	    // Web URL validation (optional)
	    if (organizationalDTO.getWebURL() != null && !organizationalDTO.getWebURL().matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
	        return new ResponseEntity<>("Please enter a valid Web URL.", HttpStatus.BAD_REQUEST);
	    }

	    // Salutation validation
	    if (organizationalDTO.getSalutation() == null || organizationalDTO.getSalutation().trim().isEmpty()) {
	        return new ResponseEntity<>("Salutation is required.", HttpStatus.BAD_REQUEST);
	    }

	    // First Name validation
	    if (organizationalDTO.getFirstName() == null || organizationalDTO.getFirstName().trim().isEmpty()) {
	        return new ResponseEntity<>("First Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (organizationalDTO.getFirstName().length() < 1 || organizationalDTO.getFirstName().length() > 30) {
	        return new ResponseEntity<>("The length of First Name must be between 1 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getFirstName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid First Name.", HttpStatus.BAD_REQUEST);
	    }
	    // Middle Name validation (optional)
	    if(organizationalDTO.getMiddleName() != null || organizationalDTO.getMiddleName().isEmpty() || organizationalDTO.getMiddleName().equals(""))
	    {
	
	    }else if (organizationalDTO.getMiddleName() != null && organizationalDTO.getMiddleName().length() > 30) {
	        return new ResponseEntity<>("The length of Middle Name must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }else if (organizationalDTO.getMiddleName() != null && !organizationalDTO.getMiddleName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Middle Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Last Name validation
	    if (organizationalDTO.getLastName() == null || organizationalDTO.getLastName().trim().isEmpty()) {
	        return new ResponseEntity<>("Last Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (organizationalDTO.getLastName().length() < 1 || organizationalDTO.getLastName().length() > 30) {
	        return new ResponseEntity<>("The length of Last Name must be between 1 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getLastName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Last Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Email validation
	    if (organizationalDTO.getEmailId() == null || organizationalDTO.getEmailId().trim().isEmpty()) {
	        return new ResponseEntity<>("Email Address cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	        return new ResponseEntity<>("Please enter a valid Email Address.", HttpStatus.BAD_REQUEST);
	    }

	    // Designation validation
	    if (organizationalDTO.getDesignation() == null || organizationalDTO.getDesignation().trim().isEmpty()) {
	        return new ResponseEntity<>("Designation is required.", HttpStatus.BAD_REQUEST);
	    }

	    // Public Key validation (optional)
	    if (organizationalDTO.getPublickey() != null && organizationalDTO.getPublickey().length() > 255) {
	        return new ResponseEntity<>("The length of Public Key must not exceed 255 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Username validation
	    if (organizationalDTO.getUserName() == null || organizationalDTO.getUserName().trim().isEmpty()) {
	        return new ResponseEntity<>("Username cannot be empty.", HttpStatus.BAD_REQUEST);
	    }


		try {
			IntentApplication intentApp = new IntentApplication();
			intentApp.setAppTypeMasterId(organizationalDTO.getAppTypeMasterId());
			intentApp.setUserName(organizationalDTO.getUserName());
			intentApp.setUniqueCode(organizationalDTO.getUniqueCode());
			intentApp.setApplicationStatus("pending");
			intentAppServ.addIntentApp(intentApp);
			
			AddressDTO addressDTO = new AddressDTO(); // Assuming you have an AddressDTO class for this purpose

			// Setting address details into the AddressDTO
			addressDTO.setBlockNo(organizationalDTO.getBlockNo());
			addressDTO.setVillage(organizationalDTO.getVillage());
			addressDTO.setPostOffice(organizationalDTO.getPostOffice());
			addressDTO.setSubDivision(organizationalDTO.getSubDivision());
			addressDTO.setCountry(organizationalDTO.getCountry());
			addressDTO.setCity(organizationalDTO.getCity());
			addressDTO.setState(organizationalDTO.getState());
			addressDTO.setPin(organizationalDTO.getPin());

			// Pass the addressDTO to the addUsers() method
			Long resAddId = intentAppServ.addUsers(addressDTO);
			
			  AppLocation resLocation = new AppLocation();
		        resLocation.setAddressId(EncryptionUtil.encrypt( String.valueOf(resAddId))); 
		        resLocation.setLocationName("OrganizationAddress");
		        resLocation.setIntentAppId(intentApp); 
		        resLocation.setStatus("Active");
		    
		        Optional<AppLocation> resLoc=appLocationServ.addAppLocation(resLocation);
			   
			GovtOrganizationApplication govtOrganizationApplication = new GovtOrganizationApplication();

			// Setting organization details
			govtOrganizationApplication.setOrgName(organizationalDTO.getOrganizationName());
			govtOrganizationApplication.setAdministrativeMinistry(organizationalDTO.getDepartment());
			govtOrganizationApplication.setOrgType(organizationalDTO.getOrgType());

			// Setting communication details
			govtOrganizationApplication.setFax(organizationalDTO.getFax());
			govtOrganizationApplication.setTelephoneNo(organizationalDTO.getTelephoneNo());
			govtOrganizationApplication.setWebPageURL(organizationalDTO.getWebURL());
			govtOrganizationApplication.setIntentAppId(intentApp);
			// Setting personal details
			govtOrganizationApplication.setSalutation(organizationalDTO.getSalutation());
			govtOrganizationApplication.setFirstName(organizationalDTO.getFirstName());
			govtOrganizationApplication.setMiddleName(organizationalDTO.getMiddleName());
			govtOrganizationApplication.setLastName(organizationalDTO.getLastName());
			govtOrganizationApplication.setEmailId(organizationalDTO.getEmailId());
			govtOrganizationApplication.setDesignation(organizationalDTO.getDesignation());

			// Setting public key and other details
			govtOrganizationApplication.setPublicKey(organizationalDTO.getPublickey());
			govtOrganizationApplication.setStatus("Active");
			
			 ApplicationTimeLine obj1 = new ApplicationTimeLine();
				obj1.setApplicationStatus("pending");
				obj1.setInitiatedBy(EncryptionUtil.encrypt(organizationalDTO.getUserName()));
				obj1.setIntentAppId(intentApp);
				
				appTimeLineServ.addApplicationTimeLine(obj1);

			Optional<GovtOrganizationApplication>result =governmentAgencyServ.addData(govtOrganizationApplication);
		
			if (result.isEmpty()) {
				return new ResponseEntity<>("Error occurred while saving First Step.",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the First Step.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(GovernmentAgencyApplicationFormAPIs.GET_GOVERNMENT_AGENCY_APPLICATION_FORMBY_USERNAME)
	public ResponseEntity<?> getGovernmentAgencyApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        System.out.println("userName ===> " + userName);

	        // Fetch the Intent Application
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        System.out.println("intentAppId ===> " + intentAppId);
	        Map<String, Object> response = new HashMap<>();

	        System.out.println("intentApp.getApplicationStatus() ===> " + intentApp.getApplicationStatus());
	        // Check for "Edit_Upon_Review" status to handle "Inactive" locations
	        if ("Edit_Upon_Review".equals(intentApp.getApplicationStatus())) {
	            handleLocationsForStatus(intentAppId, "Inactive", response);  // Handle Inactive locations for review edit mode
	            // Fallback: if no Inactive data found, try Active
	            if (response.isEmpty() || response.get("appGovtOrganizationApplication") == null) {
	                System.out.println("Fallback from Inactive to Active for Edit_Upon_Review");
	                handleLocationsForStatus(intentAppId, "Active", response);
	            }
	        }else {
	        	  // Always check for "Active" locations regardless of the status
		        handleLocationsForStatus(intentAppId, "Active", response);  // Handle Active locations
		        // Fallback: if no Active data found, try without status filter
		        if (response.isEmpty() || response.get("appGovtOrganizationApplication") == null) {
		            System.out.println("Fallback to handleLocationsWithoutStatusFilter");
		            handleLocationsWithoutStatusFilter(intentAppId, response);
		        }
	        }

	      

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private void handleLocationsForStatus(Long intentAppId, String status, Map<String, Object> response) {
	    try {
	        // Fetch App Locations based on status
	    	
	    	System.out.println("status====>1245w"+status);
	        List<AppLocation> appLocationList = appLocationServ.findIntentAppById(intentAppId, status);
	        if (appLocationList == null || appLocationList.isEmpty()) {
	            System.out.println("App Location not found for status: " + status);
	            return;
	        }

	        System.out.println("AppLocation size: " + appLocationList.size());
        // Debug: Print all locationNames to see what's being returned
        for (AppLocation loc : appLocationList) {
            System.out.println("DEBUG: Found locationName = " + loc.getLocationName() + ", status = " + loc.getStatus() + ", addressId = " + loc.getAddressId());
        }

        // Fetch Government Organization Application and add to response
        GovtOrganizationApplication appGovtOrganizationApplication = governmentAgencyServ.findIntentAppById(intentAppId,status);
        response.put("appGovtOrganizationApplication", appGovtOrganizationApplication);
        response.put("appLocation", appLocationList);

	        // Process addresses
	        IndivAddressDTO indivAddressDTO = new IndivAddressDTO();  // Initialize once and reuse
	        for (AppLocation location : appLocationList) {
	            System.out.println("AppLocation object: " + location);

	            String addressIdStr = location.getAddressId();
	            if (addressIdStr != null && !addressIdStr.isEmpty()) {
	                try {
	                    String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	                    System.out.println("Decrypted Address ID: " + decryptedAddressId);

	                    AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);
	                    System.out.println("addressDTO ===> " + addressDTO);

	                    // Compare location name and set the appropriate address in IndivAddressDTO
	                    if ("OrganizationAddress".equals(location.getLocationName())) {
	                        indivAddressDTO.setResidential(addressDTO);
	                        System.out.println("country ===> " + addressDTO.getCountry());
	                        System.out.println("Address Set: " + addressDTO);
	                    }
	                    // Add the address to the response
	                    response.put("indivAddressDTO", indivAddressDTO);
	                    System.out.println("Address Details: " + addressDTO);

	                } catch (Exception e) {
	                    System.out.println("Error processing Address ID: " + addressIdStr);
	                }
	            } else {
	                System.out.println("Address ID is null or empty for this location.");
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Error handling locations for status: " + status + ". " + e.getMessage());
	    }
	}

	// Fallback method to fetch data without status filter (for legacy data)
	private void handleLocationsWithoutStatusFilter(Long intentAppId, Map<String, Object> response) {
	    try {
	        System.out.println("Fallback: Fetching locations without status filter for intentAppId: " + intentAppId);
	        
	        // Fetch App Locations without status filter
	        List<AppLocation> appLocationList = appLocationServ.findIntentWithoutStatusAppById(intentAppId);
	        if (appLocationList == null || appLocationList.isEmpty()) {
	            System.out.println("Fallback: No App Locations found without status filter.");
	            return;
	        }

	        System.out.println("Fallback: AppLocation size: " + appLocationList.size());

	        // Fetch Government Organization Application without status filter
	        GovtOrganizationApplication appGovtOrganizationApplication = governmentAgencyServ.findWithoutIntentAppById(intentAppId);
	        response.put("appGovtOrganizationApplication", appGovtOrganizationApplication);
	        response.put("appLocation", appLocationList);

	        // Process addresses (same logic as handleLocationsForStatus)
	        IndivAddressDTO indivAddressDTO = new IndivAddressDTO();
	        for (AppLocation location : appLocationList) {
	            System.out.println("Fallback: AppLocation object: " + location);

	            String addressIdStr = location.getAddressId();
	            if (addressIdStr != null && !addressIdStr.isEmpty()) {
	                try {
	                    String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	                    System.out.println("Fallback: Decrypted Address ID: " + decryptedAddressId);

	                    AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);
	                    System.out.println("Fallback: addressDTO ===> " + addressDTO);

	                    if ("OrganizationAddress".equals(location.getLocationName())) {
	                        indivAddressDTO.setResidential(addressDTO);
	                        System.out.println("Fallback: Address Set: " + addressDTO);
	                    }
	                    response.put("indivAddressDTO", indivAddressDTO);

	                } catch (Exception e) {
	                    System.out.println("Fallback: Error processing Address ID: " + addressIdStr);
	                }
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Fallback: Error handling locations without status filter. " + e.getMessage());
	    }
	}

	
	@PostMapping(GovernmentAgencyApplicationFormAPIs.UPDATE_GOVERNMENT_AGENCY_APPLICATION_FORM)
	public ResponseEntity<?> updateGovernmentAgency(@RequestBody OrganizationDetailsDTO organizationalDTO) {
		
		 // Organization Name validation
	    if (organizationalDTO.getOrganizationName() == null || organizationalDTO.getOrganizationName().trim().isEmpty()) {
	        return new ResponseEntity<>("Organization Name is required.", HttpStatus.BAD_REQUEST);
	    }

	    // Department validation
	    if (organizationalDTO.getDepartment() == null || organizationalDTO.getDepartment().trim().isEmpty()) {
	        return new ResponseEntity<>("Department is required.", HttpStatus.BAD_REQUEST);
	    }

	    // OrgType validation
	    if (organizationalDTO.getOrgType() == null || organizationalDTO.getOrgType().trim().isEmpty()) {
	        return new ResponseEntity<>("Organization Type is required.", HttpStatus.BAD_REQUEST);
	    }

	    // Block No validation
	    if (organizationalDTO.getBlockNo() != null && organizationalDTO.getBlockNo().length() > 30) {
	        return new ResponseEntity<>("The length of Block No must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Village validation
	    if (organizationalDTO.getVillage() != null && organizationalDTO.getVillage().length() > 30) {
	        return new ResponseEntity<>("The length of Village must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Post Office validation
	    if (organizationalDTO.getPostOffice() != null && organizationalDTO.getPostOffice().length() > 30) {
	        return new ResponseEntity<>("The length of Post Office must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Subdivision validation
	    if (organizationalDTO.getSubDivision() != null && organizationalDTO.getSubDivision().length() > 30) {
	        return new ResponseEntity<>("The length of Subdivision must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Country validation
	    if (organizationalDTO.getCountry() == null || organizationalDTO.getCountry().trim().isEmpty()) {
	        return new ResponseEntity<>("Country is required.", HttpStatus.BAD_REQUEST);
	    }

	    // State validation
	    if (organizationalDTO.getState() == null || organizationalDTO.getState().trim().isEmpty()) {
	        return new ResponseEntity<>("State is required.", HttpStatus.BAD_REQUEST);
	    }

	    // City validation
	    if (organizationalDTO.getCity() == null || organizationalDTO.getCity().trim().isEmpty()) {
	        return new ResponseEntity<>("City is required.", HttpStatus.BAD_REQUEST);
	    }

	    // PIN validation
	    if (organizationalDTO.getPin() == null || organizationalDTO.getPin().trim().isEmpty()) {
	        return new ResponseEntity<>("PIN is required.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getPin().matches("\\d{6}")) {
	        return new ResponseEntity<>("Please enter a valid 6-digit PIN code.", HttpStatus.BAD_REQUEST);
	    }

	    // Fax validation (optional)
	    if (organizationalDTO.getFax() != null && organizationalDTO.getFax().length() > 15) {
	        return new ResponseEntity<>("The length of Fax must be between 0 and 15 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Telephone No validation
	    if (organizationalDTO.getTelephoneNo() != null && !organizationalDTO.getTelephoneNo().matches("\\d{10,15}")) {
	        return new ResponseEntity<>("Please enter a valid telephone number.", HttpStatus.BAD_REQUEST);
	    }

	    // Web URL validation (optional)
	    if (organizationalDTO.getWebURL() != null && !organizationalDTO.getWebURL().matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
	        return new ResponseEntity<>("Please enter a valid Web URL.", HttpStatus.BAD_REQUEST);
	    }

	    // Salutation validation
	    if (organizationalDTO.getSalutation() == null || organizationalDTO.getSalutation().trim().isEmpty()) {
	        return new ResponseEntity<>("Salutation is required.", HttpStatus.BAD_REQUEST);
	    }

	    // First Name validation
	    if (organizationalDTO.getFirstName() == null || organizationalDTO.getFirstName().trim().isEmpty()) {
	        return new ResponseEntity<>("First Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (organizationalDTO.getFirstName().length() < 1 || organizationalDTO.getFirstName().length() > 30) {
	        return new ResponseEntity<>("The length of First Name must be between 1 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getFirstName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid First Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Middle Name validation (optional)
	    if(organizationalDTO.getMiddleName() != null || organizationalDTO.getMiddleName().isEmpty() || organizationalDTO.getMiddleName().equals(""))
	    {
	
	    }else if (organizationalDTO.getMiddleName() != null && organizationalDTO.getMiddleName().length() > 30) {
	        return new ResponseEntity<>("The length of Middle Name must be between 0 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }else if (organizationalDTO.getMiddleName() != null && !organizationalDTO.getMiddleName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Middle Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Last Name validation
	    if (organizationalDTO.getLastName() == null || organizationalDTO.getLastName().trim().isEmpty()) {
	        return new ResponseEntity<>("Last Name cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (organizationalDTO.getLastName().length() < 1 || organizationalDTO.getLastName().length() > 30) {
	        return new ResponseEntity<>("The length of Last Name must be between 1 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getLastName().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Please enter a valid Last Name.", HttpStatus.BAD_REQUEST);
	    }

	    // Email validation
	    if (organizationalDTO.getEmailId() == null || organizationalDTO.getEmailId().trim().isEmpty()) {
	        return new ResponseEntity<>("Email Address cannot be empty.", HttpStatus.BAD_REQUEST);
	    }
	    if (!organizationalDTO.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	        return new ResponseEntity<>("Please enter a valid Email Address.", HttpStatus.BAD_REQUEST);
	    }

	    // Designation validation
	    if (organizationalDTO.getDesignation() == null || organizationalDTO.getDesignation().trim().isEmpty()) {
	        return new ResponseEntity<>("Designation is required.", HttpStatus.BAD_REQUEST);
	    }

	    // Public Key validation (optional)
	    if (organizationalDTO.getPublickey() != null && organizationalDTO.getPublickey().length() > 255) {
	        return new ResponseEntity<>("The length of Public Key must not exceed 255 characters.", HttpStatus.BAD_REQUEST);
	    }

	    // Username validation
	    if (organizationalDTO.getUserName() == null || organizationalDTO.getUserName().trim().isEmpty()) {
	        return new ResponseEntity<>("Username cannot be empty.", HttpStatus.BAD_REQUEST);
	    }

		try {
			
			 IntentApplication intentApp = intentAppServ.findIntentAppById(organizationalDTO.getUserName());
			 Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
			 
			 GovtOrganizationApplication appGovtOrganizationApplication = governmentAgencyServ.findWithoutIntentAppById(intentAppId);
			 
			 System.out.println("abcd====>"+appGovtOrganizationApplication.getStatus());
			 
			 Optional<GovtOrganizationApplication>result;
			  if ("Inactive".equals(appGovtOrganizationApplication.getStatus())) {
				  
				  AddressDTO addressDTO = new AddressDTO(); 

					// Setting address details into the AddressDTO
					addressDTO.setBlockNo(organizationalDTO.getBlockNo());
					addressDTO.setVillage(organizationalDTO.getVillage());
					addressDTO.setPostOffice(organizationalDTO.getPostOffice());
					addressDTO.setSubDivision(organizationalDTO.getSubDivision());
					addressDTO.setCountry(organizationalDTO.getCountry());
					addressDTO.setCity(organizationalDTO.getCity());
					addressDTO.setState(organizationalDTO.getState());
					addressDTO.setPin(organizationalDTO.getPin());

					// Pass the addressDTO to the addUsers() method
					Long resAddId = intentAppServ.addUsers(addressDTO);
					
					  AppLocation resLocation = new AppLocation();
				        resLocation.setAddressId(EncryptionUtil.encrypt( String.valueOf(resAddId))); 
				        resLocation.setLocationName("OrganizationAddress");
				        resLocation.setIntentAppId(intentApp); 
				        resLocation.setStatus("Active");
				    
				        Optional<AppLocation> resLoc=appLocationServ.addAppLocation(resLocation);
					   
					GovtOrganizationApplication govtOrganizationApplication = new GovtOrganizationApplication();

					// Setting organization details
					govtOrganizationApplication.setOrgName(organizationalDTO.getOrganizationName());
					govtOrganizationApplication.setAdministrativeMinistry(organizationalDTO.getDepartment());
					govtOrganizationApplication.setOrgType(organizationalDTO.getOrgType());

					// Setting communication details
					govtOrganizationApplication.setFax(organizationalDTO.getFax());
					govtOrganizationApplication.setTelephoneNo(organizationalDTO.getTelephoneNo());
					govtOrganizationApplication.setWebPageURL(organizationalDTO.getWebURL());
					govtOrganizationApplication.setIntentAppId(intentApp);
					// Setting personal details
					govtOrganizationApplication.setSalutation(organizationalDTO.getSalutation());
					govtOrganizationApplication.setFirstName(organizationalDTO.getFirstName());
					govtOrganizationApplication.setMiddleName(organizationalDTO.getMiddleName());
					govtOrganizationApplication.setLastName(organizationalDTO.getLastName());
					govtOrganizationApplication.setEmailId(organizationalDTO.getEmailId());
					govtOrganizationApplication.setDesignation(organizationalDTO.getDesignation());

					// Setting public key and other details
					govtOrganizationApplication.setPublicKey(organizationalDTO.getPublickey());
					govtOrganizationApplication.setStatus("Active");
					
					

					result =governmentAgencyServ.addData(govtOrganizationApplication);
				  
				  
			  }else {
				  Date date=new Date();
					AddressDTO addressDTO = new AddressDTO(); // Assuming you have an AddressDTO class for this purpose

					// Setting address details into the AddressDTO
					addressDTO.setAddressId(organizationalDTO.getAddressId());
					addressDTO.setBlockNo(organizationalDTO.getBlockNo());
					addressDTO.setVillage(organizationalDTO.getVillage());
					addressDTO.setPostOffice(organizationalDTO.getPostOffice());
					addressDTO.setSubDivision(organizationalDTO.getSubDivision());
					addressDTO.setCountry(organizationalDTO.getCountry());
					addressDTO.setCity(organizationalDTO.getCity());
					addressDTO.setState(organizationalDTO.getState());
					addressDTO.setPin(organizationalDTO.getPin());
					addressDTO.setCreated(String.valueOf(date));
					addressDTO.setUpdated(String.valueOf(date));
					addressDTO.setStatus("Active");
					
					// Pass the addressDTO to the addUsers() method
					Long resAddId = intentAppServ.updateUsers(addressDTO);	   
					GovtOrganizationApplication govtOrganizationApplication = new GovtOrganizationApplication();

					// Setting organization details
					govtOrganizationApplication.setOrgApplicationId(Long.valueOf(organizationalDTO.getOrgApplicationId()));
					govtOrganizationApplication.setOrgName(organizationalDTO.getOrganizationName());
					govtOrganizationApplication.setAdministrativeMinistry(organizationalDTO.getDepartment());
					govtOrganizationApplication.setOrgType(organizationalDTO.getOrgType());
					
					IntentApplication intent=new IntentApplication();
					intent.setIntentAppId(Long.valueOf(organizationalDTO.getIntentId()));
					govtOrganizationApplication.setIntentAppId(intent);
					// Setting communication details
					govtOrganizationApplication.setFax(organizationalDTO.getFax());
					govtOrganizationApplication.setTelephoneNo(organizationalDTO.getTelephoneNo());
					govtOrganizationApplication.setWebPageURL(organizationalDTO.getWebURL());

					// Setting personal details
					govtOrganizationApplication.setSalutation(organizationalDTO.getSalutation());
					govtOrganizationApplication.setFirstName(organizationalDTO.getFirstName());
					govtOrganizationApplication.setMiddleName(organizationalDTO.getMiddleName());
					govtOrganizationApplication.setLastName(organizationalDTO.getLastName());
					govtOrganizationApplication.setEmailId(organizationalDTO.getEmailId());
					govtOrganizationApplication.setDesignation(organizationalDTO.getDesignation());

					// Setting public key and other details
					govtOrganizationApplication.setPublicKey(organizationalDTO.getPublickey());
					govtOrganizationApplication.setStatus("Active");
					
					govtOrganizationApplication.setCreated(date);
					govtOrganizationApplication.setUpdated(date);
					

					result =governmentAgencyServ.UpdateData(govtOrganizationApplication);
				  
			  }
			
			
		
			if (result.isEmpty()) {
				return new ResponseEntity<>("Error occurred while saving First Step.",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the First Step.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(GovernmentAgencyApplicationFormAPIs.APPLICATION_FORM_GENERATE_FOR_ORGANIZATION)
	public ResponseEntity<?> generatePdfForCompanyApplication(@RequestParam("intentId") String intentId) {
	    try {
	        // Step 1: Fetch Intent Application Details
	        Long intentAppId = Long.valueOf(intentId);
	        IntentApplication intentApplication = intentAppServ.getIntentByIntentAppId(intentAppId);

	        if (intentApplication == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Intent Application not found for ID: " + intentId);
	        }

	        // Step 2: Fetch Application Documents
	        List<ApplicationDocument> applicationDocument = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentAppId);
	        if (applicationDocument == null || applicationDocument.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No documents found for Intent Application ID: " + intentId);
	        }

	        // Sort documents by creation date (most recent first)
	        List<ApplicationDocument> sortedDocuments = applicationDocument.stream()
	            .sorted(Comparator.comparing(ApplicationDocument::getCreated).reversed())
	            .collect(Collectors.toList());
	        List<String> fileNames = sortedDocuments.stream()
	            .map(ApplicationDocument::getFileName)
	            .collect(Collectors.toList());

	        // Step 3: Generate HTML Content
	        String htmlContent = buildHtmlContent(intentApplication, sortedDocuments, intentAppId);

	        // Step 4: Generate and Merge PDF
	        byte[] pdfBytes = generatePdfFromHtml(htmlContent);
	        ByteArrayOutputStream finalOutputStream = mergeWithExistingPdf(pdfBytes, fileNames);

	        // Step 5: Create HTTP Response
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=organization-application-form.pdf");

	        return ResponseEntity.ok()
	            .headers(headers)
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(finalOutputStream.toByteArray());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("An error occurred while generating the PDF: " + e.getMessage());
	    }
	}

	private String buildHtmlContent(IntentApplication intentApplication, List<ApplicationDocument> documents, Long intentAppId) {
	    StringBuilder contentBuilder = new StringBuilder();

	    contentBuilder.append("<html>")
	        .append("<head>")
	        .append("<style>")
	        .append("@page { margin: 60pt; }")
	        .append("body { font-size: 16px; font-family: 'Times New Roman', Times, serif; }")
	        .append("p { text-align: justify; text-justify: inter-word; }")
	        .append("div { text-align: justify; text-justify: inter-word; }")
	        .append("table { border-collapse: collapse; }")
	        .append("td { padding-left: 5px; }")
	        .append("</style>")
	        .append("</head>")
	        .append("<body>");
	        

	    String prefix = "CCA/CA";
	    String formattedDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
	    String updatedDate = new SimpleDateFormat("yyyy-MM-dd").format(intentApplication.getUpdated());

	    contentBuilder.append("<span style='font-weight: bold;'>Date:</span> ")
	        .append(updatedDate).append("<br>")
	        .append("<span style='font-weight: bold;'>Intent Code:</span> ")
	        .append(intentApplication.getUniqueCode()).append("<br>")
	        .append("<span style='font-weight: bold;'>Application Number:</span> ")
	        .append(prefix).append("/").append(formattedDate).append("/").append(intentApplication.getIntentAppId())
	        .append("<br>");

	    // Add organization details
	    contentBuilder.append("<h4 style='text-decoration: underline;'>For Government Ministry/Department/Agency/Authority</h4>");
	    GovtOrganizationApplication appGovtOrganizationApplication = governmentAgencyServ.findIntentAppById(intentAppId,"Active");
	    
	    List<BankDetails> bankDetailsList = bankDetailsSev.findIntentAppById(intentAppId);
	    BankDetails bankDetails = (bankDetailsList != null && !bankDetailsList.isEmpty()) ? bankDetailsList.get(0) : null;
        BankDraft bankDraft = bankDraftServ.findByindivApplicationId(intentAppId);
        
	    List<AppLocation> appLocationList = appLocationServ.findIntentAppById(intentAppId, "Active");
	    if (appLocationList == null || appLocationList.isEmpty()) {
	        contentBuilder.append("<p>No App Locations found for this Intent Application.</p>")
	        ;
	    } else {
	        contentBuilder.append("<span>28. Particulars of Organisation: *</span>")
	        .append("<br>");
	        
	        contentBuilder.append("<span style='font-weight: bold;'>Organization Name:</span> ")
            .append(appGovtOrganizationApplication.getOrgName() != null 
                    ? appGovtOrganizationApplication.getOrgName() 
                    : "N/A")
            .append("<br>");

contentBuilder.append("<span style='font-weight: bold;'>Administrative Ministry/Department:</span> ")
            .append(appGovtOrganizationApplication.getAdministrativeMinistry() != null 
                    ? appGovtOrganizationApplication.getAdministrativeMinistry() 
                    : "N/A")
            .append("<br>");

contentBuilder.append("<span style='font-weight: bold;'>Under State/Central Government:</span> ")
            .append(appGovtOrganizationApplication.getOrgType() != null 
                    ? appGovtOrganizationApplication.getOrgType() 
                    : "N/A")
            .append("<br><br>");

	        
for (AppLocation location : appLocationList) {
    // Check if the location name is "OrganizationAddress"
    if ("OrganizationAddress".equals(location.getLocationName())) {
        String addressDetails = getAddressDetails(location.getAddressId());
        contentBuilder.append(addressDetails);
        contentBuilder.append("<span style='font-weight: bold;'>Web page URL Address:</span> ")
        .append(appGovtOrganizationApplication.getWebPageURL() != null 
                ? appGovtOrganizationApplication.getWebPageURL() 
                : "N/A")
        .append("<br>");
        contentBuilder.append("<span style='font-weight: bold;'>Name of the Head of Organisation:</span> ")
        .append(appGovtOrganizationApplication.getFirstName() != null && appGovtOrganizationApplication.getMiddleName() != null && appGovtOrganizationApplication.getLastName() != null 
                ? appGovtOrganizationApplication.getFirstName() + " " + appGovtOrganizationApplication.getMiddleName()+ " " + appGovtOrganizationApplication.getLastName()
                : "N/A")
        .append("<br>");        contentBuilder.append("<span style='font-weight: bold;'>Designation:</span> ")
        .append(appGovtOrganizationApplication.getDesignation() != null 
                ? appGovtOrganizationApplication.getDesignation() 
                : "N/A")
        .append("<br>");
        contentBuilder.append("<span style='font-weight: bold;'>E-mail Address:</span> ")
        .append(appGovtOrganizationApplication.getEmailId() != null 
                ? appGovtOrganizationApplication.getEmailId() 
                : "N/A")
        .append("<br><br>");
    }
    
}

	    
	    
	    
	    for (AppLocation location : appLocationList) {
	  
	        if (!"OrganizationAddress".equals(location.getLocationName())) {
	            String addressDetails = getAddressDetails(location.getAddressId());
	            
	            contentBuilder.append("<span>31. Location of facility in India for generation of Digital Signature Certificate *: ")
	            .append(location.getLocationName() != null ? location.getLocationName() : "N/A")
	            .append("</span>");
	            contentBuilder.append(addressDetails);
	        }
	    }

	    }	    
	    

	    contentBuilder.append("<span style='font-weight: bold;'>29. Bank Details</span><br>")
	        .append("<span style='font-weight: bold;'>Bank Name*:</span> ")
	        .append(bankDetails.getBankName() != null ? bankDetails.getBankName() : "N/A")
	        .append("<br>")
	        .append("<span style='font-weight: bold;'>Branch*:</span> ")
	        .append(bankDetails.getBranchName() != null ? bankDetails.getBranchName() : "N/A")
	        .append("<br>")
	        .append("<span style='font-weight: bold;'>Bank Account No.*:</span> ")
	        .append(bankDetails.getBankAccountNo() != null ? bankDetails.getBankAccountNo() : "N/A")
	        .append("<br>")
	        .append("<span style='font-weight: bold;'>Type of Bank Account*:</span> ")
	        .append(bankDetails.getBankAccountType() != null ? bankDetails.getBankAccountType() : "N/A")
	        .append("<br><br>");


	    contentBuilder.append("<span style='font-weight: bold;'>30. Whether bank draft/pay order for licence fee enclosed:</span> ")
	        .append(bankDraft==null 
	                ? "N"
	                : "Y")
	        .append("<br>");

	    if (bankDraft!=null) {
	        contentBuilder.append("<span style='font-weight: bold;'>Name of Bank:</span> ")
	            .append(bankDraft.getBankName() != null ? bankDraft.getBankName() : "N/A")
	            .append("<br>")
	            .append("<span style='font-weight: bold;'>Draft/pay order No.:</span> ")
	            .append(bankDraft.getDraftNo() != null ? bankDraft.getDraftNo() : "N/A")
	            .append("<br>")
	            .append("<span style='font-weight: bold;'>Date of Issue:</span> ")
	            .append(bankDraft.getIssueDate() != null 
	                    ? new SimpleDateFormat("yyyy-MM-dd").format(bankDraft.getIssueDate()) 
	                    : "N/A")
	            .append("<br>")
	            .append("<span style='font-weight: bold;'>Amount:</span> ")
	            .append(bankDraft.getAmount() != null ? bankDraft.getAmount() : "N/A")
	            .append("<br>");
	    }
	    contentBuilder.append("<span style='font-weight: bold;'>32. Public Key @:</span> ")
        .append(appGovtOrganizationApplication.getPublicKey() != null 
                ? appGovtOrganizationApplication.getPublicKey() 
                : "N/A")
        .append("<br><br>");
	    
	    contentBuilder.append("</body></html>");
	    return contentBuilder.toString();
	}
	

	private String getAddressDetails(String addressIdStr) {
	    StringBuilder addressBuilder = new StringBuilder();
	    if (addressIdStr == null || addressIdStr.isEmpty()) {
	        addressBuilder.append("<p>Address ID is null or empty.</p>");
	        return addressBuilder.toString();
	    }

	    try {
	    	String processedAddressId;

	    	if (isBase64Encoded(addressIdStr)) {
	    	  
	    	    processedAddressId = EncryptionUtil.decrypt(addressIdStr);
	    	    System.out.println("Decrypted Address ID: " + processedAddressId);
	    	} else {
	    	    // Use the original addressIdStr if not encrypted
	    	    processedAddressId = addressIdStr;
	    	    System.out.println("Address ID is not encrypted: " + processedAddressId);
	    	}


	        AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(processedAddressId);
	        if (addressDTO != null) {
	            addressBuilder
	                .append("<span style='font-weight: bold;'>Flat/Door/Block No.:</span> ")
	                .append(addressDTO.getBlockNo() != null ? addressDTO.getBlockNo() : "N/A").append("<br>")
	                .append("<span style='font-weight: bold;'>Premises/Building/Village:</span> ")
	                .append(addressDTO.getVillage() != null ? addressDTO.getVillage() : "N/A").append("<br>")
	                .append("<span style='font-weight: bold;'>City:</span> ")
	                .append(addressDTO.getCity() != null ? addressDTO.getCity() : "N/A").append("<br>")
	                .append("<span style='font-weight: bold;'>Pin:</span> ")
	                .append(addressDTO.getState() != null ? addressDTO.getState() : "N/A").append("<br>")
	                .append("<span style='font-weight: bold;'>Pin:</span> ")
	                .append(addressDTO.getPin() != null ? addressDTO.getPin() : "N/A").append("<br>")
	            .append("<span style='font-weight: bold;'>Pin:</span> ")
                .append(addressDTO.getCountry() != null ? addressDTO.getCountry() : "N/A").append("<br>");
	            
	        } else {
	            addressBuilder.append("<p>No Address Details Found for Address ID: ").append(processedAddressId).append("</p>");
	        }
	    } catch (Exception e) {
	        addressBuilder.append("<p>Error processing Address ID: ").append(addressIdStr).append(" - ").append(e.getMessage()).append("</p>");
	    }
	    return addressBuilder.toString();
	}

	private boolean isBase64Encoded(String str) {
	    String base64Pattern = "^[a-zA-Z0-9+/=]+$"; 
	    Pattern pattern = Pattern.compile(base64Pattern);
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches() && str.length() % 4 == 0; 
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

			private ByteArrayOutputStream mergeWithExistingPdf(byte[] generatedPdfBytes, List<String> fileNames) {
			    try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {
			        PdfDocument finalPdfDocument = new PdfDocument(new PdfWriter(finalOutputStream));
			        PdfDocument generatedPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(generatedPdfBytes)));
			        PdfMerger pdfMerger = new PdfMerger(finalPdfDocument);
			        pdfMerger.merge(generatedPdf, 1, generatedPdf.getNumberOfPages());

			        String basePath = Constant.REAL_PATH + "//CPSDocument//Document";
			        for (String fileName : fileNames) {
			            Path filePath = Paths.get(basePath).resolve(fileName).normalize();
			            try (PdfDocument pdfInput = new PdfDocument(new PdfReader(filePath.toString()))) {
			                pdfMerger.merge(pdfInput, 1, pdfInput.getNumberOfPages());
			            }
			        }

			        finalPdfDocument.close();
			        return finalOutputStream;
			    } catch (IOException e) {
			        e.printStackTrace();
			        throw new RuntimeException("Error merging PDFs");
			    }
		  
			}


	
	
}
