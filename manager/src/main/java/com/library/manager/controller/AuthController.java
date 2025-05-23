package com.library.manager.controller;

import com.library.manager.dto.AuthRequest;
import com.library.manager.entity.User;
import com.library.manager.repo.UserRepository;
import com.library.manager.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate user (handles password encoding internally)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Retrieve user to get the role
            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token using email and role
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            // Prepare response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole().name());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // Return error response if login fails
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
