package dev.thilinifernando.gatekeeper.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// exception/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<Map<String, Object>> handleGateway(GatewayException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(Map.of("error", Map.of(
                        "code",    ex.getCode(),
                        "message", ex.getMessage()
                )));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500)
                .body(Map.of("error", Map.of(
                        "code",    "INTERNAL_ERROR",
                        "message", "An unexpected error occurred"
                )));
    }
}
