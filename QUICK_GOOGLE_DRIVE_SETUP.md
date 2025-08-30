# Quick Setup Guide: Upload Images to Your Google Drive

## What You Need to Do

### 1. Download Your Credentials File
After completing Steps 1-4 from the Google Cloud Console setup:
- Save the JSON credentials file as `google-drive-credentials.json`
- Place it in: `g:\OnlineGroceryStore-Application\src\main\resources\credentials\`

### 2. Get Your Folder ID
1. Create a folder in Google Drive for your images
2. Share it with your service account email (from the JSON file)
3. Copy the folder ID from the URL (the long string after `/folders/`)

### 3. Update Configuration
Replace `YOUR_FOLDER_ID_HERE` in your `application.properties` with your actual folder ID:

```properties
# Google Drive Configuration
google.drive.enabled=true
google.drive.credentials.path=src/main/resources/credentials/google-drive-credentials.json
google.drive.folder.id=1ABC123DEF456  # Replace with your actual folder ID
```

### 4. Restart Your Application
After making these changes, restart your Spring Boot application.

## Testing

1. Go to `http://localhost:8000/admin/data-entry`
2. You should see a GREEN status message: "Google Drive upload is enabled"
3. Try uploading an image through any of the forms
4. Check your Google Drive folder - the image should appear there!

## If Something Goes Wrong

Check the console logs for error messages. Common issues:

- **File not found**: Make sure the credentials file is in the correct location
- **Folder ID invalid**: Double-check the folder ID from your Google Drive URL  
- **Permission denied**: Ensure you shared the folder with the service account email
- **API not enabled**: Make sure Google Drive API is enabled in Google Cloud Console

## Current Status

Your project is ready! Just need to:
1. Place the credentials file
2. Update the folder ID
3. Restart the application

The code changes are all complete and ready to work with real Google Drive integration.
