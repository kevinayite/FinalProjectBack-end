//package com.panoramic.service;
//
//import com.panoramic.dto.auth.*;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.model.PasswordResetToken;
//import com.panoramic.model.User;
//import java.time.LocalDateTime;
//import com.panoramic.model.enums.Role;
//import com.panoramic.repository.PasswordResetTokenRepository;
//import com.panoramic.repository.UserRepository;
//import com.panoramic.security.JwtHelper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtHelper jwtHelper;
//    private final PasswordResetTokenRepository passwordResetTokenRepository;
//    private final EmailService emailService;
//
//    @Transactional
//    public JwtResponse signup(SignupRequest signupRequest) {
//        // Validate passwords match
//        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
//            throw new BadRequestException("Passwords do not match");
//        }
//
//        // Check if username already exists
//        if (userRepository.existsByUsername(signupRequest.getUsername())) {
//            throw new BadRequestException("Username is already taken");
//        }
//
//        // Check if email already exists
//        if (userRepository.existsByEmail(signupRequest.getEmail())) {
//            throw new BadRequestException("Email is already in use");
//        }
//
//        // Create new user
//        User user = new User();
//        user.setUsername(signupRequest.getUsername());
//        user.setEmail(signupRequest.getEmail());
//        user.setFullName(signupRequest.getFullName());
//        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
//        user.setRole(Role.EMPLOYEE); // Default role
//        user.setIsActive(true);
//
//        User savedUser = userRepository.save(user);
//
//        // Generate JWT token
//        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                .username(savedUser.getUsername())
//                .password(savedUser.getPassword())
//                .authorities("ROLE_" + savedUser.getRole().name())
//                .build();
//
//        String token = jwtHelper.generateToken(userDetails);
//
//        return new JwtResponse(
//                token,
//                "Bearer",
//                savedUser.getUsername(),
//                savedUser.getEmail(),
//                savedUser.getFullName(),
//                savedUser.getRole()
//        );
//    }
//
//    public JwtResponse login(LoginRequest loginRequest) {
//        // Authenticate user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmailOrUsername(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String token = jwtHelper.generateToken(userDetails);
//
//        // Get user details
//        User user = userRepository.findByUsernameOrEmail(
//                loginRequest.getEmailOrUsername(),
//                loginRequest.getEmailOrUsername()
//        ).orElseThrow(() -> new BadRequestException("User not found"));
//
//        return new JwtResponse(
//                token,
//                "Bearer",
//                user.getUsername(),
//                user.getEmail(),
//                user.getFullName(),
//                user.getRole()
//        );
//    }
//
//    @Transactional
//    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
//        // Find user by email
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BadRequestException("User not found with email: " + request.getEmail()));
//
//        // Check if user is active
//        if (!user.getIsActive()) {
//            throw new BadRequestException("User account is deactivated");
//        }
//
//        // Delete any existing tokens for this user
//        passwordResetTokenRepository.deleteByUser(user);
//
//        // Generate new token
//        String token = UUID.randomUUID().toString();
//
//        // Create password reset token (expires in 24 hours)
//        PasswordResetToken resetToken = new PasswordResetToken();
//        resetToken.setToken(token);
//        resetToken.setUser(user);
//        resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
//        resetToken.setIsUsed(false);
//
//        passwordResetTokenRepository.save(resetToken);
//
//        // Send email
//        emailService.sendPasswordResetEmail(user.getEmail(), token, user.getFullName());
//
//        return new ApiResponse("Password reset email sent successfully", true);
//    }
//
//    @Transactional
//    public ApiResponse resetPassword(ResetPasswordRequest request) {
//        // Validate passwords match
//        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
//            throw new BadRequestException("Passwords do not match");
//        }
//
//        // Find token
//        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
//                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));
//
//        // Check if token is already used
//        if (resetToken.getIsUsed()) {
//            throw new BadRequestException("Reset token has already been used");
//        }
//
//        // Check if token is expired
//        if (resetToken.isExpired()) {
//            throw new BadRequestException("Reset token has expired");
//        }
//
//        // Get user and update password
//        User user = resetToken.getUser();
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//
//        // Mark token as used
//        resetToken.setIsUsed(true);
//        passwordResetTokenRepository.save(resetToken);
//
//        return new ApiResponse("Password reset successfully", true);
//    }
//
//    public ApiResponse validateResetToken(String token) {
//        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
//                .orElseThrow(() -> new BadRequestException("Invalid reset token"));
//
//        if (resetToken.getIsUsed()) {
//            throw new BadRequestException("Reset token has already been used");
//        }
//
//        if (resetToken.isExpired()) {
//            throw new BadRequestException("Reset token has expired");
//        }
//
//        return new ApiResponse("Token is valid", true);
//    }
//
//    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse createUser(CreateUserRequest createUserRequest) {
//        // Check if username already exists
//        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
//            throw new BadRequestException("Username is already taken");
//        }
//
//        // Check if email already exists
//        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
//            throw new BadRequestException("Email is already in use");
//        }
//
//        // Create new user
//        User user = new User();
//        user.setUsername(createUserRequest.getUsername());
//        user.setEmail(createUserRequest.getEmail());
//        user.setFullName(createUserRequest.getFullName());
//        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
//        user.setRole(createUserRequest.getRole());
//        user.setPhoneNumber(createUserRequest.getPhoneNumber());
//        user.setIsActive(createUserRequest.getIsActive());
//
//        userRepository.save(user);
//
//        // Send welcome email with credentials
//        emailService.sendWelcomeEmail(
//                user.getEmail(),
//                user.getFullName(),
//                createUserRequest.getUsername(),
//                createUserRequest.getPassword() // You might want to send a temporary password instead
//        );
//
//        return new ApiResponse("User created successfully", true);
//    }
//}
//



