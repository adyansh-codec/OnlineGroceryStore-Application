package org.softuni.onlinegrocery.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.entities.Otp;
import org.softuni.onlinegrocery.domain.models.service.OtpServiceModel;
import org.softuni.onlinegrocery.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final ModelMapper modelMapper;
    private final AmazonSNS amazonSNS;
    private final AmazonSimpleEmailService amazonSES;

    @Value("${otp.length:6}")
    private int otpLength;

    @Value("${otp.max.attempts:3}")
    private int maxAttempts;

    @Value("${otp.rate.limit.count:3}")
    private int rateLimitCount;

    @Value("${otp.rate.limit.minutes:60}")
    private int rateLimitMinutes;

    @Value("${aws.ses.from.email:noreply@yourdomain.com}")
    private String fromEmail;

    private static final SecureRandom random = new SecureRandom();

    @Autowired
    public OtpServiceImpl(OtpRepository otpRepository, ModelMapper modelMapper, 
                         AmazonSNS amazonSNS, AmazonSimpleEmailService amazonSES) {
        this.otpRepository = otpRepository;
        this.modelMapper = modelMapper;
        this.amazonSNS = amazonSNS;
        this.amazonSES = amazonSES;
    }

    @Override
    public OtpServiceModel generateAndSendSmsOtp(String phoneNumber, Otp.OtpType otpType) {
        try {
            // Check rate limit
            if (isRateLimitExceededForPhone(phoneNumber)) {
                return new OtpServiceModel(false, "Rate limit exceeded. Please try again later.");
            }

            // Generate OTP
            String otpCode = generateOtpCode();
            
            // Save to database
            Otp otp = new Otp(phoneNumber, null, otpCode, otpType);
            otp = otpRepository.save(otp);

            // Send SMS via AWS SNS
            String message = "Your OTP code is: " + otpCode + ". This code will expire in 10 minutes.";
            PublishRequest publishRequest = new PublishRequest()
                    .withPhoneNumber(phoneNumber)
                    .withMessage(message);

            PublishResult result = amazonSNS.publish(publishRequest);

            if (result.getMessageId() != null) {
                OtpServiceModel serviceModel = modelMapper.map(otp, OtpServiceModel.class);
                serviceModel.setSuccess(true);
                serviceModel.setMessage("OTP sent successfully to " + phoneNumber);
                return serviceModel;
            } else {
                return new OtpServiceModel(false, "Failed to send OTP");
            }

        } catch (Exception e) {
            System.err.println("Error sending SMS OTP: " + e.getMessage());
            return new OtpServiceModel(false, "Failed to send OTP: " + e.getMessage());
        }
    }

    @Override
    public OtpServiceModel generateAndSendEmailOtp(String email, Otp.OtpType otpType) {
        try {
            // Check rate limit
            if (isRateLimitExceededForEmail(email)) {
                return new OtpServiceModel(false, "Rate limit exceeded. Please try again later.");
            }

            // Generate OTP
            String otpCode = generateOtpCode();
            
            // Save to database
            Otp otp = new Otp(null, email, otpCode, otpType);
            otp = otpRepository.save(otp);

            // Send Email via AWS SES
            String subject = "Your OTP Code - Grocery Store";
            String body = buildEmailBody(otpCode, otpType);

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(email))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content().withCharset("UTF-8").withData(body))
                                    .withText(new Content().withCharset("UTF-8").withData("Your OTP code is: " + otpCode)))
                            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                    .withSource(fromEmail);

            SendEmailResult result = amazonSES.sendEmail(request);

            if (result.getMessageId() != null) {
                OtpServiceModel serviceModel = modelMapper.map(otp, OtpServiceModel.class);
                serviceModel.setSuccess(true);
                serviceModel.setMessage("OTP sent successfully to " + email);
                return serviceModel;
            } else {
                return new OtpServiceModel(false, "Failed to send OTP");
            }

        } catch (Exception e) {
            System.err.println("Error sending Email OTP: " + e.getMessage());
            return new OtpServiceModel(false, "Failed to send OTP: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyOtp(String phoneNumber, String email, String otpCode) {
        try {
            Optional<Otp> otpOptional = Optional.empty();
            
            // Try to find OTP by phone number first
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                otpOptional = otpRepository.findValidOtpByPhoneAndCode(phoneNumber, otpCode, LocalDateTime.now());
            }
            
            // If not found by phone, try by email
            if (!otpOptional.isPresent() && email != null && !email.isEmpty()) {
                otpOptional = otpRepository.findValidOtpByEmailAndCode(email, otpCode, LocalDateTime.now());
            }

            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();
                
                if (otp.getAttempts() >= maxAttempts) {
                    return false;
                }

                if (otp.getOtpCode().equals(otpCode)) {
                    otpRepository.markAsVerified(otp.getId());
                    return true;
                } else {
                    otp.incrementAttempts();
                    otpRepository.save(otp);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error verifying OTP: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyOtpByCode(String otpCode) {
        try {
            Optional<Otp> otpOptional = otpRepository.findValidOtpByCode(otpCode, LocalDateTime.now());

            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();
                
                if (otp.getAttempts() >= maxAttempts) {
                    return false;
                }

                if (otp.getOtpCode().equals(otpCode)) {
                    otpRepository.markAsVerified(otp.getId());
                    return true;
                } else {
                    otp.incrementAttempts();
                    otpRepository.save(otp);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error verifying OTP by code: " + e.getMessage());
            return false;
        }
    }

    @Override
    public OtpServiceModel resendOtp(String phoneNumber, String email) {
        try {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                return generateAndSendSmsOtp(phoneNumber, Otp.OtpType.SMS);
            } else if (email != null && !email.isEmpty()) {
                return generateAndSendEmailOtp(email, Otp.OtpType.EMAIL);
            } else {
                return new OtpServiceModel(false, "Phone number or email is required");
            }
        } catch (Exception e) {
            return new OtpServiceModel(false, "Failed to resend OTP: " + e.getMessage());
        }
    }

    @Override
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupExpiredOtps() {
        try {
            otpRepository.deleteExpiredOtps(LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Error cleaning up expired OTPs: " + e.getMessage());
        }
    }

    @Override
    public boolean isRateLimitExceeded(String phoneNumber, String email) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            return isRateLimitExceededForPhone(phoneNumber);
        } else if (email != null && !email.isEmpty()) {
            return isRateLimitExceededForEmail(email);
        }
        return false;
    }

    private boolean isRateLimitExceededForPhone(String phoneNumber) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(rateLimitMinutes);
        int count = otpRepository.countOtpsByPhoneNumberSince(phoneNumber, since);
        return count >= rateLimitCount;
    }

    private boolean isRateLimitExceededForEmail(String email) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(rateLimitMinutes);
        int count = otpRepository.countOtpsByEmailSince(email, since);
        return count >= rateLimitCount;
    }

    private String generateOtpCode() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private String buildEmailBody(String otpCode, Otp.OtpType otpType) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<h2 style='color: #333;'>Your OTP Code</h2>" +
                "<p>Hello,</p>" +
                "<p>Your OTP code for " + getOtpTypeDescription(otpType) + " is:</p>" +
                "<div style='background-color: #f4f4f4; padding: 20px; text-align: center; margin: 20px 0;'>" +
                "<h1 style='color: #007bff; margin: 0; font-size: 32px; letter-spacing: 8px;'>" + otpCode + "</h1>" +
                "</div>" +
                "<p><strong>This code will expire in 10 minutes.</strong></p>" +
                "<p>If you didn't request this code, please ignore this email.</p>" +
                "<hr style='margin: 30px 0;'>" +
                "<p style='color: #666; font-size: 12px;'>This is an automated message from Grocery Store. Please do not reply.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String getOtpTypeDescription(Otp.OtpType otpType) {
        switch (otpType) {
            case LOGIN: return "login";
            case REGISTRATION: return "registration";
            case PASSWORD_RESET: return "password reset";
            default: return "verification";
        }
    }
}