package in.lms.cca.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Component
public class RouteValidator {
	

	public static final ArrayList<String> openApiEndpoints = new ArrayList<String>(
			(Arrays.asList("/admin-service/api-docs/**",
							"/admin-service/swagger-ui.html",  
							"/admin-service/swagger-ui/**",
							"/notification-service/api-docs/**",
							"/notification-service/swagger-ui.html",  
							"/notification-service/swagger-ui/**",
							"/esign-service/api-docs/**",
							"/esign-service/swagger-ui.html",  
							"/esign-service/swagger-ui/**",
							"/auth-service/api-docs/**",
							"/auth-service/swagger-ui.html",  
							"/auth-service/swagger-ui/**",
							"/newlicense-service/api-docs/**",
							"/newlicense-service/swagger-ui.html",  
							"/newlicense-service/swagger-ui/**",
							"/license-issuance-service/api-docs/**",
							"/license-issuance-service/swagger-ui.html",  
							"/license-issuance-service/swagger-ui/**",
							"/renew-license-service/api-docs/**",
							"/renew-license-service/swagger-ui.html",  
							"/renew-license-service/swagger-ui/**",
							"/esign-application-service/api-docs/**",
							"/esign-application-service/swagger-ui.html",  
							"/esign-application-service/swagger-ui/**",
							"/audit-service/api-docs/**",
							"/audit-service/swagger-ui.html",  
							"/audit-service/swagger-ui/**",
							"/renew-audit-service/api-docs/**",
							"/renew-audit-service/swagger-ui.html",  
							"/renew-audit-service/swagger-ui/**",
							"/dashboard-service/api-docs/**",
							"/dashboard-service/swagger-ui.html",  
							"/dashboard-service/swagger-ui/**",
//							"/auth-service/**", 
							"/notification-service/**",
							"/esign-service/**", 
							"/admin-service/get-all-country", 
							"/admin-service/get-all-state", 
							"/admin-service/get-all-city", 
							"/admin-service/get-intent-by-unique-code", 
							"/admin-service/add-intent", 
							"/admin-service/get-role-by-name",
							"/esign-service/esignresponse",
							"/esign-application-service/change-review-status/**")));

	private final PathMatcher pathMatcher = new AntPathMatcher();

	public Predicate<ServerHttpRequest> isSecured = request ->
	        openApiEndpoints.stream()
	                .noneMatch(uri -> pathMatcher.match(uri, request.getURI().getPath()));

}