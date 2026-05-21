package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import in.lms.cca.dto.SidebarDTO;
import in.lms.cca.dto.SubMenuDTO;
import in.lms.cca.dto.TrackerDTO;
import in.lms.cca.dto.TrackerPathDTO;
import in.lms.cca.entity.MenuMaster;
import in.lms.cca.entity.RoleMaster;
import in.lms.cca.entity.SubMenuInternalMaster;
import in.lms.cca.entity.SubMenuMaster;
import in.lms.cca.service.IMenuMasterService;
import in.lms.cca.service.IRoleMasterService;
import in.lms.cca.service.ISubMenuInternalMasterService;
import in.lms.cca.service.ISubMenuMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.SubMenuMasterServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class SubMenuMasterController {

	@Autowired
	private ISubMenuMasterService menuServ;
	
	@Autowired
	private IMenuMasterService menuMasterServ;
	
	@Autowired
	private IRoleMasterService roleMasterServ;
	
	@Autowired
	private ISubMenuInternalMasterService internalServ;
	
	@PostMapping(SubMenuMasterServiceAPIs.ADD_NEW_SUB_MENU)
	public ResponseEntity<?> addNewSubMenuMaster(@RequestBody SubMenuDTO subMenuObj){
		
		Long menuid = Long.parseLong(EncryptionUtil.decrypt(subMenuObj.getMenuId()));
		
		
		//Server Side Validation
		
		if(subMenuObj.getSubMenuName().equals("")) {
			return new ResponseEntity<>("Please enter sub menu name.", HttpStatus.BAD_REQUEST);
		}else if(!subMenuObj.getSubMenuName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(subMenuObj.getSubMenuName().length() < 3 || subMenuObj.getSubMenuName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(subMenuObj.getSubMenuPath().equals("")) {
			return new ResponseEntity<>("Please enter sub menu path.", HttpStatus.BAD_REQUEST);
		}else if(!subMenuObj.getSubMenuPath().trim().matches("^[A-Za-z/:]+$")) {
			return new ResponseEntity<>("Only alphabets,/, and : are allowed.", HttpStatus.BAD_REQUEST);
		}else if(subMenuObj.getSubMenuPath().length() < 3 || subMenuObj.getSubMenuPath().length() > 100) {
			return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(subMenuObj.getTrackerHeading().equals("")) {
			return new ResponseEntity<>("Please enter tracker heading.", HttpStatus.BAD_REQUEST);
		}else if(!subMenuObj.getTrackerHeading().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		}else if(subMenuObj.getTrackerHeading().length() < 3 || subMenuObj.getTrackerHeading().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(subMenuObj.getMenuId().equals("")) {
			return new ResponseEntity<>("Please select Menu.", HttpStatus.BAD_REQUEST);
		}
				
		
		SubMenuMaster cobj = menuServ.getSubMenuByNameAndMenuId(subMenuObj.getSubMenuName().trim(), menuid);
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Sub Menu name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		SubMenuMaster cobj1 = menuServ.getSubMenuByPathAndMenuId(subMenuObj.getSubMenuPath().trim(), menuid);
		if(cobj1 != null) {
			return new ResponseEntity<>("Duplicate Sub Menu path is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			SubMenuMaster newMenuMaster = new SubMenuMaster();
			newMenuMaster.setSubMenuName(subMenuObj.getSubMenuName());
			newMenuMaster.setSubMenuPath(subMenuObj.getSubMenuPath());
			newMenuMaster.setMenuId(menuMasterServ.getMenuById(menuid));
			newMenuMaster.setTrackerHeading(subMenuObj.getTrackerHeading());
			
			List<Integer> menuOrderList = menuServ.getAllSubMenuOrderListByMenuId(menuid);
			
			if(menuOrderList.size()==0) {
				newMenuMaster.setSubMenuOrder(1);
			}else {
				int missingOrder = IntStream.rangeClosed(1, menuOrderList.size() + 1)
					    .filter(i -> !menuOrderList.contains(i))
					    .findFirst()
					    .orElse(menuOrderList.size() + 1);

					newMenuMaster.setSubMenuOrder(missingOrder);
			}
			
			newMenuMaster.setStatus("Active");
			
			Optional<SubMenuMaster> c = menuServ.addSubMenu(newMenuMaster);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the Sub Menu. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Sub 	Menu successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the Sub Menu. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_ALL_SUB_MENU)
	public ResponseEntity<?> getSubAllMenuMaster(){
		
		List<SubMenuMaster> menuMasterList = menuServ.getAllSubMenu();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_ROUTES_BY_ROLE)
	public ResponseEntity<?> getRoutesByRole(@RequestParam("id") String role, @RequestParam("pid") String path){
		
		String roleName = EncryptionUtil.decrypt(role).replace("\"","");
		String pathName = EncryptionUtil.decrypt(path.trim());
				
		List<RoutesDTO> routes = menuServ.getRoutesByRole(roleName);
		List<RoutesDTO> routesInternal = menuServ.getInternalRoutesByRole(roleName);
		
		
	    List<RoutesDTO> allRoutes = new ArrayList<>(routes);
	    allRoutes.addAll(routesInternal);

	    boolean isValidRoute = false;
	    
	    for(RoutesDTO r: allRoutes) {
	    	if(r.getRoutePath().equals(pathName)) {
	    		isValidRoute = true;
	    	}
	    }
	    
	    if(isValidRoute) {
			return new ResponseEntity<>(EncryptionUtil.encrypt("true"), HttpStatus.OK);
	    }
		
		return new ResponseEntity<>(EncryptionUtil.encrypt("false"), HttpStatus.OK);
	}
	
	
	
	
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_TRACKER)
	public ResponseEntity<?> getTrakcer(@RequestParam("pid") String path){
		
		
		String pathName = EncryptionUtil.decrypt(path.trim());
		
		
		try {
		
			List<TrackerDTO>  trackerList = new ArrayList<>();
			
			SubMenuMaster submenu = menuServ.getSubMenuDetailsByPath(pathName);
			SubMenuInternalMaster submenuinternal = internalServ.getSubMenuInternalDetailsByPath(pathName);
		
		if(submenu != null) {
		
			SubMenuMaster homepage = menuServ.getSubMenuDetailsByPath("/"+submenu.getMenuId().getRoleId().getHomePage());
			
			
			List<TrackerPathDTO> trackPath = new ArrayList<>();
			
			//Home Page
			TrackerPathDTO t1 = new TrackerPathDTO();
			t1.setName(homepage.getMenuId().getMenuName());
			t1.setPath("/" + homepage.getMenuId().getRoleId().getHomePage());
			
			
			//Main Tracker
			TrackerDTO tracker  = new TrackerDTO();
			
			tracker.setHeading(submenu.getTrackerHeading());
			tracker.setId(submenu.getSubMenuId().toString());
			tracker.setPath("/"+submenu.getMenuId().getRoleId().getPath()+"/"+submenu.getSubMenuPath());
			
			if(t1.getPath().equals(tracker.getPath()))
				t1.setPath("");
			
			tracker.setTrackerPaths(trackPath);
			trackPath.add(t1);
			
			
			//Menu Name
			if(!t1.getName().equals(submenu.getMenuId().getMenuName()))
			{
				TrackerPathDTO t2 = new TrackerPathDTO();
				t2.setName(submenu.getMenuId().getMenuName());
				t2.setPath("");
				trackPath.add(t2);
			}
			
			//Sub Menu
			if(!homepage.getSubMenuPath().equals(submenu.getSubMenuPath()))
			{  
				//Internal
				TrackerPathDTO t3 = new TrackerPathDTO();
				t3.setName(submenu.getSubMenuName());
				t3.setPath("");
				trackPath.add(t3);
			}
			trackerList.add(tracker);
			
		}else if(submenuinternal != null) {
			
			
			
			SubMenuMaster homepage = menuServ.getSubMenuDetailsByPath("/"+submenuinternal.getSubMenuId().getMenuId().getRoleId().getHomePage());
			
			
			List<TrackerPathDTO> trackPath = new ArrayList<>();
			
			//Home Page
			TrackerPathDTO t1 = new TrackerPathDTO();
			t1.setName(homepage.getMenuId().getMenuName());
			t1.setPath("/" + homepage.getMenuId().getRoleId().getHomePage());
			
			
			//Main Tracker
			TrackerDTO tracker  = new TrackerDTO();
			
			tracker.setHeading(submenuinternal.getTrackerHeading());
			tracker.setId(submenuinternal.getSubMenuInternalId().toString());
			tracker.setPath("/"+submenuinternal.getSubMenuId().getMenuId().getRoleId().getPath()+"/"+submenuinternal.getSubMenuId().getSubMenuPath()+"/"+submenuinternal.getSubMenuInternalPath());
			
			if(t1.getPath().equals(tracker.getPath()))
				t1.setPath("");
			
			tracker.setTrackerPaths(trackPath);
			trackPath.add(t1);
			
			
			//Menu Name
			if(!t1.getName().equals(submenuinternal.getSubMenuId().getMenuId().getMenuName()))
			{
				TrackerPathDTO t2 = new TrackerPathDTO();
				t2.setName(submenuinternal.getSubMenuId().getMenuId().getMenuName());
				t2.setPath("");
				trackPath.add(t2);
			}

			//Sub Menu
			TrackerPathDTO t3 = new TrackerPathDTO();
			t3.setName(submenuinternal.getSubMenuId().getSubMenuName());
			t3.setPath("/"+submenuinternal.getSubMenuId().getMenuId().getRoleId().getPath()+"/"+submenuinternal.getSubMenuId().getSubMenuPath());
			trackPath.add(t3);
			
			//Internal Sub Menu
			
			TrackerPathDTO t4 = new TrackerPathDTO();
			t4.setName(submenuinternal.getSubMenuInternalName());
			t4.setPath("");
			trackPath.add(t4);

			
			trackerList.add(tracker);
			
			
			
			
		}
		
		return new ResponseEntity<>(trackerList, HttpStatus.OK);
		
		}catch(Exception e) {
			List<TrackerDTO>  trackerList = new ArrayList<>();
			return new ResponseEntity<>(trackerList, HttpStatus.OK);
		}
	}
	
	
	

	
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_SIDEBAR_BY_ROLE)
	public ResponseEntity<?> getSidebarByRole(@RequestParam("id") String role){
	
		String roleName = EncryptionUtil.decrypt(role).replace("\"","");
		
		
		
		try {
		
		RoleMaster rolemaster = roleMasterServ.getRoleByName(roleName);
		
		List<MenuMaster> menuMaster = menuMasterServ.getAllActiveMenusByRoleId(rolemaster.getRoleId());
		
		List<SidebarDTO> sidebarMenu = new ArrayList<>();
		
		for(MenuMaster m: menuMaster) {
			
			List<SubMenuMaster> subMenu = menuServ.getAllActiveSubMenusByMenuId(m.getMenuId());
			for (SubMenuMaster child : subMenu) {
				child.setSubMenuName(normalizeMenuLabel(child.getSubMenuName()));
				child.setTrackerHeading(normalizeMenuLabel(child.getTrackerHeading()));
			}
			
			SidebarDTO s = new SidebarDTO(m.getMenuId(), normalizeMenuLabel(m.getMenuName()), m.getMenuIcon(), m.getMenuOrder(), subMenu);
			sidebarMenu.add(s);
		}
		
		return new ResponseEntity<>(sidebarMenu, HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>("No Sidebar data found", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_ALL_ACTIVE_SUB_MENU)
	public ResponseEntity<?> getAllActiveSubMenuMaster(){
		
		List<SubMenuMaster> menuMasterList = menuServ.getAllActiveSubMenu();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_ALL_INACTIVE_SUB_MENU)
	public ResponseEntity<?> getAllInactiveSubMenu(){
		
		List<SubMenuMaster> menuMasterList = menuServ.getAllInactiveSubMenu();
		return new ResponseEntity<>(menuMasterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(SubMenuMasterServiceAPIs.CHANGE_SUB_MENU_STATUS)
	public void changeSubMenuMasterStatus(@RequestParam("id") String menuMasterId) {
		
		String id = EncryptionUtil.decrypt(menuMasterId);
		
		try {
			SubMenuMaster c = menuServ.getSubMenuById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			menuServ.updateSubMenu(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_SUB_MENU_BY_ID)
	public ResponseEntity<?> getSubMenuMasterByID(@RequestParam("id") String menuMasterId)
	{
		String id = EncryptionUtil.decrypt(menuMasterId);
		
		try {
			SubMenuMaster subMenuObj = menuServ.getSubMenuById(Long.parseLong(id));
			return new ResponseEntity<>(subMenuObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Sub Menu Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(SubMenuMasterServiceAPIs.GET_SUB_MENU_ORDER_BY_MENU_ID)
	public ResponseEntity<?> getSubMenuOrderByMenuID(@RequestParam("id") String menuId)
	{
		String id = EncryptionUtil.decrypt(menuId);
		
		try {
			List<Integer> smenuorder = menuServ.getAllSubMenuOrderListByMenuId(Long.parseLong(id));
			return new ResponseEntity<>(smenuorder, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Menu Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@PostMapping(SubMenuMasterServiceAPIs.UPDATE_SUB_MENU)
	public ResponseEntity<?> updateSubMenuMaster(@RequestBody SubMenuDTO subMenuObj){
		
		
		Long submenuid = Long.parseLong(EncryptionUtil.decrypt(subMenuObj.getSubMenuId()));
		Long menuid = Long.parseLong(EncryptionUtil.decrypt(subMenuObj.getMenuId()));
		
		
		//Server Side Validation
		
				if(subMenuObj.getSubMenuName().equals("")) {
					return new ResponseEntity<>("Please enter sub menu name.", HttpStatus.BAD_REQUEST);
				}else if(!subMenuObj.getSubMenuName().trim().matches("^[A-Za-z ]+$")) {
					return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
				}else if(subMenuObj.getSubMenuName().length() < 3 || subMenuObj.getSubMenuName().length() > 50) {
					return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
				}
						
				
				if(subMenuObj.getSubMenuPath().equals("")) {
					return new ResponseEntity<>("Please enter sub menu path.", HttpStatus.BAD_REQUEST);
				}else if(!subMenuObj.getSubMenuPath().trim().matches("^[A-Za-z/:]+$")) {
					return new ResponseEntity<>("Only alphabets,/, and : are allowed.", HttpStatus.BAD_REQUEST);
				}else if(subMenuObj.getSubMenuPath().length() < 3 || subMenuObj.getSubMenuPath().length() > 100) {
					return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
				}
				
				if(subMenuObj.getTrackerHeading().equals("")) {
					return new ResponseEntity<>("Please enter tracker heading.", HttpStatus.BAD_REQUEST);
				}else if(!subMenuObj.getTrackerHeading().trim().matches("^[A-Za-z ]+$")) {
					return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
				}else if(subMenuObj.getTrackerHeading().length() < 3 || subMenuObj.getTrackerHeading().length() > 50) {
					return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
				}
				
				if(subMenuObj.getMenuId().equals("")) {
					return new ResponseEntity<>("Please select Menu.", HttpStatus.BAD_REQUEST);
				}
				
				if(subMenuObj.getSubMenuOrder().equals("")) {
					return new ResponseEntity<>("Please Select Sub Menu Order.", HttpStatus.BAD_REQUEST);
				}
						
				//Check for Unique MenuMaster Name
				SubMenuMaster cobj = menuServ.getSubMenuByNameAndMenuId(subMenuObj.getSubMenuName().trim(), menuid);
				if(cobj != null && submenuid != cobj.getSubMenuId()) {
					return new ResponseEntity<>("Duplicate Sub Menu name is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				//Check for Unique SubMenuMaster Name
				SubMenuMaster cobj1 = menuServ.getSubMenuByPathAndMenuId(subMenuObj.getSubMenuPath().trim(), menuid);
				if(cobj1 != null && submenuid != cobj1.getSubMenuId()) {
					return new ResponseEntity<>("Duplicate Sub Menu path is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				
				
				
				try {
					
					SubMenuMaster uObj = new SubMenuMaster();
					uObj.setSubMenuId(submenuid);
					uObj.setSubMenuName(subMenuObj.getSubMenuName());
					uObj.setSubMenuPath(subMenuObj.getSubMenuPath());
					uObj.setMenuId(menuMasterServ.getMenuById(menuid));
					uObj.setTrackerHeading(subMenuObj.getTrackerHeading());
					
					List<Integer> menuOrderList = menuServ.getAllSubMenuOrderListByMenuId(menuid);
					
					
					
					if(menuOrderList.size()==0) {
						uObj.setSubMenuOrder(1);
					}else {
						
						SubMenuMaster menu = menuServ.getSubMenuByOrderNoAndMenuId(Integer.parseInt(subMenuObj.getSubMenuOrder()), menuid);
						
						if(menu != null) {
						
							SubMenuMaster menu1 = menuServ.getSubMenuById(submenuid);
							
							menu.setSubMenuOrder(menu1.getSubMenuOrder());
							menuServ.updateSubMenu(menu);
						}
						uObj.setSubMenuOrder(Integer.parseInt(subMenuObj.getSubMenuOrder()));
						
					}
					
					uObj.setStatus(subMenuObj.getStatus());
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					Date cdate = sdf.parse(subMenuObj.getCreated());	
					uObj.setCreated(cdate);
					
					
					Optional<SubMenuMaster> c = menuServ.updateSubMenu(uObj);
					
					if(c.isEmpty())
						return new ResponseEntity<>("An error occurred while updating the Sub Menu. Please Try again.", HttpStatus.OK);
					return new ResponseEntity<>("Sub Menu successfully updated.", HttpStatus.OK);
				}catch(Exception e) {
					
					return new ResponseEntity<>("An error occurred while updating the Sub Menu. Please Try again.", HttpStatus.BAD_REQUEST);
				}
		
 
		
	}

	private String normalizeMenuLabel(String label) {
		if (label == null) {
			return null;
		}
		return label.replaceAll("(?i)dashobard", "Dashboard");
	}
	
}
