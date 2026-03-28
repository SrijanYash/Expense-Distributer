@echo off
echo ====================================
echo Starting Spring Boot Microservices
echo ====================================
echo.

REM Set the base directory where all services are located
REM Modify this path to match your project structure
set BASE_DIR=%CD%

echo Starting Eureka Discovery Server...
start "Eureka-Demo" cmd /k "cd /d %BASE_DIR%\eureka-demo && mvn spring-boot:run"
timeout /t 10

echo Starting User Service...
start "User-Service" cmd /k "cd /d %BASE_DIR%\user-service && mvn spring-boot:run"
timeout /t 10

echo Starting Group Service...
start "Group-Service" cmd /k "cd /d %BASE_DIR%\group-service && mvn spring-boot:run"
timeout /t 10 

echo Starting Expense Service...
start "Expense-Service" cmd /k "cd /d %BASE_DIR%\expence-service && mvn spring-boot:run"
timeout /t 10

echo Starting User-Group Service...
start "User-Group-Service" cmd /k "cd /d %BASE_DIR%\user-group-service && mvn spring-boot:run"
timeout /t 10

echo Starting API Gateway Service...
start "API-Service" cmd /k "cd /d %BASE_DIR%\api-service && mvn spring-boot:run"
timeout /t 10

echo.
echo ====================================
echo All services are starting...
echo Each service is running in its own window
echo Close individual windows to stop services
echo ====================================
pause