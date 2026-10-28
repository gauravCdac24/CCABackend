package in.lms.cca.config;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Ensures CORS headers are present on every gateway response.
 * Default RemoveResponseHeader filters strip upstream CORS headers; Spring's
 * CorsWebFilter reliably handles OPTIONS but often omits ACAO on proxied POSTs.
 */
@Component
public class CorsGlobalFilter implements GlobalFilter, Ordered {

	private static final List<String> ALLOWED_ORIGINS = List.of(
			"http://localhost:3000",
			"http://127.0.0.1:3000"
	);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String origin = exchange.getRequest().getHeaders().getOrigin();
		HttpMethod method = exchange.getRequest().getMethod();

		if (origin != null && isAllowed(origin) && HttpMethod.OPTIONS.equals(method)) {
			var response = exchange.getResponse();
			applyCors(response.getHeaders(), origin);
			response.setStatusCode(HttpStatus.OK);
			return response.setComplete();
		}

		exchange.getResponse().beforeCommit(() -> {
			if (origin != null && isAllowed(origin)) {
				applyCors(exchange.getResponse().getHeaders(), origin);
			}
			return Mono.empty();
		});
		return chain.filter(exchange);
	}

	private static void applyCors(HttpHeaders headers, String origin) {
		headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
		headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS,PATCH");
		headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
		headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
		headers.set(HttpHeaders.VARY, "Origin");
	}

	private static boolean isAllowed(String origin) {
		if (ALLOWED_ORIGINS.contains(origin)) {
			return true;
		}
		return origin.matches("^http://10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:3000$")
				|| origin.matches("^http://192\\.168\\.\\d{1,3}\\.\\d{1,3}:3000$");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
