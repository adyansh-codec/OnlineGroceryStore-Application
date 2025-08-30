# How to Run the Online Grocery Store Application

## Prerequisites
1. **Java 11 or higher** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. **Maven** - Download from [Apache Maven](https://maven.apache.org/download.cgi)
3. **PostgreSQL** - Make sure PostgreSQL is running with the database configured in `application.properties`

## Running the Application

### Method 1: Using the Batch File (Windows)
1. Double-click `run.bat` in the project root
2. The batch file will check dependencies and start the application

### Method 2: Using Maven Commands
```bash
# Navigate to project directory
cd g:\OnlineGroceryStore-Application

# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

### Method 3: Using VS Code
1. Open the project in VS Code
2. Use Ctrl+Shift+P and run "Tasks: Run Task"
3. Select "Test Fixed Index Template"

### Method 4: Using IDE (IntelliJ IDEA/Eclipse)
1. Import the project as a Maven project
2. Right-click on `OnlineGroceryApplication.java`
3. Select "Run" or "Debug"

## Application URLs

Once the application is running, access these URLs:

- **Home Page**: http://localhost:8000/home
- **OTP Send**: http://localhost:8000/otp/send  
- **OTP Verify**: http://localhost:8000/otp/verify
- **Category Test**: http://localhost:8000/categories/test

## Common Issues and Solutions

### 1. "Unknown lifecycle phase 'build'"
**Problem**: Using incorrect Maven command
**Solution**: Use correct Maven lifecycle phases:
- `mvn clean` - Clean project
- `mvn compile` - Compile code
- `mvn spring-boot:run` - Run application
- `mvn package` - Create JAR/WAR file

### 2. Maven not found
**Problem**: Maven not installed or not in PATH
**Solutions**:
- Install Maven from https://maven.apache.org/download.cgi
- Add Maven bin directory to your system PATH
- Use IDE's built-in Maven support

### 3. Java version issues
**Problem**: Wrong Java version
**Solution**: 
- Ensure Java 11 or higher is installed
- Check with `java -version`
- Set JAVA_HOME environment variable

### 4. Database connection issues
**Problem**: PostgreSQL connection failed
**Solution**:
- Start PostgreSQL service
- Verify database name, username, password in `application.properties`
- Create the database if it doesn't exist

### 5. Port already in use
**Problem**: Port 8000 is already occupied
**Solution**:
- Change port in `application.properties`: `server.port=8080`
- Or kill the process using the port

## Development Mode

For development with auto-reload:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Building for Production

```bash
# Create JAR file
mvn clean package

# Run the JAR file
java -jar target/onlinemarket-1.0.0-SNAPSHOT.war
```

## OTP Setup

Before using OTP functionality:
1. Set up AWS credentials in `application.properties`
2. Follow the guide in `AWS_OTP_SETUP.md`
3. Test with the OTP pages

## Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Verify all prerequisites are installed
3. Check database connectivity
4. Review the application.properties configuration

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── org/softuni/onlinegrocery/
│   │       ├── OnlineGroceryApplication.java (Main class)
│   │       ├── config/
│   │       ├── domain/
│   │       ├── repository/
│   │       ├── service/
│   │       └── web/controllers/
│   └── resources/
│       ├── application.properties
│       ├── templates/
│       └── static/
└── test/
```

The main application class is `OnlineGroceryApplication.java` - this is what you need to run to start the server.
