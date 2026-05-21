package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.GetRoles;
import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.entity.RoleMaster;


public interface IRoleMasterService {

	Optional<RoleMaster> addRole(RoleMaster roleObj);
	Optional<RoleMaster> updateRole(RoleMaster roleObj);
	boolean deleteRoleById(Integer id);
	RoleMaster getRoleById(Integer id);
	List<RoleMaster> getAllRoles();
	List<RoleMaster> getAllActiveRoles();
	List<RoleMaster> getAllInactiveRoles();
	RoleMaster getRoleByName(String name);
	RoleMaster getRoleMasterByPath(String path);
	RoleMaster getRoleMasterByHomePage(String homepath);
	List<GetRoles> getRoleByUserId(String userId);
	void addAssignRole(StaffLoginDTO staffLoginDTO);
	List<StaffLoginDTO> getDetailsByUserId(String id);
	void changeUserRoleStatus(GetRoles getRoles);
	void changeUserRoleStatus(String loginId);
	List<GetRoles> getRoleByUserNAme(String userNames);
	
}
