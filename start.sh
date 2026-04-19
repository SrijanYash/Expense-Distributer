#!/bin/bash

echo "=== Starting Expense Distributer (Single Instance) ==="
echo "EUREKA_ZIPKIN_ENABLED=${EUREKA_ZIPKIN_ENABLED:-false}"
echo ""

# Start all backend services in background
echo "Starting user-service on port 8081..."
java -Xms128m -Xmx256m -jar /app/user-service.jar &
USER_PID=$!

sleep 3

echo "Starting group-service on port 8082..."
java -Xms128m -Xmx256m -jar /app/group-service.jar &
GROUP_PID=$!

sleep 3

echo "Starting expence-service on port 8083..."
java -Xms128m -Xmx256m -jar /app/expence-service.jar &
EXPENCE_PID=$!

sleep 3

echo "Starting user-group-service on port 8084..."
java -Xms128m -Xmx256m -jar /app/user-group-service.jar &
USERGROUP_PID=$!

sleep 3

echo "Starting api-service (gateway) on port 8085..."
java -Xms256m -Xmx512m -jar /app/api-service.jar &
API_PID=$!

echo ""
echo "=== All services started ==="
echo "PIDs: user=$USER_PID, group=$GROUP_PID, expence=$EXPENCE_PID, usergroup=$USERGROUP_PID, api=$API_PID"
echo "API Gateway available on port 8085"
echo ""

# Wait for any process to exit
wait -n

# If we reach here, one service exited
echo "A service exited. Stopping all services..."
kill $USER_PID $GROUP_PID $EXPENCE_PID $USERGROUP_PID $API_PID 2>/dev/null
exit 1
