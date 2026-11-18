package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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

import in.lms.cca.dto.RoutesDTO;
import in.lms.cca.dto.SubMenuInternalDTO;
import in.lms.cca.entity.SubMenuInternalMaster;
import in.lms.cca.service.ISubMenuInternalMasterService;
import in.lms.cca.service.ISubMenuMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.SubMenuInternalMasterServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class SubMenuInternalMasterController {

	@Autowired
	private ISubMenuInternalMasterService menuServ;
	
	@Autowired
	private ISubMenuMasterService subMenuMasterServ;
	
	
	
	
	@PostMapping(SubMenuInternalMasterServiceAPIs.ADD_NEW_SUB_MENU_INTERNAL)
	public ResponseEntity<?> addNewSubMenuInternalMaster(@RequestBody SubMenuInternalDTO subMenuInternalObj){
		
		
		
		
		
		//Server Side Validation
		
		if(subMenuInternalObj.getSubMenuInternalName().equals("")) {
			return new ResponseEntity<>("Please enter sub menu name.", HttpStatus.BAD_REQUEST);
		}else if(!subMenuInternalObj.getSubMenuInternalName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(subMenuInternalObj.getSubMenuInternalName().length() < 3 || subMenuInternalObj.getSubMenuInternalName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(subMenuInternalObj.getSubMenuInternalPath().equals("")) {
			return new ResponseEntity<>("Please enter sub menu path.", HttpStatus.BAD_REQUEST);
		}else if(!subMenuInternalObj.getSubMenuInternalPath().trim().matches("^[A-Za-z/:]+$")) {
			return new ResponseEntity<>("Only alphabets,/, and : are allowed.", HttpStatus.BAD_REQUEST);
		}else if(subMenuInternalObj.getSubMenuInternalPath().length() < 3 || subMenuInternalObj.getSubMenuInternalPath().length() > 100) {
			return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(subMenuInternalObj.getTrackerHeading().equals("")) {
			return new ResponseEntity<>("Please enter tracker heading.", HttpStatus.BAD_REQUEST);
		}else if(!subMenuInternalObj.getTrackerHeading().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(subMenuInternalObj.getTrackerHeading().length() < 3 || subMenuInternalObj.getTrackerHeading().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(subMenuInternalObj.getSubMenuId().equals("")) {
			return new ResponseEntity<>("Please select Sub Menu.", HttpStatus.BAD_REQUEST);
		}
		
		
		
		Long submenuid = Long.parseLong(EncryptionUtil.decrypt(subMenuInternalObj.getSubMenuId()));
		
		
		
		
		SubMenuInternalMaster cobj = menuServ.getSubMenuInternalByNameAndSubMenuId(subMenuInternalObj.getSubMenuInternalName().trim(), submenuid);
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Sub Menu internal name is not allowed for same sub menu.", HttpStatus.BAD_REQUEST);
		}
		
		
		SubMenuInternalMaster cobj1 = menuServ.getSubMenuInternalByPathAndSubMenuId(subMenuInternalObj.getSubMenuInternalPath().trim(), submenuid);
		if(cobj1 != null) {
			return new ResponseEntity<>("Duplicate Sub Menu internal path is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			SubMenuInternalMaster newMenuMaster = new SubMenuInternalMaster();
			
			newMenuMaster.setSubMenuInternalName(subMenuInternalObj.getSubMenuInternalName());
			newMenuMaster.setSubMenuInternalPath(subMenuInternalObj.getSubMenuInternalPath());
			newMenuMaster.setSubMenuId(subMenuMasterServ.getSubMenuById(submenuid));
			newMenuMaster.setTrackerHeading(subMenuInternalObj.getTrackerHeading());
			newMenuMaster.setStatus("Active");
			
			Optional<SubMenuInternalMaster> c = menuServ.addSubMenuInternal(newMenuMaster);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the Sub Menu Internal. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Sub Menu Internal successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the Sub Menu. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(SubMenuInternalMasterServiceAPIs.GET_ALL_SUB_MENU_INTERNAL)
	public ResponseEntity<?> getSubAllMenuMaster(){
		
		List<SubMenuInternalMaster> menuMasterList = menuServ.getAllSubMenuInternal();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	
	@GetMapping(SubMenuInternalMasterServiceAPIs.GET_ALL_ACTIVE_SUB_MENU_INTERNAL)
	public ResponseEntity<?> getAllActiveSubMenuInternalMaster(){
		
		List<SubMenuInternalMaster> menuMasterList = menuServ.getAllActiveSubMenuInternal();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(SubMenuInternalMasterServiceAPIs.GET_ALL_INACTIVE_SUB_MENU_INTERNAL)
	public ResponseEntity<?> getAllInactiveSubMenuInternal(){
		
		List<SubMenuInternalMaster> menuMasterList = menuServ.getAllInactiveSubMenuInternal();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(SubMenuInternalMasterServiceAPIs.CHANGE_SUB_MENU_INTERNAL_STATUS)
	public void changeSubMenuInternalMasterStatus(@RequestParam("id") String subMenuInternalMasterId) {
		
		String id = EncryptionUtil.decrypt(subMenuInternalMasterId);
		
		try {
			SubMenuInternalMaster c = menuServ.getSubMenuInternalById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			menuServ.updateSubMenuInternal(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(SubMenuInternalMasterServiceAPIs.GET_SUB_MENU_INTERNAL_BY_ID)
	public ResponseEntity<?> getSubMenuInternalMasterByID(@RequestParam("id") String menuMasterId)
	{
		String id = EncryptionUtil.decrypt(menuMasterId);
		
		try {
			SubMenuInternalMaster subMenuInternalObj = menuServ.getSubMenuInternalById(Long.parseLong(id));
			return new ResponseEntity<>(subMenuInternalObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Sub Menu Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	@PostMapping(SubMenuInternalMasterServiceAPIs.UPDATE_SUB_MENU_INTERNAL)
	public ResponseEntity<?> updateSubMenuInternalMaster(@RequestBody SubMenuInternalDTO subMenuInternalObj){
		
				
				
				
				//Server Side Validation
				
				if(subMenuInternalObj.getSubMenuInternalName().equals("")) {
					return new ResponseEntity<>("Please enter sub menu name.", HttpStatus.BAD_REQUEST);
				}else if(!subMenuInternalObj.getSubMenuInternalName().trim().matches("^[A-Za-z ]+$")) {
					return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
				}else if(subMenuInternalObj.getSubMenuInternalName().length() < 3 || subMenuInternalObj.getSubMenuInternalName().length() > 50) {
					return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
				}
						
				
				if(subMenuInternalObj.getSubMenuInternalPath().equals("")) {
					return new ResponseEntity<>("Please enter sub menu path.", HttpStatus.BAD_REQUEST);
				}else if(!subMenuInternalObj.getSubMenuInternalPath().trim().matches("^[A-Za-z/:]+$")) {
					return new ResponseEntity<>("Only alphabets,/, and : are allowed.", HttpStatus.BAD_REQUEST);
				}else if(subMenuInternalObj.getSubMenuInternalPath().length() < 3 || subMenuInternalObj.getSubMenuInternalPath().length() > 100) {
					return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
				}
				
				if(subMenuInternalObj.getTrackerHeading().equals("")) {
					return new ResponseEntity<>("Please enter tracker heading.", HttpStatus.BAD_REQUEST);
				}else if(!subMenuInternalObj.getTrackerHeading().trim().matches("^[A-Za-z ]+$")) {
					return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
				}else if(subMenuInternalObj.getTrackerHeading().length() < 3 || subMenuInternalObj.getTrackerHeading().length() > 50) {
					return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
				}
				
				if(subMenuInternalObj.getSubMenuId().equals("")) {
					return new ResponseEntity<>("Please select Sub Menu.", HttpStatus.BAD_REQUEST);
				}
				
				
				
				Long submenuinternalid = Long.parseLong(EncryptionUtil.decrypt(subMenuInternalObj.getSubMenuInternalId()));
				Long submenuid = Long.parseLong(EncryptionUtil.decrypt(subMenuInternalObj.getSubMenuId()));
				
						
				SubMenuInternalMaster cobj = menuServ.getSubMenuInternalByNameAndSubMenuId(subMenuInternalObj.getSubMenuInternalName().trim(), submenuid);
				if(cobj != null && submenuinternalid != cobj.getSubMenuInternalId()) {
					return new ResponseEntity<>("Duplicate Sub Menu internal name is not allowed for same sub menu.", HttpStatus.BAD_REQUEST);
				}
				
				
				SubMenuInternalMaster cobj1 = menuServ.getSubMenuInternalByPathAndSubMenuId(subMenuInternalObj.getSubMenuInternalPath().trim(), submenuid);
				if(cobj1 != null && submenuinternalid != cobj1.getSubMenuInternalId()) {
					return new ResponseEntity<>("Duplicate Sub Menu internal path is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				
				try {
					
					SubMenuInternalMaster uObj = new SubMenuInternalMaster();
					
					uObj.setSubMenuInternalId(submenuinternalid);
					uObj.setSubMenuInternalName(subMenuInternalObj.getSubMenuInternalName());
					uObj.setSubMenuInternalPath(subMenuInternalObj.getSubMenuInternalPath());
					uObj.setSubMenuId(subMenuMasterServ.getSubMenuById(submenuid));
					uObj.setTrackerHeading(subMenuInternalObj.getTrackerHeading());
					uObj.setStatus(subMenuInternalObj.getStatus());
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					Date cdate = sdf.parse(subMenuInternalObj.getCreated());	
					uObj.setCreated(cdate);
					
					
					Optional<SubMenuInternalMaster> c = menuServ.updateSubMenuInternal(uObj);
					
					if(c.isEmpty())
						return new ResponseEntity<>("An error occurred while updating the Sub Menu Internal. Please Try again.", HttpStatus.OK);
					return new ResponseEntity<>("Sub Menu Internal successfully updated.", HttpStatus.OK);
				}catch(Exception e) {
					return new ResponseEntity<>("An error occurred while updating the Sub Menu Internal. Please Try again.", HttpStatus.BAD_REQUEST);
				}
				
				

	}
	
}
