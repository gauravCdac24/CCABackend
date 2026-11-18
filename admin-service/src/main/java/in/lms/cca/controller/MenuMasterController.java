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

import in.lms.cca.dto.MenuMasterDTO;
import in.lms.cca.entity.MenuMaster;
import in.lms.cca.service.IMenuMasterService;
import in.lms.cca.service.IRoleMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.MenuMasterServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class MenuMasterController {

	
	@Autowired
	private IMenuMasterService menuServ;
	
	@Autowired
	private IRoleMasterService roleServ;
	
	@PostMapping(MenuMasterServiceAPIs.ADD_NEW_MENU)
	public ResponseEntity<?> addNewMenuMaster(@RequestBody MenuMasterDTO menuMasterObj){
		
		Integer roleid = Integer.parseInt(EncryptionUtil.decrypt(menuMasterObj.getRoleId()));
		
		//Server Side Validation
		
		if(menuMasterObj.getMenuName().equals("")) {
			return new ResponseEntity<>("Please enter menu name.", HttpStatus.BAD_REQUEST);
		}else if(!menuMasterObj.getMenuName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(menuMasterObj.getMenuName().length() < 3 || menuMasterObj.getMenuName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(menuMasterObj.getMenuIcon().equals("")) {
			return new ResponseEntity<>("Please select menu icon.", HttpStatus.BAD_REQUEST);
		}else if(!menuMasterObj.getMenuIcon().trim().matches("^[A-Za-z]+$")) {
			return new ResponseEntity<>("Only alphabets are allowed.", HttpStatus.BAD_REQUEST);
		}else if(menuMasterObj.getMenuIcon().length() < 3 || menuMasterObj.getMenuIcon().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(menuMasterObj.getRoleId().equals("")) {
			return new ResponseEntity<>("Please select Role.", HttpStatus.BAD_REQUEST);
		}
				
		//Check for Unique MenuMaster Name
		MenuMaster cobj = menuServ.getMenuByNameAndRoleId(menuMasterObj.getMenuName().trim(), roleid);
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Menu name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		try {
			
			MenuMaster newMenuMaster = new MenuMaster();
			newMenuMaster.setMenuName(menuMasterObj.getMenuName());
			newMenuMaster.setMenuIcon(menuMasterObj.getMenuIcon());
			newMenuMaster.setRoleId(roleServ.getRoleById(roleid));
			
			List<Integer> menuOrderList = menuServ.getAllMenuOrderList();
			
			if(menuOrderList.size()==0) {
				newMenuMaster.setMenuOrder(1);
			}else {
				int missingOrder = IntStream.rangeClosed(1, menuOrderList.size() + 1)
					    .filter(i -> !menuOrderList.contains(i))
					    .findFirst()
					    .orElse(menuOrderList.size() + 1);

					newMenuMaster.setMenuOrder(missingOrder);
			}
			
			newMenuMaster.setStatus("Active");
			
			Optional<MenuMaster> c = menuServ.addMenu(newMenuMaster);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the Menu. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Menu successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the Menu. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(MenuMasterServiceAPIs.GET_ALL_MENU)
	public ResponseEntity<?> getAllMenuMaster(){
		
		List<MenuMaster> menuMasterList = menuServ.getAllMenu();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(MenuMasterServiceAPIs.GET_ALL_ACTIVE_MENU)
	public ResponseEntity<?> getAllActiveMenuMaster(){
		
		List<MenuMaster> menuMasterList = menuServ.getAllActiveMenu();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(MenuMasterServiceAPIs.GET_ALL_INACTIVE_MENU)
	public ResponseEntity<?> getAllInactiveMenu(){
		
		List<MenuMaster> menuMasterList = menuServ.getAllInactiveMenu();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(MenuMasterServiceAPIs.GET_ALL_MENU_ORDER)
	public ResponseEntity<?> getAllMenuOrder(){
		
		List<Integer> menuOrderList = menuServ.getAllMenuOrderList();
		
		return new ResponseEntity<>(menuOrderList, HttpStatus.OK); 
		
	}
	
	@GetMapping(MenuMasterServiceAPIs.CHANGE_MENU_STATUS)
	public void changeMenuMasterStatus(@RequestParam("id") String menuMasterId) {
		
		String id = EncryptionUtil.decrypt(menuMasterId);
		
		try {
			MenuMaster c = menuServ.getMenuById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			menuServ.updateMenu(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(MenuMasterServiceAPIs.GET_MENU_BY_ID)
	public ResponseEntity<?> getMenuMasterByID(@RequestParam("id") String menuMasterId)
	{
		String id = EncryptionUtil.decrypt(menuMasterId);
		
		try {
			MenuMaster menuMasterObj = menuServ.getMenuById(Long.parseLong(id));
			return new ResponseEntity<>(menuMasterObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Menu Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@PostMapping(MenuMasterServiceAPIs.UPDATE_MENU)
	public ResponseEntity<?> updateMenuMaster(@RequestBody MenuMasterDTO menuMasterObj){
		
		
		Long menuid = Long.parseLong(EncryptionUtil.decrypt(menuMasterObj.getMenuId()));
		Integer roleid = Integer.parseInt(EncryptionUtil.decrypt(menuMasterObj.getRoleId()));
		
		//Server Side Validation
		
				if(menuMasterObj.getMenuName().equals("")) {
					return new ResponseEntity<>("Please enter menu name.", HttpStatus.BAD_REQUEST);
				}else if(!menuMasterObj.getMenuName().trim().matches("^[A-Za-z ]+$")) {
					return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
				}else if(menuMasterObj.getMenuName().length() < 3 || menuMasterObj.getMenuName().length() > 50) {
					return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
				}
						
				
				if(menuMasterObj.getMenuIcon().equals("")) {
					return new ResponseEntity<>("Please enter menu icon.", HttpStatus.BAD_REQUEST);
				}else if(!menuMasterObj.getMenuIcon().trim().matches("^[A-Za-z]+$")) {
					return new ResponseEntity<>("Only alphabets are allowed.", HttpStatus.BAD_REQUEST);
				}else if(menuMasterObj.getMenuIcon().length() < 3 || menuMasterObj.getMenuIcon().length() > 50) {
					return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
				}
				
				if(menuMasterObj.getRoleId().equals("")) {
					return new ResponseEntity<>("Please select Role.", HttpStatus.BAD_REQUEST);
				}
				
				if(menuMasterObj.getMenuOrder().equals("")) {
					return new ResponseEntity<>("Please select Menu Order.", HttpStatus.BAD_REQUEST);
				}
		
				//Check for Unique MenuMaster Name
				MenuMaster cobj = menuServ.getMenuByNameAndRoleId(menuMasterObj.getMenuName().trim(), roleid);
				if(cobj != null && menuid != cobj.getMenuId()) {
					return new ResponseEntity<>("Duplicate Menu name is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				
				
				
				try {
					
					MenuMaster uMenuMaster = new MenuMaster();
					
					uMenuMaster.setMenuId(menuid);
					uMenuMaster.setMenuName(menuMasterObj.getMenuName());
					uMenuMaster.setMenuIcon(menuMasterObj.getMenuIcon());
					uMenuMaster.setRoleId(roleServ.getRoleById(roleid));
					
					List<Integer> menuOrderList = menuServ.getAllMenuOrderList();
					
					if(menuOrderList.size()==0) {
						uMenuMaster.setMenuOrder(1);
					}else {
						
						MenuMaster menu = menuServ.getMenuByOrderNo(Integer.parseInt(menuMasterObj.getMenuOrder()));
						MenuMaster menu1 = menuServ.getMenuById(menuid);
						
						menu.setMenuOrder(menu1.getMenuOrder());
						menuServ.updateMenu(menu);
						uMenuMaster.setMenuOrder(Integer.parseInt(menuMasterObj.getMenuOrder()));
						
					}
					
					uMenuMaster.setStatus(menuMasterObj.getStatus());
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					Date cdate = sdf.parse(menuMasterObj.getCreated());	
					uMenuMaster.setCreated(cdate);
					
					
					Optional<MenuMaster> c = menuServ.updateMenu(uMenuMaster);
					
					if(c.isEmpty())
						return new ResponseEntity<>("An error occurred while updating the Menu. Please Try again.", HttpStatus.OK);
					return new ResponseEntity<>("Menu successfully updated.", HttpStatus.OK);
				}catch(Exception e) {
					return new ResponseEntity<>("An error occurred while updating the Menu. Please Try again.", HttpStatus.BAD_REQUEST);
				}
		
 
		
	}
	
	
}
