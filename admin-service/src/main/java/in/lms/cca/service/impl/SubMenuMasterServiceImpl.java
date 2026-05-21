package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.RoutesDTO;
import in.lms.cca.entity.SubMenuMaster;
import in.lms.cca.entity.logs.SubMenuLogMaster;
import in.lms.cca.repository.SubMenuMasterRepository;
import in.lms.cca.repository.logs.SubMenuMasterRepositoryLogs;
import in.lms.cca.service.ISubMenuMasterService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SubMenuMasterServiceImpl implements ISubMenuMasterService{

	@Autowired
	private SubMenuMasterRepository subMenuMasterRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private SubMenuMasterRepositoryLogs log;

	
	@Override
	public Optional<SubMenuMaster> addSubMenu(SubMenuMaster subMenuObj) {
		if(subMenuObj == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        SubMenuMaster savedSubMenuMaster = subMenuMasterRepo.save(subMenuObj);
	        if (usernameFromToken != null) {
	            log.save(new SubMenuLogMaster(savedSubMenuMaster, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(subMenuObj);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<SubMenuMaster> updateSubMenu(SubMenuMaster subMenuObj) {
		if(subMenuObj == null)
			return Optional.empty();
		
		if(subMenuObj.getSubMenuId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        SubMenuMaster savedSubMenuMaster = subMenuMasterRepo.save(subMenuObj);
	        if (usernameFromToken != null) {
	            log.save(new SubMenuLogMaster(savedSubMenuMaster, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        

	        return Optional.of(subMenuObj);
	    
	}catch(Exception e) {
		e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteSubMenuById(Long id) {
		SubMenuMaster subMenuObj = subMenuMasterRepo.findBySubMenuId(id);
		
		if(subMenuObj == null)
			return false;
		
		try {
			subMenuMasterRepo.deleteBySubMenuId(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public SubMenuMaster getSubMenuById(Long id) {
		
		return subMenuMasterRepo.findBySubMenuId(id);
	}

	@Override
	public List<SubMenuMaster> getAllSubMenu() {

		return subMenuMasterRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<SubMenuMaster> getAllActiveSubMenu() {
		
		return subMenuMasterRepo.findAllActiveSubMenus();
	}

	@Override
	public List<SubMenuMaster> getAllInactiveSubMenu() {
		
		return subMenuMasterRepo.findAllInActiveSubMenus();
	}

	@Override
	public List<SubMenuMaster> getAllSubMenusByMenuId(Long menuId) {
		return subMenuMasterRepo.findAllSubMenusByMenuId(menuId);
	}

	@Override
	public List<SubMenuMaster> getAllActiveSubMenusByMenuId(Long menuId) {
		
		return subMenuMasterRepo.findAllActiveSubMenusByMenuId(menuId);
	}

	@Override
	public List<SubMenuMaster> getAllInActiveSubMenusByMenuId(Long menuId) {
		
		return subMenuMasterRepo.findAllInActiveSubMenusByMenuId(menuId);
	}

	@Override
	public SubMenuMaster getSubMenuByPath(String path) {
		
		return subMenuMasterRepo.findSubMenuByPath(path);
	}

	@Override
	public List<Integer> getAllSubMenuOrderList() {
		
		return subMenuMasterRepo.findAllSubMenuOrderList();
	}

	@Override
	public SubMenuMaster getSubMenuByOrderNo(Integer orderno) {
		
		return subMenuMasterRepo.findByMenuOrderNo(orderno);
	}

	@Override
	public List<Integer> getAllSubMenuOrderListByMenuId(Long menuId) {
		
		return subMenuMasterRepo.findAllSubMenuOrderListByMenuId(menuId);
	}

	@Override
	public SubMenuMaster getSubMenuByOrderNoAndMenuId(Integer orderno, Long menuid) {
		
		List<SubMenuMaster> smaster =  subMenuMasterRepo.findSubMenuByOrderNoAndMenuId(orderno, menuid);
		if(smaster.size()>0)
			return smaster.get(0);
		return null;
		
	}

	@Override
	public List<RoutesDTO> getRoutesByRole(String roleName) {
		List<String> routes = subMenuMasterRepo.findRoutesByRole(roleName);
		
	    return routes.stream()
	                 .map(RoutesDTO::new)
	                 .collect(Collectors.toList());
	}

	@Override
	public SubMenuMaster getSubMenuByNameAndMenuId(String name, Long menuid) {
		return subMenuMasterRepo.findBySubMenuNameAndMenuId(name, menuid);
	}

	@Override
	public List<RoutesDTO> getInternalRoutesByRole(String roleName) {
		List<String> routes = subMenuMasterRepo.findInternalRoutesByRole(roleName);
		
	    return routes.stream()
	                 .map(RoutesDTO::new)
	                 .collect(Collectors.toList());
	}

	@Override
	public SubMenuMaster getSubMenuByPathAndMenuId(String path, Long id) {
		
		return subMenuMasterRepo.findSubMenuByPathAndMenuId(path, id);
	}

	@Override
	public SubMenuMaster getSubMenuDetailsByPath(String pathName) {
		
		return subMenuMasterRepo.findSubMenuDetailsByPath(pathName);
	}

	
	
}
