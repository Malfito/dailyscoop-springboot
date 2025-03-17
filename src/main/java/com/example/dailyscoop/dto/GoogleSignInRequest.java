package com.example.dailyscoop.dto;

import lombok.Data;

@Data
public class GoogleSignInRequest {
    private String googleId;
    public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	private String name;
    private String email;
    // Mobile is optional
    private String mobile;
}
