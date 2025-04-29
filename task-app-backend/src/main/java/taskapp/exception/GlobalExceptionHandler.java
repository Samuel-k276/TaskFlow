package taskapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication error: {}", e.getMessage());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("error", "AUTHENTICATION_ERROR");
        response.put("status", "403");
        response.put("type", e.getMessage());
        
        log.debug("Sending error response: {}", response);
        return ResponseEntity.status(403).body(response);
    }
} 