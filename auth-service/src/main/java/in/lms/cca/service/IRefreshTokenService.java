package in.lms.cca.service;

import java.util.Optional;

import in.lms.cca.entity.RefreshToken;

public interface IRefreshTokenService {

	
	public Optional<RefreshToken> findByToken(String token);

	public RefreshToken createRefreshToken(Integer loginId);
	
	public RefreshToken updateRefreshToken(String token);

	public RefreshToken verifyExpiration(RefreshToken token);
	
	public int deleteByloginId(Integer loginId);


	
	
}
