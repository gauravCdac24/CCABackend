package in.lms.cca.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class CrossOrigins {

	// Browser redirect Location after eSign callback (EsignController).
	// Local/dev frontend origin (production uses https://ccalms.cca.gov.in).
	public static final String Origins = "http://localhost:3000";
	public static final String Origins1 = "http://127.0.0.1:3000";

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(Origins, Origins1)
				.allowedMethods("GET", "POST")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
