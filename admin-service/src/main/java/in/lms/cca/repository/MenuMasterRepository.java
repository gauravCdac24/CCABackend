package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.MenuMaster;
import jakarta.transaction.Transactional;

public interface MenuMasterRepository extends JpaRepository<MenuMaster, Long> {

	@Query("FROM MenuMaster a WHERE a.menuId=:menuId")
	MenuMaster findByMenuId (Long menuId);
	
	@Query("FROM MenuMaster a WHERE a.menuName=:menuName")
	MenuMaster findByMenuName (String menuName);
	
	@Query("FROM MenuMaster WHERE status='Active' ORDER BY menuOrder ASC")
	List<MenuMaster> findAllActiveMenus ();
	
	@Query("FROM MenuMaster WHERE status='Inactive' ORDER BY menuOrder ASC")
	List<MenuMaster> findAllInActiveMenus ();
	
	@Modifying
	@Transactional
	@Query("DELETE FROM MenuMaster s WHERE s.menuId=:menuId")
	void deleteByMenuId(Long menuId);

	@Query("SELECT menuOrder FROM MenuMaster")
	List<Integer> findAllMenuOrderList();

	@Query("FROM MenuMaster a WHERE a.menuOrder=:menuOrder")
	MenuMaster findByMenuOrderNo(int menuOrder);

	@Query("FROM MenuMaster a WHERE a.roleId.roleId=:roleId ORDER BY menuOrder ASC")
	List<MenuMaster> findAllMenusByRoleId (Integer roleId);
	
	@Query("FROM MenuMaster a WHERE a.roleId.roleId=:roleId AND status='Active' ORDER BY menuOrder ASC")
	List<MenuMaster> findAllActiveMenusByRoleId (Integer roleId);
	
	@Query("FROM MenuMaster a WHERE a.roleId.roleId=:roleId AND status='Inactive' ORDER BY menuOrder ASC")
	List<MenuMaster> findAllInActiveMenusByRoleId (Integer roleId);

	@Query("FROM MenuMaster a WHERE a.menuName=:name AND a.roleId.roleId=:roleid")
	MenuMaster findMenuByNameAndRoleId(String name, Integer roleid);
	
}
