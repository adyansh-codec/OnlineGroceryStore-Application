@echo off
echo Starting Online Grocery Store Application...
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed or not in PATH.
    echo Please install Maven or use your IDE to run the application.
    echo.
    echo Alternative: If you have Java installed, you can compile and run manually:
    echo 1. Install Maven from https://maven.apache.org/download.cgi
    echo 2. Add Maven to your PATH
    echo 3. Run: mvn clean spring-boot:run
    echo.
    pause
    exit /b 1
)

echo Maven found! Starting application...
echo.

REM Clean and compile the project
echo Step 1: Cleaning and compiling...
mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Starting Spring Boot application...
echo The application will be available at: http://localhost:8000
echo.
echo OTP functionality available at: http://localhost:8000/otp/send
echo.

REM Run the Spring Boot application
mvn spring-boot:run

pause
