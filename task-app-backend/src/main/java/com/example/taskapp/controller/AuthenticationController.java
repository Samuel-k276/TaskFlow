package com.example.taskapp.controller;

import com.example.taskapp.dto.AuthenticationRequest;
import com.example.taskapp.dto.AuthenticationResponse;
import com.example.taskapp.dto.RegisterRequest;
import com.example.taskapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.taskapp.exception.AuthenticationException;

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
         return ResponseEntity.ok(authenticationService.register(request));
      } catch (AuthenticationException e) {
         Map<String, String> errorResponse = new HashMap<>();
         errorResponse.put("error", e.getMessage());
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
      }
   }

   @PostMapping("/authenticate")
   public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
      return ResponseEntity.ok(authenticationService.authenticate(request));
   }
} 