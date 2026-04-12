package dev.thilinifernando.gatekeeper.filters;

import dev.thilinifernando.gatekeeper.model.AuthenticatedUser;
import dev.thilinifernando.gatekeeper.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private static final String BEARER = "Bearer ";

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private Mono<Void> writeError(ServerWebExchange exchange,
                                  int status,
                                  String code,
                                  String message) {

        var response = exchange.getResponse();

        response.setStatusCode(HttpStatus.valueOf(status));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {"error":{"code":"%s","message":"%s"}}
        """.formatted(code, message);

        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER)) {
            return chain.filter(exchange);
        }

        String token = header.substring(BEARER.length());

        try {
            Claims claims = jwtUtil.parse(token);

            AuthenticatedUser user = new AuthenticatedUser(
                    claims.getSubject(),
                    claims.get("plan", String.class)
            );

            // Attach to Spring Security context — rate limiter reads from here
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());

            // SecurityContextHolder.getContext().setAuthentication(auth);
            // There is no security context in the Spring Cloud Gateway - Reactive
            return chain.filter(exchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(auth)
                    );

        } catch (ExpiredJwtException e) {
            return writeError(exchange, 401, "TOKEN_EXPIRED", "Token has expired");
        } catch (JwtException e) {
            return writeError(exchange, 401, "INVALID_TOKEN", "Invalid token");
        }
    }
}
