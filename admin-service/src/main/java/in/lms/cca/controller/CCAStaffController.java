package in.lms.cca.controller;

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

import in.lms.cca.dto.CCAStaffDTO;
import in.lms.cca.entity.CCAStaff;
import in.lms.cca.service.impl.CCAStaffServiceImpl;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CCAStaffServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class CCAStaffController {

	@Autowired
	private CCAStaffServiceImpl ccaserv;
	
	
	@PostMapping(CCAStaffServiceAPIs.ADD_CCA_STAFF)
    public ResponseEntity<?> addNewIntent(@RequestBody CCAStaffDTO staff) {
	  
        // Server Side Validation

        if (staff.getSalutation().isEmpty()) {
            return new ResponseEntity<>("Please enter salutation.", HttpStatus.BAD_REQUEST);
        }

        if (staff.getFirstName().isEmpty()) {
            return new ResponseEntity<>("Please enter first name.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getFirstName().trim().matches("^[A-Za-z]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the first name.", HttpStatus.BAD_REQUEST);
        } else if (staff.getFirstName().length() < 3 || staff.getFirstName().length() > 30) {
            return new ResponseEntity<>("The length of the first name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
        }
        
        if (staff.getMiddleName().isEmpty()) {
            
        } else if (!staff.getMiddleName().trim().matches("^[A-Za-z]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the middle name.", HttpStatus.BAD_REQUEST);
        } else if (staff.getMiddleName().length() < 3 || staff.getMiddleName().length() > 30) {
            return new ResponseEntity<>("The length of the middle name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
        }


        if (staff.getLastName().isEmpty()) {
            
        } else if (!staff.getLastName().trim().matches("^[A-Za-z]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
        } else if (staff.getLastName().length() < 3 || staff.getLastName().length() > 45) {
            return new ResponseEntity<>("The length of the last name must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
        }

        if (staff.getMobileNo().isEmpty()) {
            return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getMobileNo().matches("^[0-9]{10}$")) {
            return new ResponseEntity<>("Mobile number must be 10 digits.", HttpStatus.BAD_REQUEST);
        }

        if (staff.getEmailId().isEmpty()) {
            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
        }else if (staff.getEmailId().length() < 3 || staff.getEmailId().length() > 50) {
            return new ResponseEntity<>("The length of the email id must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }
        
        if (staff.getDesignation().isEmpty()) {
            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getDesignation().trim().matches("^[A-Za-z ]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the designation.", HttpStatus.BAD_REQUEST);
        } else if (staff.getDesignation().length() < 3 || staff.getDesignation().length() > 50) {
            return new ResponseEntity<>("The length of the designation must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }
        
        
        //check for unique email id
        
        CCAStaff cobj1 = ccaserv.getCCAStaffByEmailId(staff.getEmailId());
        
        if(cobj1 != null) {
        	return new ResponseEntity<>("Email Id already exists.", HttpStatus.BAD_REQUEST);
        }
        
        
        //check for unique mobile number
        

        CCAStaff cobj2 = ccaserv.getCCAStaffByMobileNo(staff.getMobileNo());
        
        if(cobj2 != null) {
        	return new ResponseEntity<>("Mobile Number already exists.", HttpStatus.BAD_REQUEST);
        }
        
     

        try {
        	         
            CCAStaff cca = new CCAStaff();
            cca.setCreatedBy(staff.getCreatedBy());
            cca.setUpdatedBy(staff.getUpdatedBy());
            cca.setDesignation(staff.getDesignation());
            cca.setEmailId(staff.getEmailId());
            cca.setFirstName(staff.getFirstName());
            cca.setMiddleName(staff.getMiddleName());
            cca.setLastName(staff.getLastName());
            cca.setMobileNo(staff.getMobileNo());
            cca.setSalutation(staff.getSalutation());
            cca.setStatus("Inactive");
            
            Optional<CCAStaff> c = ccaserv.addCCAStaff(cca);
            
            if (c.isEmpty()) {
                return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("CCA staff has been successfully added.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
        }
    }
	
	
	@PostMapping(CCAStaffServiceAPIs.UPDATE_CCA_STAFF)
    public ResponseEntity<?> updateNewIntent(@RequestBody CCAStaffDTO staff) {
	  
        // Server Side Validation

        if (staff.getSalutation().isEmpty()) {
            return new ResponseEntity<>("Please enter salutation.", HttpStatus.BAD_REQUEST);
        }

        if (staff.getFirstName().isEmpty()) {
            return new ResponseEntity<>("Please enter first name.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getFirstName().trim().matches("^[A-Za-z]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the first name.", HttpStatus.BAD_REQUEST);
        } else if (staff.getFirstName().length() < 3 || staff.getFirstName().length() > 30) {
            return new ResponseEntity<>("The length of the first name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
        }
        
        if (staff.getMiddleName().isEmpty()) {
            
        } else if (!staff.getMiddleName().trim().matches("^[A-Za-z]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the middle name.", HttpStatus.BAD_REQUEST);
        } else if (staff.getMiddleName().length() < 3 || staff.getMiddleName().length() > 30) {
            return new ResponseEntity<>("The length of the middle name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
        }


        if (staff.getLastName().isEmpty()) {
            
        } else if (!staff.getLastName().trim().matches("^[A-Za-z]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
        } else if (staff.getLastName().length() < 3 || staff.getLastName().length() > 45) {
            return new ResponseEntity<>("The length of the last name must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
        }

        if (staff.getMobileNo().isEmpty()) {
            return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getMobileNo().matches("^[0-9]{10}$")) {
            return new ResponseEntity<>("Mobile number must be 10 digits.", HttpStatus.BAD_REQUEST);
        }

        if (staff.getEmailId().isEmpty()) {
            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
        }else if (staff.getEmailId().length() < 3 || staff.getEmailId().length() > 50) {
            return new ResponseEntity<>("The length of the email id must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }
        
        if (staff.getDesignation().isEmpty()) {
            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
        } else if (!staff.getDesignation().trim().matches("^[A-Za-z ]+$")) {
            return new ResponseEntity<>("Only alphabets are allowed in the designation.", HttpStatus.BAD_REQUEST);
        } else if (staff.getDesignation().length() < 3 || staff.getDesignation().length() > 50) {
            return new ResponseEntity<>("The length of the designation must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }
        
        
        Long staffId = Long.parseLong(EncryptionUtil.decrypt(staff.getStaffId()));
        
        //check for unique email id
        
        CCAStaff cobj1 = ccaserv.getCCAStaffByEmailId(staff.getEmailId());
        
        if(cobj1 != null && cobj1.getStaffId() != staffId) {
        	return new ResponseEntity<>("Email Id already exists.", HttpStatus.BAD_REQUEST);
        }
        
        
        //check for unique mobile number
        

        CCAStaff cobj2 = ccaserv.getCCAStaffByMobileNo(staff.getMobileNo());
        
        if(cobj2 != null && cobj2.getStaffId() != staffId) {
        	return new ResponseEntity<>("Mobile Number already exists.", HttpStatus.BAD_REQUEST);
        }
        
        CCAStaff cobj = ccaserv.getCCAStaffById(staffId);

        try {
        	
        	cobj.setUpdatedBy(staff.getUpdatedBy());
        	cobj.setDesignation(staff.getDesignation());
        	cobj.setEmailId(staff.getEmailId());
        	cobj.setFirstName(staff.getFirstName());
        	cobj.setMiddleName(staff.getMiddleName());
        	cobj.setLastName(staff.getLastName());
        	cobj.setMobileNo(staff.getMobileNo());
        	cobj.setSalutation(staff.getSalutation());
            
            
            Optional<CCAStaff> c = ccaserv.addCCAStaff(cobj);
            
            if (c.isEmpty()) {
                return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("CCA staff has been successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
        }
    }
	
	
	@GetMapping(CCAStaffServiceAPIs.GET_ALL_CCA_STAFF)
	public ResponseEntity<?> getAllCCAStaff(){
		
		List<CCAStaff> ccaList =ccaserv.getAllCCAStaff();
		return new ResponseEntity<>(ccaList, HttpStatus.OK); 
		
	}
	
	@GetMapping(CCAStaffServiceAPIs.GET_CCA_STAFF_BY_ID)
	public ResponseEntity<?> getCCAStaffByID(@RequestParam("id") String staffId)
	{
		String id = EncryptionUtil.decrypt(staffId);
		
		System.out.println("--------->"+id);
		
		try {
			CCAStaff staffObj = ccaserv.getCCAStaffById(Long.parseLong(id));
			return new ResponseEntity<>(staffObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid CCA Staff Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
}
