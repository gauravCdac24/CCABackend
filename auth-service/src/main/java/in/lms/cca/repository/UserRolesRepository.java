package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.UserRoles;

public interface UserRolesRepository extends JpaRepository<UserRoles, Integer>{

	@Query("SELECT role FROM UserRoles role WHERE role.loginId.loginId = :lid")
	List<UserRoles> findByLoginId(@Param("lid") Integer loginId);

	
	@Query("SELECT role FROM UserRoles role WHERE role.userRoleId = :roleId")
	UserRoles findByLoginId(@Param("roleId")Long roleId);


	@Query("SELECT role FROM UserRoles role WHERE role.loginId.loginId = :intValue AND status='Active'")
	List<UserRoles> findAllActiveRoleByLoginId(@Param("intValue")Integer intValue);
	
}
