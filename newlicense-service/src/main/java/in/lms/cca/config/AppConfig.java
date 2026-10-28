package in.lms.cca.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate() {
    	
    	RestTemplate restTemplate = new RestTemplate();
        
        // Add the interceptor to the RestTemplate
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderInterceptor());
        restTemplate.setInterceptors(interceptors);
        
        return restTemplate;
    }
}