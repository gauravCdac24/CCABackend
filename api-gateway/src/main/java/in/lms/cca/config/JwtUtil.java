package in.lms.cca.config;

import java.security.Key;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	public static final String SECRET = "ZHJpdmluZ3B1c2htb2xlY3VsYXJuZWVkbGVmcnVpdHJhbm11c2ljY2FzZWNlbGxsYWM=QWFRTASEGTRqeescrrweyyui"; 

	public void validateToken(final String token) {
		
		
		
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}