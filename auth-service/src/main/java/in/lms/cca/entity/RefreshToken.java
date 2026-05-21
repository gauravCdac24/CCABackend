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
@Table(name = "refreshtoken", schema = "auth_users_management")
public class RefreshToken extends AbstractTimestampEntity{
	
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "refresh_token_id", length = 11)
  private Long refreshTokenId;

  @ManyToOne
  @JoinColumn(name = "login_id", referencedColumnName = "login_id")
  private UserLogin loginId;

  @Column(nullable = false, unique = true)
  private String refreshToken;

  @Column(nullable = false)
  private Instant expiryDate;

	public Long getRefreshTokenId() {
		return refreshTokenId;
	}
	
	public void setRefreshTokenId(Long refreshTokenId) {
		this.refreshTokenId = refreshTokenId;
	}
	
	public UserLogin getLoginId() {
		return loginId;
	}
	
	public void setLoginId(UserLogin loginId) {
		this.loginId = loginId;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}


}
