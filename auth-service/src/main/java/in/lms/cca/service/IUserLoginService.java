package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.UserLogin;

public interface IUserLoginService {

	UserLogin saveNewUserLogin(UserLogin ulogin);
	Optional<UserLogin> findUserLoginByUserName(String username);
	Optional<UserLogin> findUserLoginByEmailId(String emailId);
	UserLogin changeUserLoginStatusByUserLoginId(Integer userLoginId);
	boolean changePassword(String username, String newPassword);
	UserLogin statusChangeByLoginId(Integer loginId);
	UserLoginDTO changeUserStatusById(String userId);
	UserLoginDTO changeUserAuditAgencyStatusById(String userId);
	List<UserLoginDTO> getAllUserLogin();
	Optional<UserLogin> findUserLoginByUsersId(String userId);
	Optional<UserLogin> findIntentUserLoginByUsersId(String userId);
	Optional<UserLogin> findUserLoginByLoginId(String loginId);
	void sendCredentials(UserLogin userLogin);
	Optional<UserLogin> findUserLoginByMobileNo(String mobile);
	List<UserLogin> getAllIntentLogins();
	UserLogin getIntentLoginByUserId(String userId);
	UserLogin updateNewUserLogin(UserLogin ulogin);
	List<Long> getRolebyUserId(String userId);
	List<StaffLoginDTO> getDetailsbyUserId(String decryptedId);
	List<Long> getRolebyUserName(String userName);
	List<UserLogin> getAllUserAccountDetails();
	Optional<UserLogin> findUserLoginByUserId(String userId);
	UserLogin findUserByUserName(String userName);
}
