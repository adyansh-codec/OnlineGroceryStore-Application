# Development Environment Setup Script for OnlineGroceryStore-Application
# Run this script as Administrator

Write-Host "Setting up development environment..." -ForegroundColor Green

# Check if Chocolatey is installed
if (!(Get-Command choco -ErrorAction SilentlyContinue)) {
    Write-Host "Installing Chocolatey..." -ForegroundColor Yellow
    Set-ExecutionPolicy Bypass -Scope Process -Force
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
    iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
}

# Install Java 11
Write-Host "Installing OpenJDK 11..." -ForegroundColor Yellow
choco install openjdk11 -y

# Install Maven
Write-Host "Installing Apache Maven..." -ForegroundColor Yellow
choco install maven -y

# Refresh environment variables
Write-Host "Refreshing environment variables..." -ForegroundColor Yellow
refreshenv

# Verify installations
Write-Host "Verifying installations..." -ForegroundColor Green
java -version
mvn -version

Write-Host "Development environment setup complete!" -ForegroundColor Green
Write-Host "Please restart your terminal/VS Code and try running the application again." -ForegroundColor Cyan
