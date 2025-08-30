package org.softuni.onlinegrocery.web.controllers;

import org.softuni.onlinegrocery.domain.entities.Otp;
import org.softuni.onlinegrocery.domain.models.service.OtpServiceModel;
import org.softuni.onlinegrocery.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/otp")
public class OtpController extends BaseController {

    private final OtpService otpService;

    @Autowired
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @GetMapping("/send")
    public ModelAndView showSendOtpPage(ModelAndView modelAndView) {
        return view("otp/send-otp", modelAndView);
    }

    @GetMapping("/verify")
    public ModelAndView showVerifyOtpPage(ModelAndView modelAndView, HttpSession session) {
        String phoneNumber = (String) session.getAttribute("otp_phone_number");
        String email = (String) session.getAttribute("otp_email");
        
        if (phoneNumber == null && email == null) {
            return redirect("/otp/send");
        }
        
        modelAndView.addObject("phoneNumber", phoneNumber);
        modelAndView.addObject("email", email);
        return view("otp/verify-otp", modelAndView);
    }

    @PostMapping("/send/sms")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendSmsOtp(@RequestParam String phoneNumber,
                                                         @RequestParam(defaultValue = "SMS") String otpType,
                                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Otp.OtpType type = Otp.OtpType.valueOf(otpType.toUpperCase());
            OtpServiceModel result = otpService.generateAndSendSmsOtp(phoneNumber, type);
            
            if (result.isSuccess()) {
                session.setAttribute("otp_phone_number", phoneNumber);
                session.setAttribute("otp_email", null);
                response.put("success", true);
                response.put("message", result.getMessage());
            } else {
                response.put("success", false);
                response.put("message", result.getMessage());
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Invalid OTP type or error occurred");
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send/email")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendEmailOtp(@RequestParam String email,
                                                           @RequestParam(defaultValue = "EMAIL") String otpType,
                                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Otp.OtpType type = Otp.OtpType.valueOf(otpType.toUpperCase());
            OtpServiceModel result = otpService.generateAndSendEmailOtp(email, type);
            
            if (result.isSuccess()) {
                session.setAttribute("otp_email", email);
                session.setAttribute("otp_phone_number", null);
                response.put("success", true);
                response.put("message", result.getMessage());
            } else {
                response.put("success", false);
                response.put("message", result.getMessage());
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Invalid OTP type or error occurred");
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestParam String otpCode,
                                                        HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        String phoneNumber = (String) session.getAttribute("otp_phone_number");
        String email = (String) session.getAttribute("otp_email");
        
        boolean isVerified = otpService.verifyOtp(phoneNumber, email, otpCode);
        
        if (isVerified) {
            session.setAttribute("otp_verified", true);
            response.put("success", true);
            response.put("message", "OTP verified successfully!");
        } else {
            response.put("success", false);
            response.put("message", "Invalid or expired OTP code. Please try again.");
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resendOtp(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        String phoneNumber = (String) session.getAttribute("otp_phone_number");
        String email = (String) session.getAttribute("otp_email");
        
        OtpServiceModel result = otpService.resendOtp(phoneNumber, email);
        
        response.put("success", result.isSuccess());
        response.put("message", result.getMessage());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOtpStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Boolean isVerified = (Boolean) session.getAttribute("otp_verified");
        String phoneNumber = (String) session.getAttribute("otp_phone_number");
        String email = (String) session.getAttribute("otp_email");
        
        response.put("verified", isVerified != null && isVerified);
        response.put("phoneNumber", phoneNumber);
        response.put("email", email);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearOtpSession(HttpSession session) {
        session.removeAttribute("otp_verified");
        session.removeAttribute("otp_phone_number");
        session.removeAttribute("otp_email");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OTP session cleared");
        
        return ResponseEntity.ok(response);
    }
}
