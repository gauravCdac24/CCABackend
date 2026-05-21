package in.lms.cca.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.dto.OrganizationDetailsDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.GovtOrganizationApplication;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.GovernmentAgencyService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.util.api.GovernmentAgencyApplicationFormAPIs;
import in.lms.cca.util.api.RenewLicenseServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(RenewLicenseServiceAPIs.RENEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin(CrossOrigins.Origins)
public class GovernmentAgencyFormController {
	
	
	@Autowired
	private GovernmentAgencyService governmentAgencyServ;
	
	@Autowired
	private IIntentApplicationService intentAppServ;
	
	@Autowired
	private AppLocationService appLocationServ;
	
	
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
			intentApp.setApplicationStatus("Pending");
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

	        // Check for "Edit_Upon_Review" status to handle "Inactive" locations
	        if ("Edit_Upon_Review".equals(intentApp.getApplicationStatus())) {
	            handleLocationsForStatus(intentAppId, "Inactive", response);  // Handle Inactive locations if applicable
	        }else {
	        	  // Always check for "Active" locations regardless of the status
		        handleLocationsForStatus(intentAppId, "Active", response);  // Handle Active locations
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
	    	
	    	System.out.println("status====>"+status);
	        List<AppLocation> appLocationList = appLocationServ.findIntentAppById(intentAppId, status);
	        if (appLocationList == null || appLocationList.isEmpty()) {
	            System.out.println("App Location not found for status: " + status);
	            return;
	        }

	        System.out.println("AppLocation size: " + appLocationList.size());

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
	                    if ("OrganizationAddress".equals(location.getLocationName()) && decryptedAddressId.equals(addressDTO.getAddressId())) {
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
	

}
