package in.lms.cca.service;

import java.util.List;

import in.lms.cca.dto.UserLoginDTO;

public interface IUserLoginService {

	void addUser(UserLoginDTO newUser) throws Exception;


	UserLoginDTO changeLoginStatusById(String userId);


	void addUserAuditAgency(UserLoginDTO newUser)throws Exception;


	UserLoginDTO changeAuditAgencyRoleStatusById(String userId);


	List<UserLoginDTO> getAllUserLogins();


	List<UserLoginDTO> getAllIntentUserLogins();


	void changeIntentLoginStatusByUserId(String userId, String updatedBy);


	List<UserLoginDTO> getAllUserLoginsAccount();

}
