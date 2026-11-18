package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

import in.lms.cca.dto.GetRoles;
import in.lms.cca.dto.RoleMasterDTO;
import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.entity.RoleMaster;
import in.lms.cca.service.IRoleMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.RoleMasterServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class RoleMasterController {
	
	@Autowired
	private IRoleMasterService roleServ;
	
	
	@PostMapping(RoleMasterServiceAPIs.ADD_NEW_ROLE)
	public ResponseEntity<?> addNewRoleMaster(@RequestBody RoleMasterDTO roleMasterObj){
		
		//Server Side Validation
		
		if(roleMasterObj.getRoleName().equals("")) {
			return new ResponseEntity<>("Please enter role.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getRoleName().trim().matches("^[A-Za-z_]+$")) {
			return new ResponseEntity<>("Only alphabets and underscore are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getRoleName().length() < 3 || roleMasterObj.getRoleName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(roleMasterObj.getDescription().equals("")) {
			return new ResponseEntity<>("Please enter role name.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getDescription().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getDescription().length() < 3 || roleMasterObj.getDescription().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(roleMasterObj.getPath().equals("")) {
			return new ResponseEntity<>("Please enter path.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getPath().trim().matches("^[A-Za-z/:]+$")) {
			return new ResponseEntity<>("Only alphabets, /, and : are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getPath().length() < 3 || roleMasterObj.getPath().length() > 100) {
			return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(roleMasterObj.getHomePage().equals("")) {
			return new ResponseEntity<>("Please enter home page path.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getHomePage().trim().matches("^[A-Za-z/:]+$")) {
			return new ResponseEntity<>("Only alphabets, /, and : are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getHomePage().length() < 3 || roleMasterObj.getHomePage().length() > 100) {
			return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		}
				
		//Check for Unique RoleMaster Name
		RoleMaster cobj = roleServ.getRoleByName(roleMasterObj.getRoleName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Role name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		//Check for Unique path
		RoleMaster cobj1 = roleServ.getRoleMasterByPath(roleMasterObj.getPath().trim());
		if(cobj1 != null) {
			return new ResponseEntity<>("Duplicate Path is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		//Check for Unique Home Page Path
			RoleMaster cobj2 = roleServ.getRoleMasterByHomePage(roleMasterObj.getHomePage().trim());
			if(cobj2 != null) {
				return new ResponseEntity<>("Duplicate Path for Home Page is not allowed.", HttpStatus.BAD_REQUEST);
			}
		
		
		try {
			
			RoleMaster newRoleMaster = new RoleMaster();
			newRoleMaster.setDescription(roleMasterObj.getDescription());
			newRoleMaster.setHomePage(roleMasterObj.getHomePage());
			newRoleMaster.setPath(roleMasterObj.getPath());
			newRoleMaster.setRoleName(roleMasterObj.getRoleName());
			newRoleMaster.setStatus("Active");
			
			Optional<RoleMaster> c = roleServ.addRole(newRoleMaster);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the Role. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Role successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the Role. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(RoleMasterServiceAPIs.GET_ALL_ROLE)
	public ResponseEntity<?> getAllRoleMaster(){
		
		List<RoleMaster> roleMasterList = roleServ.getAllRoles();
		return new ResponseEntity<>(roleMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(RoleMasterServiceAPIs.GET_ALL_ACTIVE_ROLE)
	public ResponseEntity<?> getAllActiveRoleMaster(){
		
		List<RoleMaster> roleMasterList = roleServ.getAllActiveRoles();
		return new ResponseEntity<>(roleMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(RoleMasterServiceAPIs.GET_ALL_INACTIVE_ROLE)
	public ResponseEntity<?> getAllInactiveRole(){
		
		List<RoleMaster> roleMasterList = roleServ.getAllInactiveRoles();
		return new ResponseEntity<>(roleMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(RoleMasterServiceAPIs.CHANGE_ROLE_STATUS)
	public void changeRoleMasterStatus(@RequestParam("id") String roleMasterId) {
		
		String id = EncryptionUtil.decrypt(roleMasterId);
		
		try {
			RoleMaster c = roleServ.getRoleById(Integer.parseInt(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			roleServ.updateRole(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(RoleMasterServiceAPIs.GET_ROLE_BY_ID)
	public ResponseEntity<?> getRoleMasterByID(@RequestParam("id") String roleMasterId)
	{
		String id = EncryptionUtil.decrypt(roleMasterId);
		
		try {
			RoleMaster roleMasterObj = roleServ.getRoleById(Integer.parseInt(id));
			return new ResponseEntity<>(roleMasterObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Role Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(RoleMasterServiceAPIs.GET_ROLE_BY_NAME)
	public ResponseEntity<?> getRoleMasterByName(@RequestParam("id") String roleName)
	{
		String rname = EncryptionUtil.decrypt(roleName);
		
		try {
			RoleMaster roleMasterObj = roleServ.getRoleByName(rname);
			return new ResponseEntity<>(roleMasterObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Role Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@PostMapping(RoleMasterServiceAPIs.UPDATE_ROLE)
	public ResponseEntity<?> updateRoleMaster(@RequestBody RoleMasterDTO roleMasterObj){
		
		
		Integer roleid = Integer.parseInt(EncryptionUtil.decrypt(roleMasterObj.getRoleId()));
		
		//Server Side Validation
		
		if(roleMasterObj.getRoleName().equals("")) {
			return new ResponseEntity<>("Please enter role.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getRoleName().trim().matches("^[A-Za-z_]+$")) {
			return new ResponseEntity<>("Only alphabets and underscore are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getRoleName().length() < 3 || roleMasterObj.getRoleName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(roleMasterObj.getDescription().equals("")) {
			return new ResponseEntity<>("Please enter role name.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getDescription().trim().matches("^[A-Za-z_. ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getDescription().length() < 3 || roleMasterObj.getDescription().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(roleMasterObj.getPath().equals("")) {
			return new ResponseEntity<>("Please enter path.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getPath().trim().matches("^[A-Za-z/:]+$")) {
			return new ResponseEntity<>("Only alphabets, /, and : are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getPath().length() < 3 || roleMasterObj.getPath().length() > 100) {
			return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(roleMasterObj.getHomePage().equals("")) {
			return new ResponseEntity<>("Please enter home page path.", HttpStatus.BAD_REQUEST);
		}else if(!roleMasterObj.getHomePage().trim().matches("^[A-Za-z/:]+$")) {
			return new ResponseEntity<>("Only alphabets, /, and : are allowed.", HttpStatus.BAD_REQUEST);
		}else if(roleMasterObj.getHomePage().length() < 3 || roleMasterObj.getHomePage().length() > 100) {
			return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		}	
				
				
				//Check for Unique RoleMaster Name
				RoleMaster cobj = roleServ.getRoleByName(roleMasterObj.getRoleName().trim());
				if(cobj != null && cobj.getRoleId() != roleid) {
					return new ResponseEntity<>("Duplicate Role name is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				//Check for Unique path
				RoleMaster cobj1 = roleServ.getRoleMasterByPath(roleMasterObj.getPath().trim());
				if(cobj1 != null && cobj1.getRoleId() != roleid) {
					return new ResponseEntity<>("Duplicate Path is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				
				//Check for Unique Home Page Path
					RoleMaster cobj2 = roleServ.getRoleMasterByHomePage(roleMasterObj.getHomePage().trim());
					if(cobj2 != null && cobj2.getRoleId() != roleid) {
						return new ResponseEntity<>("Duplicate Path for Home Page is not allowed.", HttpStatus.BAD_REQUEST);
					}
				
		
		try {
			
			RoleMaster uRole = new RoleMaster();
			uRole.setDescription(roleMasterObj.getDescription());
			uRole.setHomePage(roleMasterObj.getHomePage());
			uRole.setPath(roleMasterObj.getPath());
			uRole.setRoleName(roleMasterObj.getRoleName());
			uRole.setStatus(roleMasterObj.getStatus());
			uRole.setRoleId(roleid);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(roleMasterObj.getCreated());	
			uRole.setCreated(cdate);
			
			Optional<RoleMaster> c = roleServ.addRole(uRole);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while updating the role. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Role Updated Successfully.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while updating the role. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	@GetMapping(RoleMasterServiceAPIs.GET_ROLE_BY_USERID)
	public ResponseEntity<?> getRoleMasterByUserID(@RequestParam("id") String userId) {
	    String id = EncryptionUtil.decrypt(userId);
	    System.out.println("------>" + id);

	    try {
	        // Fetch the roles for the given userId
	        List<GetRoles> roleMasterObj = roleServ.getRoleByUserId(id);
	        // Fetch all available roles
	        List<RoleMaster> roleMasterList = roleServ.getAllRoles();

	        
	        // Specify valid role names
	        List<String> validRoleNames = List.of("APPLICANT", "ADMIN", "AUDIT_AGENCY", "LICENSEE");

	        List<RoleMaster> filteredRoleMasterList;

	        // Check if roleMasterObj is null or empty
	        if (roleMasterObj == null || roleMasterObj.isEmpty()) {
	            // If roleMasterObj is null or empty, filter roleMasterList based on validRoleNames only
	            filteredRoleMasterList = roleMasterList.stream()
	                    .filter(role -> !validRoleNames.contains(role.getRoleName())) // Exclude roles in validRoleNames
	                    .collect(Collectors.toList());
	        } else {
	            // If roleMasterObj is not null or empty, create a set of roles for filtering
	        	 Set<String> rolesFromMasterObj = roleMasterObj.stream()
	                     .map(GetRoles::getRoleName) // Assuming GetRoles has a method getRoles() that returns the role name
	                     .map(role -> role.replace("ROLE_", "")) // Remove "ROLE_" prefix from each role
	                     .collect(Collectors.toSet());

	            System.out.println("=-=--->"+rolesFromMasterObj);
	            
	            // Filter roleMasterList to exclude both validRoleNames and roles in roleMasterObj
	            filteredRoleMasterList = roleMasterList.stream()
	                    .filter(role -> !validRoleNames.contains(role.getRoleName()) // Exclude roles in validRoleNames
	                            && !rolesFromMasterObj.contains(role.getRoleName())) // Exclude roles present in roleMasterObj
	                    .collect(Collectors.toList());
	            
	            System.out.println("=-=--123->"+filteredRoleMasterList);
	        }

	        return new ResponseEntity<>(filteredRoleMasterList, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace(); // Print stack trace for debugging
	        return new ResponseEntity<>("Invalid Role Id.", HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping(RoleMasterServiceAPIs.ADD_ASSIGN_ROLE)
	public ResponseEntity<?> addAssignedRole(@RequestBody StaffLoginDTO staffLoginDTO) {
	
	    
	    try {
	       
	            roleServ.addAssignRole(staffLoginDTO);
	        
	            return new ResponseEntity<>("Assigned Role Successfull", HttpStatus.OK); 
	    } catch(Exception e) {
	        e.printStackTrace(); // Print stack trace for debugging
	        return new ResponseEntity<>("Invalid Assigned Role Id.", HttpStatus.BAD_REQUEST);
	    }
	}

	
	@GetMapping(RoleMasterServiceAPIs.GET_DETAILS_BY_USERID)
	public ResponseEntity<?> getDetailsByUserID(@RequestParam("id") String userId) {
	    String id = EncryptionUtil.decrypt(userId);
	    System.out.println("------>" + id);

	    try {
	        // Fetch the roles for the given userId
	    	   List<GetRoles> roleMasterObj = roleServ.getRoleByUserId(id);
	        // Fetch all available roles
	     
	        return new ResponseEntity<>(roleMasterObj, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace(); // Print stack trace for debugging
	        return new ResponseEntity<>("Invalid Role Id.", HttpStatus.BAD_REQUEST);
	    }
	}

	
	@GetMapping(RoleMasterServiceAPIs.CHANGE_USER_ROLE_STATUS)
	public ResponseEntity<?> changedAssignedRoleStatus(@RequestParam("id") String roleId) {
	
	    
	    try {
	       
	            roleServ.changeUserRoleStatus(roleId);
	        
	            return new ResponseEntity<>("Change Role Stutus Successfull", HttpStatus.OK); 
	    } catch(Exception e) {
	        e.printStackTrace(); // Print stack trace for debugging
	        return new ResponseEntity<>("Invalid Assigned Role Id.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	@GetMapping(RoleMasterServiceAPIs.GET_DETAILS_BY_USERNAME)
	public ResponseEntity<?> getDetailsByUserName(@RequestParam("userName") String userName) {
	    String userNames = EncryptionUtil.decrypt(userName);
	    System.out.println("------>" + userNames);

	    try {
	        // Fetch the roles for the given userId
	    	   List<GetRoles> roleMasterObj = roleServ.getRoleByUserNAme(userNames);
	        // Fetch all available roles
	     
	        return new ResponseEntity<>(roleMasterObj, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace(); // Print stack trace for debugging
	        return new ResponseEntity<>("Invalid Role Id.", HttpStatus.BAD_REQUEST);
	    }
	}
}
