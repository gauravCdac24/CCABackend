package in.lms.cca.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class CrossOrigins {

	/**
	 * Browser redirect Location after eSign callback (EsignController).
	 * Must be a real URL origin — not a CORS wildcard pattern.
	 */
	public static final String Origins = "http://10.22.118.201:3000";
	public static final String Origins1 = "http://10.22.118.201:3000";

	public static final String Local = "http://localhost:3000";
	public static final String LocalLoopback = "http://127.0.0.1:3000";
	public static final String LocalLan = "http://10.*.*.*:3000";
	public static final String LocalLan192 = "http://192.168.*.*:3000";

	public static final String[] PATTERNS = {
			Origins, Origins1, Local, LocalLoopback, LocalLan, LocalLan192
	};

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns(PATTERNS)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
