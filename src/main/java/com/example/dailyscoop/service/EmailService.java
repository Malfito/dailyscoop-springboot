package com.example.dailyscoop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP is: " + otp);

            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send OTP email.");
        }
    }
}
