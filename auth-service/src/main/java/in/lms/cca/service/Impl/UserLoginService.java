package in.lms.cca.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.StaffLoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.UserLogin;
import in.lms.cca.payload.NotificationsDetailsRequest;
import in.lms.cca.repository.UserLoginRepository;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.global.Generator;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserLoginService implements IUserLoginService {
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private NotificationClientServiceImpl notificationServ;

	@Override
	public UserLogin saveNewUserLogin(UserLogin ulogin) {
		
		
		UserLogin u = userLoginRepository.getLastUserLogin();
		
		
		
		String newpwd = Generator.generatePassword(null);
		String newusername = Generator.generateUsername(u != null?u.getLoginId(): 0);
		
		ulogin.setPassword(passwordEncoder.encode(newpwd));
		ulogin.setUserName(newusername);
		
		// Send Email
		
		String title = "CCA: New User Registration";
		String message = "Please do not share login details with others: <br/><b>Username</b>: <i>"+ newusername + "</i><br /><b>" + "Password</b>:  <i>"+newpwd +"</i>";
		
		NotificationsDetailsRequest notification = new NotificationsDetailsRequest();
		notification.setUserEmail(ulogin.getEmailId());
		notification.setUserMobile(ulogin.getMobile());
		notification.setMessage(message);
		notification.setSubject(title);
		notification.setNotificationType("Email & SMS");
		notification.setNotificationDescription("Username and Password sent.");
		notification.setUsername(ulogin.getUserName());
		
		notificationServ.sendNotificationUsingDetails(notification);
		
		UserLogin ul = userLoginRepository.save(ulogin);
		
		return ul;
	
		
	}
	
	@Override
	public UserLogin updateNewUserLogin(UserLogin ulogin) {
		
		if(ulogin.getLoginId() != null) {
			UserLogin ul = userLoginRepository.save(ulogin);
			return ul;
		}	
		else
			return null;
	
		
	}



	@Override
	public Optional<UserLogin> findUserLoginByUserName(String username) {
		
		return userLoginRepository.findByUserName(username);
	}



	@Override
	public Optional<UserLogin> findUserLoginByEmailId(String emailId) {
		
		return userLoginRepository.findByEmail(emailId);
		
	}



	@Override
	public UserLogin changeUserLoginStatusByUserLoginId(Integer userLoginId) {
		
		UserLogin ulogin = userLoginRepository.findByLoginId(userLoginId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		if (ulogin.getStatus().equals("Active")) {
			ulogin.setStatus("Inactive");
		} else if (ulogin.getStatus().equals("Inactive")) {
			ulogin.setStatus("Active");
		}
		
		ulogin.setUpdated(new Date());
		
		ulogin = userLoginRepository.save(ulogin);
		
		return ulogin;
		
		
	}



	@Override
	public boolean changePassword(String username, String newPassword) {
		
		Optional<UserLogin> ulogin = userLoginRepository.findByUserName(username);
		
        if (ulogin.isPresent()) {
        	
            UserLogin u = ulogin.get();
            
            
            	u.setPreviousPassword(u.getPassword());
            	u.setPassword(passwordEncoder.encode(newPassword));
                
               
                userLoginRepository.save(u);
                
                return true;
            
        } else {
            return false;
        }
	}



	@Override
	public UserLogin statusChangeByLoginId(Integer loginId) {
		
		UserLogin ulogin = userLoginRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		if (ulogin.getStatus().equals("Verified")) {
			ulogin.setStatus("NonVerified");
		} else if (ulogin.getStatus().equals("NonVerified")) {
			ulogin.setStatus("Verified");
		}
		
		ulogin.setUpdated(new Date());
		
		ulogin = userLoginRepository.save(ulogin);
		
		return ulogin;
	}



	@Override
	public Optional<UserLogin> findUserLoginByLoginId(String loginId) {
		
		return userLoginRepository.findByLoginId(loginId);
	}



	@Override
	public UserLoginDTO changeUserStatusById(String userId) {
	    try {
	        Optional<UserLogin> userOpt = userLoginRepository.findUserByIdAndRole(userId);
	        
	        if (userOpt.isPresent()) {
	            UserLogin user = userOpt.get();
	            user.setStatus(user.getStatus().equals("Active") ? "Inactive" : "Active");
	            user.setUpdated(new Date());
	            UserLogin updatedUser = userLoginRepository.save(user);
	            
	            UserLoginDTO userLoginDTO = new UserLoginDTO();
	            userLoginDTO.setUserId(updatedUser.getUserId());
	            userLoginDTO.setStatus(updatedUser.getStatus());
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            userLoginDTO.setUpdated(sdf.format(updatedUser.getUpdated()));
	            
	            return userLoginDTO;
	        } else {
	            return null; 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}



	@Override
	public UserLoginDTO changeUserAuditAgencyStatusById(String userId) {
		try {
			System.out.println("AuthServ234==>"+userId);
	        Optional<UserLogin> userOpt = userLoginRepository.findUserByIdAuditAgencyAndRole(userId);
	        
	        if (userOpt.isPresent()) {
	            UserLogin user = userOpt.get();
	            
	            // Toggle the status
	            user.setStatus(user.getStatus().equals("Active") ? "Inactive" : "Active");
	            
	            // Update the timestamp
	            user.setUpdated(new Date());
	            
	            // Save the updated user
	            UserLogin updatedUser = userLoginRepository.save(user);
	            
	            // Convert updatedUser to UserLoginDTO
	            UserLoginDTO userLoginDTO = new UserLoginDTO();
	            userLoginDTO.setUserId(updatedUser.getUserId());
	            userLoginDTO.setStatus(updatedUser.getStatus());
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            userLoginDTO.setUpdated(sdf.format(updatedUser.getUpdated()));
	            // Add any other fields you want to include
	            
	            return userLoginDTO;
	        } else {
	            return null; // or throw a custom exception if preferred
	        }
	    } catch (Exception e) {
	        // Handle exceptions
	        e.printStackTrace();
	        return null;
	    }
	}



	@Override
	public List<UserLoginDTO> getAllUserLogin() {
	    List<UserLogin> roles = userLoginRepository.findAll();
	    List<UserLoginDTO> userLoginDTOs = new ArrayList<>();

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	    for (UserLogin role : roles) {
	        UserLoginDTO userLoginDTO = new UserLoginDTO();
	        userLoginDTO.setUserId(role.getUserId());
	        userLoginDTO.setStatus(role.getStatus());
	        userLoginDTO.setUpdated(sdf.format(role.getUpdated()));
	        // Add any other fields you want to include

	        userLoginDTOs.add(userLoginDTO);
	    }

	    return userLoginDTOs;
	}



	@Override
	public Optional<UserLogin> findUserLoginByUsersId(String userId) {
	    try {
	        // Fetch the user using the repository method
	        Optional<UserLogin> userOpt = userLoginRepository.findUserByIdAuditAgencyAndRole(userId);
	        
	        // Return the fetched user if present
	        return userOpt;
	    } catch (Exception e) {
	        // Log the exception (you may use a logger instead of printing to console)
	        System.err.println("An error occurred while fetching the user login: " + e.getMessage());
	        
	        // Return an empty Optional if an exception occurs
	        return Optional.empty();
	    }
	}

	
	@Override
	public Optional<UserLogin> findIntentUserLoginByUsersId(String userId) {
	    try {
	        // Fetch the user using the repository method
	        Optional<UserLogin> userOpt = userLoginRepository.findUserByIdIntentAndRole(userId);
	        
	        // Return the fetched user if present
	        return userOpt;
	    } catch (Exception e) {
	        // Log the exception (you may use a logger instead of printing to console)
	        System.err.println("An error occurred while fetching the user login: " + e.getMessage());
	        
	        // Return an empty Optional if an exception occurs
	        return Optional.empty();
	    }
	}



	@Override
	public void sendCredentials(UserLogin userLogin) {
		
		
		userLogin.setPreviousPassword(userLogin.getPassword());
		
		String newpwd = Generator.generatePassword(null);
		
		userLogin.setPassword(passwordEncoder.encode(newpwd));
		
		// Send Email
		
		String title = "CCA: Forgot Password Details";
		String message = "Please do not share login details with others: <br/><b>Username</b>: <i>"+ userLogin.getUserName() + "</i><br /><b>" + "Password</b>:  <i>"+newpwd +"</i>";
		
		NotificationsDetailsRequest notification = new NotificationsDetailsRequest();
		notification.setUserEmail(userLogin.getEmailId());
		notification.setUserMobile(userLogin.getMobile());
		notification.setMessage(message);
		notification.setSubject(title);
		notification.setNotificationType("Email & SMS");
		notification.setNotificationDescription("Forgot Username and Password recovered.");
		notification.setUsername(userLogin.getUserName());
		
		notificationServ.sendNotificationUsingDetails(notification);
		
		UserLogin ul = userLoginRepository.save(userLogin);
		
		
		
	}



	@Override
	public Optional<UserLogin> findUserLoginByMobileNo(String mobile) {
		
		return userLoginRepository.findUserLoginByMobileNo(mobile);
	}



	@Override
	public List<UserLogin> getAllIntentLogins() {
		
		return userLoginRepository.findAllIntentLogins();
	}



	@Override
	public UserLogin getIntentLoginByUserId(String userId) {
		
		return userLoginRepository.findIntentLoginByUserId(userId);
	}

	@Override
	public List<Long> getRolebyUserId(String userId) {
		// TODO Auto-generated method stub
		return userLoginRepository.getRolebyUserId(userId);
	}

//	@Override
//	public List<StaffLoginDTO> getDetailsbyUserId(String decryptedId) {
//		 List<StaffLoginDTO> results = userLoginRepository.getDetailsbyUserId(decryptedId);
//
//	        List<StaffLoginDTO> dtoList = new ArrayList<>();
//	        for (Object[] result : results) {
//	            StaffLoginDTO dto = new StaffLoginDTO(
//	                (String) result[0],  // userId
//	                (String) result[1],  // emailId
//	                (String) result[2],  // mobile
//	                (String) result[3],  // createdBy
//	                (String) result[4],  // updatedBy
//	                (String) result[5],  // created
//	                (String) result[6],  // updated
//	                (String) result[7],  // salutation
//	                (String) result[8],  // firstName
//	                (String) result[9],  // middleName
//	                (String) result[10], // lastName
//	                (String) result[11], // roleId
//	                (String) result[12], // roleName
//	                (String) result[13]  // status
//	            );
//	            dtoList.add(dto);
//	        }
//	        return dtoList;
//	    }
	
	
	@Override
	public List<StaffLoginDTO> getDetailsbyUserId(String decryptedId) {
	    return userLoginRepository.getDetailsbyUserId(decryptedId);
	}

	@Override
	public List<Long> getRolebyUserName(String userName) {
		// TODO Auto-generated method stub
		return userLoginRepository.getRolebyUserName(userName);
	}

	@Override
	public List<UserLogin> getAllUserAccountDetails() {
		
		return userLoginRepository.findAll();
	}

	@Override
	public Optional<UserLogin> findUserLoginByUserId(String uname) {
		try {
	        // Fetch the user using the repository method
	        Optional<UserLogin> userOpt = userLoginRepository.findUserByIdAndRoles(EncryptionUtil.encrypt((uname)));
	        
	        // Return the fetched user if present
	        return userOpt;
	    } catch (Exception e) {
	        // Log the exception (you may use a logger instead of printing to console)
	        System.err.println("An error occurred while fetching the user login: " + e.getMessage());
	        
	        // Return an empty Optional if an exception occurs
	        return Optional.empty();
	    }
}

	@Override
	public UserLogin findUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return userLoginRepository.findUserByUserName(userName);
	}

}

