package in.lms.cca.service.Impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.RefreshToken;
import in.lms.cca.entity.UserLogin;
import in.lms.cca.exceptions.TokenRefreshException;
import in.lms.cca.repository.RefreshTokenRepository;
import in.lms.cca.repository.UserLoginRepository;
import in.lms.cca.service.IRefreshTokenService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class RefreshTokenService implements IRefreshTokenService {

	private Long refreshTokenDurationMs = 11 * 60 * 60 * 1000L;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByRefreshToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(Integer loginId) {
		
		refreshTokenRepository.deleteByLoginId(loginId);
		
		Optional<UserLogin> userlogin = userLoginRepository.findByLoginId(loginId);
		
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setLoginId(userlogin.get());

		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

		refreshToken.setRefreshToken(UUID.randomUUID().toString());

		refreshToken = refreshTokenRepository.save(refreshToken);

		return refreshToken;
	}

	@Override
	public RefreshToken updateRefreshToken(String token) {
		
		RefreshToken refreshToken = new RefreshToken();
		refreshToken=refreshTokenRepository.findByRefreshToken(token).get();
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken = refreshTokenRepository.save(refreshToken);
		
		return refreshToken;
		
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			
			refreshTokenRepository.delete(token);
			
			throw new TokenRefreshException(token.getRefreshToken(),
					"Refresh token was expired. Please make a new signin request");
		}

		return token;
		
	}

	@Override
	public int deleteByloginId(Integer loginId) {
		return refreshTokenRepository.deleteByLoginId(userLoginRepository.findByLoginId(loginId).get().getLoginId());
	}

}
