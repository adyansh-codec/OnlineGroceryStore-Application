@echo off
echo Setting up development environment for OnlineGroceryStore-Application...

echo Checking for Chocolatey...
where choco >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Installing Chocolatey...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
)

echo Installing OpenJDK 11...
choco install openjdk11 -y

echo Installing Apache Maven...
choco install maven -y

echo Refreshing environment...
call refreshenv

echo Verifying installations...
java -version
mvn -version

echo.
echo Development environment setup complete!
echo Please restart your terminal/VS Code and try running the application again.
pause
