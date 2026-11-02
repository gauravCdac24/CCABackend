package in.lms.cca.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class CrossOrigins {

	// Browser redirect Location after eSign callback (EsignController).
	// Must match the public frontend origin used in production.
	public static final String Origins = "https://ccalms.cca.gov.in";
	public static final String Origins1 = "https://45.127.74.108:443";

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(Origins, Origins1)
				.allowedMethods("GET", "POST")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
