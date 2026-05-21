package in.lms.cca.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.config.CrossOrigins;
import in.lms.cca.dto.GetRoles;
import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.RefreshToken;
import in.lms.cca.entity.UserLogin;
import in.lms.cca.entity.UserRoles;
import in.lms.cca.exceptions.AccountInactiveException;
import in.lms.cca.exceptions.TokenRefreshException;
import in.lms.cca.payload.AuthenticationRequest;
import in.lms.cca.payload.AuthenticationResponse;
import in.lms.cca.payload.PasswordChangeRequest;
import in.lms.cca.payload.TokenRefreshRequest;
import in.lms.cca.payload.TokenRefreshResponse;
import in.lms.cca.payload.VerifyForgotOTPRequest;
import in.lms.cca.payload.VerifyOTPRequest;
import in.lms.cca.security.JwtTokenUtil;
import in.lms.cca.service.IOTPService;
import in.lms.cca.service.IRefreshTokenService;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.service.IUserRolesService;
import in.lms.cca.service.security.CustomUserDetailsService;
import in.lms.cca.util.api.AuthServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(AuthServiceAPIs.AUTH_SERVICE_BASE_URL)
@CrossOrigin(CrossOrigins.Origins)
public class UserLoginController {
	
	@Autowired
	private IUserLoginService userLoginService;
	
	@Autowired
	private IUserRolesService userRoleService;
	
	
	@Autowired
	private IRefreshTokenService refreshTokenService;
	
