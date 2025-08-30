package org.softuni.onlinegrocery.domain.models.service;

import org.softuni.onlinegrocery.domain.entities.Otp;

import java.time.LocalDateTime;

public class OtpServiceModel {
    
    private String id;
    private String phoneNumber;
    private String email;
    private String otpCode;
    private boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int attempts;
    private Otp.OtpType otpType;
    private boolean success;
    private String message;

    public OtpServiceModel() {
    }

    public OtpServiceModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Otp.OtpType getOtpType() {
        return otpType;
    }

    public void setOtpType(Otp.OtpType otpType) {
        this.otpType = otpType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
