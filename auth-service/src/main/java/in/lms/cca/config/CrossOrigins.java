package in.lms.cca.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class CrossOrigins {

	public static final String Origins = "https://ccalms.cca.gov.in";
	public static final String Origins1 = "https://45.127.74.108:443";
	public static final String Local = "http://localhost:3000";
	public static final String LocalLoopback = "http://127.0.0.1:3000";
	/** CRA "On Your Network" URL (e.g. http://10.22.118.224:3000). */
	public static final String LocalLan = "http://10.*.*.*:3000";
	public static final String LocalLan192 = "http://192.168.*.*:3000";

	/** Patterns for @CrossOrigin(originPatterns=...) and global CORS. */
	public static final String[] PATTERNS = {
			Origins, Origins1, Local, LocalLoopback, LocalLan, LocalLan192
	};

	/** @deprecated use PATTERNS with originPatterns */
	public static final String[] ALL = PATTERNS;

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns(PATTERNS)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