	@Autowired
	private IOTPService otpService;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	//Login for Intent
	@PostMapping(AuthServiceAPIs.ADD_NEW_USER_LOGIN_DETAILS)
	public ResponseEntity<?> addNewUserLoginDetails(@RequestBody UserLoginDTO ulogin){
		
		
		//Server Side Validation
		
		if(!ulogin.getEmailId().trim().matches("^[a-zA-Z0-9_.]{1,}[@]{1}[A-Za-z]{1,}[.]{1}[a-zA-Z]{1,}"))
			return new ResponseEntity<>("Invalid Email Id", HttpStatus.BAD_REQUEST);

		if(!ulogin.getMobile().trim().matches("^[6-9]\\d{9}$"))
			return new ResponseEntity<>("Invalid Mobile Number", HttpStatus.BAD_REQUEST);
			
		if(EncryptionUtil.decrypt(ulogin.getCreatedBy())==null) {
			ulogin.setCreatedBy(EncryptionUtil.encrypt(ulogin.getCreatedBy()));
		}
		
		if(EncryptionUtil.decrypt(ulogin.getUpdatedBy())==null) {
			ulogin.setUpdatedBy(EncryptionUtil.encrypt(ulogin.getUpdatedBy()));
		}
		
		if(EncryptionUtil.decrypt(ulogin.getUserId())==null) {
			ulogin.setUserId(EncryptionUtil.encrypt(ulogin.getUserId()));
		}
		
		//Check for duplicate email id
		Optional<UserLogin> chkLogin = userLoginService.findUserLoginByEmailId(ulogin.getEmailId());
		
		if(chkLogin.isPresent()) {
			return new ResponseEntity<>("Email id already exists.", HttpStatus.BAD_REQUEST);
		}
		
		//Check for duplicate mobile number
		Optional<UserLogin> chkMobile = userLoginService.findUserLoginByMobileNo(ulogin.getMobile());
		
		if(chkMobile.isPresent()) {
			return new ResponseEntity<>("Mobile Number already exists.", HttpStatus.BAD_REQUEST);
		}
		
		if(chkLogin.isEmpty() && chkMobile.isEmpty()) {
			
			UserLogin nlogin = new UserLogin();
			
			nlogin.setEmailId(ulogin.getEmailId());
			nlogin.setMobile(ulogin.getMobile());
			nlogin.setStatus("Active");
			nlogin.setUpdatedBy(ulogin.getUpdatedBy());
			nlogin.setCreatedBy(ulogin.getCreatedBy());
			nlogin.setUserId(ulogin.getUserId());	
			nlogin.setSalutation(ulogin.getSalutation());
			nlogin.setFirstName(nlogin.getFirstName());
			nlogin.setMiddleName(nlogin.getMiddleName());
			nlogin.setLastName(nlogin.getLastName());
			
			
			nlogin = userLoginService.saveNewUserLogin(nlogin);
			
			UserRoles newUserRole = new UserRoles();
			
			newUserRole.setRoleName("ROLE_APPLICANT");
			newUserRole.setStatus("Active");
			newUserRole.setCreatedBy(ulogin.getCreatedBy());
			newUserRole.setUpdatedBy(ulogin.getUpdatedBy());
			newUserRole.setLoginId(nlogin);
			
			userRoleService.saveUserRole(newUserRole);
			
			
			return new ResponseEntity<>("User successfully created.", HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>("Email Id and Mobile Number already exists.", HttpStatus.BAD_REQUEST);
		}
				
		
	}
	
	
	//Login for Audit Agency
	@PostMapping(AuthServiceAPIs.ADD_NEW_USER_CREATE_BY_AUDIT_AGENCY)
	public ResponseEntity<?> addNewUserCreateByAuditAgency(@RequestBody UserLoginDTO ulogin){
		
		//Server Side Validation
		
		System.out.println("hggug=--->"+ulogin.toString());
		
//		if(!ulogin.getEmailId().trim().matches("^[a-zA-Z0-9_.]{1,}[@]{1}[A-Za-z]{1,}[.]{1}[a-zA-Z]{1,}"))
//			return new ResponseEntity<>("Invalid Email Id", HttpStatus.CREATED);
//
//		if(!ulogin.getMobile().trim().matches("^[6-9]\\d{9}$"))
//			return new ResponseEntity<>("Invalid Mobile Number", HttpStatus.CREATED);
			
		if(EncryptionUtil.decrypt(ulogin.getCreatedBy())==null) {
			ulogin.setCreatedBy(EncryptionUtil.encrypt(ulogin.getCreatedBy()));
		}
		
		if(EncryptionUtil.decrypt(ulogin.getUpdatedBy())==null) {
			ulogin.setUpdatedBy(EncryptionUtil.encrypt(ulogin.getUpdatedBy()));
		}
		
		if(EncryptionUtil.decrypt(ulogin.getUserId())==null) {
			ulogin.setUserId(EncryptionUtil.encrypt(ulogin.getUserId()));
		}
		
		System.out.println("hggug=--->"+ulogin.toString());
		//Check for duplicate email id
		Optional<UserLogin> chkLogin = userLoginService.findUserLoginByEmailId(ulogin.getEmailId());
				
		if(chkLogin.isPresent()) {
			return new ResponseEntity<>("Email id already exists.", HttpStatus.BAD_REQUEST);
		}
				
		//Check for duplicate mobile number
		Optional<UserLogin> chkMobile = userLoginService.findUserLoginByMobileNo(ulogin.getMobile());
				
		if(chkMobile.isPresent()) {
			return new ResponseEntity<>("Mobile Number already exists.", HttpStatus.BAD_REQUEST);
		}
		System.out.println("hggug=--->"+ulogin.toString());
		if(chkLogin.isEmpty() && chkMobile.isEmpty()) {
			
			UserLogin nlogin = new UserLogin();
			
			nlogin.setFirstName(ulogin.getFirstName());
			nlogin.setEmailId(ulogin.getEmailId());
			nlogin.setMobile(ulogin.getMobile());
			nlogin.setStatus("Active");
			nlogin.setUpdatedBy(ulogin.getUpdatedBy());
			nlogin.setCreatedBy(ulogin.getCreatedBy());
			nlogin.setUserId(ulogin.getUserId());	
			
			nlogin = userLoginService.saveNewUserLogin(nlogin);
			
			UserRoles newUserRole = new UserRoles();
			
			newUserRole.setRoleName("ROLE_AUDIT_AGENCY");
			newUserRole.setStatus("Active");
			newUserRole.setCreatedBy(ulogin.getCreatedBy());
			newUserRole.setUpdatedBy(ulogin.getUpdatedBy());
			newUserRole.setLoginId(nlogin);
			
			userRoleService.saveUserRole(newUserRole);
			
			
			return new ResponseEntity<>("User successfully added.", HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>("Email Id and Mobile Number already exists.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	//Login for CCA Staff
	
	@PostMapping(AuthServiceAPIs.ADD_NEW_CCA_STAFF_LOGIN)
	public ResponseEntity<?> addNewCCAStaffLoginDetails(@RequestBody StaffLoginDTO ulogin){
		
		//Server Side Validation
		
		
		
		System.out.println("hggug=--->"+ulogin.toString());
		
//		if(!ulogin.getEmailId().trim().matches("^[a-zA-Z0-9_.]{1,}[@]{1}[A-Za-z]{1,}[.]{1}[a-zA-Z]{1,}"))
//			return new ResponseEntity<>("Invalid Email Id", HttpStatus.BAD_REQUEST);

		if(!ulogin.getMobile().trim().matches("^[6-9]\\d{9}$"))
			return new ResponseEntity<>("Invalid Mobile Number", HttpStatus.BAD_REQUEST);
			
		if(EncryptionUtil.decrypt(ulogin.getCreatedBy())==null) {
			ulogin.setCreatedBy(EncryptionUtil.encrypt(ulogin.getCreatedBy()));
		}
		
		if(EncryptionUtil.decrypt(ulogin.getUpdatedBy())==null) {
			ulogin.setUpdatedBy(EncryptionUtil.encrypt(ulogin.getUpdatedBy()));
		}
		
		if(EncryptionUtil.decrypt(ulogin.getUserId())==null) {
			ulogin.setUserId(EncryptionUtil.encrypt(ulogin.getUserId()));
		}
		
		
		//Check for duplicate email id
		Optional<UserLogin> chkLogin = userLoginService.findUserLoginByEmailId(ulogin.getEmailId());
		
		//Check for duplicate mobile number
		Optional<UserLogin> chkMobile = userLoginService.findUserLoginByMobileNo(ulogin.getMobile());
		
		if(chkMobile.isPresent() && chkLogin.isPresent()) {
			
		}else if(chkMobile.isPresent()) {
			return new ResponseEntity<>("Mobile Number already exists.", HttpStatus.BAD_REQUEST);
		}else if(chkLogin.isPresent()) {
			return new ResponseEntity<>("Email id already exists.", HttpStatus.BAD_REQUEST);
		}
		
		try {
		
		if(chkLogin.isEmpty() && chkMobile.isEmpty()) {
			
			UserLogin nlogin = new UserLogin();
			nlogin.setSalutation(ulogin.getSalutation());
			nlogin.setFirstName(ulogin.getFirstName());
			nlogin.setMiddleName(ulogin.getMiddleName());
			nlogin.setLastName(ulogin.getLastName());
			nlogin.setEmailId(ulogin.getEmailId());
			nlogin.setMobile(ulogin.getMobile());
			nlogin.setStatus("Active");
			nlogin.setUpdatedBy(ulogin.getUpdatedBy());
			nlogin.setCreatedBy(ulogin.getCreatedBy());
			nlogin.setUserId(ulogin.getUserId());	
			
			nlogin = userLoginService.saveNewUserLogin(nlogin);
			
			
			
			for(Object r: ulogin.getRoles()) {
			
				if(!r.equals("ADMIN")&&!r.equals("APPLICANT")&&!r.equals("AUDIT_AGENCY")&&!r.equals("LICENSEE")) {
					UserRoles newUserRole = new UserRoles();
					newUserRole.setRoleName("ROLE_"+r);
					newUserRole.setStatus("Active");
					newUserRole.setCreatedBy(ulogin.getCreatedBy());
					newUserRole.setUpdatedBy(ulogin.getUpdatedBy());
					newUserRole.setLoginId(nlogin);
					userRoleService.saveUserRole(newUserRole);
				}
			}
			
			
			return new ResponseEntity<>("User successfully created.", HttpStatus.OK);
			
		}else {
			
			UserLogin ul = chkLogin.get();
			
			
			for(Object r: ulogin.getRoles()) {
				
				if(!r.equals("ADMIN")&&!r.equals("APPLICANT")&&!r.equals("AUDIT_AGENCY")&&!r.equals("LICENSEE")) {
					UserRoles newUserRole = new UserRoles();
					newUserRole.setRoleName("ROLE_"+r);
					newUserRole.setStatus("Active");
					newUserRole.setCreatedBy(ulogin.getCreatedBy());
					newUserRole.setUpdatedBy(ulogin.getUpdatedBy());
					newUserRole.setLoginId(ul);
					userRoleService.saveUserRole(newUserRole);
				}
			}
			
			
				return new ResponseEntity<>("Roles successfully assigned", HttpStatus.OK);
			
			}
		}catch(Exception e) {
			
			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
			
		}
				
		
	}
	
	
	
	// Change login status by login id
	@GetMapping(AuthServiceAPIs.LOGIN_STATUS_CHANGE_BY_ID)
	public void loginStatusChange(@RequestParam("qid") String id){
			
		String bid = EncryptionUtil.decrypt(id);

		try {
			
			userLoginService.statusChangeByLoginId(Integer.parseInt(bid));	
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		
	}
	
	//Change the account status of intent
	
	 @GetMapping(AuthServiceAPIs.CHANGE_INTENT_LOGIN_STATUS+"/userId")
	    public ResponseEntity<?> changeRoleStatus(@PathVariable String userId) {
	        try {
	        	 String decryptedId = EncryptionUtil.encrypt(userId);
	            UserLoginDTO userLoginDTO = userLoginService.changeUserStatusById(decryptedId);

	            if (userLoginDTO != null) {
	                return ResponseEntity.ok(userLoginDTO);
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	            }
	        } catch (NumberFormatException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating role status");
	        }
	    }

	 @GetMapping("/statusauditagency/{userId}")
	    public ResponseEntity<?> changeRoleAditAgencyStatus(@PathVariable String userId) {
	    	System.out.println("AuthServ==>"+userId);
	        try {
	            // Retrieve the RoleDTO by roleId (parsing the string ID to Long)
	        	System.out.println("AuthServ==>1"+userId);
	        	 String decryptedId = EncryptionUtil.encrypt(userId);
	        	 System.out.println("AuthServ56==>"+decryptedId);
	            UserLoginDTO userLoginDTO = userLoginService.changeUserAuditAgencyStatusById(decryptedId);

	            if (userLoginDTO != null) {
	                // Return the updated role entity with an OK status
	                return ResponseEntity.ok(userLoginDTO);
	            } else {
	                // Return a not found response if the role does not exist
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	            }
	        } catch (NumberFormatException e) {
	            // Handle the case where the roleId cannot be parsed to a Long
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
	        } catch (Exception e) {
	            // Handle any other exceptions
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating role status");
	        }
	    }
	@PostMapping(AuthServiceAPIs.AUTHENTICATE)
	public ResponseEntity<?> generateOtp(@RequestBody AuthenticationRequest request) throws Exception {
		
		
		
		//validate username
		if(!request.getUsername().trim().matches("^[A-Za-z0-9]+$"))
			return new ResponseEntity<>("Invalid Username", HttpStatus.CREATED);
		
		if(request.getUsername().trim().length()>20)
			return new ResponseEntity<>("Invalid Username", HttpStatus.CREATED);
		
		
		Optional<UserLogin> ulogin = userLoginService.findUserLoginByEmailId(request.getUsername());
		
		
		if(ulogin.isEmpty()) {
			
			ulogin = userLoginService.findUserLoginByUserName(request.getUsername());	
		}
		
		
		
		if(ulogin.isEmpty()) {
			return new ResponseEntity<>("Username or password is wrong.", HttpStatus.BAD_REQUEST);
		}
		
		if(ulogin.get().getStatus().equals("Inactive")) {
			return new ResponseEntity<>("Your account is Inactive, Please contact to Admin...", HttpStatus.BAD_REQUEST);
		}

		
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  
		
		
		if(ulogin.isPresent() && encoder.matches(request.getPassword(), ulogin.get().getPassword())){
			
			
			request.setUsername(ulogin.get().getUserName());
			
			try {
				otpService.generateOtp(request.getUsername());
			
				return new ResponseEntity<>(EncryptionUtil.encrypt("SUCCESS"), HttpStatus.CREATED);
			}catch(Exception e) {
				return new ResponseEntity<>("Please check your internet connection and try again.", HttpStatus.BAD_REQUEST);
			}

			
		}else {
			return new ResponseEntity<>("Username or Password is wrong.", HttpStatus.BAD_REQUEST);
		}
	
	}
	
	
	
	@PostMapping(AuthServiceAPIs.VERIFY_OTP)
	public ResponseEntity<?> verifyOtpAndGenerateJwt(@RequestBody VerifyOTPRequest verifyRequest,
			HttpServletResponse response) throws Exception {

		
		
		
		Optional<UserLogin> user = userLoginService.findUserLoginByUserName(verifyRequest.getUserName());
		
		
		
		if(user.isEmpty()) {
			
			user = userLoginService.findUserLoginByEmailId(verifyRequest.getUserName());	
		}
				
		
		Boolean emailOtp = otpService.verifyEmailOtp(verifyRequest.getOtp().trim(), user.get().getUserName());
		Boolean mobileOtp = otpService.verifyMobileOtp(verifyRequest.getOtp().trim(), user.get().getUserName());
		
		if (emailOtp == false && mobileOtp == false) {
			return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
		}

		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.get().getUserName());
		
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get().getLoginId());
		
		List<UserRoles> uroles = userRoleService.getAllActiveRolesByLoginId(user.get().getLoginId());
		
		List<String> roles = uroles.stream().map((i) -> i.getRoleName().toString()).collect(Collectors.toList());

		String msg = EncryptionUtil.encrypt("VERIFIED");
		
		String name = (user.get().getSalutation()!=null?user.get().getSalutation()+" ":"") + (user.get().getFirstName()!=null?user.get().getFirstName()+" ":"") + (user.get().getMiddleName()!=null?user.get().getMiddleName()+" ":"")+(user.get().getLastName()!=null?user.get().getLastName()+" ":""); 
		
		return ResponseEntity.ok(
				new AuthenticationResponse(token, refreshToken.getRefreshToken(), roles, user.get().getUserName(), msg, name));
	}



	@PostMapping(AuthServiceAPIs.REFRESH_TOKEN)
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		
		String requestRefreshToken = request.getRefreshToken();
		
		Optional<RefreshToken> rtoken = refreshTokenService.findByToken(requestRefreshToken);

		UserDetails userDetails = userDetailsService.loadUserByUsername(rtoken.get().getLoginId().getUserName());

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getLoginId)
				.map(user -> {
					
					final String token = jwtTokenUtil.generateToken(userDetails);
					
					refreshTokenService.updateRefreshToken(requestRefreshToken);
					
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
					
				})
				
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is invalid!"));
	}
	

	@PostMapping(AuthServiceAPIs.LOGOUT)
	public ResponseEntity<?> logout(jakarta.servlet.http.HttpServletRequest request) {
		try {
			String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return new ResponseEntity<>("Authorization token is required.", HttpStatus.BAD_REQUEST);
			}

			String jwtToken = authHeader.substring(7);
			String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

			Optional<UserLogin> user = userLoginService.findUserLoginByUserName(username);
			if (user.isEmpty()) {
				return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
			}

			// Delete all refresh tokens for this user
			refreshTokenService.deleteByloginId(user.get().getLoginId());

			return new ResponseEntity<>("Logged out successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error during logout.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(AuthServiceAPIs.CHANGE_PASSWORD)
	 public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
		
		
		
		String username = EncryptionUtil.decrypt(request.getUserName());
		String newPassword = EncryptionUtil.decrypt(request.getNewPassword());
		String confirmPassword = EncryptionUtil.decrypt(request.getConfirmPassword());
		String currentPassword = EncryptionUtil.decrypt(request.getCurrentPassword());
		
		if(!newPassword.equals(confirmPassword))
			return new ResponseEntity<>("New Password and Confirm Password do not match.", HttpStatus.BAD_REQUEST);
		
		if(!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&!]).{8,20}$"))
			return new ResponseEntity<>("Password must be a minimum of 8 characters, including at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 symbol from @#$%&!, with a maximum length of 20 characters.", HttpStatus.BAD_REQUEST);
		
		
			Optional<UserLogin> ul = userLoginService.findUserLoginByUserName(username.trim());
			
			
			if(!(passwordEncoder.matches(currentPassword,ul.get().getPassword())))
				 return new ResponseEntity<>("You have entered wrong current password.", HttpStatus.BAD_REQUEST);
		
			if((passwordEncoder.matches(newPassword,ul.get().getPassword())))
				return new ResponseEntity<>("Please enter a new password, not the current one.", HttpStatus.BAD_REQUEST);
			
			if((passwordEncoder.matches(newPassword,ul.get().getPreviousPassword())))
				return new ResponseEntity<>("You have already used this password, please enter new one.", HttpStatus.BAD_REQUEST);
			
			
	        Boolean b = userLoginService.changePassword(username, newPassword);
	        if(b)
	        	return new ResponseEntity<>("Your Password has been successfully changed.", HttpStatus.OK);
	        else
	        	return new ResponseEntity<>("Error in changing the password.", HttpStatus.BAD_REQUEST);
	        
	        
	}
	
	
	
	@GetMapping({AuthServiceAPIs.VALIDATE_FORGOT_PASSWORD_EMAIL})
	public ResponseEntity<?> forgotPassword(@RequestParam("qid") String userEmail){
		
			String email = EncryptionUtil.decrypt(userEmail);
			
			Optional<UserLogin> user = userLoginService.findUserLoginByEmailId(email);
			
			
			if(user.isEmpty()) {
				return new ResponseEntity<>("No user found associated with this email id.", HttpStatus.BAD_REQUEST);
			}
			
			try {
				otpService.generateForgotOTP(email);
				
				return new ResponseEntity<>(EncryptionUtil.encrypt("SUCCESS"), HttpStatus.CREATED);
			}catch(Exception e) {
				return new ResponseEntity<>("Please check your internet connection and try again.", HttpStatus.BAD_REQUEST);
			}
	}
	
	
	
	@PostMapping({AuthServiceAPIs.VALIDATE_FORGOT_OTP})
	public ResponseEntity<?> verifyForgotOTP(@RequestBody VerifyForgotOTPRequest verifyRequest,
			HttpServletResponse response) throws Exception {

		String emailid = EncryptionUtil.decrypt(verifyRequest.getQid());
		String votp = EncryptionUtil.decrypt(verifyRequest.getOtp());
		
		Optional<UserLogin> user = userLoginService.findUserLoginByEmailId(emailid);
		Boolean otp = otpService.verifyEmailOtp(votp.trim(), user.get().getUserName());
		
		if (otp == false) {
			return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
		}
		
		
		

		userLoginService.sendCredentials(user.get());
		
		
		return new ResponseEntity<>("Your username and password has been sent to your registered email id.", HttpStatus.OK);
	}
	
	
	@GetMapping(AuthServiceAPIs.GET_USER_BY_USERNAME + "/{username}")
	public ResponseEntity<?> getUserByLoginId(@PathVariable String username){
		
		String uname = username;
		
		UserLogin ulogin = userLoginService.findUserLoginByUserName(uname).get();
		
		return new ResponseEntity<>(ulogin, HttpStatus.OK);
	}
	
	@GetMapping(AuthServiceAPIs.GET_NEW_LOGIN_OTP)
	public ResponseEntity<?> getNewLoginOTP(@RequestParam("qid") String qid){
		
				String username = EncryptionUtil.decrypt(qid);
			
				Optional<UserLogin> ulogin = userLoginService.findUserLoginByEmailId(username);
				
				
				if(ulogin.isEmpty()) {
					
					ulogin = userLoginService.findUserLoginByUserName(username);	
				}
				
				
				
				if(ulogin.isEmpty()) {
					return new ResponseEntity<>("Username is wrong.", HttpStatus.BAD_REQUEST);
				}
				
				if(ulogin.get().getStatus().equals("Inactive")) {
					throw new AccountInactiveException("Your account is Inactive, Please contact to Admin...");
				}

				
				if(ulogin.isPresent()){
					
		
					
					otpService.generateOtp(username);
					
					return new ResponseEntity<>("Otp has been sent to your email id.", HttpStatus.CREATED);

					
				}else {
					return new ResponseEntity<>("Username is wrong.", HttpStatus.BAD_REQUEST);
				}
	}
	
	
	@GetMapping(AuthServiceAPIs.GET_NEW_FORGOT_OTP)
	public ResponseEntity<?> getNewForgotOTP(@RequestParam("qid") String qid){
		
				String emailid = EncryptionUtil.decrypt(qid);
				
				Optional<UserLogin> ulogin = userLoginService.findUserLoginByEmailId(emailid);
				
				
				
				if(ulogin.isEmpty()) {
					return new ResponseEntity<>("Email id is invalid.", HttpStatus.BAD_REQUEST);
				}
				
				if(ulogin.get().getStatus().equals("Inactive")) {
					throw new AccountInactiveException("Your account is Inactive, You cannot reset your password now, please contact to Admin...");
				}

				
				if(ulogin.isPresent()){
					
		
					
					otpService.generateForgotOTP(emailid);
					
					return new ResponseEntity<>("Otp has been sent to your email id.", HttpStatus.CREATED);

					
				}else {
					return new ResponseEntity<>("Email id is invalid.", HttpStatus.BAD_REQUEST);
				}
	}

	
	@GetMapping("/alls")
	public ResponseEntity<?> getAllRoles() {
	    try {
	        List<UserLoginDTO> userLogins = userLoginService.getAllUserLogin();
	        
	        System.out.println(userLogins.size());
	        List<UserLogin> ulogins = new ArrayList<>();

	        for (UserLoginDTO userLoginDTO : userLogins) {
	            UserLogin ulogin = userLoginService.findUserLoginByUsersId(userLoginDTO.getUserId()).orElse(null);
	            if (ulogin != null) {
	            	
	                ulogins.add(ulogin);
	            }
	        }

	        // Return the list of UserLogin objects
	        return new ResponseEntity<>(ulogins, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("An error occurred while retrieving roles.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/alles")
	public ResponseEntity<?> getAllIntentRoles() {
	    try {
	        List<UserLoginDTO> userLogins = userLoginService.getAllUserLogin();
	        List<UserLogin> ulogins = new ArrayList<>();

	        for (UserLoginDTO userLoginDTO : userLogins) {
	            UserLogin ulogin = userLoginService.findIntentUserLoginByUsersId(userLoginDTO.getUserId()).orElse(null);
	            if (ulogin != null) {
	            	
	                ulogins.add(ulogin);
	            }
	        }

	        // Return the list of UserLogin objects
	        return new ResponseEntity<>(ulogins, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("An error occurred while retrieving roles.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	/*-------------*/
	//
	@GetMapping(AuthServiceAPIs.GET_ALL_INTENT_LOGINS)
	public ResponseEntity<?> getAllIntentLogins() {
		
		try {
		
			List<UserLogin> ulogins = userLoginService.getAllIntentLogins();
			
			return new ResponseEntity<>(ulogins, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuthServiceAPIs.CHANGE_INTENT_ACCOUNT_STATUS)
	public ResponseEntity<?> changeIntentAccountStatus(@RequestParam("userId") String userId, @RequestParam("updatedBy") String updatedBy) {
		
		
		
		try {
		
			UserLogin u = userLoginService.getIntentLoginByUserId(userId);
		
			
			
			if(u.getStatus().equals("Active"))
				u.setStatus("Inactive");
			else
				u.setStatus("Active");
			
			UserLogin u1 = userLoginService.updateNewUserLogin(u);
			if(u1 == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping("/getrolebyuserid/{userId}")
	public ResponseEntity<?> getRoleByUserId(@PathVariable String userId) {
	    System.out.println("AuthServ==>" + userId);
	    try {
	        // Decrypt the userId
	        String decryptedId = EncryptionUtil.encrypt(userId);
	        System.out.println("Decrypted ID==>" + decryptedId);

	        // Get the list of login IDs based on the decrypted userId
	        List<Long> loginIds = userLoginService.getRolebyUserId(decryptedId);
	        System.out.println("loginIds size---->" + loginIds.size());

	        // Create a list for GetRoles DTO
	        List<GetRoles> rolesDtoList = new ArrayList<>();

	        // Specify roles to exclude
	        List<String> excludedRoles = List.of("APPLICANT", "ADMIN", "AUDIT_AGENCY", "LICENSEE");

	        // Iterate through each loginId to get user roles
	        for (Long loginId : loginIds) {
	            // Get the user roles for each loginId
	            List<UserRoles> userRoles = userRoleService.getAllRolesByLoginId(loginId.intValue());
	            System.out.println("userRoles------>" + userRoles.toString());

	            // If no roles are found, add a GetRoles object with null roles
	            if (userRoles.isEmpty()) {
//	                GetRoles getRoles = new GetRoles();
//	                getRoles.setRoleId(null);
//	                rolesDtoList.add(getRoles);
	            	return ResponseEntity.ok(null);
	            } else {
	                // Map UserRoles to GetRoles DTO
	                for (UserRoles role : userRoles) {
	                    String roleName = role.getRoleName(); // Get the role name
	                    // Only add the role if it's not in the excluded list
	                    if (!excludedRoles.contains(roleName)) {
	                        GetRoles getRoles = new GetRoles();
	                        getRoles.setRoleName(roleName);
	                        getRoles.setRoleId(String.valueOf(role.getUserRoleId()));
	                        getRoles.setStatus(role.getStatus());
	                        rolesDtoList.add(getRoles);
	                    }
	                }
	            }
	        }

	        // Return the roles DTO list with an OK status
	        return ResponseEntity.ok(rolesDtoList);

	    } catch (NumberFormatException e) {
	        // Handle invalid user ID format
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
	    } catch (Exception e) {
	        // Handle any other exceptions
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching roles");
	    }
	}

	@GetMapping("/change-user-role-status/{loginId}")
	public ResponseEntity<?> changeUserRoleStatus(@PathVariable String loginId) {
	    System.out.println("AuthServ==> " + loginId);
	    try {
	        // Decrypt the loginId to retrieve the user ID
	        String decryptedId = EncryptionUtil.decrypt(loginId);
	        
	        // Convert decryptedId to Long (assuming decryptedId is the userRoleId)
	        Long roleId = Long.parseLong(decryptedId);
	        
	        // Fetch the user role by ID
	        UserRoles userRole = userRoleService.getRuserRoleById(roleId);
	        
	        // Check and toggle status
	        if (userRole != null) {
	            if (userRole.getStatus().equalsIgnoreCase("Active")) {
	                userRole.setStatus("Inactive");
	            } else {
	                userRole.setStatus("Active");
	            }
	            
	            // Update the role status in the database
	            userRoleService.updateRole(userRole);
	            
	            // Return success response
	            return ResponseEntity.ok("Role status updated successfully.");
	        } else {
	            // Return error if no role found
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
	        }

	    } catch (NumberFormatException e) {
	        // Handle invalid decryptedId format
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format.");
	    } catch (Exception e) {
	        // Handle any other exceptions
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating role status.");
	    }
	}


	@GetMapping("/getrolebyusername/{userName}")
	public ResponseEntity<?> getRoleByUserName(@PathVariable String userName) {
	    System.out.println("AuthServ==>" + userName);
	    try {
	        // Decrypt the userId
	      //  String decryptedId = EncryptionUtil.encrypt(userId);
	        System.out.println("Decrypted ID==>" + userName);

	        // Get the list of login IDs based on the decrypted userId
	        List<Long> loginIds = userLoginService.getRolebyUserName(userName);
	        System.out.println("loginIds size---->" + loginIds.size());

	        // Create a list for GetRoles DTO
	        List<GetRoles> rolesDtoList = new ArrayList<>();

	        // Specify roles to exclude
	        List<String> excludedRoles = List.of("APPLICANT", "ADMIN", "AUDIT_AGENCY", "LICENSEE");

	        // Iterate through each loginId to get user roles
	        for (Long loginId : loginIds) {
	            // Get the user roles for each loginId
	            List<UserRoles> userRoles = userRoleService.getAllActiveRolesByLoginId(loginId.intValue());
	            System.out.println("userRoles------>" + userRoles.toString());

	            // If no roles are found, add a GetRoles object with null roles
	            if (userRoles.isEmpty()) {
	                GetRoles getRoles = new GetRoles();
	                getRoles.setRoleId(null);
	                rolesDtoList.add(getRoles);
	            } else {
	                // Map UserRoles to GetRoles DTO
	                for (UserRoles role : userRoles) {
	                    String roleName = role.getRoleName(); // Get the role name
	                    // Only add the role if it's not in the excluded list
	                    if (!excludedRoles.contains(roleName)) {
	                        GetRoles getRoles = new GetRoles();
	                        getRoles.setRoleName(roleName);
	                        getRoles.setRoleId(String.valueOf(role.getUserRoleId()));
	                        getRoles.setStatus(role.getStatus());
	                        rolesDtoList.add(getRoles);
	                    }
	                }
	            }
	        }

	        // Return the roles DTO list with an OK status
	        return ResponseEntity.ok(rolesDtoList);

	    } catch (NumberFormatException e) {
	        // Handle invalid user ID format
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
	    } catch (Exception e) {
	        // Handle any other exceptions
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching roles");
	    }
	}
	
	//Get user login by username
	
	
	@GetMapping(AuthServiceAPIs.GET_USER_LOGIN_BY_USERNAME)
	public ResponseEntity<?> getUserLoginByUsername(@RequestParam("qid") String username) {
		
		String uname = username;
		
		if(EncryptionUtil.decrypt(uname) != null)
			uname = EncryptionUtil.decrypt(uname);
		
		
		
		try {
				Optional<UserLogin> ul = userLoginService.findUserLoginByUserName(uname.trim());
				
				if(ul.isPresent()) {
					
					
					return new ResponseEntity<>(new UserLogin(ul.get().getUserName(), ul.get().getEmailId(), ul.get().getMobile(), ul.get().getSalutation(), 
							ul.get().getFirstName(), ul.get().getMiddleName(), ul.get().getLastName()), HttpStatus.OK);
				}
				else
					return new ResponseEntity<>(new UserLogin(), HttpStatus.OK);
		}catch(Exception e) {
			
			
			return new ResponseEntity<>(new UserLogin(), HttpStatus.OK);
		}
	}
	
	
	@GetMapping(AuthServiceAPIs.GET_ALL_USER_LOGINS)
	public ResponseEntity<?> getAllUserLoginsAccount() {
		
		try {
					List<UserLogin> list = userLoginService.getAllUserAccountDetails();
					
					List<UserLoginDTO> ulist = new ArrayList<>();
					
					for(UserLogin u: list) {
						
						UserLoginDTO ul = new UserLoginDTO();
						ul.setCreated(u.getCreated().toString());
						ul.setUpdated(u.getUpdated().toString());
						ul.setCreatedBy(u.getCreatedBy());
						ul.setUpdatedBy(u.getUpdatedBy());
						ul.setEmailId(u.getEmailId());
						ul.setFirstName(u.getFirstName());
						ul.setMiddleName(u.getMiddleName());
						ul.setLastName(u.getLastName());
						ul.setMobile(u.getMobile());
						ul.setSalutation(u.getSalutation());
						ul.setStatus(u.getStatus());
						ul.setUserId(u.getUserId());
						ul.setUserName(u.getUserName());
					
						ulist.add(ul);
						
					}
					
					
					
					return new ResponseEntity<>(ulist, HttpStatus.OK);
		}catch(Exception e) {
			
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	//Change Applicant Role
	
	@GetMapping(AuthServiceAPIs.CHANGE_APPLICANT_ROLE_TO_LICENSEE)
	public ResponseEntity<?> changeApplicantRoleToLicensee(@RequestParam("username") String username) {
		
		String uname = username;
		
		if(EncryptionUtil.decrypt(uname) != null)
			uname = EncryptionUtil.decrypt(uname);
		
		
		
		try {
				Optional<UserLogin> ul = userLoginService.findUserLoginByUserName(uname.trim());
				
				List<UserRoles> roles = userRoleService.getAllRolesByLoginId(ul.get().getLoginId());
				
				if(roles.size()>0) {
					
					for(UserRoles r: roles) {
						if(r.getRoleName().equals("ROLE_APPLICANT")) {
							
							r.setRoleName("ROLE_LICENSEE");
							userRoleService.updateRole(r);
							
						}
					}
					
					return new ResponseEntity<>("Role updated Successfully.", HttpStatus.OK);
					
				}
				else
					return new ResponseEntity<>("Invalid Username", HttpStatus.OK);
		}catch(Exception e) {
			
			
			return new ResponseEntity<>("Error in changing the role.", HttpStatus.OK);
		}
	}

    
	@GetMapping("/get-all-detail-by-userId/{userId}")
	public ResponseEntity<?> getUserByUserId(@PathVariable String userId){
		
		
		System.out.println("-=-=-=-=->"+userId);
		
		UserLogin ulogin = userLoginService.findUserLoginByUserId(userId).get();
		
		UserLoginDTO loginDTO =new UserLoginDTO();
		
		loginDTO.setUserName(ulogin.getUserName());
		
		return new ResponseEntity<>(loginDTO, HttpStatus.OK);
	}
	
	@GetMapping(AuthServiceAPIs.CHANGE_LICENSEE_ROLE_TO_EXLICENSEE)
	public ResponseEntity<?> changeLicenseeRoleToExLicensee(@RequestParam("username") String username) {
		
		String uname = username;
		
		if(EncryptionUtil.decrypt(uname) != null)
			uname = EncryptionUtil.decrypt(uname);
		
		
		
		try {
				Optional<UserLogin> ul = userLoginService.findUserLoginByUserName(uname.trim());
				
				List<UserRoles> roles = userRoleService.getAllRolesByLoginId(ul.get().getLoginId());
				
				if(roles.size()>0) {
					
					for(UserRoles r: roles) {
						if(r.getRoleName().equals("ROLE_LICENSEE")) {
							
							r.setRoleName("ROLE_EXLICENSEE");
							userRoleService.updateRole(r);
							
						}
					}
					
					return new ResponseEntity<>("Role updated Successfully.", HttpStatus.OK);
					
				}
				else
					return new ResponseEntity<>("Invalid Username", HttpStatus.OK);
		}catch(Exception e) {
			
			
			return new ResponseEntity<>("Error in changing the role.", HttpStatus.OK);
		}
	}

	
	@GetMapping("/get-all-detail-by-username/{userName}")
	public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
	    System.out.println("Received userName: " + userName);

	   
	    UserLogin ulogin = userLoginService.findUserByUserName(userName);
	    
	    System.out.println("ulogin userName: " + ulogin.getEmailId());
	    
	    UserLoginDTO userLoginDTO = new UserLoginDTO();
	    userLoginDTO.setUserId(ulogin.getUserId());
	    userLoginDTO.setSalutation(ulogin.getSalutation());
	    userLoginDTO.setFirstName(ulogin.getFirstName());
	    userLoginDTO.setMiddleName(ulogin.getMiddleName());
	    userLoginDTO.setLastName(ulogin.getLastName());
	    userLoginDTO.setEmailId(ulogin.getEmailId());
	    userLoginDTO.setMobile(ulogin.getMobile());
	    userLoginDTO.setCreatedBy(ulogin.getCreatedBy());
	    userLoginDTO.setUpdatedBy(ulogin.getUpdatedBy());
	    userLoginDTO.setStatus(ulogin.getStatus());
	    userLoginDTO.setUserName(ulogin.getUserName());


	    return new ResponseEntity<>(userLoginDTO, HttpStatus.OK);
	}

	
}
	


