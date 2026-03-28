# Notes on Today's Changes

## Backend Changes

### 1. CORS Configuration
- A new `CorsConfig.java` file was created in the backend API Gateway service.
- This configuration allows cross-origin requests from the frontend.
- Specific settings include allowed origins, methods, and headers to ensure seamless communication between the frontend and backend.

### 2. API Gateway Routing
- The `ApiServiceApplication.java` file was updated to enable service discovery and routing.
- A `customRouteLocator` bean was added to define routes for the following services:
  - User Service
  - Group Service
  - Expense Service
  - User-Group Service

## Frontend Changes

### 1. Proxy Configuration
- The `package.json` file in the frontend was updated to include a proxy configuration pointing to `http://localhost:8085`.
- This ensures that API requests from the frontend are routed to the backend during development.

## Script for Running Both Frontend and Backend

### 1. `run-app.bat`
- A batch script named `run-app.bat` was created to start both the frontend and backend services simultaneously.
- The script includes commands to navigate to their respective directories and execute their start scripts.
- A timeout was introduced to allow the backend to initialize before starting the frontend.