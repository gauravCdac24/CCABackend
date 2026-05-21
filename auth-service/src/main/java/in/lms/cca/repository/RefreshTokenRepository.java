package in.lms.cca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.RefreshToken;
import jakarta.transaction.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

	  Optional<RefreshToken> findByRefreshToken(String token);
	  
	  @Modifying
	  @Transactional
	  @Query("DELETE FROM RefreshToken r WHERE r.loginId.loginId=:loginId")
	  int deleteByLoginId(Integer loginId);
	
}
