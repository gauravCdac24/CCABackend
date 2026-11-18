package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.UserRoles;

public interface IUserRolesService {

	UserRoles saveUserRole(UserRoles urole);
	List<UserRoles> getAllRolesByLoginId(Integer loginId);
	Optional<UserRoles> updateRole(UserRoles updatedRole);
	UserRoles getRuserRoleById(Long roleId );
	List<UserRoles> getAllActiveRolesByLoginId(Integer intValue);
	
}
