# AWS OTP Setup Guide

## Prerequisites
1. AWS Account (Free Tier is sufficient)
2. AWS CLI installed (optional but recommended)

## AWS SNS Setup (for SMS OTP)

### Step 1: Create IAM User
1. Go to AWS Console → IAM → Users
2. Click "Add User"
3. User name: `otp-service-user`
4. Access type: Programmatic access
5. Attach policies:
   - `AmazonSNSFullAccess` (or create custom policy with sns:Publish permission)

### Step 2: Configure AWS Credentials
Add your AWS credentials to `application.properties`:

```properties
aws.access.key.id=YOUR_AWS_ACCESS_KEY_ID
aws.secret.access.key=YOUR_AWS_SECRET_ACCESS_KEY
aws.region=us-east-1
```

### Step 3: Enable SMS in AWS SNS
1. Go to AWS Console → SNS
2. Click "Text messaging (SMS)" → "Preferences"
3. Set spending limit (start with $1.00 for testing)
4. Choose default message type: "Transactional"

**Important**: For production, you need to:
- Verify your identity with AWS
- Request production access for SMS

## AWS SES Setup (for Email OTP)

### Step 1: Verify Your Email Domain/Address
1. Go to AWS Console → SES
2. Click "Verified identities"
3. Click "Create identity"
4. Choose "Email address" and add your sender email
5. Check your email and click the verification link

### Step 2: Configuration
Update `application.properties`:

```properties
aws.ses.from.email=noreply@yourdomain.com
```

**Important**: In SES Sandbox mode, you can only send emails to verified addresses.

## Testing the OTP System

### 1. SMS OTP Testing
- Use your own phone number for testing
- SMS costs approximately $0.00645 per message in the US

### 2. Email OTP Testing  
- Use verified email addresses in SES sandbox
- Email sending is free in AWS SES (up to limits)

## Free Tier Limits

### AWS SNS (SMS)
- Pay per message (~$0.00645 per SMS in US)
- No free SMS messages

### AWS SES (Email)
- First 62,000 emails per month are FREE
- After that: $0.10 per 1,000 emails

## Production Considerations

1. **Move out of SES Sandbox**: Request production access
2. **Phone Number Verification**: For SMS, you may need to verify your use case
3. **Rate Limiting**: Implement proper rate limiting (already included in the code)
4. **Security**: Use IAM roles instead of access keys in production
5. **Monitoring**: Set up CloudWatch alerts for costs and failures

## Environment Variables (Recommended for Production)

Instead of putting credentials in `application.properties`, use environment variables:

```bash
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key
export AWS_REGION=us-east-1
```

## Testing URLs

Once the application is running:

1. **Send OTP**: http://localhost:8000/otp/send
2. **Verify OTP**: http://localhost:8000/otp/verify
3. **API Status**: http://localhost:8000/otp/status

## Troubleshooting

### Common Issues:

1. **SMS not sending**:
   - Check if SMS service is enabled in your AWS region
   - Verify spending limits in SNS
   - Check phone number format (+1234567890)

2. **Email not sending**:
   - Verify the sender email in SES
   - Check if you're in sandbox mode
   - Verify recipient email if in sandbox

3. **AWS Credentials**:
   - Verify IAM user has correct permissions
   - Check access key and secret key are correct
   - Ensure region matches your AWS setup

### Cost Monitoring:
- Set up billing alerts in AWS
- Monitor usage in AWS Console
- Start with small spending limits

## Security Best Practices

1. **Rate Limiting**: Already implemented (3 OTPs per hour)
2. **Expiration**: OTPs expire in 10 minutes
3. **Attempt Limiting**: Max 3 verification attempts
4. **Cleanup**: Expired OTPs are automatically cleaned up
5. **Session Management**: OTP verification status is stored in session

## Code Integration Examples

### In Registration Controller:
```java
// Send OTP during registration
OtpServiceModel result = otpService.generateAndSendSmsOtp(phoneNumber, Otp.OtpType.REGISTRATION);

// Verify before completing registration
if (otpService.verifyOtp(phoneNumber, null, otpCode)) {
    // Complete registration
}
```

### In Login Controller:
```java
// Two-factor authentication
OtpServiceModel result = otpService.generateAndSendEmailOtp(email, Otp.OtpType.LOGIN);
```

The OTP system is now ready to use with AWS free tier services!
