package in.lms.cca.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

import in.lms.cca.dto.AuditAgencyDTO;
import in.lms.cca.dto.AuditAgencyEmailDTO;
import in.lms.cca.dto.AuditAgencyMobileDTO;
import in.lms.cca.dto.AuditorsDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.Address;
import in.lms.cca.entity.AuditAgency;
import in.lms.cca.entity.AuditAgencyEmail;
import in.lms.cca.entity.AuditAgencyMobile;
import in.lms.cca.entity.Auditors;
import in.lms.cca.entity.City;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.State;
import in.lms.cca.service.IAddressService;
import in.lms.cca.service.IAddressTypeService;
import in.lms.cca.service.IAuditAgencyEmailService;
import in.lms.cca.service.IAuditAgencyMobileService;
import in.lms.cca.service.IAuditAgencyService;
import in.lms.cca.service.IAuditorsService;
import in.lms.cca.service.ICityService;
import in.lms.cca.service.ICountryService;
import in.lms.cca.service.IStateService;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditAgencyServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;
        



@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditAgencyController {
	
	@Autowired
	private IStateService stateServ;
	
	@Autowired
	private ICountryService countryServ;
	
	@Autowired
	private ICityService cityServ;
	
	@Autowired
	private IAddressTypeService addressTypeServ;
	
	@Autowired
	private IAuditAgencyEmailService auditAgencyEmailServ;
	
	@Autowired
	private IAuditAgencyMobileService auditAgencyMobileServ;
	
	@Autowired
	private IAuditorsService AuditorServ;
	
	@Autowired
	private IAddressService addressServ;
	
	@Autowired
	private IAuditAgencyService  auditAgencyServ;
	
	@Autowired
	private IAuditorsService  auditorsServ;
	
	@Autowired
	private IUserLoginService userLoginServ;
	

	
	@PostMapping(AuditAgencyServiceAPIs.ADD_AUDIT_AGENCY)
	public ResponseEntity<String> addAuditAgency(@RequestBody AuditAgencyDTO auditAgencyObj) {

	    // Server Side Validation

	    if (auditAgencyObj.getBlockNo() == null || auditAgencyObj.getBlockNo().isEmpty()) {
	        return new ResponseEntity<>("Please enter block number.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getBlockNo().trim().matches("^[A-Za-z0-9- ]+$")) {
	        return new ResponseEntity<>("Only alphabets, numbers, -, and spaces are allowed in the block number.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getBlockNo().length() < 3 || auditAgencyObj.getBlockNo().length() > 15) {
	        return new ResponseEntity<>("The length of the block number must be between 3 and 15 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getVillage() == null || auditAgencyObj.getVillage().isEmpty()) {
	        return new ResponseEntity<>("Please enter village name.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getVillage().trim().matches("^[A-Za-z0-9 ]+$")) {
	        return new ResponseEntity<>("Only alphabets, numbers, and spaces are allowed in the village name.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getVillage().length() < 3 || auditAgencyObj.getVillage().length() > 30) {
	        return new ResponseEntity<>("The length of the village name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getPostOffice() == null || auditAgencyObj.getPostOffice().isEmpty()) {
	        return new ResponseEntity<>("Please enter post office.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getPostOffice().trim().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Only alphabets and spaces are allowed in the post office.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getPostOffice().length() < 3 || auditAgencyObj.getPostOffice().length() > 30) {
	        return new ResponseEntity<>("The length of the post office must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getSubDivision() == null || auditAgencyObj.getSubDivision().isEmpty()) {
	        return new ResponseEntity<>("Please enter sub division.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getSubDivision().trim().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Only alphabets and spaces are allowed in the subdivision.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getSubDivision().length() < 3 || auditAgencyObj.getSubDivision().length() > 30) {
	        return new ResponseEntity<>("The length of the sub division must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getCountry() == null || auditAgencyObj.getCountry().isEmpty()) {
	        return new ResponseEntity<>("Please select a country.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getCity() == null || auditAgencyObj.getCity().isEmpty()) {
	        return new ResponseEntity<>("Please select a city.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getState() == null || auditAgencyObj.getState().isEmpty()) {
	        return new ResponseEntity<>("Please select a state.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getPin() == null || auditAgencyObj.getPin().isEmpty()) {
	        return new ResponseEntity<>("Please enter PIN.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getPin().matches("^[0-9]{6}$")) {
	        return new ResponseEntity<>("PIN must be 6 digits.", HttpStatus.BAD_REQUEST);
	    }

	    // Email Validation
	    if (auditAgencyObj.getEmailId() != null) {
	        for (AuditAgencyEmailDTO emailDTO : auditAgencyObj.getEmailId()) {
	            if (emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
	            	
	                return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
	                
	            }else if(emailDTO.getEmail().length() < 3 || emailDTO.getEmail().length() > 50) {
	            	
	            	return new ResponseEntity<>("The length of the email id must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	            }
	            
	            else if (!emailDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            	
	                return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
	                
	            }
	           
	        }
	    }
	    

	    // Mobile Validation
	    if (auditAgencyObj.getPhoneRecord() != null) {
	        for (AuditAgencyMobileDTO mobileDTO : auditAgencyObj.getPhoneRecord()) {
	        	
	        	if(mobileDTO.getContactType() == null || mobileDTO.getContactType().isEmpty())
	        		return new ResponseEntity<>("Please select contact type.", HttpStatus.BAD_REQUEST);
	        	
	        	if(mobileDTO.getContactType().equals("telephone")) {
	        		
	        		if(mobileDTO.getAreaCode() == null || mobileDTO.getAreaCode().isEmpty())
	        			return new ResponseEntity<>("Please enter area code.", HttpStatus.BAD_REQUEST);
	        		else if(mobileDTO.getAreaCode().length()<2 || mobileDTO.getAreaCode().length()>3)
	        			return new ResponseEntity<>("Area code can be of 2 or 3 digits.", HttpStatus.BAD_REQUEST);
	        		else if(!mobileDTO.getAreaCode().matches("^[0-9]+$")) {
	        			return new ResponseEntity<>("Area code can be digits only.", HttpStatus.BAD_REQUEST);
	        		}
	        		
	        		if (mobileDTO.getMobile() == null || mobileDTO.getMobile().isEmpty()) {
		                return new ResponseEntity<>("Please enter telephone number.", HttpStatus.BAD_REQUEST);
	        		}else if(mobileDTO.getMobile().length() < 7 || mobileDTO.getMobile().length() > 8) {
		        			return new ResponseEntity<>("The length of telephone number should be 7 or 8.", HttpStatus.BAD_REQUEST);    
		            } else if (!mobileDTO.getMobile().matches("^[1-9][0-9]+$")) {
		                return new ResponseEntity<>("Telephone number cannot start with 0.", HttpStatus.BAD_REQUEST);
		            }	
	        		
	        	}else if(mobileDTO.getContactType().equals("mobile")){
	        		
	        		if (mobileDTO.getMobile() == null || mobileDTO.getMobile().isEmpty()) {
		                return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
	        		}else if(mobileDTO.getMobile().length() != 10) {
		        			return new ResponseEntity<>("Mobile number must be of 10 digits.", HttpStatus.BAD_REQUEST);    
		            } else if (!mobileDTO.getMobile().matches("^[6-9][0-9]{9}$")) {
		                return new ResponseEntity<>("Only numbers are allowed starting with 6,7,8 and 9.", HttpStatus.BAD_REQUEST);
		            }	
	        	}
	        	
	            
	        }
	        
	        
	        
	    }
	    
	    
	    // Auditors Validation
	    if (auditAgencyObj.getAuditors() != null) {
	        for (AuditorsDTO auditorsDTO : auditAgencyObj.getAuditors()) {
	            if (auditorsDTO.getSalutation() == null || auditorsDTO.getSalutation().isEmpty()) {
	                return new ResponseEntity<>("Please enter salutation.", HttpStatus.BAD_REQUEST);
	            }

	            if (auditorsDTO.getFirstName() == null || auditorsDTO.getFirstName().isEmpty()) {
	                return new ResponseEntity<>("Please enter first name.", HttpStatus.BAD_REQUEST);
	            } else if (!auditorsDTO.getFirstName().trim().matches("^[A-Za-z]+$")) {
	                return new ResponseEntity<>("Only alphabets are allowed in the first name.", HttpStatus.BAD_REQUEST);
	            } else if (auditorsDTO.getFirstName().length() < 3 || auditorsDTO.getFirstName().length() > 30) {
	                return new ResponseEntity<>("The length of the first name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	            }
	            
	            
	            if (auditorsDTO.getMiddleName() == null || auditorsDTO.getMiddleName().isEmpty()) {
	                
	            } else if (!auditorsDTO.getMiddleName().trim().matches("^[A-Za-z]+$")) {
	                return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
	            } else if (auditorsDTO.getMiddleName().length() < 3 || auditorsDTO.getMiddleName().length() > 30) {
	                return new ResponseEntity<>("The length of the middle name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	            }

	            if (auditorsDTO.getLastName() == null || auditorsDTO.getLastName().isEmpty()) {
	                
	            } else if (!auditorsDTO.getLastName().trim().matches("^[A-Za-z]+$")) {
	                return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
	            } else if (auditorsDTO.getLastName().length() < 3 || auditorsDTO.getLastName().length() > 45) {
	                return new ResponseEntity<>("The length of the last name must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
	            }
	        }
	    }

	    if (auditAgencyObj.getEffectiveFrom() == null || auditAgencyObj.getEffectiveFrom().trim().isEmpty()) {
	        return new ResponseEntity<>("Please enter effective from date.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getEffectiveFrom().matches("\\d{4}-\\d{2}-\\d{2}")) {
	        return new ResponseEntity<>("Effective from date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getEffectiveTo() == null || auditAgencyObj.getEffectiveTo().trim().isEmpty()) {
	        return new ResponseEntity<>("Please enter effective to date.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getEffectiveTo().matches("\\d{4}-\\d{2}-\\d{2}")) {
	        return new ResponseEntity<>("Effective to date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
	    }

	    // Check for Unique State Name
	    State cobj = stateServ.getStateName(auditAgencyObj.getState().trim());
	    if (cobj != null) {
	        return new ResponseEntity<>("Duplicate state name is not allowed.", HttpStatus.BAD_REQUEST);
	    }

	    try {
	        // Address
	        Address newAddress = new Address();
	        String countryId = EncryptionUtil.decrypt(auditAgencyObj.getCountry());
	        Country country = countryServ.getCountryById(Long.parseLong(countryId));
	        newAddress.setCountryId(country);

	        String stateId = EncryptionUtil.decrypt(auditAgencyObj.getState());
	        State state = stateServ.getStateById(Long.parseLong(stateId));
	        newAddress.setStateId(state);

	        String cityId = EncryptionUtil.decrypt(auditAgencyObj.getCity());
	        City city = cityServ.getCityById(Long.parseLong(cityId));
	        newAddress.setCityId(city);
	        newAddress.setPincode(auditAgencyObj.getPin());
	        newAddress.setAddressTypeId(addressTypeServ.getAddressTypeByName("Permanent"));
	        newAddress.setBlockNo(auditAgencyObj.getBlockNo());
	        newAddress.setSubDivision(auditAgencyObj.getSubDivision());
	        newAddress.setPostOffice(auditAgencyObj.getPostOffice());
	        newAddress.setVillage(auditAgencyObj.getVillage());
	        Optional<Address> addressObj = addressServ.addAddress(newAddress);

	        AuditAgency newAuditAgency = new AuditAgency();
	        newAuditAgency.setAddressId(addressObj.get());

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Date effectiveFromParsed = dateFormat.parse(auditAgencyObj.getEffectiveFrom());
	            Date effectiveToParsed = dateFormat.parse(auditAgencyObj.getEffectiveTo());
  
	            newAuditAgency.setEffectiveFrom(effectiveFromParsed);
	            newAuditAgency.setEffectiveTo(effectiveToParsed);
	            
	        }catch (ParseException e) {
	            return new ResponseEntity<>("Error parsing dates", HttpStatus.BAD_REQUEST);
	        }
	        newAuditAgency.setAgencyName(auditAgencyObj.getAuditAgencyName());
	        newAuditAgency.setStatus("Inactive");

	        Optional<AuditAgency> auditAgency = auditAgencyServ.addAuditAgency(newAuditAgency);

	        for (AuditAgencyMobileDTO auditAgencyMobileDTO : auditAgencyObj.getPhoneRecord()) {
	        	
	            AuditAgencyMobile newAuditAgencyMobile = new AuditAgencyMobile();
	            
	            newAuditAgencyMobile.setAuditAgencyId(auditAgency.get());
	            newAuditAgencyMobile.setAreaCode(auditAgencyMobileDTO.getAreaCode());
	            newAuditAgencyMobile.setContactType(auditAgencyMobileDTO.getContactType());
	            newAuditAgencyMobile.setContactNo(auditAgencyMobileDTO.getMobile());
	            newAuditAgencyMobile.setStatus("Active");
	            auditAgencyMobileServ.addAuditAgencyMobile(newAuditAgencyMobile);
	        }

	        for (int i = 0; i < auditAgencyObj.getEmailId().size(); i++) {
	            AuditAgencyEmailDTO auditAgencyEmailDTO = auditAgencyObj.getEmailId().get(i);
	            AuditAgencyEmail newAuditAgencyEmail = new AuditAgencyEmail();

	            newAuditAgencyEmail.setAuditAgencyId(newAuditAgency);
	            newAuditAgencyEmail.setEmail(auditAgencyEmailDTO.getEmail());

	            // Set the first email as primary
	            if (i == 0) {
	                newAuditAgencyEmail.setEmailType("Primary");
	            }else {
	            	newAuditAgencyEmail.setEmailType("Alternative");
	            }

	            newAuditAgencyEmail.setStatus("Active");
	            auditAgencyEmailServ.addAuditAgencyEmail(newAuditAgencyEmail);
	        }


	        for (AuditorsDTO auditorsDTO : auditAgencyObj.getAuditors()) {
	            Auditors newAuditors = new Auditors();
	            newAuditors.setAuditAgencyId(auditAgency.get());
	            newAuditors.setSalutation(auditorsDTO.getSalutation());
	            newAuditors.setFirstName(auditorsDTO.getFirstName());
	            newAuditors.setMiddleName(auditorsDTO.getMiddleName());
	            newAuditors.setLastName(auditorsDTO.getLastName());
	            newAuditors.setStatus("Active");
	            AuditorServ.addAuditAuditors(newAuditors);
	        }

	        return new ResponseEntity<>("Audit Agency Successfully Added", HttpStatus.OK);

	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while adding the audit agency.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	

	@GetMapping(AuditAgencyServiceAPIs.GET_ALL_AUDIT_AGENCY)
	public ResponseEntity<?> getAllAuditAgency() {
	    // Fetch all audit agencies
	    List<AuditAgency> auditAgencyList = auditAgencyServ.getAllAuditAgency();

	    // Prepare the list to hold DTOs
	    List<AuditAgencyDTO> auditAgencyDTOList = new ArrayList<>();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    // Iterate over the list of audit agencies
	    for (AuditAgency auditAgency : auditAgencyList) {
	        // Create DTO for each audit agency
	        AuditAgencyDTO auditAgencyDTO = new AuditAgencyDTO();
	        
	        // Populate DTO fields
	        auditAgencyDTO.setAuditAgencyId(String.valueOf(auditAgency.getAuditAgencyId()));
	        auditAgencyDTO.setAuditAgencyName(auditAgency.getAgencyName());

	        // Convert Date to String using SimpleDateFormat
	        String effectiveFromStr = auditAgency.getEffectiveFrom() != null 
	            ? dateFormat.format(auditAgency.getEffectiveFrom()) 
	            : null;
	        String effectiveToStr = auditAgency.getEffectiveTo() != null 
	            ? dateFormat.format(auditAgency.getEffectiveTo()) 
	            : null;
	        
	        auditAgencyDTO.setEffectiveFrom(effectiveFromStr);
	        auditAgencyDTO.setEffectiveTo(effectiveToStr);
	        auditAgencyDTO.setStatus(auditAgency.getStatus());
	        auditAgencyDTO.setCreatedBy(auditAgency.getCreatedBy());

	        // Fetch auditors for the current audit agency
	        List<Auditors> auditorsList = auditorsServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());
	        
	        // Fetch address using addressId
            Long addressId = auditAgency.getAddressId().getAddressId(); // Ensure this returns a Long
            Address address = addressServ.getAddressById(addressId);

            if (address != null) {
                auditAgencyDTO.setBlockNo(address.getBlockNo());
                auditAgencyDTO.setVillage(address.getVillage());
                auditAgencyDTO.setPostOffice(address.getPostOffice());
                auditAgencyDTO.setSubDivision(address.getSubDivision());

                // Convert Country, State, and City objects to Strings
                String countryName = address.getCountryId() != null ? address.getCountryId().getCountryName() : null;
                String stateName = address.getStateId() != null ? address.getStateId().getStateName() : null;
                String cityName = address.getCityId() != null ? address.getCityId().getCityName() : null;

                auditAgencyDTO.setCountry(countryName);
                auditAgencyDTO.setState(stateName);
                auditAgencyDTO.setCity(cityName);
                auditAgencyDTO.setPin(address.getPincode());
            }

	        // Map auditors to DTO
	        List<AuditorsDTO> auditorsDTOList = auditorsList.stream()
	            .map(auditor -> {
	                AuditorsDTO auditorsDTO = new AuditorsDTO();
	                auditorsDTO.setSalutation(auditor.getSalutation());
	                auditorsDTO.setFirstName(auditor.getFirstName());
	                auditorsDTO.setMiddleName(auditor.getMiddleName());
	                auditorsDTO.setLastName(auditor.getLastName());
	                return auditorsDTO;
	            })
	            .collect(Collectors.toList());

	        // Set the auditors in the DTO
	        auditAgencyDTO.setAuditors(auditorsDTOList);

	        // Fetch email records for the current audit agency
	        List<AuditAgencyEmail> auditAgencyEmailList = auditAgencyEmailServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());

	        // Map emails to DTO
	        List<AuditAgencyEmailDTO> auditAgencyEmailDTOList = auditAgencyEmailList.stream()
	            .map(auditAgencyEmail -> {
	                AuditAgencyEmailDTO auditAgencyEmailDTO = new AuditAgencyEmailDTO();
	                auditAgencyEmailDTO.setEmailType(auditAgencyEmail.getEmailType());
	                auditAgencyEmailDTO.setEmail(auditAgencyEmail.getEmail());
	                return auditAgencyEmailDTO;
	            })
	            .collect(Collectors.toList());

	        // Set the email list in the DTO
	        auditAgencyDTO.setEmailId(auditAgencyEmailDTOList);

	        // Fetch mobile records for the current audit agency
	        List<AuditAgencyMobile> auditAgencyMobileList = auditAgencyMobileServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());

	        System.out.println(auditAgencyMobileList);
	        // Map mobile records to DTO
	        List<AuditAgencyMobileDTO> auditAgencyMobileDTOList = auditAgencyMobileList.stream()
	            .map(auditAgencyMobile -> {
	                AuditAgencyMobileDTO auditAgencyMobileDTO = new AuditAgencyMobileDTO();
	                auditAgencyMobileDTO.setContactType(auditAgencyMobile.getContactType());
	                
	                auditAgencyMobileDTO.setAreaCode(auditAgencyMobile.getAreaCode());
	                auditAgencyMobileDTO.setMobile(auditAgencyMobile.getContactNo());
	                return auditAgencyMobileDTO;
	            })
	            .collect(Collectors.toList());


	        // Set the mobile records in the DTO
	        auditAgencyDTO.setPhoneRecord(auditAgencyMobileDTOList);
	        
	        auditAgencyDTOList.add(auditAgencyDTO);
	    }

	    // Return the response with the list of DTOs
	    return new ResponseEntity<>(auditAgencyDTOList, HttpStatus.OK);
	}

	@PostMapping(AuditAgencyServiceAPIs.UPDATE_AUDIT_AGENCY)
	public ResponseEntity<String> updateAuditAgency(@RequestBody AuditAgencyDTO auditAgencyObj) {

		// Server Side Validation

	    if (auditAgencyObj.getBlockNo() == null || auditAgencyObj.getBlockNo().isEmpty()) {
	        return new ResponseEntity<>("Please enter block number.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getBlockNo().trim().matches("^[A-Za-z0-9- ]+$")) {
	        return new ResponseEntity<>("Only alphabets, numbers, -, and spaces are allowed in the block number.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getBlockNo().length() < 3 || auditAgencyObj.getBlockNo().length() > 15) {
	        return new ResponseEntity<>("The length of the block number must be between 3 and 15 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getVillage() == null || auditAgencyObj.getVillage().isEmpty()) {
	        return new ResponseEntity<>("Please enter village name.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getVillage().trim().matches("^[A-Za-z0-9 ]+$")) {
	        return new ResponseEntity<>("Only alphabets, numbers, and spaces are allowed in the village name.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getVillage().length() < 3 || auditAgencyObj.getVillage().length() > 30) {
	        return new ResponseEntity<>("The length of the village name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getPostOffice() == null || auditAgencyObj.getPostOffice().isEmpty()) {
	        return new ResponseEntity<>("Please enter post office.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getPostOffice().trim().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Only alphabets and spaces are allowed in the post office.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getPostOffice().length() < 3 || auditAgencyObj.getPostOffice().length() > 30) {
	        return new ResponseEntity<>("The length of the post office must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getSubDivision() == null || auditAgencyObj.getSubDivision().isEmpty()) {
	        return new ResponseEntity<>("Please enter subdivision.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getSubDivision().trim().matches("^[A-Za-z ]+$")) {
	        return new ResponseEntity<>("Only alphabets and spaces are allowed in the subdivision.", HttpStatus.BAD_REQUEST);
	    } else if (auditAgencyObj.getSubDivision().length() < 3 || auditAgencyObj.getSubDivision().length() > 30) {
	        return new ResponseEntity<>("The length of the subdivision must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getCountry() == null || auditAgencyObj.getCountry().isEmpty()) {
	        return new ResponseEntity<>("Please select a country.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getCity() == null || auditAgencyObj.getCity().isEmpty()) {
	        return new ResponseEntity<>("Please select a city.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getState() == null || auditAgencyObj.getState().isEmpty()) {
	        return new ResponseEntity<>("Please select a state.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getPin() == null || auditAgencyObj.getPin().isEmpty()) {
	        return new ResponseEntity<>("Please enter PIN.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getPin().matches("^[0-9]{6}$")) {
	        return new ResponseEntity<>("PIN must be 6 digits.", HttpStatus.BAD_REQUEST);
	    }  
	    
	 // Email Validation
	    if (auditAgencyObj.getEmailId() != null) {
	        for (AuditAgencyEmailDTO emailDTO : auditAgencyObj.getEmailId()) {
	            if (emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
	            	
	                return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
	                
	            }else if(emailDTO.getEmail().length() < 3 || emailDTO.getEmail().length() > 50) {
	            	
	            	return new ResponseEntity<>("The length of the email id must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	            }
	            
	            else if (!emailDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            	
	                return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
	                
	            }
	           
	        }
	    }
	    

	    // Mobile Validation
	    if (auditAgencyObj.getPhoneRecord() != null) {
	        for (AuditAgencyMobileDTO mobileDTO : auditAgencyObj.getPhoneRecord()) {
	        	
	        	if(mobileDTO.getContactType() == null || mobileDTO.getContactType().isEmpty())
	        		return new ResponseEntity<>("Please select contact type.", HttpStatus.BAD_REQUEST);
	        	
	        	if(mobileDTO.getContactType().equals("telephone")) {
	        		
	        		if(mobileDTO.getAreaCode() == null || mobileDTO.getAreaCode().isEmpty())
	        			return new ResponseEntity<>("Please enter area code.", HttpStatus.BAD_REQUEST);
	        		else if(mobileDTO.getAreaCode().length()<2 || mobileDTO.getAreaCode().length()>3)
	        			return new ResponseEntity<>("Area code can be of 2 or 3 digits.", HttpStatus.BAD_REQUEST);
	        		else if(!mobileDTO.getAreaCode().matches("^[0-9]+$")) {
	        			return new ResponseEntity<>("Area code can be digits only.", HttpStatus.BAD_REQUEST);
	        		}
	        		
	        		if (mobileDTO.getMobile() == null || mobileDTO.getMobile().isEmpty()) {
		                return new ResponseEntity<>("Please enter telephone number.", HttpStatus.BAD_REQUEST);
	        		}else if(mobileDTO.getMobile().length() < 7 || mobileDTO.getMobile().length() > 8) {
		        			return new ResponseEntity<>("The length of telephone number should be 7 or 8.", HttpStatus.BAD_REQUEST);    
		            } else if (!mobileDTO.getMobile().matches("^[1-9][0-9]+$")) {
		                return new ResponseEntity<>("Telephone number cannot start with 0.", HttpStatus.BAD_REQUEST);
		            }	
	        		
	        	}else if(mobileDTO.getContactType().equals("mobile")){
	        		
	        		if (mobileDTO.getMobile() == null || mobileDTO.getMobile().isEmpty()) {
		                return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
	        		}else if(mobileDTO.getMobile().length() != 10) {
		        			return new ResponseEntity<>("Mobile number must be of 10 digits.", HttpStatus.BAD_REQUEST);    
		            } else if (!mobileDTO.getMobile().matches("^[6-9][0-9]{9}$")) {
		                return new ResponseEntity<>("Only numbers are allowed starting with 6,7,8 and 9.", HttpStatus.BAD_REQUEST);
		            }	
	        	}
	        	
	            
	        }
	        
	        
	        
	    }
	    
	    
	    // Auditors Validation
	    if (auditAgencyObj.getAuditors() != null) {
	        for (AuditorsDTO auditorsDTO : auditAgencyObj.getAuditors()) {
	            if (auditorsDTO.getSalutation() == null || auditorsDTO.getSalutation().isEmpty()) {
	                return new ResponseEntity<>("Please enter salutation.", HttpStatus.BAD_REQUEST);
	            }

	            if (auditorsDTO.getFirstName() == null || auditorsDTO.getFirstName().isEmpty()) {
	                return new ResponseEntity<>("Please enter first name.", HttpStatus.BAD_REQUEST);
	            } else if (!auditorsDTO.getFirstName().trim().matches("^[A-Za-z]+$")) {
	                return new ResponseEntity<>("Only alphabets are allowed in the first name.", HttpStatus.BAD_REQUEST);
	            } else if (auditorsDTO.getFirstName().length() < 3 || auditorsDTO.getFirstName().length() > 30) {
	                return new ResponseEntity<>("The length of the first name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	            }
	            
	            
	            if (auditorsDTO.getMiddleName() == null || auditorsDTO.getMiddleName().isEmpty()) {
	                
	            } else if (!auditorsDTO.getMiddleName().trim().matches("^[A-Za-z]+$")) {
	                return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
	            } else if (auditorsDTO.getMiddleName().length() < 3 || auditorsDTO.getMiddleName().length() > 30) {
	                return new ResponseEntity<>("The length of the middle name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	            }

	            if (auditorsDTO.getLastName() == null || auditorsDTO.getLastName().isEmpty()) {
	                
	            } else if (!auditorsDTO.getLastName().trim().matches("^[A-Za-z]+$")) {
	                return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
	            } else if (auditorsDTO.getLastName().length() < 3 || auditorsDTO.getLastName().length() > 45) {
	                return new ResponseEntity<>("The length of the last name must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
	            }
	        }
	    }

	    if (auditAgencyObj.getEffectiveFrom() == null || auditAgencyObj.getEffectiveFrom().trim().isEmpty()) {
	        return new ResponseEntity<>("Please enter effective from date.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getEffectiveFrom().matches("\\d{4}-\\d{2}-\\d{2}")) {
	        return new ResponseEntity<>("Effective from date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
	    }

	    if (auditAgencyObj.getEffectiveTo() == null || auditAgencyObj.getEffectiveTo().trim().isEmpty()) {
	        return new ResponseEntity<>("Please enter effective to date.", HttpStatus.BAD_REQUEST);
	    } else if (!auditAgencyObj.getEffectiveTo().matches("\\d{4}-\\d{2}-\\d{2}")) {
	        return new ResponseEntity<>("Effective to date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
	    }

	    // Check for Unique State Name
	    State cobj = stateServ.getStateName(auditAgencyObj.getState().trim());
	    if (cobj != null) {
	        return new ResponseEntity<>("Duplicate state name is not allowed.", HttpStatus.BAD_REQUEST);
	    }

	    try {
	    	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        // Address
	        Address newAddress = new Address();

	        String countryId = EncryptionUtil.decrypt(auditAgencyObj.getCountry());
	        Country country = countryServ.getCountryById(Long.parseLong(countryId));
	        newAddress.setCountryId(country);

	        String stateId = EncryptionUtil.decrypt(auditAgencyObj.getState());
	        State state = stateServ.getStateById(Long.parseLong(stateId));
	        newAddress.setStateId(state);

	        String cityId = EncryptionUtil.decrypt(auditAgencyObj.getCity());
	        City city = cityServ.getCityById(Long.parseLong(cityId));
	        newAddress.setCityId(city);
	        
	        newAddress.setAddressId(Long.valueOf(auditAgencyObj.getAddressId()));
	        newAddress.setAddressTypeId(addressTypeServ.getAddressTypeByName("Permanent"));
	        newAddress.setBlockNo(auditAgencyObj.getBlockNo());
	        newAddress.setSubDivision(auditAgencyObj.getSubDivision());
	        newAddress.setPostOffice(auditAgencyObj.getPostOffice());
	        newAddress.setVillage(auditAgencyObj.getVillage());
	        newAddress.setPincode(auditAgencyObj.getPin());
	        Date date=new Date();
	        newAddress.setUpdated(date);
	        Date created = dateFormat.parse(auditAgencyObj.getCreated());
	        newAddress.setCreated(created);
	        Optional<Address> addressObj = addressServ.updateAddress(newAddress);
	 
	        AuditAgency newAuditAgency = new AuditAgency();
	        newAuditAgency.setAddressId(addressObj.get());

	      
	        try {
	            Date effectiveFromParsed = dateFormat.parse(auditAgencyObj.getEffectiveFrom());
	            Date effectiveToParsed = dateFormat.parse(auditAgencyObj.getEffectiveTo());

	            newAuditAgency.setEffectiveFrom(effectiveFromParsed);
	            newAuditAgency.setEffectiveTo(effectiveToParsed);
	        }catch (ParseException e) {
	            return new ResponseEntity<>("Error parsing dates", HttpStatus.BAD_REQUEST);
	        }
	        newAuditAgency.setAgencyName(auditAgencyObj.getAuditAgencyName());
	        
	        String decryptedAuditAgencyId = EncryptionUtil.decrypt(auditAgencyObj.getAuditAgencyId());
	        
	        AuditAgency a = auditAgencyServ.getAuditAgencyById(Long.valueOf(decryptedAuditAgencyId));
	        
	        newAuditAgency.setAuditAgencyId(Long.valueOf(decryptedAuditAgencyId)); 
	        newAuditAgency.setUpdated(date);
	        newAuditAgency.setCreated(created);
	        newAuditAgency.setStatus(a.getStatus());

	        Optional<AuditAgency> auditAgency = auditAgencyServ.updateAddress(newAuditAgency);

	        // Step 1: Collect the IDs of the mobile records to be updated
	        List<Long> updatedMobileIds = new ArrayList<>();

	        for (AuditAgencyMobileDTO auditAgencyMobileDTO : auditAgencyObj.getPhoneRecord()) {
	            AuditAgencyMobile newAuditAgencyMobile = new AuditAgencyMobile();

	            // If auditAgencyMobileDTO.getAuditAgencyMobileId() is not null, it's an update; otherwise, it's a new record
	            if (auditAgencyMobileDTO.getAuditAgencyMobileId() != null) {
	                newAuditAgencyMobile.setAgencyMobileId(Long.valueOf(auditAgencyMobileDTO.getAuditAgencyMobileId()));
	                updatedMobileIds.add(Long.valueOf(auditAgencyMobileDTO.getAuditAgencyMobileId()));
	            }

	            newAuditAgencyMobile.setAuditAgencyId(auditAgency.get());
	            newAuditAgencyMobile.setAreaCode(auditAgencyMobileDTO.getAreaCode());
	            newAuditAgencyMobile.setContactType(auditAgencyMobileDTO.getContactType());
	            newAuditAgencyMobile.setContactNo(auditAgencyMobileDTO.getMobile());
	            newAuditAgencyMobile.setUpdated(date);
	            newAuditAgencyMobile.setCreated(created);
	            newAuditAgencyMobile.setStatus("Active");

	            auditAgencyMobileServ.updateAuditAgencyMobile(newAuditAgencyMobile);
	        }

	        // Step 2: Fetch existing mobile records and set status to "Inactive" for those not in the updated list
	        List<AuditAgencyMobile> existingMobiles = auditAgencyMobileServ.findByAuditAgencyId(auditAgency.get().getAuditAgencyId());

	        for (AuditAgencyMobile existingMobile : existingMobiles) {
	            if (!updatedMobileIds.contains(existingMobile.getAgencyMobileId())) {
	                existingMobile.setStatus("Inactive");
	                auditAgencyMobileServ.updateAuditAgencyMobile(existingMobile);
	            }
	        }

	        // Step 1: Fetch all existing email records for the AuditAgency
	        List<AuditAgencyEmail> existingEmails = auditAgencyEmailServ.findByAuditAgencyId(newAuditAgency.getAuditAgencyId());

	        // Step 2: Iterate over the DTOs and update or create records
	        Set<Long> updatedEmailIds = new HashSet<>();
	        for (AuditAgencyEmailDTO auditAgencyEmailDTO : auditAgencyObj.getEmailId()) {
	            AuditAgencyEmail newAuditAgencyEmail;

	            if (auditAgencyEmailDTO.getAuditAgencyEmailId() != null && !auditAgencyEmailDTO.getAuditAgencyEmailId().isEmpty()) {
	                // Fetch existing email record for update
	                newAuditAgencyEmail = auditAgencyEmailServ.getAuditAgencyEmailById(Long.valueOf(auditAgencyEmailDTO.getAuditAgencyEmailId()));
	                       

	                // Update existing email record
	                newAuditAgencyEmail.setEmailType(auditAgencyEmailDTO.getEmailType());
	                newAuditAgencyEmail.setEmail(auditAgencyEmailDTO.getEmail());
	                newAuditAgencyEmail.setUpdated(date);
	                newAuditAgencyEmail.setStatus("Active");

	                // Track the updated email ID
	                updatedEmailIds.add(newAuditAgencyEmail.getAgencyEmailId());

	                // Update the email in the database
	                auditAgencyEmailServ.updateAuditAgencyEmail(newAuditAgencyEmail);

	            } else {
	                // Create a new email record if auditAgencyEmailId is not present
	                newAuditAgencyEmail = new AuditAgencyEmail();

	                newAuditAgencyEmail.setAuditAgencyId(newAuditAgency);
	                newAuditAgencyEmail.setEmailType(auditAgencyEmailDTO.getEmailType());
	                newAuditAgencyEmail.setEmail(auditAgencyEmailDTO.getEmail());
	                newAuditAgencyEmail.setCreated(created);
	                newAuditAgencyEmail.setUpdated(date);
	                newAuditAgencyEmail.setStatus("Active");

	                // Save the new email record in the database
	                auditAgencyEmailServ.addAuditAgencyEmail(newAuditAgencyEmail);

	                // Track the new email ID
	                updatedEmailIds.add(newAuditAgencyEmail.getAgencyEmailId());
	            }
	        }

	        // Step 3: Set the status to "Inactive" for any remaining records not updated
	        for (AuditAgencyEmail existingEmail : existingEmails) {
	            if (!updatedEmailIds.contains(existingEmail.getAgencyEmailId())) {
	                existingEmail.setStatus("Inactive");
	                existingEmail.setUpdated(date);
	                auditAgencyEmailServ.updateAuditAgencyEmail(existingEmail);
	            }
	        }


	        // Step 1: Collect the IDs of the auditors to be updated
	        List<Long> updatedAuditorsIds = new ArrayList<>();

	        for (AuditorsDTO auditorsDTO : auditAgencyObj.getAuditors()) {
	            Auditors newAuditors = new Auditors();
	            
	            // If auditorsDTO.getAuditorsId() is null, it's a new record; otherwise, it's an update
	            if (auditorsDTO.getAuditorsId() != null && !auditorsDTO.getAuditorsId().isEmpty()) {
	            	newAuditors = AuditorServ.getAuditorById(Long.valueOf(auditorsDTO.getAuditorsId()));
	                newAuditors.setAuditorsId(Long.valueOf(auditorsDTO.getAuditorsId()));
	                newAuditors.setFirstName(auditorsDTO.getFirstName());
	                newAuditors.setMiddleName(auditorsDTO.getMiddleName());
	                newAuditors.setLastName(auditorsDTO.getLastName());
	                newAuditors.setCreated(created);
	                newAuditors.setUpdated(date);
	                newAuditors.setStatus("Active");
	                updatedAuditorsIds.add(Long.valueOf(auditorsDTO.getAuditorsId()));
	                AuditorServ.updateAuditors(newAuditors);
	            }else {

	            newAuditors.setAuditAgencyId(auditAgency.get());
	            newAuditors.setSalutation(auditorsDTO.getSalutation());
	            newAuditors.setFirstName(auditorsDTO.getFirstName());
	            newAuditors.setMiddleName(auditorsDTO.getMiddleName());
	            newAuditors.setLastName(auditorsDTO.getLastName());
	            newAuditors.setUpdated(date);
	            newAuditors.setCreated(created);
	            newAuditors.setStatus("Active");

	            AuditorServ.addAuditAuditors(newAuditors);
	        }
	        }
	        // Step 2: Fetch existing auditors and set status to "Inactive" for those not in the updated list
	        List<Auditors> existingAuditors = AuditorServ.findByAuditAgencyId(auditAgency.get().getAuditAgencyId());

	        for (Auditors existingAuditor : existingAuditors) {
	            if (!updatedAuditorsIds.contains(existingAuditor.getAuditorsId())) {
	                existingAuditor.setStatus("Inactive");
	                existingAuditor.setUpdated(date);
	                AuditorServ.updateAuditors(existingAuditor);
	            }
	        }


	        return new ResponseEntity<>("Audit Agency Successfully Updated", HttpStatus.OK);

	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while updated the audit agency.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	@GetMapping(AuditAgencyServiceAPIs.GET_AUDIT_AGENCY_BY_ID)
	public ResponseEntity<?> getAuditAgencyById(@RequestParam("id") String auditAgencyId) {
	    
	    String id = EncryptionUtil.decrypt(auditAgencyId);
	    // Fetch the audit agency by ID
	    AuditAgency auditAgency = auditAgencyServ.getAuditAgencyById(Long.valueOf(id));

	    if (auditAgency == null) {
	        return new ResponseEntity<>("Audit Agency not found", HttpStatus.NOT_FOUND);
	    }

	    // Create DTO for the audit agency
	    AuditAgencyDTO auditAgencyDTO = new AuditAgencyDTO();

	    // Populate DTO fields
	    auditAgencyDTO.setAuditAgencyId(String.valueOf(auditAgency.getAuditAgencyId()));
	    auditAgencyDTO.setAuditAgencyName(auditAgency.getAgencyName());

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    // Convert Date to String using SimpleDateFormat
	    String effectiveFromStr = auditAgency.getEffectiveFrom() != null 
	        ? dateFormat.format(auditAgency.getEffectiveFrom()) 
	        : null;
	    String effectiveToStr = auditAgency.getEffectiveTo() != null 
	        ? dateFormat.format(auditAgency.getEffectiveTo()) 
	        : null;

	    auditAgencyDTO.setEffectiveFrom(effectiveFromStr);
	    auditAgencyDTO.setEffectiveTo(effectiveToStr);
	    auditAgencyDTO.setStatus(auditAgency.getStatus());
	    auditAgencyDTO.setCreated(String.valueOf(auditAgency.getCreated()));
	    auditAgencyDTO.setUpdated(String.valueOf(auditAgency.getUpdated()));
	    auditAgencyDTO.setCreatedBy(auditAgency.getCreatedBy());

	    // Fetch auditors for the current audit agency
	    List<Auditors> auditorsList = auditorsServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());

	    // Map auditors to DTO and filter by status
	    List<AuditorsDTO> auditorsDTOList = auditorsList.stream()
	        .filter(auditor -> "Active".equals(auditor.getStatus()))  // Filter by status
	        .map(auditor -> {
	            AuditorsDTO auditorsDTO = new AuditorsDTO();
	            auditorsDTO.setSalutation(auditor.getSalutation());
	            auditorsDTO.setFirstName(auditor.getFirstName());
	            auditorsDTO.setMiddleName(auditor.getMiddleName());
	            auditorsDTO.setLastName(auditor.getLastName());
	            auditorsDTO.setAuditorsId(String.valueOf(auditor.getAuditorsId()));
	            auditorsDTO.setStatus(String.valueOf(auditor.getStatus()));
	            System.out.println(auditorsDTO);
	            return auditorsDTO;
	        })
	        .collect(Collectors.toList());

	    // Set the auditors in the DTO
	    auditAgencyDTO.setAuditors(auditorsDTOList);

	    // Fetch address using addressId
	    Address address = auditAgency.getAddressId() != null ? addressServ.getAddressById(auditAgency.getAddressId().getAddressId()) : null;

	    if (address != null) {
	        auditAgencyDTO.setAddressId(String.valueOf(address.getAddressId()));
	        auditAgencyDTO.setAuditAgencyId(auditAgencyId);
	        auditAgencyDTO.setBlockNo(address.getBlockNo());
	        auditAgencyDTO.setVillage(address.getVillage());
	        auditAgencyDTO.setPostOffice(address.getPostOffice());
	        auditAgencyDTO.setSubDivision(address.getSubDivision());

	        // Convert Country, State, and City objects to Strings
	        Long countryId = address.getCountryId() != null ? address.getCountryId().getCountryId() : null;
	        Long stateId = address.getStateId() != null ? address.getStateId().getStateId() : null;
	        Long cityId = address.getCityId() != null ? address.getCityId().getCityId() : null;

	        auditAgencyDTO.setCountry(String.valueOf(countryId));
	        auditAgencyDTO.setState(String.valueOf(stateId));
	        auditAgencyDTO.setCity(String.valueOf(cityId));
	        auditAgencyDTO.setPin(address.getPincode());
	    }

	    // Fetch email records for the current audit agency
	    List<AuditAgencyEmail> auditAgencyEmailList = auditAgencyEmailServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());

	    // Map emails to DTO and filter by status
	    List<AuditAgencyEmailDTO> auditAgencyEmailDTOList = auditAgencyEmailList.stream()
	        .filter(email -> "Active".equals(email.getStatus()))  // Filter by status
	        .map(email -> {
	            AuditAgencyEmailDTO auditAgencyEmailDTO = new AuditAgencyEmailDTO();
	            auditAgencyEmailDTO.setAuditAgencyEmailId(String.valueOf(email.getAgencyEmailId()));
	            auditAgencyEmailDTO.setStatus(String.valueOf(email.getStatus()));
	            auditAgencyEmailDTO.setEmailType(email.getEmailType());
	            
	            auditAgencyEmailDTO.setEmail(email.getEmail());
	            return auditAgencyEmailDTO;
	        })
	        .collect(Collectors.toList());

	    // Set the email list in the DTO
	    auditAgencyDTO.setEmailId(auditAgencyEmailDTOList);

	    // Fetch mobile records for the current audit agency
	    List<AuditAgencyMobile> auditAgencyMobileList = auditAgencyMobileServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());

	    // Map mobile records to DTO and filter by status
	    List<AuditAgencyMobileDTO> auditAgencyMobileDTOList = auditAgencyMobileList.stream()
	        .filter(mobile -> "Active".equals(mobile.getStatus()))  // Filter by status
	        .map(mobile -> {
	            AuditAgencyMobileDTO auditAgencyMobileDTO = new AuditAgencyMobileDTO();
	            auditAgencyMobileDTO.setAuditAgencyMobileId(String.valueOf(mobile.getAgencyMobileId()));
	            auditAgencyMobileDTO.setStatus(String.valueOf(mobile.getStatus()));
	            auditAgencyMobileDTO.setContactType(mobile.getContactType());
	            auditAgencyMobileDTO.setAreaCode(mobile.getAreaCode());
	            
	            auditAgencyMobileDTO.setMobile(mobile.getContactNo());
	            return auditAgencyMobileDTO;
	        })
	        .collect(Collectors.toList());

	    // Set the mobile records in the DTO
	    auditAgencyDTO.setPhoneRecord(auditAgencyMobileDTOList);

	    // Return the response with the DTO
	    return new ResponseEntity<>(auditAgencyDTO, HttpStatus.OK);
	}
	
	@GetMapping(AuditAgencyServiceAPIs.DOWNLOAD_FILE)
	public ResponseEntity<byte[]> downloadPdf() {
	    List<AuditAgency> auditAgencyList = auditAgencyServ.getAllAuditAgency();

	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    StringBuilder contentBuilder = new StringBuilder();

	    contentBuilder.append("<html>");
	    contentBuilder.append("<head>");
	    contentBuilder.append("<style>");
	    contentBuilder.append("body { font-size: 12px; }"); // Set font size to 12px
	    contentBuilder.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
	    contentBuilder.append("th, td { border: 1px solid black; padding: 10px; text-align: left; }");
	    contentBuilder.append("h2, h3 { margin: 0; margin-top:4px; text-align: center} p { margin: 0; margin-top:4px; text-align: justify; text-justify: inter-word;}");
	    contentBuilder.append("</style>");
	    contentBuilder.append("</head>");
	    contentBuilder.append("<body>");

	    // Add the header section
	    contentBuilder.append("<div style='padding: 10px;'>");
	    contentBuilder.append("<h2>Controller of Certifying Authorities (CCA)</h2>");
	    contentBuilder.append("<h3>Details of the Empanelled Audit Agency</h3>");
	    contentBuilder.append("<div style='text-align: right;'>");

	    // Find the earliest effectiveFrom and latest effectiveTo dates
	    Date earliestEffectiveFrom = null;
	    Date latestEffectiveTo = null;

	    for (AuditAgency auditAgency : auditAgencyList) {
	        Date effectiveFrom = auditAgency.getEffectiveFrom();
	        Date effectiveTo = auditAgency.getEffectiveTo();

	        if (effectiveFrom != null) {
	            if (earliestEffectiveFrom == null || effectiveFrom.before(earliestEffectiveFrom)) {
	                earliestEffectiveFrom = effectiveFrom;
	            }
	        }

	        if (effectiveTo != null) {
	            if (latestEffectiveTo == null || effectiveTo.after(latestEffectiveTo)) {
	                latestEffectiveTo = effectiveTo;
	            }
	        }
	    }

	    contentBuilder.append("<h4>Effective Date: ");
	    if (earliestEffectiveFrom != null && latestEffectiveTo != null) {
	        contentBuilder.append(dateFormat.format(earliestEffectiveFrom)).append(" to ").append(dateFormat.format(latestEffectiveTo));
	    } else {
	        contentBuilder.append("N/A");
	    }
	    contentBuilder.append("</h4>");

	    contentBuilder.append("</div>");
	    contentBuilder.append("</div>");

	    // Add the main content section
	    contentBuilder.append("<p>* The empanel agency should ensure that the notified team of auditors (as per the records submitted/updated by the agency to the office of CCA)\r\n"
	            + "shall only conduct the Audit. The audit conducted by any other person/agency will not be accepted.</p>");

	    contentBuilder.append("<p>** The empanel agency should ensure that, in case of any addition and deletion in the auditor’s team during the course of empanelment, the\r\n"
	            + "empanel agency is requested to contact O/o CCA before deployment/taking up any new assignment with new person.</p>");

	    // Add the table structure
	    contentBuilder.append("<table>");
	    contentBuilder.append("<tr>");
	    contentBuilder.append("<th>Sl. No.</th>");
	    contentBuilder.append("<th>Name & Address of the applicant organisation</th>");
	    contentBuilder.append("<th>Telephone/Mobile Numbers</th>");
	    contentBuilder.append("<th>Email Id</th>");
	    contentBuilder.append("<th>Auditor</th>");
	    contentBuilder.append("</tr>");

	    int index = 1;
	    for (AuditAgency auditAgency : auditAgencyList) {
	        contentBuilder.append("<tr>");
	        contentBuilder.append("<td>").append(index++).append("</td>");

	        // Address details
	        Long addressId = auditAgency.getAddressId().getAddressId();
	        Address address = addressServ.getAddressById(addressId);
	        if (address != null) {
	            String countryName = address.getCountryId() != null ? address.getCountryId().getCountryName() : "";
	            String stateName = address.getStateId() != null ? address.getStateId().getStateName() : "";
	            String cityName = address.getCityId() != null ? address.getCityId().getCityName() : "";
	            contentBuilder.append("<td>")
	                .append(auditAgency.getAgencyName()).append("<br>")
	                .append(address.getVillage()).append(", ")
	                .append(address.getSubDivision()).append(", ")
	                .append(address.getBlockNo()).append(", ")
	                .append(cityName).append(", ")
	                .append(stateName).append(", ")
	                .append(countryName).append(" - ")
	                .append(address.getPincode())
	                .append("</td>");
	        } else {
	            contentBuilder.append("<td>N/A</td>");
	        }

	        List<AuditAgencyMobile> auditAgencyMobileList = auditAgencyMobileServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());
	        contentBuilder.append("<td>");

	        for (int i = 0; i < auditAgencyMobileList.size(); i++) {
	            AuditAgencyMobile mobile = auditAgencyMobileList.get(i);
	            
	            // Append area code if present, otherwise just the mobile number
	            if (mobile.getAreaCode() != null && !mobile.getAreaCode().isEmpty()) {
	                contentBuilder.append(mobile.getAreaCode()).append("-");
	            }
	            contentBuilder.append(mobile.getContactNo());

	            // Append a comma and line break only if it's not the last element in the list
	            if (i < auditAgencyMobileList.size() - 1) {
	                contentBuilder.append(",<br>");
	            }
	        }
	        contentBuilder.append("</td>");

	        // Email Id(s)
	        List<AuditAgencyEmail> auditAgencyEmailList = auditAgencyEmailServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());
	        contentBuilder.append("<td>");

	        int size = auditAgencyEmailList.size();
	        for (int i = 0; i < size; i++) {
	            AuditAgencyEmail email = auditAgencyEmailList.get(i);
	            contentBuilder.append(email.getEmail());
	            if (i < size - 1) {
	                contentBuilder.append(",<br> ");
	            }
	            
	        }

	        contentBuilder.append("</td>");

	        // Auditors
	        List<Auditors> auditorsList = auditorsServ.findByAuditAgencyId(auditAgency.getAuditAgencyId());
	        contentBuilder.append("<td>");

	        int sizes = auditorsList.size();
	        for (int i = 0; i < sizes; i++) {
	            Auditors auditor = auditorsList.get(i);
	            String fullName = (auditor.getFirstName() != null ? auditor.getFirstName() + " " : "")
	                            + (auditor.getMiddleName() != null ? auditor.getMiddleName() + " " : "")
	                            + (auditor.getLastName() != null ? auditor.getLastName() : "");
	            contentBuilder.append(fullName);

	            // Append a comma only if it's not the last element
	            if (i < sizes - 1) {
	                contentBuilder.append(", ");
	            }
	            contentBuilder.append("<br>");
	        }

	        contentBuilder.append("</td>");
	        contentBuilder.append("</tr>");
	    }

	    contentBuilder.append("</table>");
	    contentBuilder.append("</div>");
	    contentBuilder.append("</body>");
	    contentBuilder.append("</html>");

	    String htmlContent = contentBuilder.toString();

	    byte[] pdfBytes;
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        // Convert HTML to PDF
	        ConverterProperties converterProperties = new ConverterProperties();
	        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	        pdfBytes = outputStream.toByteArray();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }

	    // Return the generated PDF as a downloadable file
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=auditAgency.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);
	}
	
	
	//Verify audit agency account
	@GetMapping(AuditAgencyServiceAPIs.CHANGE_AUDIT_AGENCY_STATUS)
	public void changeIntentStatus(@RequestParam("id") String intentId, @RequestParam("qid") String username) {
	    String id = EncryptionUtil.decrypt(intentId);

	    try {
	    	System.out.println("id=-=->"+id);
	        AuditAgency c = auditAgencyServ.getAuditAgencyById(Long.parseLong(id));
	        
	        System.out.println("c=--->"+c.getStatus());

	        List<AuditAgencyEmail> auditAgencyEmailList = auditAgencyEmailServ.findByAuditAgencyId(c.getAuditAgencyId());
	        List<AuditAgencyMobile> auditAgencyMobileList = auditAgencyMobileServ.findByAuditAgencyId(c.getAuditAgencyId());

	        UserLoginDTO u = new UserLoginDTO();
	        u.setCreatedBy(username);
	        u.setUpdatedBy(username);
	        
	        
	        if (!auditAgencyEmailList.isEmpty()) {
	            u.setEmailId(String.valueOf(auditAgencyEmailList.get(0).getEmail())); 
	        }
	        
	       
	        if (!auditAgencyMobileList.isEmpty()) {
	            u.setMobile(auditAgencyMobileList.get(0).getContactNo());
	        }
	        
	        u.setFirstName(c.getAgencyName());
	        u.setStatus("Active");
	        u.setUserId(EncryptionUtil.encrypt(c.getAuditAgencyId().toString()));
	        
	        try {
				userLoginServ.addUserAuditAgency(u);
			} catch (Exception ex) {
				// TODO: handle exception
				System.out.println("User creation skipped (likely exists): " + ex.getMessage());
			}
	        
	       
	        c.setStatus("Active");
	        auditAgencyServ.updateAddress(c);
	    } catch (Exception e) {
	        
	        e.printStackTrace();
	    }
	}


	/**
	 * Get audit agency by username (createdBy field) or by agency name
	 * Used to look up the numeric audit_agency_id from a user's encrypted username
	 */
	@GetMapping(AuditAgencyServiceAPIs.GET_AUDIT_AGENCY_BY_USERNAME)
	public ResponseEntity<?> getAuditAgencyByUsername(@PathVariable String username) {
		try {
			// First try by createdBy (encrypted username)
			AuditAgency auditAgency = auditAgencyServ.getAuditAgencyByCreatedBy(username);
			
			// If not found by createdBy, try by agency name 
			// (matches user's first_name which is the agency name)
			if (auditAgency == null) {
				// We need to get the user's first_name from auth-service
				// For now, as a fallback, try decrypting the username and use it as agency name
				try {
					String decryptedName = EncryptionUtil.decrypt(username);
					auditAgency = auditAgencyServ.getAuditAgencyByAgencyName(decryptedName);
					System.out.println("Tried finding by decrypted name: " + decryptedName + ", found: " + (auditAgency != null));
				} catch (Exception ex) {
					System.out.println("Failed to decrypt username for agency name lookup: " + ex.getMessage());
				}
			}
			
			if (auditAgency == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No audit agency found for username: " + username);
			}
			
			// Return just the audit_agency_id as a string
			return ResponseEntity.ok(String.valueOf(auditAgency.getAuditAgencyId()));
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching audit agency: " + e.getMessage());
		}
	}

	/**
	 * Get audit agency by agency name
	 * Used to look up audit_agency_id when user's first_name matches agency_name
	 */
	@GetMapping(AuditAgencyServiceAPIs.GET_AUDIT_AGENCY_BY_NAME)
	public ResponseEntity<?> getAuditAgencyByName(@PathVariable String agencyName) {
		try {
			System.out.println("Looking up audit agency by name: " + agencyName);
			AuditAgency auditAgency = auditAgencyServ.getAuditAgencyByAgencyName(agencyName);
			
			if (auditAgency == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No audit agency found with name: " + agencyName);
			}
			
			System.out.println("Found audit_agency_id: " + auditAgency.getAuditAgencyId());
			return ResponseEntity.ok(String.valueOf(auditAgency.getAuditAgencyId()));
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching audit agency by name: " + e.getMessage());
		}
	}

}
