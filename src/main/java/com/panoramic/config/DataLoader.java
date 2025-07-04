package com.panoramic.config;

import com.panoramic.model.User;
import com.panoramic.model.enums.Role;
import com.panoramic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("kevinayikson14@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin123");
            admin.setEmail("kevinayikson14@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setPhoneNumber("1234567890");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            userRepository.save(admin);
        }
    }
}
