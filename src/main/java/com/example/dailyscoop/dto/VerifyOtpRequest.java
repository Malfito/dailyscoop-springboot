package com.example.dailyscoop.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	// Here we use email to identify the user.
    private String email;
    private String otp;
}
