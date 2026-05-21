package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.SubMenuMaster;
import jakarta.transaction.Transactional;

public interface SubMenuMasterRepository extends JpaRepository<SubMenuMaster, Long>{

	@Query("FROM SubMenuMaster a WHERE a.subMenuId=:subMenuId")
	SubMenuMaster findBySubMenuId (Long subMenuId);
	
	
	
	@Query("FROM SubMenuMaster WHERE status='Active' ORDER BY subMenuOrder ASC")
	List<SubMenuMaster> findAllActiveSubMenus ();
	
	@Query("FROM SubMenuMaster WHERE status='Inactive' ORDER BY subMenuOrder ASC")
	List<SubMenuMaster> findAllInActiveSubMenus ();

	@Query("FROM SubMenuMaster a WHERE a.menuId.menuId=:menuId ORDER BY subMenuOrder ASC")
	List<SubMenuMaster> findAllSubMenusByMenuId (Long menuId);
	
	@Query("FROM SubMenuMaster a WHERE a.menuId.menuId=:menuId AND status='Active' ORDER BY subMenuOrder ASC")
	List<SubMenuMaster> findAllActiveSubMenusByMenuId (Long menuId);
	
	@Query("FROM SubMenuMaster a WHERE a.menuId.menuId=:menuId AND status='Inactive' ORDER BY subMenuOrder ASC")
	List<SubMenuMaster> findAllInActiveSubMenusByMenuId (Long menuId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM SubMenuMaster s WHERE s.subMenuId=:subMenuId")
	void deleteBySubMenuId(Long subMenuId);

	@Query("FROM SubMenuMaster a WHERE a.subMenuPath=:subMenuPath")
	SubMenuMaster findSubMenuByPath(String subMenuPath);
	
	@Query("SELECT subMenuOrder FROM SubMenuMaster")
	List<Integer> findAllSubMenuOrderList();
	
	@Query("FROM SubMenuMaster a WHERE a.subMenuOrder=:menuOrder")
	SubMenuMaster findByMenuOrderNo(int menuOrder);

	@Query("SELECT subMenuOrder FROM SubMenuMaster WHERE menuId.menuId=:menuId")
	List<Integer> findAllSubMenuOrderListByMenuId(Long menuId);

	@Query("FROM SubMenuMaster a WHERE a.subMenuOrder=:orderno AND menuId.menuId=:menuid")
	List<SubMenuMaster> findSubMenuByOrderNoAndMenuId(Integer orderno, Long menuid);

	
	@Query("SELECT CONCAT('/', r.path, '/', sm.subMenuPath) " +
		       "FROM SubMenuMaster sm " +
		       "JOIN RoleMaster r ON sm.menuId.roleId.roleId = r.roleId " +
		       "WHERE r.roleName = :roleName AND sm.status='Active'")
		List<String> findRoutesByRole(String roleName);

		@Query("SELECT CONCAT('/', r.path, '/', sm.subMenuPath, '/', si.subMenuInternalPath) " +
		       "FROM SubMenuMaster sm " +
		       "JOIN RoleMaster r ON sm.menuId.roleId.roleId = r.roleId " +
		       "JOIN SubMenuInternalMaster si ON si.subMenuId.subMenuId = sm.subMenuId " +
		       "WHERE r.roleName = :roleName AND si.status='Active'")
		List<String> findInternalRoutesByRole(String roleName);


	@Query("FROM SubMenuMaster a WHERE a.subMenuName=:subMenuName AND menuId.menuId=:menuid")
	SubMenuMaster findBySubMenuNameAndMenuId(String subMenuName, Long menuid);


	@Query("FROM SubMenuMaster a WHERE a.subMenuPath=:path AND a.menuId.menuId=:menuid")
	SubMenuMaster findSubMenuByPathAndMenuId(String path, Long menuid);



	@Query("FROM SubMenuMaster sm " +
		       "JOIN RoleMaster r ON sm.menuId.roleId.roleId = r.roleId " +
		       "WHERE CONCAT('/', r.path, '/', sm.subMenuPath) = :pathName")
	SubMenuMaster findSubMenuDetailsByPath(String pathName);
	
}
