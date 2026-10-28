package in.lms.cca.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // Retrieve the current HTTP request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest currentRequest = null;

        if (requestAttributes instanceof ServletRequestAttributes) {
            currentRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        // Retrieve the Authorization header from the current request
        String authHeader = currentRequest != null ? currentRequest.getHeader("Authorization") : null;
        
        HttpHeaders headers = request.getHeaders();

        // Set the Authorization header in the outgoing RestTemplate request
        if (authHeader != null) {
            headers.set("Authorization", authHeader);
        }
       

        return execution.execute(request, body);
    }
}
