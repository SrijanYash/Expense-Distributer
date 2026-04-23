#!/bin/bash

echo "=== Starting Expense Distributer (Single Instance) ==="
echo "EUREKA_ZIPKIN_ENABLED=${EUREKA_ZIPKIN_ENABLED:-false}"
echo ""

wait_for_port() {
  local port=$1
  local name=$2
  local max_attempts=30
  local attempt=0
  echo "Waiting for $name on port $port..."
  while ! nc -z localhost $port 2>/dev/null; do
    attempt=$((attempt + 1))
    if [ $attempt -ge $max_attempts ]; then
      echo "$name failed to start on port $port"
      return 1
    fi
    sleep 1
  done
  echo "$name is ready on port $port"
}

# JVM flags optimized for low-memory containers (512MB free tier)
# -XX:MaxRAMPercentage: Use max 70% of container memory for heap
# -XX:InitialRAMPercentage: Start with 25% to reduce initial footprint
# -XX:+UseSerialGC: Serial GC has lower overhead than G1/Parallel
# -XX:MaxMetaspaceSize: Limit metaspace to prevent unbounded growth
JVM_OPTS="-XX:+UseContainerSupport -XX:InitialRAMPercentage=25 -XX:MaxRAMPercentage=70 -XX:+UseSerialGC -XX:MaxMetaspaceSize=128m -XX:+ExitOnOutOfMemoryError"

# Start all backend services in background with minimal heap
# Note: Explicitly set -Dserver.port to override any SERVER_PORT env var from cloud provider
echo "Starting user-service on port 8081..."
java $JVM_OPTS -Xms64m -Xmx96m -Dserver.port=8081 -jar /app/user-service.jar &
USER_PID=$!
wait_for_port 8081 "user-service"

echo "Starting group-service on port 8082..."
java $JVM_OPTS -Xms64m -Xmx96m -Dserver.port=8082 -jar /app/group-service.jar &
GROUP_PID=$!
wait_for_port 8082 "group-service"

echo "Starting expence-service on port 8083..."
java $JVM_OPTS -Xms64m -Xmx96m -Dserver.port=8083 -jar /app/expence-service.jar &
EXPENCE_PID=$!
wait_for_port 8083 "expence-service"

echo "Starting user-group-service on port 8084..."
java $JVM_OPTS -Xms64m -Xmx96m -Dserver.port=8084 -jar /app/user-group-service.jar &
USERGROUP_PID=$!
wait_for_port 8084 "user-group-service"

echo "Starting api-service (gateway) on port ${PORT:-8085}..."
java $JVM_OPTS -Xms96m -Xmx128m -Dserver.port=${PORT:-8085} -jar /app/api-service.jar &
API_PID=$!
wait_for_port ${PORT:-8085} "api-service"

echo ""
echo "=== All services started ==="
echo "PIDs: user=$USER_PID, group=$GROUP_PID, expence=$EXPENCE_PID, usergroup=$USERGROUP_PID, api=$API_PID"
echo "API Gateway available on port 8085"
echo ""

# Wait for any process to exit (POSIX-compatible)
wait -n 2>/dev/null || {
  # Fallback for shells without wait -n: wait for all processes
  wait $USER_PID 2>/dev/null || wait $GROUP_PID 2>/dev/null || wait $EXPENCE_PID 2>/dev/null || wait $USERGROUP_PID 2>/dev/null || wait $API_PID 2>/dev/null
}

# If we reach here, one service exited
echo "A service exited. Stopping all services..."
kill $USER_PID $GROUP_PID $EXPENCE_PID $USERGROUP_PID $API_PID 2>/dev/null
exit 1
