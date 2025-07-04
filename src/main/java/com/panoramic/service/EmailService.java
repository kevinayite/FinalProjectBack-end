//package com.panoramic.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    @Value("${app.frontend.url:http://localhost:3000}")
//    private String frontendUrl;
//
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//
//    public void sendPasswordResetEmail(String toEmail, String token, String userName) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(toEmail);
//            message.setSubject("Password Reset Request - Panoramic System");
//
//            String resetUrl = frontendUrl + "/reset-password?token=" + token;
//
//            String emailBody = String.format(
//                    "Dear %s,\n\n" +
//                            "You have requested to reset your password for Panoramic System.\n\n" +
//                            "Please click the link below to reset your password:\n" +
//                            "%s\n\n" +
//                            "This link will expire in 24 hours.\n\n" +
//                            "If you did not request this password reset, please ignore this email.\n\n" +
//                            "Best regards,\n" +
//                            "Panoramic System Team",
//                    userName, resetUrl
//            );
//
//            message.setText(emailBody);
//            mailSender.send(message);
//
//            log.info("Password reset email sent successfully to: {}", toEmail);
//
//        } catch (Exception e) {
//            log.error("Failed to send password reset email to: {}", toEmail, e);
//            throw new RuntimeException("Failed to send password reset email");
//        }
//    }
//}





package com.panoramic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String token, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request - Panoramic System");

            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "You have requested to reset your password for Panoramic System.\n\n" +
                            "Please click the link below to reset your password:\n" +
                            "%s\n\n" +
                            "This link will expire in 24 hours.\n\n" +
                            "If you did not request this password reset, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "Panoramic System Team",
                    userName, resetUrl
            );

            message.setText(emailBody);
            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    public void sendAccountSetupEmail(String email, String fullName, String username, String setupToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Welcome to Panoramic System - Complete Your Account Setup");

            String setupUrl = frontendUrl + "/setup-account?token=" + setupToken;
            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Welcome to Panoramic System! Your account has been created by the administrator.\n\n" +
                            "Your username: %s\n\n" +
                            "To complete your account setup and create your password, please click the link below:\n" +
                            "%s\n\n" +
                            "This setup link will expire in 48 hours.\n\n" +
                            "SECURITY TIPS:\n" +
                            "- Choose a strong password with at least 8 characters\n" +
                            "- Include uppercase, lowercase, numbers, and special characters\n" +
                            "- Do not share your login credentials with anyone\n\n" +
                            "If you have any questions or need assistance, please contact your system administrator.\n\n" +
                            "Best regards,\n" +
                            "Panoramic System Team",
                    fullName, username, setupUrl
            );

            message.setText(emailBody);
            mailSender.send(message);
            log.info("Account setup email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send account setup email to: {}", email, e);
            throw new RuntimeException("Failed to send account setup email");
        }
    }
}
