package in.lms.cca.controller;

import java.util.HashMap;
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

import in.lms.cca.dto.IntentUniqueCodeDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.IntentUniqueCode;
import in.lms.cca.service.IApplicationTypeMasterService;
import in.lms.cca.service.IIntentUniqueCodeService;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.service.impl.UserLoginServiceImpl;
import in.lms.cca.util.api.IntentUniqueCodeServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.api.AdminServiceAPIs;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class IntentUniqueCodeController {

	@Autowired
	private IIntentUniqueCodeService userv;
	
	@Autowired
	private IApplicationTypeMasterService mserv;
	
	@Autowired
	private IUserLoginService userLoginServ;
	
	@PostMapping(IntentUniqueCodeServiceAPIs.ADD_INTENT_UNIQUE_CODE)
	public ResponseEntity<?> addIntentUniqueCode(@RequestBody IntentUniqueCodeDTO obj){
		
		
		
		if (obj.getMobileNo().isEmpty()) {
            return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
        } else if (!obj.getMobileNo().matches("^[6-9]\\d{9}$")) {
            return new ResponseEntity<>("Mobile number must starts with 6, 7, 8, or 9. and must be 10 digits long", HttpStatus.BAD_REQUEST);
        }
		
		if (obj.getEmailId().isEmpty()) {
            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
        } else if (!obj.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
        }else if(obj.getEmailId().length() < 2 || obj.getEmailId().length() > 50) {
			return new ResponseEntity<>("The length must be between 2 and 50 characters.", HttpStatus.BAD_REQUEST);
		}	
		
		if(obj.getApplicationType().isEmpty()) {
			return new ResponseEntity<>("Please select application type.", HttpStatus.BAD_REQUEST);
		}
		
		if(!obj.getApplicationType().isEmpty() && !obj.getApplicationType().equals("Individual")) {
			
			if (obj.getOrganizationName().isEmpty()) {
	            return new ResponseEntity<>("Please enter organization name.", HttpStatus.BAD_REQUEST);
	        } else if (!obj.getOrganizationName().trim().matches("^[A-Za-z ]+$")) {
	            return new ResponseEntity<>("Only alphabets and space are allowed in the organization name.", HttpStatus.BAD_REQUEST);
	        } else if (obj.getOrganizationName().length() < 3 || obj.getOrganizationName().length() > 75) {
	            return new ResponseEntity<>("The length of the first name must be between 3 and 75 characters.", HttpStatus.BAD_REQUEST);
	        }
			
		}
		
		//Unique email id
		
		IntentUniqueCode cobj = userv.getUniqueCodeByEmailId(obj.getEmailId().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Email Id is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		//Unique Mobile No
		IntentUniqueCode cobj1 = userv.getUniqueCodeByMobileNo(obj.getMobileNo().trim());
		if(cobj1 != null) {
			return new ResponseEntity<>("Duplicate Mobile Number is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		if(obj.getApplicationType().equals("Individual"))
			obj.setOrganizationName(null);
		else {
			//Unique Organization Name
			IntentUniqueCode cobj2 = userv.getUniqueCodeByOrganizationName(obj.getOrganizationName().trim());
			if(cobj2 != null){
				return new ResponseEntity<>("Duplicate Organization Name is not allowed.", HttpStatus.BAD_REQUEST);
			}
		}
			
		
		
		try {
			
			
			
			IntentUniqueCode u = new IntentUniqueCode();
			u.setEmailId(obj.getEmailId());
			u.setMobileNo(obj.getMobileNo());
			u.setStatus("Active");
			u.setOrganizationName(obj.getOrganizationName());
			u.setAppTypeMasterId(mserv.getApplicationTypeMasterByName(obj.getApplicationType()));
			u.setCreatedBy(obj.getCreatedBy());
			u.setUpdatedBy(obj.getUpdatedBy());
			
			//Generate Unique Code
			Long uniqueCode = null;
			
			while(uniqueCode == null) {
				
				Long ucode = userv.generateUniqueCode();
				IntentUniqueCode u1 = userv.getByUniqueCode(ucode);
				
				if(u1 == null)
					uniqueCode = ucode;
				
			}
			
			
			
			u.setUniqueCode(uniqueCode);
			
			Optional<IntentUniqueCode> ucode = userv.addUniqueCode(u);
			
			 if (ucode.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while generating the unique code. Please Try again later.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>(ucode.get(), HttpStatus.OK);
	            
		} catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while generating the unique code. Please Try again later.", HttpStatus.BAD_REQUEST);
        }
		
		
		
	}
	
	@GetMapping(IntentUniqueCodeServiceAPIs.GET_ACTIVE_INTENT_UNIQUE_CODE_BY_ID)
	public ResponseEntity<?> getById(@RequestParam("id") String id){
		
		String uid = EncryptionUtil.decrypt(id);
		
		IntentUniqueCode obj = userv.getActiveUniqueCodeById(Long.parseLong(uid));
		
		return new ResponseEntity<>(obj, HttpStatus.OK); 
		
	}
	
	
	
	@GetMapping(IntentUniqueCodeServiceAPIs.GET_ALL_INTENT_UNIQUE_CODE)
	public ResponseEntity<?> getAllUniqueCode(){
		
		List<IntentUniqueCode> list = userv.getAllUniqueCode();
		
		List<UserLoginDTO> udto = userLoginServ.getAllUserLoginsAccount();
		
		HashMap<String, String> map = new HashMap<>();
		
		
		
		for(UserLoginDTO d: udto) {
			
			String salutation = d.getSalutation() != null ? d.getSalutation()+" ": "";
			String fname = d.getFirstName() != null ? d.getFirstName()+" ": "";
			String mname = d.getMiddleName() != null ? d.getMiddleName()+" ": "";
			String lname = d.getLastName() != null ? d.getLastName()+" ": "";
			
			String name = salutation + fname + mname + lname;
			String username = EncryptionUtil.decrypt(d.getUserName()) == null ? d.getUserName() : EncryptionUtil.decrypt(d.getUserName());
			
			map.put(username, name);	
		}
		
		for(IntentUniqueCode i: list) {
			
			String createdby = EncryptionUtil.decrypt(i.getCreatedBy()) == null ? i.getCreatedBy() : EncryptionUtil.decrypt(i.getCreatedBy());
			String updatedby = EncryptionUtil.decrypt(i.getUpdatedBy()) == null ? i.getUpdatedBy() : EncryptionUtil.decrypt(i.getUpdatedBy());
			
			
			i.setCreatedBy(map.get(createdby)!=null?map.get(createdby):"NA");
			i.setUpdatedBy(map.get(updatedby)!=null?map.get(updatedby):"NA");
			
			
			
		}
	
		
		
		return new ResponseEntity<>(list, HttpStatus.OK); 
		
	}
	
	@GetMapping(IntentUniqueCodeServiceAPIs.GET_BY_INTENT_UNIQUE_CODE)
	public ResponseEntity<?> getAllIntentByUniqueCode(@RequestParam("id") String uniqueCode){
		
		//Get active intent unique code, not used one
		
		String id = EncryptionUtil.decrypt(uniqueCode);
		
		if (id.isEmpty()) {
            return new ResponseEntity<>("Please enter unique code.", HttpStatus.BAD_REQUEST);
        } else if (!id.matches("^[0-9]+$")) {
            return new ResponseEntity<>("Please enter a valid code.", HttpStatus.BAD_REQUEST);
        }else if (id.length() !=8) {
            return new ResponseEntity<>("Please enter a valid code.", HttpStatus.BAD_REQUEST);
        }
		
		
		IntentUniqueCode uniquecode = userv.getByActiveUniqueCode(Long.parseLong(id));
		
		if(uniquecode != null)
			return new ResponseEntity<>(uniquecode, HttpStatus.OK);
		else
			return new ResponseEntity<>("Invalid code.", HttpStatus.BAD_REQUEST);
	}
	
	
	@PostMapping(IntentUniqueCodeServiceAPIs.UPDATE_INTENT_UNIQUE_CODE)
	public ResponseEntity<?> updateIntentUniqueCode(@RequestBody IntentUniqueCodeDTO obj){
		
		
		
		if (obj.getMobileNo().isEmpty()) {
            return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
        } else if (!obj.getMobileNo().matches("^[6-9]\\d{9}$")) {
            return new ResponseEntity<>("Mobile number must starts with 6, 7, 8, or 9. and must be 10 digits long", HttpStatus.BAD_REQUEST);
        }
		
		if (obj.getEmailId().isEmpty()) {
            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
        } else if (!obj.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
        }else if(obj.getEmailId().length() < 2 || obj.getEmailId().length() > 50) {
			return new ResponseEntity<>("The length must be between 2 and 50 characters.", HttpStatus.BAD_REQUEST);
		}	
		
		if(obj.getApplicationType().isEmpty()) {
			return new ResponseEntity<>("Please select application type.", HttpStatus.BAD_REQUEST);
		}
		
		if(!obj.getApplicationType().isEmpty() && !obj.getApplicationType().equals("Individual")) {
			
			if (obj.getOrganizationName().isEmpty()) {
	            return new ResponseEntity<>("Please enter organization name.", HttpStatus.BAD_REQUEST);
	        } else if (!obj.getOrganizationName().trim().matches("^[A-Za-z ]+$")) {
	            return new ResponseEntity<>("Only alphabets and space are allowed in the organization name.", HttpStatus.BAD_REQUEST);
	        } else if (obj.getOrganizationName().length() < 3 || obj.getOrganizationName().length() > 75) {
	            return new ResponseEntity<>("The length of the first name must be between 3 and 75 characters.", HttpStatus.BAD_REQUEST);
	        }
			
		}
		
		String iuid = EncryptionUtil.decrypt(obj.getUniqueCodeId());
		
		Long unicodeid = Long.parseLong(iuid);
		
		//Unique email id
		
		IntentUniqueCode cobj = userv.getUniqueCodeByEmailId(obj.getEmailId().trim());
		if(cobj != null && unicodeid != cobj.getUniqueCodeId()) {
			return new ResponseEntity<>("Duplicate Email Id is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		//Unique Mobile No
		IntentUniqueCode cobj1 = userv.getUniqueCodeByMobileNo(obj.getMobileNo().trim());
		if(cobj1 != null && unicodeid != cobj1.getUniqueCodeId()) {
			return new ResponseEntity<>("Duplicate Mobile Number is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		//Unique Organization Name
		IntentUniqueCode cobj2 = userv.getUniqueCodeByOrganizationName(obj.getOrganizationName().trim());
		if(cobj2 != null && unicodeid != cobj2.getUniqueCodeId()){
			return new ResponseEntity<>("Duplicate Organization Name is not allowed.", HttpStatus.BAD_REQUEST);
		}
			
		
		
		try {
			
			IntentUniqueCode uobj = userv.getUniqueCodeById(unicodeid);
			
			uobj.setEmailId(obj.getEmailId());
			uobj.setMobileNo(obj.getMobileNo());
			uobj.setOrganizationName(obj.getOrganizationName());
			uobj.setAppTypeMasterId(mserv.getApplicationTypeMasterByName(obj.getApplicationType()));
			uobj.setUpdatedBy(obj.getUpdatedBy());
			
			
			Optional<IntentUniqueCode> ucode = userv.updateUniqueCode(uobj);
			
			 if (ucode.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while generating the unique code. Please Try again later.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("The details has been successfully updated.", HttpStatus.OK);
	            
		} catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while generating the unique code. Please Try again later.", HttpStatus.BAD_REQUEST);
        }
	}
	
	@GetMapping(IntentUniqueCodeServiceAPIs.REGENERATE_INTENT_UNIQUE_CODE_BY_ID)
	public ResponseEntity<?> regenrateUniqueCodeById(@RequestParam("id") String id, @RequestParam("qid") String qid){
		
		String uid = EncryptionUtil.decrypt(id);
		
		
		
		IntentUniqueCode obj = userv.getActiveUniqueCodeById(Long.parseLong(uid));
		
		if(obj.getStatus().equals("Inactive")) {
			return new ResponseEntity<>("You cannot regenerate the unique code as the intent has already been registered.", HttpStatus.BAD_REQUEST);
		}
		
		
		//Generate Unique Code
		Long uniqueCode = null;
		
		while(uniqueCode == null) {
			
			Long ucode = userv.generateUniqueCode();
			IntentUniqueCode u1 = userv.getByActiveUniqueCode(ucode);
			
			if(u1 == null)
				uniqueCode = ucode;
			
		}
		
		obj.setUniqueCode(uniqueCode);
		obj.setUpdatedBy(qid);
		
		Optional<IntentUniqueCode> iu = userv.addUniqueCode(obj);
		
		return new ResponseEntity<>("New Unique Code is: " + iu.get().getUniqueCode(), HttpStatus.OK); 
		
	}
	
	
	
}
