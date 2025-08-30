# Google Drive Integration Setup Guide

## Overview
The data entry system now supports uploading images directly to Google Drive. This guide explains how to set up and configure Google Drive integration.

## Current Status
- **Mock Mode**: Currently running in mock mode (returns placeholder URLs)
- **Google Drive API**: Dependencies added but requires proper OAuth2 setup for production

## Features Added

### 1. File Upload Support
- **Category Form**: Upload category images to Google Drive
- **Subcategory Form**: Upload subcategory images to Google Drive  
- **Product Form**: Upload product images to Google Drive

### 2. Dual Image Options
- **Image URL**: Traditional URL input (existing functionality)
- **File Upload**: New file upload to Google Drive
- **Priority**: If both are provided, uploaded file takes priority

### 3. File Handling
- **Accepted Formats**: JPG, PNG, GIF
- **File Size Limit**: 10MB (configurable in application.properties)
- **Naming Convention**: `{type}_{timestamp}_{original_filename}`

## Configuration

### Application Properties
```properties
# Google Drive Configuration
google.drive.enabled=false  # Set to true when credentials are configured
google.drive.credentials.path=path/to/your/credentials.json
google.drive.folder.id=your-google-drive-folder-id

# File Upload Configuration  
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Production Setup (Required for Real Google Drive Integration)

### Step 1: Create Google Cloud Project
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google Drive API

### Step 2: Create Service Account
1. Go to "APIs & Services" → "Credentials"
2. Click "Create Credentials" → "Service Account"
3. Fill in service account details
4. Download the JSON credentials file

### Step 3: Configure Google Drive
1. Create a folder in Google Drive for storing images
2. Share the folder with the service account email
3. Copy the folder ID from the URL

### Step 4: Update Configuration
1. Place credentials JSON file in your project
2. Update `application.properties`:
   ```properties
   google.drive.enabled=true
   google.drive.credentials.path=src/main/resources/google-drive-credentials.json  
   google.drive.folder.id=your-actual-folder-id
   ```

### Step 5: Update GoogleDriveService
Replace the mock implementation in `GoogleDriveService.java` with proper authentication:

```java
@PostConstruct
public void initializeDriveService() {
    if (!googleDriveEnabled) return;
    
    try {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        // Load credentials from JSON file
        GoogleCredential credential = GoogleCredential
            .fromStream(new FileInputStream(credentialsPath))
            .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
            
        driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
            
        System.out.println("Google Drive service initialized successfully");
        
    } catch (Exception e) {
        System.err.println("Failed to initialize Google Drive service: " + e.getMessage());
        googleDriveEnabled = false;
    }
}
```

## Usage Instructions

### For Users
1. **Navigate** to `http://localhost:8000/admin/data-entry`
2. **Login** if required (authentication needed)
3. **Choose** Add Category, Subcategory, or Product
4. **Fill** in the required information
5. **Upload Image**: Either:
   - Enter an image URL in the "Image URL" field, OR  
   - Click "Choose File" and select an image to upload to Google Drive
6. **Submit** the form

### Image Handling Logic
- If only URL provided → Uses the URL
- If only file uploaded → Uploads to Google Drive and uses that URL
- If both provided → Uploads file to Google Drive (takes priority)

## Technical Details

### Dependencies Added
```xml
<!-- Google Drive API -->
<dependency>
    <groupId>com.google.apis</groupId>
    <artifactId>google-api-services-drive</artifactId>
    <version>v3-rev20220815-2.0.0</version>
</dependency>
<dependency>
    <groupId>com.google.auth</groupId>
    <artifactId>google-auth-library-oauth2-http</artifactId>
    <version>1.19.0</version>
</dependency>
<dependency>
    <groupId>com.google.http-client</groupId>
    <artifactId>google-http-client-jackson2</artifactId>
    <version>1.43.3</version>
</dependency>
```

### Form Changes
- Added `enctype="multipart/form-data"` to all forms
- Added file input fields with proper styling
- Added informational alerts about upload options

### Controller Updates
- Updated all save methods to handle `MultipartFile` parameters
- Added file upload logic before entity saving
- Added proper error handling for upload failures
- Added success messages using `RedirectAttributes`

## Testing

### Mock Mode Testing (Current)
1. Upload any image file through the forms
2. Check console logs for mock upload messages
3. Verify placeholder URLs are saved to database

### Production Testing (After Setup)
1. Upload image files through the forms
2. Check Google Drive folder for uploaded files
3. Verify public URLs are generated and saved
4. Test access to uploaded images via generated URLs

## Troubleshooting

### Common Issues
1. **File Size Too Large**: Increase limits in application.properties
2. **Invalid File Type**: Ensure only image files (JPG, PNG, GIF)
3. **Upload Failures**: Check Google Drive API credentials and permissions
4. **Template Errors**: Ensure all forms have `enctype="multipart/form-data"`

### Error Messages
- Form validation errors appear at the top of forms
- Upload errors are displayed with specific error messages
- Console logs provide detailed error information

## Security Considerations
- Service account has limited permissions (file creation only)
- Uploaded files are made publicly readable
- File names include timestamps to avoid conflicts
- Temporary files are cleaned up after upload
