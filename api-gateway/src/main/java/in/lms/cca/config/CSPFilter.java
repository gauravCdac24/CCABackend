package in.lms.cca.config;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class CSPFilter extends AbstractGatewayFilterFactory<CSPFilter.Config> {

    public CSPFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();
            
            // Generate a nonce
            String nonce = generateNonce();

            // Define the CSP policy with the nonce
            String cspPolicy = "default-src 'self'; " +
                               "script-src 'self' 'nonce-" + nonce + "'; " +
                               "style-src 'self' 'nonce-" + nonce + "'; " +
                               "img-src 'self' data:; " +
                               "connect-src 'self'; " +
                               "font-src 'self'; " +
                               "object-src 'none'; " +
                               "frame-src 'none'; " +
                               "base-uri 'self'; " +
                               "form-action 'self';";
            
            response.getHeaders().add("Content-Security-Policy", cspPolicy);

            // Add the nonce to the response header for the frontend
            response.getHeaders().add("CSP-Nonce", nonce);
            
            response.getHeaders().add("X-Content-Type-Options", "nosniff");
            response.getHeaders().add("X-Frame-Options", "DENY");

            return chain.filter(exchange);
        };
    }

    private String generateNonce() {
        byte[] nonceBytes = new byte[16];
        new SecureRandom().nextBytes(nonceBytes);
        return Base64.getEncoder().encodeToString(nonceBytes);
    }

    public static class Config {
        // Configuration properties, if needed
    }
}
