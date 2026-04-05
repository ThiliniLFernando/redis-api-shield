package dev.thilinifernando.gatekeeper.controller;

import dev.thilinifernando.gatekeeper.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// controller/AuthController.java
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Replace with a real UserService + DB lookup
    private static final Map<String, String[]> MOCK_USERS = Map.of(
            "demo@example.com", new String[]{"user_01", "pro"}
    );

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String[] user = MOCK_USERS.get(email);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", Map.of("code", "INVALID_CREDENTIALS")));
        }

        String token = jwtUtil.issue(user[0], user[1]);
        return ResponseEntity.ok(Map.of("token", token, "expiresIn", 3600));
    }
}
