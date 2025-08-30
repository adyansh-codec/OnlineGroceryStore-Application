# Real AWS SMS and Email OTP Implementation Guide

## Current Status

✅ **Mock Implementation Active**: The application currently uses mock implementations for both SMS (SNS) and Email (SES) services.
✅ **Database Integration**: OTPs are stored in PostgreSQL and verification works correctly.
✅ **Full UI/UX**: Complete frontend with proper CSRF token handling.

## To Enable Real SMS and Email Sending

### Step 1: Set Up AWS Account

1. **Sign up for AWS**: Go to https://aws.amazon.com/ and create an account
2. **Free Tier**: AWS offers free tier that includes:
   - SNS: 1,000 SMS messages per month (first 12 months)
   - SES: 62,000 emails per month (always free)

### Step 2: Configure AWS SNS (for SMS)

1. **Access AWS Console**: Login to AWS Console
2. **Go to SNS Service**: Search for "SNS" in services
3. **Create Topic** (Optional): For organized message sending
4. **Set up SMS preferences**:
   - Go to Text messaging (SMS) → SMS preferences
   - Set monthly spending limit (to control costs)
   - Set default message type (Promotional or Transactional)

### Step 3: Configure AWS SES (for Email)

1. **Go to SES Service**: Search for "SES" in AWS Console
2. **Verify Email Domain/Address**:
   - Go to "Verified identities"
   - Add your sender email address (e.g., noreply@yourdomain.com)  
   - Follow verification process
3. **Request Production Access**:
   - Initially, SES is in "sandbox mode" (can only send to verified emails)
   - Request production access to send to any email address
   - This may take 24-48 hours for approval

### Step 4: Create AWS IAM User

1. **Go to IAM Service**: Search for "IAM" in AWS Console
2. **Create User**:
   - Click "Users" → "Add user"
   - Choose "Programmatic access"
   - Create user name (e.g., "otp-service-user")
3. **Attach Policies**:
   - Attach `AmazonSNSFullAccess` policy
   - Attach `AmazonSESFullAccess` policy
   - Or create custom policy with minimal permissions
4. **Save Credentials**:
   - Download the Access Key ID and Secret Access Key
   - **Keep these secure and never commit to version control**

### Step 5: Update Application Configuration

Update your `application.properties`:

```properties
# AWS Configuration for REAL OTP sending
aws.access.key.id=YOUR_ACTUAL_AWS_ACCESS_KEY_ID
aws.secret.access.key=YOUR_ACTUAL_AWS_SECRET_ACCESS_KEY
aws.region=us-east-1
aws.enabled=true
aws.ses.from.email=your-verified-email@yourdomain.com

# OTP Configuration
otp.length=6
otp.max.attempts=3
otp.rate.limit.count=3
otp.rate.limit.minutes=60
```

### Step 6: Test the Implementation

1. **Start Application**: 
   ```bash
   mvn spring-boot:run
   ```

2. **Test SMS OTP**:
   - Go to http://localhost:8000/otp/send
   - Select SMS tab
   - Enter your phone number (with country code, e.g., +1234567890)
   - Click "Send SMS OTP"
   - You should receive a real SMS

3. **Test Email OTP**:
   - Go to Email tab
   - Enter any email address
   - Click "Send Email OTP"
   - Check your email inbox

### Step 7: Monitor Usage and Costs

1. **AWS CloudWatch**: Monitor SNS and SES usage
2. **Billing Dashboard**: Check costs regularly
3. **Set Billing Alerts**: Create alerts for unexpected charges

## Security Best Practices

### 1. Environment Variables (Recommended)
Instead of putting credentials in `application.properties`, use environment variables:

```bash
# Set environment variables (Windows)
set AWS_ACCESS_KEY_ID=your_access_key
set AWS_SECRET_ACCESS_KEY=your_secret_key

# Set environment variables (Linux/Mac)
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key
```

Then update `application.properties`:
```properties
aws.access.key.id=${AWS_ACCESS_KEY_ID}
aws.secret.access.key=${AWS_SECRET_ACCESS_KEY}
```

### 2. IAM Roles (For Production)
For production deployment on AWS EC2, use IAM roles instead of access keys.

### 3. Minimal Permissions
Create custom IAM policy with only required permissions:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "sns:Publish"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ses:SendEmail",
                "ses:SendRawEmail"
            ],
            "Resource": "*"
        }
    ]
}
```

## Troubleshooting

### Common Issues:

1. **SMS not delivered**:
   - Check phone number format (include country code)
   - Verify AWS SNS is available in your region
   - Check AWS CloudWatch logs
   - Ensure you're not in SNS sandbox mode

2. **Email not delivered**:
   - Verify sender email address in SES
   - Check spam folder
   - Ensure SES is out of sandbox mode
   - Check bounce/complaint rates

3. **AWS Credentials Error**:
   - Verify access key and secret key are correct
   - Check IAM user has proper permissions
   - Ensure credentials haven't expired

4. **Region Issues**:
   - Ensure your AWS region supports SNS/SES
   - Some regions have different availability

## Cost Estimation

### SMS (SNS) Costs:
- US: ~$0.0075 per SMS
- International: Varies by country ($0.02-$0.10+ per SMS)

### Email (SES) Costs:
- First 62,000 emails/month: Free
- After that: $0.10 per 1,000 emails

### Example Monthly Costs:
- 1,000 SMS OTPs: ~$7.50
- 10,000 Email OTPs: Free (under limit)
- Total estimated: ~$7.50/month for moderate usage

## Production Considerations

1. **Rate Limiting**: Already implemented in the service
2. **Monitoring**: Set up CloudWatch alarms
3. **Backup**: Consider multiple SMS providers
4. **Compliance**: Ensure GDPR/privacy compliance
5. **Logging**: Monitor OTP usage patterns

## Current Mock vs Real Behavior

### Mock Mode (Current):
- Console logs: "MOCK SMS: Sending OTP to..."
- Console logs: "MOCK EMAIL: Sending to..."
- No real messages sent
- Perfect for development/testing

### Real Mode (After Configuration):
- Actual SMS messages sent via AWS SNS
- Actual emails sent via AWS SES  
- Real costs incurred
- Production-ready

## Development vs Production

### Development:
```properties
aws.enabled=false  # Uses mock implementations
```

### Production:
```properties
aws.enabled=true   # Uses real AWS services
aws.access.key.id=your_real_key
aws.secret.access.key=your_real_secret
```

This allows you to develop and test without costs, then enable real messaging for production.
