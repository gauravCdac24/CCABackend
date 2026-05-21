package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import in.lms.cca.entity.SubMenuInternalMaster;

public interface SubMenuInternalMasterRepository  extends JpaRepository<SubMenuInternalMaster, Long> {

	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuInternalId=:subMenuInternalId")
	SubMenuInternalMaster findBySubMenuInternalId (Long subMenuInternalId);
	
	@Query("FROM SubMenuInternalMaster WHERE status='Active' ORDER BY created DESC")
	List<SubMenuInternalMaster> findAllActiveSubMenuInternal ();
	
	@Query("FROM SubMenuInternalMaster WHERE status='Inactive' ORDER BY created DESC")
	List<SubMenuInternalMaster> findAllInActiveSubMenuInternal ();

	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuId.subMenuId=:subMenuId ORDER BY created DESC")
	List<SubMenuInternalMaster> findAllSubMenuInternalBySubMenuId (Long subMenuId);
	
	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuId.subMenuId=:subMenuId AND status='Active' ORDER BY created DESC")
	List<SubMenuInternalMaster> findAllActiveSubMenuInternalBySubMenuId (Long subMenuId);
	
	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuId.subMenuId=:subMenuId AND status='Inactive' ORDER BY created DESC")
	List<SubMenuInternalMaster> findAllInActiveSubMenuInternalBySubMenuId (Long subMenuId);
	
	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuInternalPath=:subMenuInternalPath")
	SubMenuInternalMaster findSubMenuInternalByPath(String subMenuInternalPath);

	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuInternalName=:name AND subMenuId.subMenuId=:submenuid")
	SubMenuInternalMaster findSubMenuInternalByNameAndSubMenuId(String name, Long submenuid);

	@Query("FROM SubMenuInternalMaster a WHERE a.subMenuInternalPath=:path AND subMenuId.subMenuId=:submenuid")
	SubMenuInternalMaster findSubMenuInternalByPathAndSubMenuId(String path, Long submenuid);

	
	@Query("FROM SubMenuInternalMaster si " +
			"JOIN SubMenuMaster sm ON si.subMenuId.subMenuId = sm.subMenuId " +
			 "JOIN RoleMaster r ON sm.menuId.roleId.roleId = r.roleId "  +
		       "WHERE CONCAT('/', r.path, '/', sm.subMenuPath, '/', si.subMenuInternalPath) = :pathName")
	SubMenuInternalMaster findSubMenuInternalDetailsByPath(String pathName);

	
}
