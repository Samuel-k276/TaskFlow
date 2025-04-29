package taskapp.controller;

import taskapp.dto.AuthenticationRequest;
import taskapp.dto.AuthenticationResponse;
import taskapp.dto.RegisterRequest;
import taskapp.exception.AuthenticationException;
import taskapp.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

   private final AuthenticationService authenticationService;

   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
      try {
         AuthenticationResponse response = authenticationService.register(request);
         return ResponseEntity.ok(response);
      } catch (AuthenticationException e) {
         Map<String, String> errorResponse = new HashMap<>();
         errorResponse.put("error", e.getMessage());
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
      }
   }

   @PostMapping("/authenticate")
   public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
      return ResponseEntity.ok(authenticationService.authenticate(request));
   }
}