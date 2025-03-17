package com.example.dailyscoop.controller;

import com.example.dailyscoop.dto.GoogleSignInRequest;
import com.example.dailyscoop.dto.LoginRequest;
import com.example.dailyscoop.dto.SignupRequest;
import com.example.dailyscoop.dto.VerifyOtpRequest;
import com.example.dailyscoop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // API endpoint for user signup
    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    // API endpoint for OTP verification
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest request) {
        return userService.verifyOtp(request);
    }

    // API endpoint for user login
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    // API endpoint for Google sign in (or signup)
    @PostMapping("/google-signin")
    public String googleSignIn(@RequestBody GoogleSignInRequest request) {
        return userService.googleSignIn(request);
    }
}
