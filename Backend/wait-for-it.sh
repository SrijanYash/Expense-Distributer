#!/usr/bin/env bash
# wait-for-it.sh - Wait for a service to be available before executing a command

set -e

HOST=""
PORT=""
TIMEOUT=15
QUIET=0
COMMAND=()

while [[ $# -gt 0 ]]
do
    case "$1" in
        *:* )
            HOST=$(echo "$1" | cut -d: -f1)
            PORT=$(echo "$1" | cut -d: -f2)
            shift 1
            ;;
        -t|--timeout)
            TIMEOUT="$2"
            shift 2
            ;;
        -q|--quiet)
            QUIET=1
            shift 1
            ;;
        --)
            shift
            COMMAND=("$@")
            break
            ;;
        *)
            COMMAND=("$@")
            break
            ;;
    esac
done

echo_timeout() {
    if [[ $QUIET -eq 0 ]]; then
        echo "$1"
    fi
}

# Wait for the host:port to be available
echo_timeout "Waiting for $HOST:$PORT..."

counter=0
while ! nc -z "$HOST" "$PORT" > /dev/null 2>&1; do
    if [[ $counter -ge $TIMEOUT ]]; then
        echo_timeout "Timeout: $HOST:$Port is not available after ${TIMEOUT}s"
        exit 1
    fi
    sleep 1
    counter=$((counter + 1))
done

echo_timeout "$HOST:$PORT is available!"

# Execute the command
exec "${COMMAND[@]}"
