@echo off
echo Setting up Java and Maven environment...

REM Set JAVA_HOME
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11.0.21.9-hotspot
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Program Files\Java\jdk-11.0.21
)
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Program Files\OpenJDK\openjdk-11.0.21_9
)

REM Set MAVEN_HOME
set MAVEN_HOME=C:\Program Files\apache-maven-3.9.5

REM Add to PATH
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo JAVA_HOME: %JAVA_HOME%
echo MAVEN_HOME: %MAVEN_HOME%

echo Testing installations...
java -version
echo.
mvn -version

echo.
echo Environment setup complete!
echo You can now run: mvn clean spring-boot:run
