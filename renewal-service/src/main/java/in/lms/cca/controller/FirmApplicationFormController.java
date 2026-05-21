package in.lms.cca.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.FileDTO;
import in.lms.cca.dto.FileDocumentDTO;
import in.lms.cca.dto.FirmApplicationDTO;
import in.lms.cca.dto.FirmAuthorizedRepresentativeDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.dto.OrganizationDetailsDTO;
import in.lms.cca.dto.PartnerDetailsDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.FirmApplication;
import in.lms.cca.entity.FirmApplication2;
import in.lms.cca.entity.FirmAuthorizedRepresentative;
import in.lms.cca.entity.FirmPartnerDetails;
import in.lms.cca.entity.GovtOrganizationApplication;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.FirmApplication2service;
import in.lms.cca.service.FirmApplicationService;
import in.lms.cca.service.FirmAuthorizedRepresentiveService;
import in.lms.cca.service.FirmPartnerService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.util.api.FirmApplicationFormAPIs;
import in.lms.cca.util.api.GovernmentAgencyApplicationFormAPIs;
import in.lms.cca.util.api.RenewLicenseServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;


@SuppressWarnings("unused")
@RestController
@RequestMapping(RenewLicenseServiceAPIs.RENEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin(CrossOrigins.Origins)
public class FirmApplicationFormController {
	
	
	@Autowired
	private FirmApplicationService firmApplicationServ;
	
	@Autowired
	private IIntentApplicationService intentAppServ;
	
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
	
	@PostMapping(FirmApplicationFormAPIs.ADD_FIRM_APPLICATION_FORM)
	public ResponseEntity<?> addGovernmentAgency(@RequestBody FirmApplicationDTO firmApplicationDTO) {

        // Block No validation
        if (firmApplicationDTO.getBlockNo() != null && firmApplicationDTO.getBlockNo().length() > 75) {
            return new ResponseEntity<>("The length of Block No must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
        }

        // Village validation
        if (firmApplicationDTO.getVillage() != null && firmApplicationDTO.getVillage().length() > 75) {
            return new ResponseEntity<>("The length of Village must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
        }

        // Post Office validation
        if (firmApplicationDTO.getPostOffice() != null && firmApplicationDTO.getPostOffice().length() > 75) {
            return new ResponseEntity<>("The length of Post Office must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
        }

        // Subdivision validation
        if (firmApplicationDTO.getSubDivision() != null && firmApplicationDTO.getSubDivision().length() > 75) {
            return new ResponseEntity<>("The length of Subdivision must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
        }

        // Country validation
        if (firmApplicationDTO.getCountry() == null || firmApplicationDTO.getCountry().trim().isEmpty()) {
            return new ResponseEntity<>("Country is required.", HttpStatus.BAD_REQUEST);
        }

        // State validation
        if (firmApplicationDTO.getState() == null || firmApplicationDTO.getState().trim().isEmpty()) {
            return new ResponseEntity<>("State is required.", HttpStatus.BAD_REQUEST);
        }

        // City validation
        if (firmApplicationDTO.getCity() == null || firmApplicationDTO.getCity().trim().isEmpty()) {
            return new ResponseEntity<>("City is required.", HttpStatus.BAD_REQUEST);
        }

        // PIN validation
        if (firmApplicationDTO.getPin() == null || firmApplicationDTO.getPin().trim().isEmpty()) {
            return new ResponseEntity<>("PIN is required.", HttpStatus.BAD_REQUEST);
        }
        if (!firmApplicationDTO.getPin().matches("\\d{6}")) {
            return new ResponseEntity<>("Please enter a valid 6-digit PIN code.", HttpStatus.BAD_REQUEST);
        }

        // Fax validation (optional)
        if (firmApplicationDTO.getFax() != null && firmApplicationDTO.getFax().length() > 15) {
            return new ResponseEntity<>("The length of Fax must be between 0 and 15 characters.", HttpStatus.BAD_REQUEST);
        }

        // Telephone No validation
        if (firmApplicationDTO.getTelephoneNo() != null && !firmApplicationDTO.getTelephoneNo().matches("\\d{10,15}")) {
            return new ResponseEntity<>("Please enter a valid telephone number.", HttpStatus.BAD_REQUEST);
        }

//        // Web URL validation (optional)
//        if (firmApplicationDTO.getWebPageURL() != null && !firmApplicationDTO.getWebPageURL().matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
//            return new ResponseEntity<>("Please enter a valid Web URL.", HttpStatus.BAD_REQUEST);
//        }

        // Public Key validation (optional)
        if (firmApplicationDTO.getPublicKey() != null && firmApplicationDTO.getPublicKey().length() > 255) {
            return new ResponseEntity<>("The length of Public Key must not exceed 255 characters.", HttpStatus.BAD_REQUEST);
        }

        // Username validation
        if (firmApplicationDTO.getUserName() == null || firmApplicationDTO.getUserName().trim().isEmpty()) {
            return new ResponseEntity<>("Username cannot be empty.", HttpStatus.BAD_REQUEST);
        }

		
		
		try {
			IntentApplication intentApp = new IntentApplication();
			intentApp.setAppTypeMasterId(firmApplicationDTO.getAppTypeMasterId());
			intentApp.setUserName(firmApplicationDTO.getUserName());
			intentApp.setApplicationStatus("Pending");
			intentAppServ.addIntentApp(intentApp);
			
			AddressDTO addressDTO = new AddressDTO(); 

			// Setting address details into the AddressDTO
			addressDTO.setBlockNo(firmApplicationDTO.getBlockNo());
			addressDTO.setVillage(firmApplicationDTO.getVillage());
			addressDTO.setPostOffice(firmApplicationDTO.getPostOffice());
			addressDTO.setSubDivision(firmApplicationDTO.getSubDivision());
			addressDTO.setCountry(firmApplicationDTO.getCountry());
			addressDTO.setCity(firmApplicationDTO.getCity());
			addressDTO.setState(firmApplicationDTO.getState());
			addressDTO.setPin(firmApplicationDTO.getPin());

			// Pass the addressDTO to the addUsers() method
			Long resAddId = intentAppServ.addUsers(addressDTO);
			
			    AppLocation resLocation = new AppLocation();
		        resLocation.setAddressId(EncryptionUtil.encrypt( String.valueOf(resAddId))); 
		        resLocation.setLocationName("FirmAddress");
		        resLocation.setIntentAppId(intentApp); 
		        resLocation.setStatus("Active");
		    
		        Optional<AppLocation> resLoc=appLocationServ.addAppLocation(resLocation);
			   
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		        FirmApplication firmApplication = new FirmApplication();

		        firmApplication.setRegistrationNo(firmApplicationDTO.getRegistrationNo());

		        try {
		            // Check if the incorporation date is not null and not empty before parsing
		            if (firmApplicationDTO.getIncorporationDate() != null && !firmApplicationDTO.getIncorporationDate().trim().isEmpty()) {
		                Date incorporationDate = new Date(dateFormat.parse(firmApplicationDTO.getIncorporationDate()).getTime());
		                firmApplication.setIncorporationDate(incorporationDate);
		            } else {
		                firmApplication.setIncorporationDate(null); // Set to null if the date is empty or null
		            }
		        } catch (ParseException e) {
		            e.printStackTrace();
		            // Optionally log or handle the error
		        }
		     firmApplication.setOfficeName(firmApplicationDTO.getOfficeName());
		     firmApplication.setAddressId(firmApplicationDTO.getAddressId());
		     firmApplication.setTelephoneNo(firmApplicationDTO.getTelephoneNo());
		     firmApplication.setFax(firmApplicationDTO.getFax());
		     firmApplication.setWebPageURL(firmApplicationDTO.getWebPageURL());
		     firmApplication.setNoOfBranches( Integer.valueOf(firmApplicationDTO.getNoOfBranch()));
		     firmApplication.setNatureOfBusiness(firmApplicationDTO.getNaturesOfBusiness());
		     firmApplication.setIntentAppId(intentApp);
		     firmApplication.setCreatedBy(firmApplicationDTO.getCreatedBy());
		     firmApplication.setUpdatedBy(firmApplicationDTO.getUpdatedBy());
			firmApplication.setStatus("Active");
			
			Optional<FirmApplication>result =firmApplicationServ.addData(firmApplication);
		
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
	
	
	@GetMapping(FirmApplicationFormAPIs.GET_FIRM_APPLICATION_FORMBY_USERNAME)
	public ResponseEntity<?> getFirmApplicationByUsername(@RequestParam("userName") String userName) {
	    try {
	        System.out.println("userName===>" + userName);

	        // Retrieve IntentApplication by userName
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        List<FirmApplication> firmApplications = firmApplicationServ.findIntentWithoutStatusAppById(intentAppId);
	        List<AppLocation> appLocations = appLocationServ.findIntentWithoutStatusAppById(intentAppId);

	        System.out.println("firmApplications===>" + firmApplications.get(0).getAddressId());
	        System.out.println("firmApplicationsSize()===>" + firmApplications.size());

	        // Get the most recent FirmApplication
	        Optional<FirmApplication> mostFirmApplication = firmApplications.stream()
	                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	                .findFirst();

	        // Get the most recent AppLocation with LocationName "FirmAddress"
	        Optional<AppLocation> mostAppLocation = appLocations.stream()
	                .filter(location -> "FirmAddress".equals(location.getLocationName()))
	                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	                .findFirst();

	        Map<String, Object> response = new HashMap<>();
	        IndivAddressDTO indivAddressDTO = new IndivAddressDTO();  // Initialize once and reuse

	        // Handle FirmApplication based on its status
	        mostFirmApplication.ifPresent(application -> {
	            response.put("appFirmApplication", application);
	        });
	        
	        mostAppLocation.ifPresent(location -> {
	            handleLocationStatus(location,  response);
	        });

	        response.put("appLocation", mostAppLocation.orElse(null));  // Add location if present

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// Helper method to handle location status
	private void handleLocationStatus(AppLocation location,  Map<String, Object> response) {
	    String addressIdStr = location.getAddressId();
	    if (addressIdStr != null && !addressIdStr.isEmpty()) {
	        try {
	            // Decrypt the Address ID
	            String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	            System.out.println(" Address ID: " + addressIdStr);
	            System.out.println(" decryptedAddressId Address ID: " + decryptedAddressId);
	            // Fetch the address details using the decrypted Address ID
	            AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);
	            System.out.println("addressDTO==>" + addressDTO);
	            IndivAddressDTO indivAddressDTO=new IndivAddressDTO();
	            // Set the appropriate address in indivAddressDTO
	            if ("FirmAddress".equals(location.getLocationName()) && decryptedAddressId.equals(addressDTO.getAddressId())) {
	                indivAddressDTO.setResidential(addressDTO);
	                System.out.println("country==>" + addressDTO.getCountry());
	                System.out.println("Address Set: " + addressDTO);
	            }

	            // Update the response map with indivAddressDTO
	            response.put("indivAddressDTO", indivAddressDTO);

	        } catch (Exception e) {
	            System.out.println("Error processing Address ID: " + addressIdStr);
	            e.printStackTrace();  // Log full error details
	        }
	    } else {
	        System.out.println("Address ID is null or empty for this location.");
	    }
	}

	
	@PostMapping(FirmApplicationFormAPIs.UPDATE_FIRM_APPLICATION_FORM)
	public ResponseEntity<?> updateGovernmentAgency(@RequestBody FirmApplicationDTO firmApplicationDTO) {

	    // Validate the FirmApplicationDTO fields
	    ResponseEntity<?> validationResponse = validateFirmApplicationDTO(firmApplicationDTO);
	    if (validationResponse != null) {
	        return validationResponse;
	    }

	    try {
	        IntentApplication intentApp = intentAppServ.findIntentAppById(firmApplicationDTO.getUserName());
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        
	    	
			intentApp.setApplicationStatus("PENDING-FOR-RENEWAL");
			intentAppServ.updateIntentApp(intentApp);

	        List<FirmApplication> firmApplications = firmApplicationServ.findIntentWithoutStatusAppById(intentAppId);
	        System.out.println("firmApplications===>" + firmApplications.get(0).getAddressId());
	        System.out.println("firmApplicationsSize()===>" + firmApplications.size());

	        List<AppLocation> appLocations = appLocationServ.findIntentWithoutStatusAppById(intentAppId);

	        // Get the most recent AppLocation with LocationName "FirmAddress"
	        Optional<AppLocation> mostAppLocation = appLocations.stream()
	                .filter(location -> "FirmAddress".equals(location.getLocationName()))
	                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	                .findFirst();

	        // Handle AppLocation
	        mostAppLocation.ifPresent(appLocation -> {
	            AddressDTO addressDTO = new AddressDTO();
	            if ("Inactive".equals(appLocation.getStatus())) {
	                // Set address details into AddressDTO
	                populateAddressDTOFromFirmApplicationDTO(firmApplicationDTO, addressDTO);

	                // Add new address and update AppLocation
	                Long resAddId = intentAppServ.addUsers(addressDTO);
	                addAppLocation(resAddId, intentApp, "Active");
	            } else {
	                // Update or add address
	                try {
	                    Long resAddId = updateOrAddAddress(firmApplicationDTO);
	                    // You can update the existing AppLocation with new address data if needed
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });

	        if (appLocations == null || appLocations.isEmpty()) {
	            return new ResponseEntity<>("App Location not found.", HttpStatus.NOT_FOUND);
	        }

	        // Get the most recent FirmApplication
	        Optional<FirmApplication> mostFirmApplication = firmApplications.stream()
	                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	                .findFirst();

	        // Save or update the firm application based on the status
	        if (mostFirmApplication.isPresent()) {
	            FirmApplication application = mostFirmApplication.get();
	            if ("Inactive".equals(application.getStatus())) {
	                // Add new FirmApplication
	                FirmApplication firmApplication1 = createFirmApplicationFromDTO(firmApplicationDTO, intentApp, "Active");
	                firmApplicationServ.addData(firmApplication1);
	            } else {
	                // Update existing FirmApplication
	                FirmApplication firmApplication1 = createFirmApplicationFromDTO(firmApplicationDTO, intentApp, "Active");
	                firmApplicationServ.updateData(firmApplication1);
	            }
	        } else {
	            // Handle case where there is no mostFirmApplication (add new FirmApplication)
	            FirmApplication firmApplication1 = createFirmApplicationFromDTO(firmApplicationDTO, intentApp, "Active");
	            firmApplicationServ.addData(firmApplication1);
	        }

	        return new ResponseEntity<>(HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while updating the Firm Application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// Helper methods
	private void populateAddressDTOFromFirmApplicationDTO(FirmApplicationDTO firmApplicationDTO, AddressDTO addressDTO) {
	    addressDTO.setBlockNo(firmApplicationDTO.getBlockNo());
	    addressDTO.setVillage(firmApplicationDTO.getVillage());
	    addressDTO.setPostOffice(firmApplicationDTO.getPostOffice());
	    addressDTO.setSubDivision(firmApplicationDTO.getSubDivision());
	    addressDTO.setCountry(firmApplicationDTO.getCountry());
	    addressDTO.setCity(firmApplicationDTO.getCity());
	    addressDTO.setState(firmApplicationDTO.getState());
	    addressDTO.setPin(firmApplicationDTO.getPin());
	}

	private FirmApplication createFirmApplicationFromDTO(FirmApplicationDTO firmApplicationDTO, IntentApplication intentApp, String status) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    FirmApplication firmApplication1 = new FirmApplication();

	    firmApplication1.setRegistrationNo(firmApplicationDTO.getRegistrationNo());
	    try {
	        // Check if the incorporation date is not null and not empty before parsing
	        if (firmApplicationDTO.getIncorporationDate() != null && !firmApplicationDTO.getIncorporationDate().trim().isEmpty()) {
	            Date incorporationDate = new Date(dateFormat.parse(firmApplicationDTO.getIncorporationDate()).getTime());
	            firmApplication1.setIncorporationDate(incorporationDate);
	        } else {
	            firmApplication1.setIncorporationDate(null); // Set to null if the date is empty or null
	        }
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    firmApplication1.setOfficeName(firmApplicationDTO.getOfficeName());
	    firmApplication1.setAddressId(firmApplicationDTO.getAddressId());
	    firmApplication1.setTelephoneNo(firmApplicationDTO.getTelephoneNo());
	    firmApplication1.setFax(firmApplicationDTO.getFax());
	    firmApplication1.setWebPageURL(firmApplicationDTO.getWebPageURL());
	    firmApplication1.setNoOfBranches(Integer.valueOf(firmApplicationDTO.getNoOfBranch()));
	    firmApplication1.setNatureOfBusiness(firmApplicationDTO.getNaturesOfBusiness());
	    firmApplication1.setIntentAppId(intentApp);
	    firmApplication1.setCreatedBy(firmApplicationDTO.getCreatedBy());
	    firmApplication1.setUpdatedBy(firmApplicationDTO.getUpdatedBy());
	    firmApplication1.setStatus(status);

	    return firmApplication1;
	}

	private void addAppLocation(Long addressId, IntentApplication intentApp, String status) {
	    AppLocation resLocation = new AppLocation();
	    resLocation.setAddressId(EncryptionUtil.encrypt(String.valueOf(addressId)));
	    resLocation.setLocationName("FirmAddress");
	    resLocation.setIntentAppId(intentApp);
	    resLocation.setStatus(status);
	    appLocationServ.addAppLocation(resLocation);
	}


	// Helper method to validate the input
	private ResponseEntity<?> validateFirmApplicationDTO(FirmApplicationDTO firmApplicationDTO) {
	    if (firmApplicationDTO.getBlockNo() != null && firmApplicationDTO.getBlockNo().length() > 75) {
	        return new ResponseEntity<>("The length of Block No must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getVillage() != null && firmApplicationDTO.getVillage().length() > 75) {
	        return new ResponseEntity<>("The length of Village must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getPostOffice() != null && firmApplicationDTO.getPostOffice().length() > 75) {
	        return new ResponseEntity<>("The length of Post Office must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getSubDivision() != null && firmApplicationDTO.getSubDivision().length() > 75) {
	        return new ResponseEntity<>("The length of Subdivision must be between 0 and 75 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getCountry() == null || firmApplicationDTO.getCountry().trim().isEmpty()) {
	        return new ResponseEntity<>("Country is required.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getState() == null || firmApplicationDTO.getState().trim().isEmpty()) {
	        return new ResponseEntity<>("State is required.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getCity() == null || firmApplicationDTO.getCity().trim().isEmpty()) {
	        return new ResponseEntity<>("City is required.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getPin() == null || !firmApplicationDTO.getPin().matches("\\d{6}")) {
	        return new ResponseEntity<>("Please enter a valid 6-digit PIN code.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getFax() != null && firmApplicationDTO.getFax().length() > 15) {
	        return new ResponseEntity<>("The length of Fax must be between 0 and 15 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getTelephoneNo() != null && !firmApplicationDTO.getTelephoneNo().matches("\\d{10,15}")) {
	        return new ResponseEntity<>("Please enter a valid telephone number.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getPublicKey() != null && firmApplicationDTO.getPublicKey().length() > 255) {
	        return new ResponseEntity<>("The length of Public Key must not exceed 255 characters.", HttpStatus.BAD_REQUEST);
	    }
	    if (firmApplicationDTO.getUserName() == null || firmApplicationDTO.getUserName().trim().isEmpty()) {
	        return new ResponseEntity<>("Username cannot be empty.", HttpStatus.BAD_REQUEST);
	    }

	    return null;  // Validation passed
	}

	// Helper method to handle AddressDTO update/add
	private Long updateOrAddAddress(FirmApplicationDTO firmApplicationDTO) throws Exception {
	    AddressDTO addressDTO = new AddressDTO();
	    addressDTO.setAddressId(firmApplicationDTO.getAddressId());
	    addressDTO.setBlockNo(firmApplicationDTO.getBlockNo());
	    addressDTO.setVillage(firmApplicationDTO.getVillage());
	    addressDTO.setPostOffice(firmApplicationDTO.getPostOffice());
	    addressDTO.setSubDivision(firmApplicationDTO.getSubDivision());
	    addressDTO.setCountry(firmApplicationDTO.getCountry());
	    addressDTO.setCity(firmApplicationDTO.getCity());
	    addressDTO.setState(firmApplicationDTO.getState());
	    addressDTO.setPin(firmApplicationDTO.getPin());
	    return intentAppServ.updateUsers(addressDTO);
	}

	// Helper method to populate FirmApplication entity
	private FirmApplication prepareFirmApplication(FirmApplicationDTO firmApplicationDTO, IntentApplication intentApp) throws ParseException {
	    FirmApplication firmApplication = new FirmApplication();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    firmApplication.setFirmApplicationId(Long.valueOf(firmApplicationDTO.getFirmApplicationId()));
	    firmApplication.setRegistrationNo(firmApplicationDTO.getRegistrationNo());

	    Date incorporationDate = firmApplicationDTO.getIncorporationDate() != null
	            ? new Date(dateFormat.parse(firmApplicationDTO.getIncorporationDate()).getTime())
	            : null;
	    firmApplication.setIncorporationDate(incorporationDate);

	    firmApplication.setOfficeName(firmApplicationDTO.getOfficeName());
	    firmApplication.setAddressId(firmApplicationDTO.getAddressId());
	    firmApplication.setTelephoneNo(firmApplicationDTO.getTelephoneNo());
	    firmApplication.setFax(firmApplicationDTO.getFax());
	    firmApplication.setWebPageURL(firmApplicationDTO.getWebPageURL());
	    firmApplication.setNoOfBranches(Integer.valueOf(firmApplicationDTO.getNoOfBranch()));
	    firmApplication.setNatureOfBusiness(firmApplicationDTO.getNaturesOfBusiness());
	    firmApplication.setIntentAppId(intentApp);
	    firmApplication.setCreatedBy(EncryptionUtil.encrypt(firmApplicationDTO.getCreatedBy()));
	    firmApplication.setUpdatedBy(EncryptionUtil.encrypt(firmApplicationDTO.getUpdatedBy()));
	    firmApplication.setStatus("Active");
	    Date date = new Date();
	    firmApplication.setCreated(date);
	    firmApplication.setUpdated(date);

	    return firmApplication;
	}

	
	 @PostMapping(FirmApplicationFormAPIs.ADD_FIRM_APPLICATION_FORM_STEP2)
	    public ResponseEntity<?> addGovernmentAgencyStep2(
	        @RequestParam(value = "turnOver", required = false) String turnOver,
	        @RequestParam(value = "incomTaxPanNo", required = false) String incomTaxPanNo,
	        @RequestParam(value = "paidUpCapital", required = false) String paidUpCapital,
	        @RequestParam(value = "netWorth", required = false) String netWorth,
	        @RequestParam(value = "insurerCompany", required = false) String insurerCompany,
	        @RequestParam(value = "policyNo", required = false) String policyNo,
	        @RequestParam(value = "file1", required = false) MultipartFile file1,
	        @RequestParam(value = "file2", required = false) MultipartFile file2,
	        @RequestParam(value = "file3", required = false) MultipartFile file3,
	        @RequestParam(value = "userName", required = false) String userName) {

	        try {
	        	
	        	System.out.println("userName==>"+userName);
	        	
	        	  IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	  	        if (intentApp == null) {
	  	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	  	        }

	  	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	  	        System.out.println("intentAppId===>" + intentAppId);
	            FirmApplication2 firmApplication = new FirmApplication2();
	            firmApplication.setInsuranceCompany(insurerCompany);
	            firmApplication.setFirmNetWorth(netWorth);
	            firmApplication.setPan(incomTaxPanNo);
	            firmApplication.setFirmTurnover(turnOver);
	            firmApplication.setPaidUpCapital(paidUpCapital);
	            firmApplication.setInsuranceCompany(insurerCompany);
	            firmApplication.setInsurancePolicyNo(policyNo);
	            firmApplication.setIntentAppId(intentApp);
	            firmApplication.setCreatedBy(EncryptionUtil.encrypt(userName)); 
	            //firmApplication.setUpdatedBy(EncryptionUtil.encrypt(userName)); 
	            firmApplication.setStatus("Active");
	            Optional<FirmApplication2> result = firmApplication2Serv.addData(firmApplication);

	            if (result.isEmpty()) {
	                return new ResponseEntity<>("Error occurred while saving Firm Application.",
	                        HttpStatus.INTERNAL_SERVER_ERROR);
	            }

	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	            String currentDate = LocalDateTime.now().format(formatter);

	            // Handle file1
	            if (file1 != null && !file1.isEmpty()) {
	                ApplicationDocument file1Document = new ApplicationDocument();
	                file1Document.setDocumentTitle("firmPanCardDocument"); 
	                String file1FileName = saveFile(file1, "firmPanCardDocument", currentDate);
	                file1Document.setFileName(file1FileName);
	                file1Document.setIntentAppId(intentApp); 
	                file1Document.setStatus("ACTIVE");
	                applicationDocumentServiceServ.addApplicationDocument(file1Document);
	            }

	            // Handle file2
	            if (file2 != null && !file2.isEmpty()) {
	                ApplicationDocument file2Document = new ApplicationDocument();
	                file2Document.setDocumentTitle("firmNetWorthDocument"); 
	                String file2FileName = saveFile(file2, "firmNetWorthDocument", currentDate);
	                file2Document.setFileName(file2FileName);
	                file2Document.setIntentAppId(intentApp); 
	                file2Document.setStatus("ACTIVE");
	                applicationDocumentServiceServ.addApplicationDocument(file2Document);
	            }

	            // Handle file3
	            if (file3 != null && !file3.isEmpty()) {
	                ApplicationDocument file3Document = new ApplicationDocument();
	                file3Document.setDocumentTitle("paidUpCapitalDocument"); 
	                String file3FileName = saveFile(file3, "paidUpCapitalDocument", currentDate);
	                file3Document.setFileName(file3FileName);
	                file3Document.setIntentAppId(intentApp); 
	                file3Document.setStatus("ACTIVE");
	                applicationDocumentServiceServ.addApplicationDocument(file3Document);
	            }


	            return new ResponseEntity<>(result, HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while saving the form.",
	                    HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	
//	// Helper method for saving files to a directory with a unique name
//		private String saveFile(MultipartFile file, String documentName, String currentDate) throws IOException {
//		    String originalFileName = file.getOriginalFilename();
//		    String fileExtension = "";
//
//		    // Extract the file extension (if available)
//		    if (originalFileName != null && originalFileName.contains(".")) {
//		        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//		    }
//
//		    // Construct a unique file name using the document name and current date
//		    String uniqueFileName = documentName + "_" + currentDate + fileExtension;
//
//		    // Define the file path (replace with your actual upload path)
//		    Path directoryPath = Paths.get(Constant.REAL_PATH, "AdditionalDocument", "Document");
//		    Path filePath = directoryPath.resolve(uniqueFileName);
//
//		    // Ensure the directory exists
//		    Files.createDirectories(directoryPath);
//
//		    // Copy the file to the specified location
//		    try (InputStream inputStream = file.getInputStream()) {
//		        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//		    }
//
//		    return uniqueFileName;
//		}
//	
	 @GetMapping(FirmApplicationFormAPIs.GET_FIRM_APPLICATION_FORM_STEP2_BY_USERNAME)
	 public ResponseEntity<?> getFirmApplicationStep2ByUsername(@RequestParam("userName") String userName) {
	     try {
	         System.out.println("userName===>" + userName);

	         IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	         if (intentApp == null) {
	             return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	         }
	         Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	         System.out.println("intentAppId===>" + intentAppId);	       
	         Map<String, Object> response = new HashMap<>();	

	         List<FirmApplication2> firmApplications = firmApplication2Serv.findIntentWithoutStatusAppById(intentAppId);
	         Optional<FirmApplication2> mostFirmApplication = firmApplications.stream()
	                 .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	                 .findFirst();

	         if (mostFirmApplication.isPresent()) {
	             FirmApplication2 appFirmApplication = mostFirmApplication.get();
	             response.put("appFirmApplication", appFirmApplication);	 
	         }

	         List<ApplicationDocument> applicationDocuments = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
	         System.out.println("applicationDocuments===>" + applicationDocuments.get(0).getFileName());
	         System.out.println("applicationDocumentsSize()===>" + applicationDocuments.size());
	         
	         // Collect all active documents generated today
	         List<ApplicationDocument> recentActiveDocuments = applicationDocuments.stream()
	                 .filter(doc -> {
	                     return (doc.getDocumentTitle().startsWith("firmPanCardDocument") ||
	                             doc.getDocumentTitle().startsWith("firmNetWorthDocument") ||
	                             doc.getDocumentTitle().startsWith("paidUpCapitalDocument"));   
	                 })
	                 .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
	                 .collect(Collectors.toList());
	         
	         // Add the list of recent active documents to the response
	         if (!recentActiveDocuments.isEmpty()) {
	             response.put("applicationDocuments", recentActiveDocuments);
	         }

	         // Optional: If you want to return the file names, adjust accordingly
	         List<String> fileNames = applicationDocuments.stream()
	                 .map(ApplicationDocument::getFileName) 
	                 .collect(Collectors.toList());
	         
	         response.put("fileNames", fileNames);
	         
	         return new ResponseEntity<>(response, HttpStatus.OK);

	     } catch (NumberFormatException e) {
	         return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	     } catch (Exception e) {
	         // Log the exception for debugging purposes
	         e.printStackTrace();
	         return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	 }

	
	
		@PostMapping(FirmApplicationFormAPIs.UPDATE_FIRM_APPLICATION_FORM_STEP2)
		public ResponseEntity<?> updateGovernmentAgencyStep2(
		        @RequestParam(value = "turnOver", required = false) String turnOver,
		        @RequestParam(value = "incomTaxPanNo", required = false) String incomTaxPanNo,
		        @RequestParam(value = "paidUpCapital", required = false) String paidUpCapital,
		        @RequestParam(value = "netWorth", required = false) String netWorth,
		        @RequestParam(value = "insurerCompany", required = false) String insurerCompany,
		        @RequestParam(value = "policyNo", required = false) String policyNo,
		        @RequestParam(value = "file1", required = false) MultipartFile file1,
		        @RequestParam(value = "file2", required = false) MultipartFile file2,
		        @RequestParam(value = "file3", required = false) MultipartFile file3,
		        @RequestParam(value = "userName", required = false) String userName,
		        @RequestParam(value = "firmApplicationId", required = false) String firmApplicationId,
		        @RequestParam(value = "appLocationId", required = false) String appLocationId) {
		    
		    try {
		        System.out.println("userName==>" + userName);
		        Date date = new Date();
		        
		        // Fetch IntentApplication by userName
		        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
		        if (intentApp == null) {
		            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
		        }
		        
		        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
		        System.out.println("intentAppId===>" + intentAppId);
		        
		     // Fetch the most recent FirmApplication2 record
		        List<FirmApplication2> firmApplications = firmApplication2Serv.findIntentWithoutStatusAppById(intentAppId);
		        Optional<FirmApplication2> mostFirmApplication = firmApplications.stream()
		                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
		                .findFirst();

		        if (mostFirmApplication.isPresent()) {
		            FirmApplication2 firmApplication = mostFirmApplication.get();

		            // Populate firm application data
		        
		            
		            System.out.println("firmApplicatione()===>" + firmApplication.getStatus());
		            // Update or save the firm application based on its current status
		            if ("Inactive".equals(firmApplication.getStatus())) {
		                populateFirmApplication(firmApplication, turnOver, incomTaxPanNo, paidUpCapital, netWorth, insurerCompany, policyNo, firmApplicationId, intentApp, userName, date,"Inactive");
		                // If the status is Inactive, we need to add a new record
		                Optional<FirmApplication2> result = firmApplication2Serv.addData(firmApplication);
		                if (result.isPresent()) {
		                    System.out.println("Firm application added successfully.");
		                } else {
		                    System.out.println("Failed to add firm application.");
		                }
		            } else {
		                // Otherwise, update the existing record
		                populateFirmApplication(firmApplication, turnOver, incomTaxPanNo, paidUpCapital, netWorth, insurerCompany, policyNo, firmApplicationId, intentApp, userName, date,"Active");
		                Optional<FirmApplication2> result = firmApplication2Serv.updateData(firmApplication);
		                if (result.isPresent()) {
		                    System.out.println("Firm application updated successfully.");
		                } else {
		                    System.out.println("Failed to update firm application.");
		                }
		            }
		        } else {
		            // If no existing application is found, create a new one
		            FirmApplication2 newFirmApplication = new FirmApplication2();
		            // Populate the new firm application data
		            populateFirmApplication(newFirmApplication, turnOver, incomTaxPanNo, paidUpCapital, netWorth, insurerCompany, policyNo, firmApplicationId, intentApp, userName, date,"Inative");
		            
		            // Add the new firm application
		            Optional<FirmApplication2> result = firmApplication2Serv.addData(newFirmApplication);
		            if (result.isPresent()) {
		                System.out.println("New firm application added successfully.");
		            } else {
		                System.out.println("Failed to add new firm application.");
		            }
		        }

		        
		        // Fetch application documents for the intent application
		        List<ApplicationDocument> applicationDocuments = applicationDocumentServiceServ.findIntentWithoutStatusAppById(intentApp.getIntentAppId());
		        System.out.println("applicationDocuments===>" + applicationDocuments.get(0).getFileName());
		        System.out.println("applicationDocumentsSize()===>" + applicationDocuments.size());
		        
		        // Get the most recent document
		        Optional<ApplicationDocument> mostRecentDocument = applicationDocuments.stream()
		                .filter(doc -> doc.getFileName().startsWith("firmPanCardDocument") ||
		                               doc.getFileName().startsWith("firmNetWorthDocument"))
		                .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated()))
		                .findFirst();
		        
		        // Process based on the document status
		        mostRecentDocument.ifPresentOrElse(applicationDocument -> {
		            System.out.println("applicationDocument Status===>" + applicationDocument.getStatus());
		            
		            try {
		                if ("Inactive".equals(applicationDocument.getStatus())) {
		                    // If document is inactive, ADD new files
		                    processFiles(intentApp, applicationDocument.getFileName(),  applicationDocument.getAppDocId(), file1, file2, file3, date, "Inactive");
		                } else {
		                    // If document is not inactive, UPDATE the existing files
		                    processFiles(intentApp,applicationDocument.getFileName(), applicationDocument.getAppDocId(), file1, file2, file3, date, "Active");
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }, () -> {
		            // Handle case when there is no document (fallback)
		            try {
		              //  processFiles(intentApp,  applicationDocument.getAppDocId(), file1, file2, file3, date, "Inactive");
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        });
		        
		        return new ResponseEntity<>("Firm Application updated successfully.", HttpStatus.OK);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ResponseEntity<>("An error occurred while saving the form.", HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

		// Helper method to populate the FirmApplication2 object
		private void populateFirmApplication(FirmApplication2 firmApplication, String turnOver, String incomTaxPanNo, 
		                                     String paidUpCapital, String netWorth, String insurerCompany, String policyNo, 
		                                     String firmApplicationId, IntentApplication intentApp, String userName, Date date,String Status) {
			if("Active".equals(Status)) {
		    firmApplication.setFirmApplicationId(Long.valueOf(firmApplicationId));
			}else {
				firmApplication.setFirmApplicationId(null);
			}
		    firmApplication.setInsuranceCompany(insurerCompany);
		    firmApplication.setFirmNetWorth(netWorth);
		    firmApplication.setPan(incomTaxPanNo);
		    firmApplication.setFirmTurnover(turnOver);
		    firmApplication.setPaidUpCapital(paidUpCapital);
		    firmApplication.setInsurancePolicyNo(policyNo);
		    firmApplication.setIntentAppId(intentApp);
		    firmApplication.setStatus("Active");
		    firmApplication.setCreatedBy(EncryptionUtil.encrypt(userName)); 
		    firmApplication.setUpdatedBy(EncryptionUtil.encrypt(userName)); 
		    firmApplication.setCreated(date);
		    firmApplication.setUpdated(date);
		   
		}

		// Helper function to handle file processing
		private void processFiles(IntentApplication intentApp,String fileName, Long appLocationId, MultipartFile file1, 
		                          MultipartFile file2, MultipartFile file3, Date date, String firmApplicationStatus) throws Exception {

		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		    String currentDate = LocalDateTime.now().format(formatter);
		    
		    if ("Inactive".equals(firmApplicationStatus)) {
		        // Add new files
		      
		            ApplicationDocument file1Document = createNewApplicationDocument(appLocationId,fileName, "firmPanCardDocument", currentDate, file1, intentApp, date ,firmApplicationStatus);
		            applicationDocumentServiceServ.addApplicationDocument(file1Document);
		        
		     
		            ApplicationDocument file2Document = createNewApplicationDocument(appLocationId,fileName, "firmNetWorthDocument", currentDate, file2, intentApp, date,firmApplicationStatus);
		            applicationDocumentServiceServ.addApplicationDocument(file2Document);
		        
		       
		            ApplicationDocument file3Document = createNewApplicationDocument(appLocationId, fileName,"paidUpCapitalDocument", currentDate, file3, intentApp, date,firmApplicationStatus);
		            applicationDocumentServiceServ.addApplicationDocument(file3Document);
		        

		    } else {
		        // Update existing files
		     
		            ApplicationDocument file1Document = createNewApplicationDocument(appLocationId,fileName, "firmPanCardDocument", currentDate, file1, intentApp, date,firmApplicationStatus);
		            applicationDocumentServiceServ.updateApplicationDocument(file1Document);
		        
		       
		            ApplicationDocument file2Document = createNewApplicationDocument(appLocationId, fileName,"firmNetWorthDocument", currentDate, file2, intentApp, date,firmApplicationStatus);
		            applicationDocumentServiceServ.updateApplicationDocument(file2Document);
		        
		       
		            ApplicationDocument file3Document = createNewApplicationDocument(appLocationId,fileName, "paidUpCapitalDocument", currentDate, file3, intentApp, date,firmApplicationStatus);
		            applicationDocumentServiceServ.updateApplicationDocument(file3Document);
		        
		    }
		}

		// Helper function to create a new ApplicationDocument
		private ApplicationDocument createNewApplicationDocument(Long appLocationId,String fileName, String documentType, 
		                                                         String currentDate, MultipartFile file, 
		                                                         IntentApplication intentApp, Date date,String firmApplicationStatus) throws Exception {
		    
		    ApplicationDocument document = new ApplicationDocument();
		    if("Active".equals(firmApplicationStatus)) {
		    document.setAppDocId(Long.valueOf(appLocationId));
		    }else {
		    	
		    }
		    document.setDocumentTitle(documentType);
		  		    if (file != null && !file.isEmpty()) {
		        String savedFileName = saveFile(file, documentType, currentDate);
		        document.setFileName(savedFileName);
		    } else {
		        document.setFileName(fileName);
		    }
		    document.setIntentAppId(intentApp);
		    document.setCreated(date);
		    document.setStatus("Active");  // Set as Inactive by default or as needed
		    return document;
		}

	@PostMapping(FirmApplicationFormAPIs.ADD_FIRM_APPLICATION_FORM_STEP3)
	public ResponseEntity<?> submitPartnerDetails(@RequestParam("partnerDetails") String partnerDetailsJson,
	                                              @RequestParam Map<String, MultipartFile> files) throws Exception {
	    // Deserialize the partnerDetails JSON string
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<PartnerDetailsDTO> partnerDetailsList = objectMapper.readValue(partnerDetailsJson, new TypeReference<List<PartnerDetailsDTO>>() {});

	    String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date()); // Get current date as a string
	    System.out.println("partnerDetailsList.size(): " + partnerDetailsList.size());

	    // Iterate over the partner details
	    for (int i = 0; i < partnerDetailsList.size(); i++) {
	        PartnerDetailsDTO partner = partnerDetailsList.get(i);
	        System.out.println("Processing partner: " + partner.getFirstName());

	        // Initialize document names
	        String passportDocumentName = null;
	        String idCardDocumentName = null;
	        String panCardDocumentName = null;

	        // Iterate through the files and match by the index
	        for (String key : files.keySet()) {
	            MultipartFile file = files.get(key);
	            if (file != null && !file.isEmpty()) {
	                String originalName = file.getOriginalFilename();
	                System.out.println("Received file for key: " + key + ", filename: " + originalName);

	                // Extract the index from the file key (e.g., "files[passportDocument][1]")
	                String fileIndexStr = key.replaceAll("[^0-9]", ""); // Extract numbers from the key
	                int fileIndex = Integer.parseInt(fileIndexStr);

	                // Check if the fileIndex matches the current partner index
	                if (fileIndex == i) {
	                    // Match the keys and save the file accordingly
	                    if (key.contains("files[passportDocument]")) {
	                        passportDocumentName = saveFile(file, "passportDocument", currentDate); // Save passport document
	                    } else if (key.contains("files[idCardDocument]")) {
	                        idCardDocumentName = saveFile(file, "idCardDocument", currentDate); // Save ID card document
	                    } else if (key.contains("files[panCardDocument]")) {
	                        panCardDocumentName = saveFile(file, "panCardDocument", currentDate); // Save PAN card document
	                    } else {
	                        System.out.println("No matching key for file: " + originalName);
	                    }
	                }
	            } else {
	                System.out.println("No file received for key: " + key);
	            }
	        }

	        // Save partner details with document names
	        savePartnerDetails(partner, passportDocumentName, idCardDocumentName, panCardDocumentName);
	    }

	    return new ResponseEntity<>("Form submitted successfully!", HttpStatus.OK);
	}


	// Method to save partner details
	private void savePartnerDetails(PartnerDetailsDTO partner, String passportDocumentName, String idCardDocumentName, String panCardDocumentName) {
	    // Find the intent application by username
	    IntentApplication intentApps = intentAppServ.findIntentAppById(partner.getUserName());

	    // Create a new FirmPartner entity
	    FirmPartnerDetails firmPartner = new FirmPartnerDetails();

	    // Populate the address data from the partner DTO
	    AddressDTO addressDTO = new AddressDTO();
	    addressDTO.setBlockNo(partner.getBlockNo());
	    addressDTO.setVillage(partner.getVillage());
	    addressDTO.setPostOffice(partner.getPostOffice());
	    addressDTO.setSubDivision(partner.getSubDivision());
	    addressDTO.setCountry(partner.getCountry());
	    addressDTO.setCity(partner.getCity());
	    addressDTO.setState(partner.getState());
	    addressDTO.setPin(partner.getPin());
	    addressDTO.setFax(partner.getFax());
	    addressDTO.setMobile(partner.getMobile());
	    addressDTO.setTelephoneNo(partner.getTelephoneNo());
	    // Save the address and get the ID
	    Long resAddId = intentAppServ.addUsers(addressDTO);

	    // Create AppLocation and save it
	    AppLocation resLocation = new AppLocation();
	    resLocation.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId))); 
	    resLocation.setLocationName("FirmPartnershipAddress");
	    resLocation.setIntentAppId(intentApps); 
	    resLocation.setStatus("Active");
	    appLocationServ.addAppLocation(resLocation);

	    // Set the partner details fields
	    firmPartner.setFax(partner.getFax());
	    firmPartner.setTelephoneNo(partner.getTelephoneNo());
	    firmPartner.setMobileNo(partner.getMobile());
	    firmPartner.setEmailId(partner.getEmail());
	    firmPartner.setFirstName(partner.getFirstName());
	    firmPartner.setLastName(partner.getLastName());
	    firmPartner.setMiddleName(partner.getMiddleName());
	    firmPartner.setSalutation(partner.getSalutation());
	    firmPartner.setPassportNo(partner.getPassposrtNo());
	    firmPartner.setPassportIssuingAuthority(partner.getPassportIssuingAthority());

	    // Parse and set passport expiry date
	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date passportExpiryDate = dateFormat.parse(partner.getPassportExpirydate());
	        firmPartner.setPassportExpiryDate(passportExpiryDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        firmPartner.setPassportExpiryDate(null); // Handle parse exception
	    }

	    // Set documents and additional details
	    firmPartner.setVoterIdCard(partner.getVoterCardNo());
	    firmPartner.setPan(partner.getPancardNo());
	    firmPartner.setPanDocument(panCardDocumentName);
	    firmPartner.setVoterIdCardDocument(idCardDocumentName);
	    firmPartner.setPassportDocument(passportDocumentName);
	    firmPartner.setPersonalWebPage(partner.getWebURL());
	    firmPartner.setNationality(partner.getNationality());
	    firmPartner.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId)));
	    firmPartner.setIntentAppId(intentApps);
	    firmPartner.setStatus("Active");
	    // Save FirmPartner entity
	    firmPartnerServ.addData(firmPartner);
	}

	private String saveFile(MultipartFile file, String documentName, String currentDate) throws IOException {
	    String originalFileName = file.getOriginalFilename();
	    String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

	    // Create a unique file name using documentName, current date, and a UUID
	    String uniqueFileName = documentName + "_" + currentDate + "_" + UUID.randomUUID().toString() + fileExtension;

	    // Define the directory where the file will be saved
	    Path directoryPath = Paths.get(Constant.REAL_PATH, "FirmDocuments", "Documents");
	    Path filePath = directoryPath.resolve(uniqueFileName);

	    // Create the directory if it doesn't exist
	    Files.createDirectories(directoryPath);

	    // Save the file
	    try (InputStream inputStream = file.getInputStream()) {
	        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	    }

	    // Return the unique file name
	    return uniqueFileName;
	}

	@GetMapping(FirmApplicationFormAPIs.GET_FIRM_APPLICATION_FORM3BY_USERNAME)
	public ResponseEntity<?> getFirmApplicationByThirdUsername(@RequestParam("userName") String userName) {
	    try {
	        System.out.println("userName===>" + userName);

	        // Retrieve the Intent Application by username
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        System.out.println("intentAppId===>" + intentAppId);

	        // Prepare the response map
	        Map<String, Object> response = new HashMap<>();
	        String status = "Edit_Upon_Review".equals(intentApp.getApplicationStatus()) ? "Inactive" : "Active";
	        // Retrieve AppLocation and FirmPartnerDetails
	       

	        List<FirmPartnerDetails> appFirmPartnerDetails = firmPartnerServ.findIntentAppById(intentAppId);

	     // Sort the list in descending order by creation date
	     List<FirmPartnerDetails> sortedFirmPartnerDetails = appFirmPartnerDetails.stream()
	             .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	             .limit(1)
	             .collect(Collectors.toList());

	     // Add the sorted list to the response
	     response.put("appFirmApplications", sortedFirmPartnerDetails);

	     List<AppLocation> appLocation = appLocationServ.findIntentWithoutStatusAppById(intentAppId);

	  // Sort the AppLocation list by created date in descending order
	  List<AppLocation> sortedAppLocations = appLocation.stream()
	      .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	      .collect(Collectors.toList()); // Collect to list

	  // List to hold multiple AddressDTO objects and relevant AppLocation objects
	  List<AddressDTO> addressDTOList = new ArrayList<>();
	  List<AppLocation> filteredAppLocations = new ArrayList<>();

	  // Use a Set to track unique location names
	  Set<String> uniqueLocationNames = new HashSet<>();

	  for (AppLocation location : sortedAppLocations) {
	      String locationName = location.getLocationName();

	      // Only process the location if its name is not already processed
	      if (!uniqueLocationNames.contains(locationName)) {
	          uniqueLocationNames.add(locationName); // Mark the locationName as processed
	          System.out.println("AppLocation object: " + locationName);

	          // Check if the location name is "FirmPartnershipAddress"
	          if ("FirmPartnershipAddress".equals(locationName)) {
	              String addressIdStr = location.getAddressId();
	              if (addressIdStr != null && !addressIdStr.isEmpty()) {
	                  try {
	                      // Decrypt the address ID
	                      String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	                      System.out.println("Decrypted Address ID: " + decryptedAddressId);

	                      // Retrieve AddressDTO by decrypted address ID
	                      AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);
	                      System.out.println("addressDTO==>" + addressDTO);

	                      // Add the AddressDTO to the list if it's not null
	                      if (addressDTO != null) {
	                          addressDTOList.add(addressDTO);
	                          // Add the AppLocation object to the filtered list
	                          filteredAppLocations.add(location);
	                      } else {
	                          System.out.println("No AddressDTO found for decrypted Address ID: " + decryptedAddressId);
	                      }

	                  } catch (Exception e) {
	                      System.out.println("Error processing Address ID: " + addressIdStr + " - " + e.getMessage());
	                  }
	              } else {
	                  System.out.println("Address ID is null or empty for this location.");
	              }
	          } else {
	              System.out.println("Location name is not FirmPartnershipAddress, skipping.");
	          }
	      } else {
	          System.out.println("Duplicate location name found: " + locationName + ", skipping.");
	      }
	  }

	      // Add the list of addresses and filtered app locations to the response
	      response.put("addressDTOs", addressDTOList);
	      response.put("filteredAppLocations", filteredAppLocations);


	      // Add the list of addresses and filtered app locations to the response
	      response.put("addressDTOs", addressDTOList);
	      response.put("filteredAppLocations", filteredAppLocations);

	        // Return the response with both appFirmPartnerDetails, addressDTOList, and filteredAppLocations
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PostMapping(FirmApplicationFormAPIs.UPDATE_FIRM_APPLICATION_FORM_STEP3)
	public ResponseEntity<?> updatePartnerDetails(
	        @RequestParam("partnerDetails") String partnerDetailsJson,
	        @RequestParam Map<String, MultipartFile> files) throws Exception {
	    
	    // Deserialize the partnerDetails JSON string as a List
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<PartnerDetailsDTO> partnerDetailsList = objectMapper.readValue(partnerDetailsJson, 
	            new TypeReference<List<PartnerDetailsDTO>>() {});
	    
	    String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date()); // Get current date as a string
	    System.out.println("partnerDetailsList.size(): " + partnerDetailsList.size());

	    IntentApplication intentApps = intentAppServ.findIntentAppById(partnerDetailsList.get(0).getUserName());
	    
	      List<FirmPartnerDetails>firmPartnerDetails=firmPartnerServ.findIntentAppWithoutStatusById(intentApps.getIntentAppId());
	      List<AppLocation> appLocation = appLocationServ.findIntentWithoutStatusAppById(intentApps.getIntentAppId());
	      
	   // Check if any of the FirmPartnerDetails are inactive
	      Optional<AppLocation> mostRecentDocument = appLocation.stream()
	    		    .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	    		    .findFirst(); 

	      

	  
	      if ("Inactive".equals(mostRecentDocument.get().getStatus())) {
	          // At least one of the lists contains an inactive status
	  	    // Iterate over the partner details
	  	    for (int i = 0; i < partnerDetailsList.size(); i++) {
	  	        PartnerDetailsDTO partner = partnerDetailsList.get(i);
	  	        System.out.println("Processing partner: " + partner.getFirstName());

	  	        // Initialize document names
	  	        String passportDocumentName = null;
	  	        String idCardDocumentName = null;
	  	        String panCardDocumentName = null;

	  	        // Iterate through the files and match by the index
	  	        for (String key : files.keySet()) {
	  	            MultipartFile file = files.get(key);
	  	            if (file != null && !file.isEmpty()) {
	  	                String originalName = file.getOriginalFilename();
	  	                System.out.println("Received file for key: " + key + ", filename: " + originalName);

	  	                // Extract the index from the file key (e.g., "files[passportDocument][1]")
	  	                String fileIndexStr = key.replaceAll("[^0-9]", ""); // Extract numbers from the key
	  	                int fileIndex = Integer.parseInt(fileIndexStr);

	  	                // Check if the fileIndex matches the current partner index
	  	                if (fileIndex == i) {
	  	                    // Match the keys and save the file accordingly
	  	                    if (key.contains("files[passportDocument]")) {
	  	                        passportDocumentName = saveFile(file, "passportDocument", currentDate); // Save passport document
	  	                    } else if (key.contains("files[idCardDocument]")) {
	  	                        idCardDocumentName = saveFile(file, "idCardDocument", currentDate); // Save ID card document
	  	                    } else if (key.contains("files[panCardDocument]")) {
	  	                        panCardDocumentName = saveFile(file, "panCardDocument", currentDate); // Save PAN card document
	  	                    } else {
	  	                        System.out.println("No matching key for file: " + originalName);
	  	                    }
	  	                }
	  	            } else {
	  	                System.out.println("No file received for key: " + key);
	  	            }
	  	        }

	  	        // Save partner details with document names
	  	        savePartnerDetails(partner, passportDocumentName, idCardDocumentName, panCardDocumentName);
	  	    }
	          System.out.println("At least one entry is inactive in either FirmPartnerDetails or AppLocation.");
	          // Add your handling logic here
	      } else {
	          // Both lists are active
	    	  
	    	  // Iterate over the partner details
	  	    for (int i = 0; i < partnerDetailsList.size(); i++) {
	  	        PartnerDetailsDTO partner = partnerDetailsList.get(i);
	  	        System.out.println("Processing partner: " + partner.getFirstName());

	  	        // Initialize document names
	  	        String passportDocumentName = null;
	  	        String idCardDocumentName = null;
	  	        String panCardDocumentName = null;

	  	        // Iterate through the files and match by the index
	  	        for (String key : files.keySet()) {
	  	            MultipartFile file = files.get(key);
	  	            if (file != null && !file.isEmpty()) {
	  	                String originalName = file.getOriginalFilename();
	  	                System.out.println("Received file for key: " + key + ", filename: " + originalName);

	  	                // Extract the index from the file key (e.g., "files[passportDocument][1]")
	  	                String fileIndexStr = key.replaceAll("[^0-9]", ""); // Extract numbers from the key
	  	                int fileIndex = Integer.parseInt(fileIndexStr);

	  	                // Check if the fileIndex matches the current partner index
	  	                if (fileIndex == i) {
	  	                    // Match the keys and save the file accordingly
	  	                    if (key.contains("files[passportDocument]")) {
	  	                        passportDocumentName = saveFile(file, "passportDocument", currentDate); // Save passport document
	  	                    } else if (key.contains("files[idCardDocument]")) {
	  	                        idCardDocumentName = saveFile(file, "idCardDocument", currentDate); // Save ID card document
	  	                    } else if (key.contains("files[panCardDocument]")) {
	  	                        panCardDocumentName = saveFile(file, "panCardDocument", currentDate); // Save PAN card document
	  	                    } else {
	  	                        System.out.println("No matching key for file: " + originalName);
	  	                    }
	  	                }
	  	            } else {
	  	                System.out.println("No file received for key: " + key);
	  	            }
	  	        }

	  	        // Save partner details with document names
	  	        savePartnerDetail(partner, passportDocumentName, idCardDocumentName, panCardDocumentName);
	  	    }
	          System.out.println("All entries are active in both FirmPartnerDetails and AppLocation.");
	          // Proceed with your logic
	      }
	    
	   

	    return new ResponseEntity<>("Form submitted successfully!", HttpStatus.OK);
	}

	// Method to save partner details
	private void savePartnerDetail(PartnerDetailsDTO partner, String passportDocumentName, String idCardDocumentName, String panCardDocumentName) {
	    // Find the intent application by username
	    IntentApplication intentApps = intentAppServ.findIntentAppById(partner.getUserName());

	    // Create a new FirmPartner entity
	    FirmPartnerDetails firmPartner = new FirmPartnerDetails();

	    // Populate the address data from the partner DTO
	    AddressDTO addressDTO = new AddressDTO();
	    addressDTO.setAddressId(partner.getAddressId());
	    addressDTO.setBlockNo(partner.getBlockNo());
	    addressDTO.setVillage(partner.getVillage());
	    addressDTO.setPostOffice(partner.getPostOffice());
	    addressDTO.setSubDivision(partner.getSubDivision());
	    addressDTO.setCountry(partner.getCountry());
	    addressDTO.setCity(partner.getCity());
	    addressDTO.setState(partner.getState());
	    addressDTO.setPin(partner.getPin());
	    addressDTO.setFax(partner.getFax());
	    addressDTO.setMobile(partner.getMobile());
	    addressDTO.setTelephoneNo(partner.getTelephoneNo());
	    
	    // Save the address and get the ID
	    Long resAddId = intentAppServ.updateUsers(addressDTO);

	    // Set the partner details fields
	    firmPartner.setFax(partner.getFax());
	    firmPartner.setTelephoneNo(partner.getTelephoneNo());
	    firmPartner.setMobileNo(partner.getMobile());
	    firmPartner.setEmailId(partner.getEmail());
	    firmPartner.setFirstName(partner.getFirstName());
	    firmPartner.setLastName(partner.getLastName());
	    firmPartner.setMiddleName(partner.getMiddleName());
	    firmPartner.setSalutation(partner.getSalutation());
	    firmPartner.setPassportNo(partner.getPassposrtNo());
	    firmPartner.setPassportIssuingAuthority(partner.getPassportIssuingAthority());

	    // Parse and set passport expiry date
	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date passportExpiryDate = dateFormat.parse(partner.getPassportExpirydate());
	        firmPartner.setPassportExpiryDate(passportExpiryDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        firmPartner.setPassportExpiryDate(null); // Handle parse exception
	    }

	    // Set documents and additional details
	    firmPartner.setPartnerDetailId(Long.valueOf(partner.getPartnerDetailId()));
	    firmPartner.setVoterIdCard(partner.getVoterCardNo());
	    firmPartner.setPan(partner.getPancardNo());
	    
	    firmPartner.setPanDocument(partner.getPancardDocument());
	    firmPartner.setVoterIdCardDocument(partner.getVoterCardDocument());
	    firmPartner.setPassportDocument(partner.getPassportDocument());
	    firmPartner.setPersonalWebPage(partner.getWebURL());
	    firmPartner.setNationality(partner.getNationality());
	    firmPartner.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId)));
	    firmPartner.setIntentAppId(intentApps);
Date date = new Date();
firmPartner.setCreated(date);
firmPartner.setUpdated(date);
firmPartner.setStatus("Active");
	    // Save FirmPartner entity
	    firmPartnerServ.updateData(firmPartner);
	}


	@PostMapping(FirmApplicationFormAPIs.ADD_FIRM_APPLICATION_FORM_STEP4)
	public ResponseEntity<?> addFirmAutherizedRepresentive(@RequestBody FirmAuthorizedRepresentativeDTO firmAuthorizedRepresentativeDTO) {

      
		try {
			  IntentApplication intentApp = intentAppServ.findIntentAppById(firmAuthorizedRepresentativeDTO.getUserName());
		        if (intentApp == null) {
		            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
		        }

		        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
		        System.out.println("intentAppId===>" + intentAppId);
			
			AddressDTO addressDTO = new AddressDTO(); 

			// Setting address details into the AddressDTO
			addressDTO.setBlockNo(firmAuthorizedRepresentativeDTO.getBlockNo());
			addressDTO.setVillage(firmAuthorizedRepresentativeDTO.getVillage());
			addressDTO.setPostOffice(firmAuthorizedRepresentativeDTO.getPostOffice());
			addressDTO.setSubDivision(firmAuthorizedRepresentativeDTO.getSubDivision());
			addressDTO.setCountry(firmAuthorizedRepresentativeDTO.getCountry());
			addressDTO.setCity(firmAuthorizedRepresentativeDTO.getCity());
			addressDTO.setState(firmAuthorizedRepresentativeDTO.getState());
			addressDTO.setPin(firmAuthorizedRepresentativeDTO.getPin());

			// Pass the addressDTO to the addUsers() method
			Long resAddId = intentAppServ.addUsers(addressDTO);
			
			   
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		        FirmAuthorizedRepresentative firmAuthorizedRepresentative = new FirmAuthorizedRepresentative();

		        firmAuthorizedRepresentative.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId)));
		        firmAuthorizedRepresentative.setFirstName(firmAuthorizedRepresentativeDTO.getFirstName());
		        firmAuthorizedRepresentative.setLastName(firmAuthorizedRepresentativeDTO.getLastName());
		        firmAuthorizedRepresentative.setMiddleName(firmAuthorizedRepresentativeDTO.getMiddleName());
		        firmAuthorizedRepresentative.setSalutation(firmAuthorizedRepresentativeDTO.getSalutation());
		        firmAuthorizedRepresentative.setFax(firmAuthorizedRepresentativeDTO.getFax());
		        firmAuthorizedRepresentative.setNatureOfBusiness(firmAuthorizedRepresentativeDTO.getNaturesOfBusiness());
		        firmAuthorizedRepresentative.setIntentAppId(intentApp);
		        firmAuthorizedRepresentative.setTelephoneNo(firmAuthorizedRepresentativeDTO.getTelephoneNo());
		        firmAuthorizedRepresentative.setStatus("Active");
		        firmAuthorizedRepresentative.setCreatedBy(EncryptionUtil.encrypt(String.valueOf(firmAuthorizedRepresentativeDTO.getUserName())));
			
		      
			
			Optional<FirmAuthorizedRepresentative>result =firmAuthorizedRepresentiveService.addData(firmAuthorizedRepresentative);
		
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
	
	@GetMapping(FirmApplicationFormAPIs.GET_FIRM_APPLICATION_FORM4BY_USERNAME)
	public ResponseEntity<?> getFirmApplicationByFourthUsername(@RequestParam("userName") String userName) {
	    try {
	        System.out.println("userName===>" + userName);

	        // Retrieve the Intent Application by username
	        IntentApplication intentApp = intentAppServ.findIntentAppById(userName);
	        if (intentApp == null) {
	            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
	        }

	        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
	        System.out.println("intentAppId===>" + intentAppId);

	        // Prepare the response map
	        Map<String, Object> response = new HashMap<>();

	        // Retrieve authorized representatives
	        List<FirmAuthorizedRepresentative> authorizedRepresentatives = firmAuthorizedRepresentiveService.findIntentWitoutAppById(intentAppId);

	        // Get the most recent FirmAuthorizedRepresentative document
	        Optional<FirmAuthorizedRepresentative> mostRecent = authorizedRepresentatives.stream()
	            .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
	            .findFirst();

	        if (mostRecent.isPresent()) {
	            FirmAuthorizedRepresentative recentRepresentative = mostRecent.get();
	            response.put("FirmAuthorizedRepresentative", recentRepresentative); // Add the actual object, not the Optional

	            // List to hold multiple addressDTO objects
	            List<AddressDTO> addressDTOList = new ArrayList<>();

	            // Get address information for the most recent representative
	            String addressIdStr = recentRepresentative.getAddressId();
	            if (addressIdStr != null && !addressIdStr.isEmpty()) {
	                try {
	                    // Decrypt the address ID
	                    String decryptedAddressId = EncryptionUtil.decrypt(addressIdStr);
	                    System.out.println("Decrypted Address ID: " + decryptedAddressId);

	                    // Retrieve AddressDTO by decrypted address ID
	                    AddressDTO addressDTO = intentAppServ.getAllLocationByAddressId(decryptedAddressId);
	                    System.out.println("addressDTO==>" + addressDTO);

	                    // Add the AddressDTO to the list if it's not null
	                    if (addressDTO != null) {
	                        addressDTOList.add(addressDTO);
	                    } else {
	                        System.out.println("No AddressDTO found for decrypted Address ID: " + decryptedAddressId);
	                    }

	                } catch (Exception e) {
	                    System.out.println("Error processing Address ID: " + addressIdStr + " - " + e.getMessage());
	                }
	            } else {
	                System.out.println("Address ID is null or empty for this location.");
	            }

	            // Add the list of addresses to the response
	            response.put("addressDTOs", addressDTOList);

	        } else {
	            System.out.println("No authorized representative found.");
	            return new ResponseEntity<>("No authorized representative found.", HttpStatus.NOT_FOUND);
	        }

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving application data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PostMapping(FirmApplicationFormAPIs.UPDATE_FIRM_APPLICATION_FORM_STEP4)
	public ResponseEntity<?> updateFirmAutherizedRepresentive(@RequestBody FirmAuthorizedRepresentativeDTO firmAuthorizedRepresentativeDTO) {

      
		try {
			  IntentApplication intentApp = intentAppServ.findIntentAppById(firmAuthorizedRepresentativeDTO.getUserName());
		        if (intentApp == null) {
		            return new ResponseEntity<>("Intent Application not found.", HttpStatus.NOT_FOUND);
		        }

		        Long intentAppId = Long.valueOf(intentApp.getIntentAppId());
		        System.out.println("intentAppId===>" + intentAppId);
		        Optional<FirmAuthorizedRepresentative>result;
		       List<FirmAuthorizedRepresentative>authorizedRepresentatives=firmAuthorizedRepresentiveService.findIntentWitoutAppById(intentAppId);
		        
		       Optional<FirmAuthorizedRepresentative> mostRecent = authorizedRepresentatives.stream()
		    		    .sorted((b1, b2) -> b2.getCreated().compareTo(b1.getCreated())) // Sort in descending order
		    		    .findFirst(); 

		      if("Inactive".equals(mostRecent.get().getStatus())) {
		    		AddressDTO addressDTO = new AddressDTO(); 

					// Setting address details into the AddressDTO
					addressDTO.setBlockNo(firmAuthorizedRepresentativeDTO.getBlockNo());
					addressDTO.setVillage(firmAuthorizedRepresentativeDTO.getVillage());
					addressDTO.setPostOffice(firmAuthorizedRepresentativeDTO.getPostOffice());
					addressDTO.setSubDivision(firmAuthorizedRepresentativeDTO.getSubDivision());
					addressDTO.setCountry(firmAuthorizedRepresentativeDTO.getCountry());
					addressDTO.setCity(firmAuthorizedRepresentativeDTO.getCity());
					addressDTO.setState(firmAuthorizedRepresentativeDTO.getState());
					addressDTO.setPin(firmAuthorizedRepresentativeDTO.getPin());

					// Pass the addressDTO to the addUsers() method
					Long resAddId = intentAppServ.addUsers(addressDTO);
					
					   
				        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				        FirmAuthorizedRepresentative firmAuthorizedRepresentative = new FirmAuthorizedRepresentative();

				        firmAuthorizedRepresentative.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId)));
				        firmAuthorizedRepresentative.setFirstName(firmAuthorizedRepresentativeDTO.getFirstName());
				        firmAuthorizedRepresentative.setLastName(firmAuthorizedRepresentativeDTO.getLastName());
				        firmAuthorizedRepresentative.setMiddleName(firmAuthorizedRepresentativeDTO.getMiddleName());
				        firmAuthorizedRepresentative.setSalutation(firmAuthorizedRepresentativeDTO.getSalutation());
				        firmAuthorizedRepresentative.setFax(firmAuthorizedRepresentativeDTO.getFax());
				        firmAuthorizedRepresentative.setNatureOfBusiness(firmAuthorizedRepresentativeDTO.getNaturesOfBusiness());
				        firmAuthorizedRepresentative.setIntentAppId(intentApp);
				        firmAuthorizedRepresentative.setTelephoneNo(firmAuthorizedRepresentativeDTO.getTelephoneNo());
				        firmAuthorizedRepresentative.setStatus("Active");
				        firmAuthorizedRepresentative.setCreatedBy(EncryptionUtil.encrypt(String.valueOf(firmAuthorizedRepresentativeDTO.getUserName())));
					
				      
					
					result =firmAuthorizedRepresentiveService.addData(firmAuthorizedRepresentative);
				
		    	  
		      }else {
		    	  AddressDTO addressDTO = new AddressDTO(); 

					// Setting address details into the AddressDTO
					addressDTO.setAddressId(firmAuthorizedRepresentativeDTO.getAddressId());
					addressDTO.setBlockNo(firmAuthorizedRepresentativeDTO.getBlockNo());
					addressDTO.setVillage(firmAuthorizedRepresentativeDTO.getVillage());
					addressDTO.setPostOffice(firmAuthorizedRepresentativeDTO.getPostOffice());
					addressDTO.setSubDivision(firmAuthorizedRepresentativeDTO.getSubDivision());
					addressDTO.setCountry(firmAuthorizedRepresentativeDTO.getCountry());
					addressDTO.setCity(firmAuthorizedRepresentativeDTO.getCity());
					addressDTO.setState(firmAuthorizedRepresentativeDTO.getState());
					addressDTO.setPin(firmAuthorizedRepresentativeDTO.getPin());

					// Pass the addressDTO to the addUsers() method
					Long resAddId = intentAppServ.updateUsers(addressDTO);
					
					   
				        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				        FirmAuthorizedRepresentative firmAuthorizedRepresentative = new FirmAuthorizedRepresentative();

				        firmAuthorizedRepresentative.setAuthorizedRepresentativeId(Long.valueOf(firmAuthorizedRepresentativeDTO.getAuthorizedRepresentativeId()));
				        firmAuthorizedRepresentative.setAddressId(EncryptionUtil.encrypt(String.valueOf(resAddId)));
				        firmAuthorizedRepresentative.setFirstName(firmAuthorizedRepresentativeDTO.getFirstName());
				        firmAuthorizedRepresentative.setLastName(firmAuthorizedRepresentativeDTO.getLastName());
				        firmAuthorizedRepresentative.setMiddleName(firmAuthorizedRepresentativeDTO.getMiddleName());
				        firmAuthorizedRepresentative.setSalutation(firmAuthorizedRepresentativeDTO.getSalutation());
				        firmAuthorizedRepresentative.setFax(firmAuthorizedRepresentativeDTO.getFax());
				        firmAuthorizedRepresentative.setNatureOfBusiness(firmAuthorizedRepresentativeDTO.getNaturesOfBusiness());
				        firmAuthorizedRepresentative.setIntentAppId(intentApp);
				        firmAuthorizedRepresentative.setTelephoneNo(firmAuthorizedRepresentativeDTO.getTelephoneNo());
				        firmAuthorizedRepresentative.setStatus("Active");
				        firmAuthorizedRepresentative.setCreatedBy(EncryptionUtil.encrypt(String.valueOf(firmAuthorizedRepresentativeDTO.getUserName())));
					Date date =new Date();
					firmAuthorizedRepresentative.setCreated(date);
					firmAuthorizedRepresentative.setUpdated(date);
					firmAuthorizedRepresentative.setUpdatedBy(EncryptionUtil.encrypt(String.valueOf(firmAuthorizedRepresentativeDTO.getUserName())));
				      
					
					result =firmAuthorizedRepresentiveService.updateData(firmAuthorizedRepresentative);
		      }
		        
			
		
			if (result.isEmpty()) {
				return new ResponseEntity<>("Error occurred while saving  Step.",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the  Step.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	}
