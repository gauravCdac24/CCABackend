package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.MenuMaster;
import in.lms.cca.entity.logs.MenuMasterLogs;
import in.lms.cca.repository.MenuMasterRepository;
import in.lms.cca.repository.logs.MenuMasterRepositoryLogs;
import in.lms.cca.service.IMenuMasterService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuMasterServiceImpl implements IMenuMasterService{

	@Autowired
	private MenuMasterRepository menuMasterRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private MenuMasterRepositoryLogs log;

	
	@Override
	public Optional<MenuMaster> addMenu(MenuMaster menuObj) {
		
		if(menuObj == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        MenuMaster savedMenuMaster = menuMasterRepo.save(menuObj);
	        if (usernameFromToken != null) {
	            log.save(new MenuMasterLogs(savedMenuMaster, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(menuObj);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<MenuMaster> updateMenu(MenuMaster menuObj) {
		
		if(menuObj == null)
			return Optional.empty();
		
		if(menuObj.getMenuId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        MenuMaster savedMenuMaster = menuMasterRepo.save(menuObj);
	        if (usernameFromToken != null) {
	            log.save(new MenuMasterLogs(savedMenuMaster, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(menuObj);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteMenuById(Long id) {
		
		MenuMaster menuObj = menuMasterRepo.findByMenuId(id);
		
		if(menuObj == null)
			return false;
		
		try {
			menuMasterRepo.deleteByMenuId(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public MenuMaster getMenuById(Long id) {
		
		return menuMasterRepo.findByMenuId(id);
	}

	@Override
	public List<MenuMaster> getAllMenu() {
		
		return menuMasterRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<MenuMaster> getAllActiveMenu() {
		
		return menuMasterRepo.findAllActiveMenus();
	}

	@Override
	public List<MenuMaster> getAllInactiveMenu() {
		
		return menuMasterRepo.findAllInActiveMenus();
	}

	@Override
	public MenuMaster getMenuByName(String name) {
		
		return menuMasterRepo.findByMenuName(name);
	}

	@Override
	public List<Integer> getAllMenuOrderList() {
		
		return menuMasterRepo.findAllMenuOrderList();
	}

	@Override
	public MenuMaster getMenuByOrderNo(int orderno) {
		
		return menuMasterRepo.findByMenuOrderNo(orderno);
	}
	
	@Override
	public List<MenuMaster> getAllMenusByRoleId(Integer roleId) {
		
		return menuMasterRepo.findAllMenusByRoleId(roleId);
	}

	@Override
	public List<MenuMaster> getAllActiveMenusByRoleId(Integer roleId) {
		
		return menuMasterRepo.findAllActiveMenusByRoleId(roleId);
	}

	@Override
	public List<MenuMaster> getAllInActiveMenusByRoleId(Integer roleId) {
		
		return menuMasterRepo.findAllInActiveMenusByRoleId(roleId);
	}

	@Override
	public MenuMaster getMenuByNameAndRoleId(String name, Integer roleid) {
		
		return menuMasterRepo.findMenuByNameAndRoleId(name, roleid);
	}

}
