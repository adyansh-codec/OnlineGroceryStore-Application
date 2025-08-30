# Java and Maven Installation Guide

## Option 1: Using Chocolatey (Recommended)

### Install Chocolatey first:
```powershell
# Run PowerShell as Administrator and execute:
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### Install Java and Maven:
```cmd
# After Chocolatey is installed, run:
choco install openjdk11 -y
choco install maven -y
```

## Option 2: Manual Installation

### Install Java 11:
1. Download OpenJDK 11 from: https://adoptium.net/temurin/releases/
2. Choose: OpenJDK 11 (LTS) for Windows x64
3. Download the .msi installer
4. Run installer and install to default location
5. Note the installation path (usually: C:\Program Files\Eclipse Adoptium\jdk-11.x.x.x-hotspot\)

### Install Maven:
1. Download Maven from: https://maven.apache.org/download.cgi
2. Download the Binary zip archive (apache-maven-3.x.x-bin.zip)
3. Extract to: C:\Program Files\Apache Maven\apache-maven-3.x.x\
4. Add to System Environment Variables:
   - JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-11.x.x.x-hotspot\
   - MAVEN_HOME = C:\Program Files\Apache Maven\apache-maven-3.x.x\
   - Add to PATH: %JAVA_HOME%\bin;%MAVEN_HOME%\bin

## Option 3: Use Project Wrapper (Recommended for this project)

If you don't want to install Maven globally, you can use the Maven Wrapper if it exists in the project:

```cmd
# Look for these files in the project root:
mvnw.cmd (Windows)
mvnw (Linux/Mac)

# If they exist, use them instead of mvn:
.\mvnw.cmd clean spring-boot:run
```

## Verification Commands:
```cmd
java -version
mvn -version
```

## Troubleshooting:
- Restart your terminal/VS Code after installation
- Make sure to set JAVA_HOME environment variable
- Ensure both Java and Maven bin folders are in PATH
- Run `refreshenv` if using Chocolatey

## Quick Test:
```cmd
mvn --version
```
Should show Maven version and Java version being used.
