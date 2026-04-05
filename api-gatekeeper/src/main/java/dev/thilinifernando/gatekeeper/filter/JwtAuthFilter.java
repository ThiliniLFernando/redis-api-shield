package dev.thilinifernando.gatekeeper.filter;

import dev.thilinifernando.gatekeeper.model.AuthenticatedUser;
import dev.thilinifernando.gatekeeper.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final String BEARER = "Bearer ";

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER)) {
            chain.doFilter(request, response); // let SecurityConfig reject it
            return;
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

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (ExpiredJwtException e) {
            writeError(response, 401, "TOKEN_EXPIRED", "Token has expired");
            return;
        } catch (JwtException e) {
            writeError(response, 401, "INVALID_TOKEN", "Invalid token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response,
                            int status, String code, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                """
                {"error":{"code":"%s","message":"%s"}}
                """.formatted(code, message)
        );
    }
}
