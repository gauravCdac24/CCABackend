package in.lms.cca.service.Impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.OTP;
import in.lms.cca.entity.UserLogin;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.repository.OTPRepository;
import in.lms.cca.repository.UserLoginRepository;
import in.lms.cca.service.IOTPService;
import in.lms.cca.util.global.Generator;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class OTPService implements IOTPService {

	@Autowired
	private UserLoginRepository userLoginRepo;
	
	@Autowired
	private OTPRepository otpRepo;
	
	@Autowired
	private NotificationClientServiceImpl notificationServ;
	
	private Long otpDuration = 5 * 60 * 1000L;
	
	@Override
	public void generateOtp(String userName) {
		
		Optional<UserLogin> ulogin = userLoginRepo.findByUserName(userName);
		
		List<OTP> otpList = otpRepo.findAllActiveOtpByUserName(userName);
		
		if (!otpList.isEmpty()) {
            for (OTP otp : otpList) {
                otp.setStatus("Inactive");
            }
            otpRepo.saveAll(otpList); 
        }
    
		
		
		if(ulogin.isPresent()) {
			//String mobileOtp = Generator.generateOTP();
			//String emailOtp = Generator.generateOTP();

			String mobileOtp = "123456";
			String emailOtp = "123456";
			
			System.out.println("Mobile OTP: "+ mobileOtp );
			System.out.println("Email OTP: "+ emailOtp );
			
			OTP otp = new OTP();
			
			otp.setEmailOTP(emailOtp);
			otp.setMobileOTP(mobileOtp);
			otp.setExpiry(Instant.now().plusMillis(otpDuration));
			otp.setLoginId(ulogin.get());
			otp.setStatus("Active");
			
			otpRepo.save(otp);
			
			//Notification
			
			String title = "CCA Login OTP";
			String message = "Please do not share your OTP with anyone, your OTP is " + emailOtp;
			
			NotificationsRequest notification = new NotificationsRequest();
			
			notification.setUsername(ulogin.get().getUserName());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & SMS");
			notification.setNotificationDescription("OTP sent.");
			
			notificationServ.sendNotification(notification);
			
			
		}else {
			
			throw new IllegalArgumentException("User not found");
			
		}
		
	}

	@Override
	public boolean verifyEmailOtp(String otp, String userName) {
		
		List<OTP> uotp = otpRepo.findAllActiveOtpByUserName(userName);
		
		if(uotp.size()>0) {
		
			if(uotp.get(0).getEmailOTP().equals(otp)) {
				return true;
			}
			
		}
		return false;
		
	}
	
	
	@Override
	public boolean verifyMobileOtp(String otp, String userName) {
		
		List<OTP> uotp = otpRepo.findAllActiveOtpByUserName(userName);
		
		if(uotp.size()>0) {
		
			if(uotp.get(0).getMobileOTP().equals(otp)) {
				return true;
			}
			
		}
		return false;
		
	}

	@Override
	public void generateForgotOTP(String email) {
		
		
		Optional<UserLogin> ulogin = userLoginRepo.findByEmail(email);
		
		List<OTP> otpList = otpRepo.findAllActiveOtpByUserName(ulogin.get().getUserName());
		
		if (!otpList.isEmpty()) {
            for (OTP otp : otpList) {
                otp.setStatus("Inactive");
            }
            otpRepo.saveAll(otpList); 
        }
    
		
		
		if(ulogin.isPresent()) {
			
			//String emailOtp = Generator.generateOTP();

			String emailOtp = "123456";
			
			System.out.println("Email OTP: "+ emailOtp );
			
			OTP otp = new OTP();
			
			otp.setEmailOTP(emailOtp);
			otp.setMobileOTP(emailOtp);
			otp.setExpiry(Instant.now().plusMillis(otpDuration));
			otp.setLoginId(ulogin.get());
			otp.setStatus("Active");
			
			otpRepo.save(otp);
			
			//Email
			
			String title = "CCA Forgot Password OTP";
			String message = "Please do not share your OTP with anyone, your OTP is " + emailOtp;
			
			NotificationsRequest notification = new NotificationsRequest();
			
			notification.setUsername(ulogin.get().getUserName());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & SMS");
			notification.setNotificationDescription("Forgot Password OTP sent.");

			notificationServ.sendNotification(notification);
			
			
		}else {
			
			throw new IllegalArgumentException("User not found");
			
		}
	
	}
		
		
		
		
		
		

}
