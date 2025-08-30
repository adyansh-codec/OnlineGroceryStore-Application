package org.softuni.onlinegrocery.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveService {

    private static final String APPLICATION_NAME = "Online Grocery Store";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${google.drive.folder.id:}")
    private String folderId;

    @Value("${google.drive.credentials.path:#{null}}")
    private String credentialsPath;

    @Value("${google.drive.enabled:false}")
    private boolean googleDriveEnabled;

    private Drive driveService;

    @PostConstruct
    public void initializeDriveService() {
        if (!googleDriveEnabled) {
            System.out.println("Google Drive is disabled. Using mock implementation.");
            return;
        }

        if (credentialsPath == null || credentialsPath.isEmpty()) {
            System.err.println("Google Drive credentials path not configured.");
            googleDriveEnabled = false;
            return;
        }

        if (folderId == null || folderId.isEmpty() || folderId.equals("YOUR_FOLDER_ID_HERE")) {
            System.err.println("Google Drive folder ID not configured.");
            googleDriveEnabled = false;
            return;
        }

        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            
            // Load credentials from JSON file - try multiple path approaches
            java.io.File credentialsFile = new java.io.File(credentialsPath);
            
            // If relative path doesn't work, try as resource
            if (!credentialsFile.exists()) {
                // Try loading from classpath
                InputStream credentialsStream = getClass().getClassLoader()
                    .getResourceAsStream("credentials/google-drive-credentials.json");
                    
                if (credentialsStream == null) {
                    System.err.println("Credentials file not found: " + credentialsPath);
                    System.err.println("Also tried classpath: credentials/google-drive-credentials.json");
                    googleDriveEnabled = false;
                    return;
                }
                
                GoogleCredential credential = GoogleCredential
                    .fromStream(credentialsStream)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
                    
                driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
                    
            } else {
                // File exists, use file path
                GoogleCredential credential = GoogleCredential
                    .fromStream(new FileInputStream(credentialsFile))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
                    
                driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            }
                
            System.out.println("Google Drive service initialized successfully with folder ID: " + folderId);
            
            // Test folder access
            testFolderAccess();
            
        } catch (Exception e) {
            System.err.println("Failed to initialize Google Drive service: " + e.getMessage());
            e.printStackTrace();
            googleDriveEnabled = false;
        }
    }

    public String uploadImage(MultipartFile multipartFile, String fileName) throws IOException {
        if (!googleDriveEnabled) {
            return uploadImageMock(multipartFile, fileName);
        }

        try {
            System.out.println("Attempting to upload to Google Drive folder: " + folderId);
            
            // Convert MultipartFile to temporary file
            java.io.File tempFile = convertMultipartFileToFile(multipartFile);
            
            // Create file metadata
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            // Create file content
            FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);

            // Upload file - use supportsAllDrives for shared drives
            File file = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id,webViewLink,webContentLink")
                    .setSupportsAllDrives(true)
                    .execute();

            // Make file publicly viewable
            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");
            driveService.permissions().create(file.getId(), permission)
                    .setSupportsAllDrives(true)
                    .execute();

            // Clean up temporary file
            tempFile.delete();

            // Return viewable link
            String publicLink = "https://drive.google.com/uc?id=" + file.getId();
            System.out.println("File uploaded successfully: " + publicLink);
            
            return publicLink;

        } catch (Exception e) {
            System.err.println("Error uploading to Google Drive: " + e.getMessage());
            throw new IOException("Failed to upload image to Google Drive", e);
        }
    }

    private String uploadImageMock(MultipartFile multipartFile, String fileName) {
        // Mock implementation - returns a placeholder URL
        String mockUrl = "https://drive.google.com/uc?id=mock-" + System.currentTimeMillis();
        System.out.println("Mock Google Drive upload: " + fileName + " -> " + mockUrl);
        return mockUrl;
    }

    private java.io.File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        java.io.File tempFile = java.io.File.createTempFile("upload", null);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    public boolean isEnabled() {
        return googleDriveEnabled;
    }

    public String getUploadInfo() {
        if (googleDriveEnabled) {
            return "Google Drive upload is enabled";
        } else {
            return "Google Drive upload is disabled (using mock URLs)";
        }
    }

    public void testFolderAccess() {
        if (!googleDriveEnabled) {
            System.out.println("Google Drive is disabled - cannot test folder access");
            return;
        }

        try {
            // Test the specific folder directly
            File folder = driveService.files().get(folderId)
                    .setSupportsAllDrives(true)
                    .execute();
            System.out.println("✓ Folder access test successful!");
            System.out.println("  Folder name: " + folder.getName());
            System.out.println("  Folder ID: " + folder.getId());
            System.out.println("  Ready for file uploads!");
        } catch (Exception e) {
            System.err.println("✗ Folder access test failed!");
            System.err.println("  Error: " + e.getMessage());
            System.err.println("  SOLUTION:");
            System.err.println("    1. Create a new Shared Drive in Google Drive");
            System.err.println("    2. Add this service account as Manager:");
            System.err.println("       grocery-store-drive@online-grocery-store-467101.iam.gserviceaccount.com");
            System.err.println("    3. Update the folder ID in application.properties");
        }
    }
}
