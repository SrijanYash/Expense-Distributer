@echo off
echo ====================================
echo Starting Spring Boot Microservices
echo ====================================
echo.

REM Set the base directory where all services are located
REM Modify this path to match your project structure
set BASE_DIR=%CD%

echo Setting environment variables for database connection...
set SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require
set SPRING_DATASOURCE_USERNAME=postgres.naviyfnqtkimwjohuabz
set SPRING_DATASOURCE_PASSWORD=benjiro!344

echo Checking and assigning service ports to avoid conflicts...
set USER_PORT=8081
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr :%USER_PORT%') do set USER_BUSY=1
if defined USER_BUSY set USER_PORT=8181

set GROUP_PORT=8082
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr :%GROUP_PORT%') do set GROUP_BUSY=1
if defined GROUP_BUSY set GROUP_PORT=8092

set EXPENCE_PORT=8083
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr :%EXPENCE_PORT%') do set EXPENCE_BUSY=1
if defined EXPENCE_BUSY set EXPENCE_PORT=8193

set USER_GROUP_PORT=8084
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr :%USER_GROUP_PORT%') do set USER_GROUP_BUSY=1
if defined USER_GROUP_BUSY set USER_GROUP_PORT=8194

set API_PORT=8085
for /f "tokens=5" %%p in ('netstat -ano ^| findstr LISTENING ^| findstr :%API_PORT%') do set API_BUSY=1
if defined API_BUSY set API_PORT=8095

echo Starting Eureka Discovery Server...
start "Eureka-Demo" cmd /k "cd /d %BASE_DIR%\eureka-demo && mvn spring-boot:run"
timeout /t 10

echo Starting User Service...
start "User-Service" cmd /k "set SERVER_PORT=%USER_PORT% && cd /d %BASE_DIR%\user-service && mvn spring-boot:run"
timeout /t 10

echo Starting Group Service...
start "Group-Service" cmd /k "set SERVER_PORT=%GROUP_PORT% && cd /d %BASE_DIR%\group-service && mvn spring-boot:run"
timeout /t 10 

echo Starting Expense Service...
start "Expense-Service" cmd /k "set SERVER_PORT=%EXPENCE_PORT% && cd /d %BASE_DIR%\expence-service && mvn spring-boot:run"
timeout /t 10

echo Starting User-Group Service...
start "User-Group-Service" cmd /k "set SERVER_PORT=%USER_GROUP_PORT% && cd /d %BASE_DIR%\user-group-service && mvn spring-boot:run"
timeout /t 10

echo Starting API Gateway Service...
start "API-Service" cmd /k "set SERVER_PORT=%API_PORT% && cd /d %BASE_DIR%\api-service && mvn spring-boot:run"
timeout /t 10

echo.
echo ====================================
echo All services are starting...
echo Each service is running in its own window
echo Close individual windows to stop services
echo ====================================
pause
