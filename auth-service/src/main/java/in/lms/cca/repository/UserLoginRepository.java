package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.UserLogin;


public interface UserLoginRepository extends JpaRepository<UserLogin, Integer>{

	@Query("SELECT u FROM UserLogin u WHERE u.userName = :uname")
	Optional<UserLogin> findByUserName(@Param("uname") String userName);

	@Query("SELECT u FROM UserLogin u WHERE u.emailId = :uemail")
	Optional<UserLogin> findByEmail(@Param("uemail") String email);
	
	
	
	@Query("SELECT u FROM UserLogin u WHERE u.loginId = :lid")
	Optional<UserLogin> findByLoginId(@Param("lid") Integer loginId);

	@Query("SELECT u FROM UserLogin u ORDER BY u.loginId DESC LIMIT 1")
	public UserLogin getLastUserLogin();

	@Query(value = "SELECT a.login_id,  " +
            "a.user_id, a.first_name, a.salutation, a.middle_name, a.last_name, a.created,a.updated,a.created_by,a.updated_by,a.username," +
            "a.status,a.email_id,a.mobile,a.password,a.previous_password, " +
            "b.role_name  " +
            "FROM auth_users_management.user_login AS a " +
            "JOIN auth_users_management.user_roles AS b " +
            "ON a.login_id = b.login_id " +
            "WHERE a.user_id = :userId " +
            "AND b.role_name = 'ROLE_APPLICANT'", 
    nativeQuery = true)
Optional<UserLogin> findUserByIdAndRole(@Param("userId") String userId);

	@Query(value = "SELECT a.login_id,  " +
            "a.user_id, a.first_name, a.salutation, a.middle_name, a.last_name, a.created,a.updated,a.created_by,a.updated_by,a.username," +
            "a.status,a.email_id,a.mobile,a.password,a.previous_password, " +
            "b.role_name  " +
            "FROM auth_users_management.user_login AS a " +
            "JOIN auth_users_management.user_roles AS b " +
            "ON a.login_id = b.login_id " +
            "WHERE a.user_id = :userId " +
            "AND b.role_name = 'ROLE_AUDIT_AGENCY'", 
    nativeQuery = true)
Optional<UserLogin> findUserByIdAuditAgencyAndRole(@Param("userId") String userId);

	@Query(value = "SELECT a.login_id,  " +
            "a.user_id, a.first_name, a.salutation, a.middle_name, a.last_name, a.created,a.updated,a.created_by,a.updated_by,a.username," +
            "a.status,a.email_id,a.mobile,a.password,a.previous_password, " +
            "b.role_name  " +
            "FROM auth_users_management.user_login AS a " +
            "JOIN auth_users_management.user_roles AS b " +
            "ON a.login_id = b.login_id " +
            "WHERE a.user_id = :userId " +
            "AND b.role_name = 'ROLE_APPLICANT'", 
    nativeQuery = true)
	Optional<UserLogin> findUserByIdIntentAndRole(@Param("userId")String userId);

	@Query("SELECT u FROM UserLogin u WHERE u.loginId = :uid")
	Optional<UserLogin> findByLoginId(@Param("uid") String loginId);

	@Query("SELECT u FROM UserLogin u WHERE u.mobile = :mobile")
	Optional<UserLogin> findUserLoginByMobileNo(@Param("mobile") String mobile);

	
	@Query(value = "FROM UserLogin AS a " +
            "JOIN UserRoles AS b " +
            "ON a.loginId = b.loginId.loginId " +
            "WHERE b.roleName = 'ROLE_APPLICANT'")
	List<UserLogin> findAllIntentLogins();

	@Query(value = "FROM UserLogin AS a " +
            "JOIN UserRoles AS b " +
            "ON a.loginId = b.loginId.loginId " +
            "WHERE a.userId =:userId AND b.roleName = 'ROLE_APPLICANT'")
	UserLogin findIntentLoginByUserId(String userId);

	@Query("SELECT a.loginId FROM UserLogin AS a WHERE a.userId = :userId")
	List<Long> getRolebyUserId(@Param("userId")String userId);

	 @Query("SELECT a.userId, a.emailId, a.mobile, a.createdBy, a.updatedBy, " +
	           "a.created, a.updated, a.salutation, a.firstName, a.middleName, a.lastName, b.userRoleId, b.roleName, a.status " +
	           "FROM UserLogin AS a " +
	           "JOIN UserRoles AS b ON a.loginId = b.loginId.loginId " +
	           "WHERE a.userId = :decryptedId")
	    List<StaffLoginDTO> getDetailsbyUserId(@Param("decryptedId") String decryptedId);

	 @Query("SELECT a.loginId FROM UserLogin AS a WHERE a.userName = :userName AND status='Active'")
	List<Long> getRolebyUserName(@Param("userName")String userName);
	
	 
	 @Query(value = "SELECT a.login_id,  " +
	            "a.user_id, a.first_name, a.salutation, a.middle_name, a.last_name, a.created,a.updated,a.created_by,a.updated_by,a.username," +
	            "a.status,a.email_id,a.mobile,a.password,a.previous_password, " +
	            "b.role_name  " +
	            "FROM auth_users_management.user_login AS a " +
	            "JOIN auth_users_management.user_roles AS b " +
	            "ON a.login_id = b.login_id " +
	            "WHERE a.user_id = :userId " +
	            "AND b.role_name = 'ROLE_AUDIT_AGENCY'", 
	    nativeQuery = true)
	Optional<UserLogin> findUserByIdAndRoles(@Param("userId") String userId);

	 @Query("SELECT u FROM UserLogin u WHERE u.userName = :userName")
	 UserLogin findUserByUserName(@Param("userName")String userName);

	 
}
