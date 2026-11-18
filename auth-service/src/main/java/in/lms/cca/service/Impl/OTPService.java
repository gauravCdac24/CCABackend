package in.lms.cca.service.Impl;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import in.lms.cca.entity.OTP;
import in.lms.cca.entity.UserLogin;
import in.lms.cca.payload.NotificationsDetailsRequest;
import in.lms.cca.repository.OTPRepository;
import in.lms.cca.repository.UserLoginRepository;
import in.lms.cca.service.IOTPService;
import in.lms.cca.util.global.Generator;
import in.lms.cca.util.global.SmsTemplateKey;
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

	@Autowired
	private Environment environment;

	private Long otpDuration = 5 * 60 * 1000L;

	private boolean isTestProfile() {
		return Arrays.asList(environment.getActiveProfiles()).contains("test");
	}

	/**
	 * Same OTP on email + SMS. Sent after DB commit via by-details (no auth callback).
	 * Runs on the request thread so delivery is attempted before /authenticate returns.
	 */
	private void sendOtpNotificationAfterCommit(UserLogin user, String subject, String message,
			String description, String otpValue) {
		NotificationsDetailsRequest notification = new NotificationsDetailsRequest();
		notification.setUsername(user.getUserName());
		notification.setUserEmail(user.getEmailId());
		notification.setUserMobile(user.getMobile());
		notification.setMessage(message);
		notification.setSubject(subject);
		notification.setNotificationType("Email & SMS");
		notification.setNotificationDescription(description);
		notification.setSmsTemplateKey(SmsTemplateKey.OTP);
		notification.setSmsVars(List.of(otpValue));

		Runnable send = () -> {
			try {
				System.out.println("Sending OTP notification to email=" + user.getEmailId()
						+ " mobile=" + user.getMobile());
				String result = notificationServ.sendNotificationUsingDetails(notification);
				System.out.println("OTP notification result: " + result);
			} catch (Exception e) {
				System.err.println("OTP notification failed (OTP still valid in DB): " + e.getMessage());
				e.printStackTrace();
			}
		};

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					send.run();
				}
			});
		} else {
			send.run();
		}
	}

	private String createOtpValue() {
		return isTestProfile() ? "123456" : Generator.generateOTP();
	}

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

		if (ulogin.isPresent()) {
			// One OTP for both channels so email/SMS/verify stay consistent
			String otpValue = createOtpValue();
			System.out.println("Login OTP for " + userName + ": " + otpValue);

			OTP otp = new OTP();
			otp.setEmailOTP(otpValue);
			otp.setMobileOTP(otpValue);
			otp.setExpiry(Instant.now().plusMillis(otpDuration));
			otp.setLoginId(ulogin.get());
			otp.setStatus("Active");
			otpRepo.save(otp);

			String title = "CCA Login OTP";
			String message = "Please do not share your OTP with anyone, your OTP is " + otpValue + ".";
			sendOtpNotificationAfterCommit(ulogin.get(), title, message, "OTP sent.", otpValue);
		} else {
			throw new IllegalArgumentException("User not found");
		}
	}

	@Override
	public boolean verifyEmailOtp(String otp, String userName) {
		List<OTP> list = otpRepo.findAllActiveOtpByUserName(userName);
		return list.size() > 0 && list.get(0).getEmailOTP().equals(otp);
	}

	@Override
	public boolean verifyMobileOtp(String otp, String userName) {
		List<OTP> list = otpRepo.findAllActiveOtpByUserName(userName);
		return list.size() > 0 && list.get(0).getMobileOTP().equals(otp);
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

		if (ulogin.isPresent()) {
			String emailOtp = createOtpValue();
			System.out.println("Forgot OTP for " + email + ": " + emailOtp);

			OTP otp = new OTP();
			otp.setEmailOTP(emailOtp);
			otp.setMobileOTP(emailOtp);
			otp.setExpiry(Instant.now().plusMillis(otpDuration));
			otp.setLoginId(ulogin.get());
			otp.setStatus("Active");
			otpRepo.save(otp);

			String title = "CCA Forgot Password OTP";
			String message = "Please do not share your OTP with anyone, your OTP is " + emailOtp + ".";
			sendOtpNotificationAfterCommit(ulogin.get(), title, message, "Forgot Password OTP sent.", emailOtp);
		} else {
			throw new IllegalArgumentException("User not found");
		}
	}

}