package com.panoramic.service;

import com.panoramic.dto.auth.*;
import com.panoramic.exception.BadRequestException;
import com.panoramic.model.AccountSetupToken;
import com.panoramic.model.BlacklistedToken;
import com.panoramic.model.PasswordResetToken;
import com.panoramic.model.User;
import java.time.LocalDateTime;
import com.panoramic.model.enums.Role;
import com.panoramic.repository.AccountSetupTokenRepository;
import com.panoramic.repository.BlacklistedTokenRepository;
import com.panoramic.repository.PasswordResetTokenRepository;
import com.panoramic.repository.UserRepository;
import com.panoramic.security.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AccountSetupTokenRepository accountSetupTokenRepository;
    private final EmailService emailService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createUser(CreateUserRequest createUserRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        // Create new user with temporary password
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setFullName(createUserRequest.getFullName());
        // Set a temporary password that will be changed during setup
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole(createUserRequest.getRole());
        user.setPhoneNumber(createUserRequest.getPhoneNumber());
        user.setIsActive(createUserRequest.getIsActive());
        user.setIsSetupComplete(false); // User needs to complete setup

        User savedUser = userRepository.save(user);

        // Generate setup token
        String setupToken = UUID.randomUUID().toString();

        // Create account setup token (expires in 48 hours)
        AccountSetupToken accountSetupToken = new AccountSetupToken();
        accountSetupToken.setToken(setupToken);
        accountSetupToken.setUser(savedUser);
        accountSetupToken.setExpiryDate(LocalDateTime.now().plusHours(48));
        accountSetupToken.setIsUsed(false);

        accountSetupTokenRepository.save(accountSetupToken);

        // Send account setup email
        emailService.sendAccountSetupEmail(
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getUsername(),
                setupToken
        );

        return new ApiResponse("User created successfully. Setup email sent to " + savedUser.getEmail(), true);
    }

    @Transactional
    public JwtResponse completeAccountSetup(AccountSetupRequest setupRequest) {
        // Validate passwords match
        if (!setupRequest.getNewPassword().equals(setupRequest.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Find setup token
        AccountSetupToken setupToken = accountSetupTokenRepository.findByToken(setupRequest.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired setup token"));

        // Check if token is already used
        if (setupToken.getIsUsed()) {
            throw new BadRequestException("Setup token has already been used");
        }

        // Check if token is expired
        if (setupToken.isExpired()) {
            throw new BadRequestException("Setup token has expired");
        }

        // Get user and complete setup
        User user = setupToken.getUser();
        user.setPassword(passwordEncoder.encode(setupRequest.getNewPassword()));
        user.setIsSetupComplete(true);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Mark token as used
        setupToken.setIsUsed(true);
        accountSetupTokenRepository.save(setupToken);

        // Generate JWT token for immediate login
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .authorities("ROLE_" + savedUser.getRole().name())
                .build();

        String jwtToken = jwtHelper.generateToken(userDetails);

        return new JwtResponse(
                jwtToken,
                "Bearer",
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole()
        );
    }

    public ApiResponse validateSetupToken(String token) {
        AccountSetupToken setupToken = accountSetupTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid setup token"));

        if (setupToken.getIsUsed()) {
            throw new BadRequestException("Setup token has already been used");
        }

        if (setupToken.isExpired()) {
            throw new BadRequestException("Setup token has expired");
        }

        User user = setupToken.getUser();
        return new ApiResponse("Token is valid for user: " + user.getFullName(), true);
    }

    // This method should only be used for creating the first admin user
    @Transactional
    public JwtResponse createInitialAdmin(SignupRequest signupRequest) {
        // Check if any admin already exists
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new BadRequestException("Admin user already exists. Contact existing admin to create new users.");
        }

        // Validate passwords match
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        // Create admin user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setFullName(signupRequest.getFullName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(Role.ADMIN); // Force admin role
        user.setIsActive(true);
        user.setIsSetupComplete(true); // Admin setup is complete

        User savedUser = userRepository.save(user);

        // Generate JWT token
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .authorities("ROLE_" + savedUser.getRole().name())
                .build();

        String token = jwtHelper.generateToken(userDetails);

        return new JwtResponse(
                token,
                "Bearer",
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole()
        );
    }

    public JwtResponse login(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmailOrUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Get user details
        User user = userRepository.findByUsernameOrEmail(
                loginRequest.getEmailOrUsername(),
                loginRequest.getEmailOrUsername()
        ).orElseThrow(() -> new BadRequestException("User not found"));

        // Check if user is active
        if (!user.getIsActive()) {
            throw new BadRequestException("User account is deactivated");
        }

        // Check if user has completed setup (except for admin)
        if (!user.getIsSetupComplete() && user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Account setup is not complete. Please check your email for setup instructions.");
        }

        String token = jwtHelper.generateToken(userDetails);

        return new JwtResponse(
                token,
                "Bearer",
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    @Transactional
    public ApiResponse resendSetupEmail(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + email));

        // Check if setup is already complete
        if (user.getIsSetupComplete()) {
            throw new BadRequestException("Account setup is already complete");
        }

        // Delete any existing setup tokens for this user
        accountSetupTokenRepository.deleteByUser(user);

        // Generate new setup token
        String setupToken = UUID.randomUUID().toString();

        // Create new account setup token (expires in 48 hours)
        AccountSetupToken accountSetupToken = new AccountSetupToken();
        accountSetupToken.setToken(setupToken);
        accountSetupToken.setUser(user);
        accountSetupToken.setExpiryDate(LocalDateTime.now().plusHours(48));
        accountSetupToken.setIsUsed(false);

        accountSetupTokenRepository.save(accountSetupToken);

        // Send setup email
        emailService.sendAccountSetupEmail(
                user.getEmail(),
                user.getFullName(),
                user.getUsername(),
                setupToken
        );

        return new ApiResponse("Setup email resent successfully", true);
    }

    @Transactional
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found with email: " + request.getEmail()));

        // Check if user is active
        if (!user.getIsActive()) {
            throw new BadRequestException("User account is deactivated");
        }

        // Check if user has completed setup
        if (!user.getIsSetupComplete()) {
            throw new BadRequestException("Account setup is not complete. Please complete your account setup first.");
        }

        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        // Generate new token
        String token = UUID.randomUUID().toString();

        // Create password reset token (expires in 24 hours)
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        resetToken.setIsUsed(false);

        passwordResetTokenRepository.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), token, user.getFullName());

        return new ApiResponse("Password reset email sent successfully", true);
    }

    @Transactional
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Find token
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        // Check if token is already used
        if (resetToken.getIsUsed()) {
            throw new BadRequestException("Reset token has already been used");
        }

        // Check if token is expired
        if (resetToken.isExpired()) {
            throw new BadRequestException("Reset token has expired");
        }

        // Get user and update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setIsUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return new ApiResponse("Password reset successfully", true);
    }

    public ApiResponse validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (resetToken.getIsUsed()) {
            throw new BadRequestException("Reset token has already been used");
        }

        if (resetToken.isExpired()) {
            throw new BadRequestException("Reset token has expired");
        }

        return new ApiResponse("Token is valid", true);
    }

    @Transactional
    public ApiResponse logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {

                LocalDateTime expiresAt = jwtHelper.getExpirationDateTimeFromToken(token);


                BlacklistedToken blacklistedToken = new BlacklistedToken(token, expiresAt);
                blacklistedTokenRepository.save(blacklistedToken);

                return new ApiResponse("Logged out successfully", true);
            } catch (Exception e) {

                return new ApiResponse("Logged out successfully", true);
            }
        }

        return new ApiResponse("Logged out successfully", true);
    }


    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }


    @Transactional
    public void cleanupExpiredTokens() {
        blacklistedTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
