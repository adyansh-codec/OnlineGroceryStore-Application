@echo off
echo Starting Online Grocery Store Application (Direct Path Method)...
echo.

REM Define paths to Java and Maven
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11.0.21.9-hotspot
set MAVEN_HOME=C:\Program Files\apache-maven-3.9.5

REM Check alternative Java locations
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Program Files\Java\jdk-11.0.21
)
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Program Files\OpenJDK\openjdk-11.0.21_9
)

REM Set PATH for this session
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo Using JAVA_HOME: %JAVA_HOME%
echo Using MAVEN_HOME: %MAVEN_HOME%
echo.

REM Check if Java is available
"%JAVA_HOME%\bin\java" -version
if %ERRORLEVEL% NEQ 0 (
    echo Java not found! Please install Java 11 first.
    echo Download from: https://adoptium.net/temurin/releases/
    pause
    exit /b 1
)

echo.
REM Check if Maven is available
"%MAVEN_HOME%\bin\mvn" --version
if %ERRORLEVEL% NEQ 0 (
    echo Maven not found! Please install Maven first.
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo.
echo Both Java and Maven found! Starting application...
echo.

REM Clean and compile the project
echo Step 1: Cleaning and compiling...
"%MAVEN_HOME%\bin\mvn" clean compile
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Starting Spring Boot application...
echo The application will be available at: http://localhost:8000
echo.
echo Press Ctrl+C to stop the application
echo.

REM Run the Spring Boot application
"%MAVEN_HOME%\bin\mvn" spring-boot:run

pause
