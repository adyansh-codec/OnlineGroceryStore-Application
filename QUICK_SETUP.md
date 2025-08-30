# Alternative Setup Options

## Option 1: Use VS Code Java Extension Pack
1. Install "Extension Pack for Java" in VS Code
2. VS Code will automatically download and configure OpenJDK
3. This is the easiest approach for development

## Option 2: Manual Download Links
- Java 11 MSI: https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.21%2B9/OpenJDK11U-jdk_x64_windows_hotspot_11.0.21_9.msi
- Maven ZIP: https://archive.apache.org/dist/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip

## Option 3: Use run.bat script
Check if there's a run.bat file in the project root that doesn't require Maven to be in PATH.

## Option 4: Use the provided setup scripts
Run these in order:
1. `setup-dev-environment.bat` (as Administrator)
2. `setup-env.bat` (in current terminal)
3. Restart VS Code
4. Try `mvn clean spring-boot:run`

## Quick Test Commands:
```cmd
# Test Java installation
"C:\Program Files\Eclipse Adoptium\jdk-11.0.21.9-hotspot\bin\java" -version

# Test Maven installation  
"C:\Program Files\apache-maven-3.9.5\bin\mvn" --version

# Run the application directly
"C:\Program Files\apache-maven-3.9.5\bin\mvn" clean spring-boot:run
```
