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
# -Djava.net.preferIPv4Stack=true: Force IPv4 to avoid IPv6 localhost issues
# -XX:MaxRAMPercentage: Use max 70% of container memory for heap
# -XX:InitialRAMPercentage: Start with 25% to reduce initial footprint
# -XX:+UseSerialGC: Serial GC has lower overhead than G1/Parallel
# -XX:MaxMetaspaceSize: Limit metaspace to prevent unbounded growth
JVM_OPTS="-Djava.net.preferIPv4Stack=true -XX:+UseContainerSupport -XX:MaxRAM=128m -XX:InitialRAMPercentage=15 -XX:MaxRAMPercentage=25 -XX:+UseSerialGC -XX:MaxMetaspaceSize=64m -XX:CompressedClassSpaceSize=32m -XX:+ExitOnOutOfMemoryError"

# Function to start a service with auto-restart
start_service() {
  local name=$1
  local port=$2
  local heap=$3
  local jar_name=$4

  echo "Starting $name on port $port..."
  (
    while true; do
      java $JVM_OPTS -Xms${heap}m -Xmx$((heap*2))m -Dserver.port=$port -jar /app/$jar_name.jar
      echo "$name exited. Restarting in 5s..." >&2
      sleep 5
    done
  ) &
  eval "${name^^}_PID=$!"
  wait_for_port $port "$name"
  echo "$name started (PID ${!name^^_PID})"
}

# Start all backend services with auto-restart
start_service "user" 8081 16 "user-service"
start_service "group" 8082 16 "group-service"
start_service "expence" 8083 16 "expence-service"
start_service "usergroup" 8084 16 "user-group-service"

echo "Starting api-service (gateway) on port ${PORT:-8085}..."
java $JVM_OPTS -Xms32m -Xmx64m -Dserver.port=${PORT:-8085} -jar /app/api-service.jar &
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
