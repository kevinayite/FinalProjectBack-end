//package com.panoramic.controller;
//
//import com.panoramic.dto.auth.*;
//import com.panoramic.service.AuthService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<JwtResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
//        JwtResponse response = authService.signup(signupRequest);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
//        JwtResponse response = authService.login(loginRequest);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
//        ApiResponse response = authService.forgotPassword(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
//        ApiResponse response = authService.resetPassword(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/validate-reset-token")
//    public ResponseEntity<ApiResponse> validateResetToken(@RequestParam String token) {
//        ApiResponse response = authService.validateResetToken(token);
//        return ResponseEntity.ok(response);
//    }
//
//    // Admin-only endpoint to create new users
//    @PostMapping("/create-user")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
//        ApiResponse response = authService.createUser(createUserRequest);
//        return ResponseEntity.ok(response);
//    }
//}




package com.panoramic.controller;

import com.panoramic.dto.auth.*;
import com.panoramic.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // This endpoint should only be used to create the first admin user
    @PostMapping("/create-initial-admin")
    public ResponseEntity<JwtResponse> createInitialAdmin(@Valid @RequestBody SignupRequest signupRequest) {
        JwtResponse response = authService.createInitialAdmin(signupRequest);
        return ResponseEntity.ok(response);
    }

    // Admin-only endpoint to create new users
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        ApiResponse response = authService.createUser(createUserRequest);
        return ResponseEntity.ok(response);
    }

    // Complete account setup with password
    @PostMapping("/complete-setup")
    public ResponseEntity<JwtResponse> completeAccountSetup(@Valid @RequestBody AccountSetupRequest setupRequest) {
        JwtResponse response = authService.completeAccountSetup(setupRequest);
        return ResponseEntity.ok(response);
    }

    // Validate setup token
    @GetMapping("/validate-setup-token")
    public ResponseEntity<ApiResponse> validateSetupToken(@RequestParam String token) {
        ApiResponse response = authService.validateSetupToken(token);
        return ResponseEntity.ok(response);
    }

    // Resend setup email
    @PostMapping("/resend-setup-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> resendSetupEmail(@RequestParam String email) {
        ApiResponse response = authService.resendSetupEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ApiResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ApiResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<ApiResponse> validateResetToken(@RequestParam String token) {
        ApiResponse response = authService.validateResetToken(token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        try {
            ApiResponse response = authService.logout(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Logged out successfully", true));
        }
    }
}
