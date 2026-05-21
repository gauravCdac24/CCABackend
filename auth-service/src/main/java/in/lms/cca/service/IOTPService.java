package in.lms.cca.service;

public interface IOTPService {

	void generateOtp(String userName);
	boolean verifyEmailOtp(String otp, String userName);
	boolean verifyMobileOtp(String otp, String userName);
	void generateForgotOTP(String email);
	
}
