package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.RoutesDTO;
import in.lms.cca.entity.SubMenuMaster;

public interface ISubMenuMasterService {

	Optional<SubMenuMaster> addSubMenu(SubMenuMaster subMenuObj);
	Optional<SubMenuMaster> updateSubMenu(SubMenuMaster subMenuObj);
	boolean deleteSubMenuById(Long id);
	SubMenuMaster getSubMenuById(Long id);
	List<SubMenuMaster> getAllSubMenu();
	List<SubMenuMaster> getAllActiveSubMenu();
	List<SubMenuMaster> getAllInactiveSubMenu();
	List<SubMenuMaster> getAllSubMenusByMenuId (Long menuId);
	List<SubMenuMaster> getAllActiveSubMenusByMenuId (Long menuId);
	List<SubMenuMaster> getAllInActiveSubMenusByMenuId (Long menuId);
	SubMenuMaster getSubMenuByPath(String path);
	List<Integer> getAllSubMenuOrderList();
	SubMenuMaster getSubMenuByOrderNo(Integer orderno);
	List<Integer> getAllSubMenuOrderListByMenuId(Long menuId);
	SubMenuMaster getSubMenuByOrderNoAndMenuId(Integer orderno, Long menuid);
	List<RoutesDTO> getRoutesByRole(String roleName);
	SubMenuMaster getSubMenuByNameAndMenuId(String name, Long menuid);
	List<RoutesDTO> getInternalRoutesByRole(String roleName);
	SubMenuMaster getSubMenuByPathAndMenuId(String path, Long id);
	SubMenuMaster getSubMenuDetailsByPath(String pathName);
}
