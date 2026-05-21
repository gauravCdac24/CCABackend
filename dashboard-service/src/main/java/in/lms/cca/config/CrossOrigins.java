package in.lms.cca.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class CrossOrigins {

	//public static final String Origins = "http://10.22.118.211:3000";
	//public static final String Origins = "http://10.224.0.49:3000";
	public static final String Origins = "http://localhost:3000";

public static final String Origins1 = "http://10.224.0.86:3000";
	
	//public static final String Origins = "http://ccalmsbe.delhi.cdac.in:3000";
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**")
				.allowedOrigins(Origins , Origins1 )
				.allowedMethods("GET", "POST")
				.allowedHeaders("*") // Allow all headers
				.allowCredentials(true);
	}
}
