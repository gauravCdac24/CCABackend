package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.MenuMaster;

public interface IMenuMasterService {

	Optional<MenuMaster> addMenu(MenuMaster menuObj);
	Optional<MenuMaster> updateMenu(MenuMaster menuObj);
	boolean deleteMenuById(Long id);
	MenuMaster getMenuById(Long id);
	List<MenuMaster> getAllMenu();
	List<MenuMaster> getAllActiveMenu();
	List<MenuMaster> getAllInactiveMenu();
	MenuMaster getMenuByName(String name);
	List<Integer> getAllMenuOrderList();
	MenuMaster getMenuByOrderNo(int orderno);
	List<MenuMaster> getAllMenusByRoleId (Integer roleId);
	List<MenuMaster> getAllActiveMenusByRoleId (Integer roleId);
	List<MenuMaster> getAllInActiveMenusByRoleId (Integer roleId);
	MenuMaster getMenuByNameAndRoleId(String name, Integer roleid);
}
