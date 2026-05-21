package in.lms.cca.controller;

import java.util.List;

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

import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.UserLoginAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class UserLoginController {
	
	
	@Autowired
	private IUserLoginService userLoginServ;
	
	
	 @PostMapping(UserLoginAPIs.ASSIGN_NEW_USER_BY_INTENT)
	    public ResponseEntity<?> assignNewRole(@RequestBody UserLoginDTO newUser) {
		 
		 
	        try {
	        	userLoginServ.addUser(newUser);
	            return new ResponseEntity<>("User created successfully", HttpStatus.OK);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return new ResponseEntity<>("Failed to create user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	    }
	
	
	 @GetMapping(UserLoginAPIs.CHANGE_USER_STATUS_BY_ID)
	    public ResponseEntity<?> changeRoleStatusById(@RequestParam("id") String userId) {
	        try {
	        	System.out.println("abc12==>"+userId);
	        	String decryptedId = EncryptionUtil.decrypt(userId);
	        	userLoginServ.changeLoginStatusById(decryptedId);
	            return new ResponseEntity<>("User status changed successfully", HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Failed to change user status: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	    }


	 @PostMapping(UserLoginAPIs.ASSIGN_NEW_USER_BY_AUDIT_AGENCY)
	    public ResponseEntity<?> assignAuditAgencyNewRole(@RequestBody UserLoginDTO newUser) {
		 System.out.println(newUser);
		 
	        try {
	        	userLoginServ.addUserAuditAgency(newUser);
	            return new ResponseEntity<>("User created successfully", HttpStatus.OK);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return new ResponseEntity<>("Failed to create user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	    }
	
	
	 @GetMapping(UserLoginAPIs.CHANGE_USER_AUDIT_AGENCY_STATUS_BY_ID)
	    public ResponseEntity<?> changeAuditAgencyRoleStatusById(@RequestParam("id") String userId) {
		 System.out.println(userId);
	        try {
	        	 String decryptedId = EncryptionUtil.decrypt(userId);
	        	userLoginServ.changeAuditAgencyRoleStatusById(decryptedId);
	            return new ResponseEntity<>("User status changed successfully", HttpStatus.OK);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return new ResponseEntity<>("Failed to change user status: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	    }
	 
	 @GetMapping(UserLoginAPIs.GET_ALL_USERS)
	    public ResponseEntity<List<UserLoginDTO>> getAllRoles() {
	        List<UserLoginDTO> roles = userLoginServ.getAllUserLogins();
	        return new ResponseEntity<>(roles, HttpStatus.OK);
	    }
	 
	 @GetMapping(UserLoginAPIs.GET_ALL_INTENT_USERS)
	    public ResponseEntity<List<UserLoginDTO>> getAllIntentRoles() {
	        List<UserLoginDTO> roles = userLoginServ.getAllIntentUserLogins();
	        return new ResponseEntity<>(roles, HttpStatus.OK);
	    }
}
