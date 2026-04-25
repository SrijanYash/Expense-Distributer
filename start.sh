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

# Start services with auto-restart wrapper
start_with_restart() {
  local jar_name=$1
  local port=$2
  local heap=$3

  while true; do
    java $JVM_OPTS -Xms${heap}m -Xmx$((heap*2))m -Dserver.port=$port -jar /app/$jar_name.jar
    echo "$(date): $jar_name exited. Restarting in 5s..."
    sleep 5
  done
}

# Start all backend services with auto-restart in background
echo "Starting user-service on port 8081..."
start_with_restart "user-service" 8081 16 &
USER_PID=$!
wait_for_port 8081 "user-service"

echo "Starting group-service on port 8082..."
start_with_restart "group-service" 8082 16 &
GROUP_PID=$!
wait_for_port 8082 "group-service"

echo "Starting expence-service on port 8083..."
start_with_restart "expence-service" 8083 16 &
EXPENCE_PID=$!
wait_for_port 8083 "expence-service"

echo "Starting user-group-service on port 8084..."
start_with_restart "user-group-service" 8084 16 &
USERGROUP_PID=$!
wait_for_port 8084 "user-group-service"

echo "Starting api-service (gateway) on port ${PORT:-8085}..."
java $JVM_OPTS -Xms32m -Xmx64m -Dserver.port=${PORT:-8085} -jar /app/api-service.jar &
API_PID=$!
wait_for_port ${PORT:-8085} "api-service"

echo ""
echo "=== All services started ==="
echo "PIDs: user=$USER_PID, group=$GROUP_PID, expence=$EXPENCE_PID, usergroup=$USERGROUP_PID, api=$API_PID"
echo "API Gateway available on port 8085"
echo ""

# Keep running - if any service exits, restart it (shouldn't happen due to while loops above)
wait
