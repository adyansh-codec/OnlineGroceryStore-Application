package org.softuni.onlinegrocery.service;

import org.softuni.onlinegrocery.domain.entities.Otp;
import org.softuni.onlinegrocery.domain.models.service.OtpServiceModel;

public interface OtpService {
    
    /**
     * Generate and send OTP via SMS
     */
    OtpServiceModel generateAndSendSmsOtp(String phoneNumber, Otp.OtpType otpType);
    
    /**
     * Generate and send OTP via Email
     */
    OtpServiceModel generateAndSendEmailOtp(String email, Otp.OtpType otpType);
    
    /**
     * Verify OTP code
     */
    boolean verifyOtp(String phoneNumber, String email, String otpCode);
    
    /**
     * Verify OTP code by code only
     */
    boolean verifyOtpByCode(String otpCode);
    
    /**
     * Resend OTP
     */
    OtpServiceModel resendOtp(String phoneNumber, String email);
    
    /**
     * Clean up expired OTPs
     */
    void cleanupExpiredOtps();
    
    /**
     * Check if rate limit exceeded
     */
    boolean isRateLimitExceeded(String phoneNumber, String email);
}
