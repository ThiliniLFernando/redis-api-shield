package dev.thilinifernando.gatekeeper.exception;

import org.springframework.http.HttpStatusCode;

public class GatewayException extends RuntimeException {
    private final HttpStatusCode status;
    private final String code;
    public GatewayException(HttpStatusCode status,String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
