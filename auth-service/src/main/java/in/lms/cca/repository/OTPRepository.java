package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.OTP;

public interface OTPRepository extends JpaRepository<OTP, Long>{

	@Query("SELECT otp FROM OTP otp WHERE otp.loginId.userName = :userName AND status = 'Active'")
	public List<OTP> findAllActiveOtpByUserName(@Param("userName") String userName);
	
	
		
}
