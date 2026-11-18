package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.RoleMaster;
import jakarta.transaction.Transactional;

public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer>{

	@Query("FROM RoleMaster a WHERE a.roleId=:roleId")
	RoleMaster findByRoleId (Integer roleId);
	
	@Query("FROM RoleMaster a WHERE a.roleName=:roleName")
	RoleMaster findByRoleName (String roleName);
	
	@Query("FROM RoleMaster WHERE status='Active' ORDER BY created DESC")
	List<RoleMaster> findAllActiveRoles ();
	
	@Query("FROM RoleMaster WHERE status='Inactive' ORDER BY created DESC")
	List<RoleMaster> findAllInActiveRoles ();
	
	@Modifying
	@Transactional
	@Query("DELETE FROM RoleMaster s WHERE s.roleId=:roleId")
	void deleteByRoleId(Integer roleId);

	@Query("FROM RoleMaster a WHERE a.path=:path")
	RoleMaster findRoleMasterByPath(String path);

	@Query("FROM RoleMaster a WHERE a.homePage=:homepath")
	RoleMaster findRoleMasterByHomePage(String homepath);

	
}
