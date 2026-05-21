package in.lms.cca.util.api;

public class AuthServiceAPIs {
	
	public static final String AUTH_SERVICE_BASE_URL = "/auth-service";

	
	// User APIs
	
	public static final String ADD_NEW_USER_LOGIN_DETAILS = "/add-new-user-login-intent";
	public static final String ADD_NEW_USER_CREATE_BY_AUDIT_AGENCY = "/add-new-user-login-audit-agency";
	public static final String LOGIN_STATUS_CHANGE_BY_ID = "/login-status-change-by-id";
	public static final String REFRESH = "/refresh";
	public static final String AUTHENTICATE = "/authenticate";
	public static final String VERIFY_OTP = "/verify-otp";
	public static final String REFRESH_TOKEN = "/refreshtoken";	
	public static final String CHANGE_PASSWORD = "/change-password";
	public static final String GENERATE_FORGOT_PASSWORD_OTP = "/forgot-password-otp";
	public static final String VALIDATE_FORGOT_OTP = "/validate-forgot-otp";
	public static final String GET_NEW_LOGIN_OTP = "/get-new-login-otp";
	public static final String VALIDATE_FORGOT_PASSWORD_EMAIL = "/validate-forgot-password-email";
	public static final String LOGOUT = "/logout";
	
	//Roles APIs
	public static final String ASSIGN_NEW_ROLE = "/assign-new-role";
	public static final String GET_ALL_ROLES = "/get-All-Roles";
    public static final String GET_ROLE_BY_ID = "/get-role-by-id";
    public static final String UPDATE_ROLE = "/update-role";
    public static final String CHANGE_ROLE_STATUS_BY_ID = "/change-role-status-by-id";
    public static final String DELETE_ROLE = "/delete-role";
	public static final String REVOKE_ROLE = "/revoke-role";


	//
	public static final String GET_USER_BY_USERNAME = "/get-by-username";
	public static final String GET_NEW_FORGOT_OTP = "/get-new-forgot-otp";
	public static final String ADD_NEW_CCA_STAFF_LOGIN = "/add-new-cca-staff-login";


	//
	public static final String CHANGE_INTENT_LOGIN_STATUS = "/change-intent-login-status";
	public static final String CHANGE_AUDIT_AGENCY_LOGIN_STATUS = "/change-audit-agency-login-status";
	public static final String CHANGE_CCA_STAFF_LOGIN_STATUS = "/change-cca-staff-login-status";


	//
	public static final String GET_ALL_INTENT_LOGINS = "/get-all-intent-logins";


	public static final String CHANGE_INTENT_ACCOUNT_STATUS = "/change-intent-account-status";


	//
	public static final String GET_USER_LOGIN_BY_USERNAME = "/get-userlogin-by-username";


	public static final String GET_ALL_USER_LOGINS = "/get-all-user-logins";


	public static final String CHANGE_APPLICANT_ROLE_TO_LICENSEE = "/change-applicant-role-to-licensee";


	public static final String CHANGE_LICENSEE_ROLE_TO_EXLICENSEE =  "/change-licensee-role-to-exlicensee";


	



	


	


	

}
