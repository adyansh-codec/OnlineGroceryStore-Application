@echo off
echo Starting Online Grocery Store on Port 8001 (Admin Mode)
echo Access admin panel at: http://localhost:8001/admin/data-entry
echo.
mvn clean spring-boot:run -Dspring-boot.run.profiles=admin
pause
