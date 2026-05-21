package in.lms.cca.entity;

import java.time.Instant;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "otp", schema="auth_users_management")
public class OTP extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otp_id", length = 11)
	private Long otpId;
	
	@ManyToOne
	@JoinColumn(name = "login_id", referencedColumnName = "login_id")
	private UserLogin loginId;
	
	@Column(name = "mobile_otp", length = 6)
	private String mobileOTP;
	
	@Column(name = "email_otp", length = 6)
	private String emailOTP;
	
	@Column(nullable = false)
	private Instant expiry;
	

	public Long getOtpId() {
		return otpId;
	}

	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}

	public UserLogin getLoginId() {
		return loginId;
	}

	public void setLoginId(UserLogin loginId) {
		this.loginId = loginId;
	}

	public String getMobileOTP() {
		return mobileOTP;
	}

	public void setMobileOTP(String mobileOTP) {
		this.mobileOTP = mobileOTP;
	}

	public String getEmailOTP() {
		return emailOTP;
	}

	public void setEmailOTP(String emailOTP) {
		this.emailOTP = emailOTP;
	}

	public Instant getExpiry() {
		return expiry;
	}

	public void setExpiry(Instant expiry) {
		this.expiry = expiry;
	}
	
	
	

}
