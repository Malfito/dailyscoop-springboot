package com.example.dailyscoop.service;

import com.example.dailyscoop.dto.GoogleSignInRequest;
import com.example.dailyscoop.dto.LoginRequest;
import com.example.dailyscoop.dto.SignupRequest;
import com.example.dailyscoop.dto.VerifyOtpRequest;
import com.example.dailyscoop.model.User;
import com.example.dailyscoop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;

    // Simulate sending OTP via email and SMS.
    private void sendOtp(User user) {
        // In a real implementation, integrate with an email service and SMS gateway.
        System.out.println("Sending OTP " + user.getOtp() +
                " to email " + user.getEmail() +
                " and mobile " + user.getMobile());
    }

    // Generate a random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Signup method: store the user as unverified, generate and send OTP.
    public String signup(SignupRequest request) {
        // Check if a user with this email already exists.
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return "User already exists with this email.";
        }
        // Create a new user record.
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        // In production, hash the password before storing it.
        user.setPassword(request.getPassword());
        user.setVerified(false);
        // Generate OTP and set expiry (e.g., valid for 5 minutes)
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        
        emailService.sendOtpEmail(request.getEmail(), otp);

        // Simulate sending the OTP
        sendOtp(user);

        return "Signup successful. OTP sent to email and mobile.";
    }

    // Verify the OTP. If valid, mark the user as verified.
    public String verifyOtp(VerifyOtpRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            return "User not found.";
        }
        User user = userOpt.get();
        if (user.isVerified()) {
            return "User already verified.";
        }
        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            return "Invalid OTP.";
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP expired.";
        }
        // OTP is valid; mark the user as verified.
        user.setVerified(true);
        // Clear OTP fields.
        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return "User verified successfully.";
    }

    // Login using email and password.
    public String login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            return "User not found.";
        }
        User user = userOpt.get();
        if (!user.isVerified()) {
            return "User not verified.";
        }
        // In production, compare the hashed password.
        if (!user.getPassword().equals(request.getPassword())) {
            return "Invalid credentials.";
        }
        // Generate a session token or JWT (omitted for brevity)
        return "Login successful.";
    }

    // Google sign in (or sign up). OTP is not required.
    public String googleSignIn(GoogleSignInRequest request) {
        // Try to find the user by googleId; alternatively, check by email.
        Optional<User> userOpt = userRepository.findByGoogleId(request.getGoogleId());
        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmail(request.getEmail());
        }
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            // If the googleId was not already set, update it.
            if (user.getGoogleId() == null) {
                user.setGoogleId(request.getGoogleId());
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }
        } else {
            // Create a new user record with the given Google details.
            user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setMobile(request.getMobile());
            // No password is needed when using Google sign in.
            user.setPassword(null);
            user.setVerified(true); // Assume Google sign in is trusted.
            user.setGoogleId(request.getGoogleId());
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
        // Generate a session token or JWT (omitted for brevity)
        return "Google sign in successful.";
    }
}
