@echo off
echo Starting Expense Distributer Application...

:: Start the backend services in a new window
start cmd /k "cd Backend && call run-backend.bat"

:: Wait for backend to initialize
echo Waiting for backend services to start...
timeout /t 10 /nobreak

:: Start the frontend in a new window
start cmd /k "cd frontend && npm start"

echo Expense Distributer application is starting...
echo Backend: http://localhost:8085
echo Frontend: http://localhost:3000