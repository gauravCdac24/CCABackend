#!/usr/bin/env bash
# Start CCAB backends (test profile) safely on a developer laptop.
#
# Why the old all-at-once start crashed the desktop:
#   14x `mvnw spring-boot:run` = Maven JVM + app JVM each, often 1GB+ peak.
#   That OOMs a 16GB machine and can kill gnome-shell / log you out.
#
# This script:
#   - caps heap hard
#   - starts at most PARALLEL services at a time
#   - waits for each wave to settle
#   - does NOT start the frontend (run that separately so UI stays up)
#
# Usage:
#   ./start-lan.sh                  # all backends, staggered
#   ./start-lan.sh core             # eureka + auth + gateway only
#   ./start-lan.sh fresh            # new/fresh license application workflow
#   ./start-lan.sh renewal          # renewal application workflow
#   ./start-lan.sh annual           # annual audit workflow
#   PARALLEL=1 ./start-lan.sh       # one-at-a-time (safest)
#   START_FRONTEND=1 ./start-lan.sh # also start CCAFrontend
#
# Frontend (recommended separate terminal):
#   cd ../CCAFrontend && npm start
#
# UI:  http://127.0.0.1:3000
# API: http://127.0.0.1:8080/api

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRONTEND="$(cd "$ROOT/../CCAFrontend" 2>/dev/null && pwd || true)"
LOG_DIR="$ROOT/.local-logs"
mkdir -p "$LOG_DIR"

export JAVA_HOME="${JAVA_HOME:-/home/gaurav/.local/jdk/jdk-17.0.21+9}"
export PATH="$JAVA_HOME/bin:$PATH"

# Keep Maven itself small while downloading/compiling.
export MAVEN_OPTS="${MAVEN_OPTS:--Xms64m -Xmx256m -XX:+UseSerialGC}"

# App JVMs forked by spring-boot:run
JVM_ARGS="${JVM_ARGS:--Xms64m -Xmx256m -XX:+UseSerialGC -XX:MaxMetaspaceSize=192m}"

PARALLEL="${PARALLEL:-2}"
WAVE_WAIT_SEC="${WAVE_WAIT_SEC:-25}"
MODE="${1:-all}"
START_FRONTEND="${START_FRONTEND:-0}"

if ! command -v java >/dev/null 2>&1; then
  echo "Java not found. Install JDK 17 or set JAVA_HOME." >&2
  exit 1
fi

echo "JAVA_HOME=$JAVA_HOME"
java -version
echo "MAVEN_OPTS=$MAVEN_OPTS"
echo "App JVM args: $JVM_ARGS"
echo "Parallel starts: $PARALLEL  |  wave wait: ${WAVE_WAIT_SEC}s  |  mode: $MODE"
free -h | awk 'NR==1 || NR==2'
echo

core_services=(
  eureka-server
  auth-service
  api-gateway
)

# New/fresh license application path (applicant → scrutiny → esign → issuance)
fresh_services=(
  eureka-server
  auth-service
  notification-service
  admin-service
  dashboard-service
  newlicense-service
  esign-service
  esign-application-service
  license-issuance-service
  api-gateway
)

# Renewal application path (UI also calls newlicense-service for some shared APIs)
renewal_services=(
  eureka-server
  auth-service
  notification-service
  admin-service
  dashboard-service
  newlicense-service
  renewal-service
  esign-service
  esign-application-service
  license-issuance-service
  api-gateway
)

# Annual audit path (agency NC / CCA review / licensee schedule)
annual_services=(
  eureka-server
  auth-service
  notification-service
  admin-service
  dashboard-service
  license-issuance-service
  annual-audit-service
  api-gateway
)

all_services=(
  eureka-server
  auth-service
  notification-service
  esign-service
  admin-service
  newlicense-service
  license-issuance-service
  esign-application-service
  renewal-service
  audit-service
  cessation-service
  dashboard-service
  annual-audit-service
  api-gateway
)

if [[ "$MODE" == "core" ]]; then
  services=("${core_services[@]}")
elif [[ "$MODE" == "fresh" ]]; then
  services=("${fresh_services[@]}")
elif [[ "$MODE" == "renewal" ]]; then
  services=("${renewal_services[@]}")
elif [[ "$MODE" == "annual" ]]; then
  services=("${annual_services[@]}")
else
  services=("${all_services[@]}")
fi

is_up() {
  local pid_file="$1"
  [[ -f "$pid_file" ]] && kill -0 "$(cat "$pid_file")" 2>/dev/null
}

start_one() {
  local name="$1"
  local dir="$ROOT/$name"
  local log="$LOG_DIR/$name.out.log"
  local err="$LOG_DIR/$name.err.log"
  local pid_file="$LOG_DIR/$name.pid"

  if is_up "$pid_file"; then
    echo "Already running: $name (pid $(cat "$pid_file"))"
    return 0
  fi

  : >"$log"
  : >"$err"

  local args=(
    spring-boot:run
    "-Dspring-boot.run.profiles=test"
    "-Dspring-boot.run.jvmArguments=${JVM_ARGS}"
  )
  if [[ "$name" == "api-gateway" ]]; then
    args+=("-Dspring-boot.run.arguments=--server.port=8080")
  fi

  (
    cd "$dir"
    nohup ./mvnw "${args[@]}" >"$log" 2>"$err" &
    echo $! >"$pid_file"
  )
  echo "Started $name (pid $(cat "$pid_file")) → $log"
}

started_in_wave=0
for name in "${services[@]}"; do
  start_one "$name"
  started_in_wave=$((started_in_wave + 1))
  if (( started_in_wave >= PARALLEL )); then
    echo "Waiting ${WAVE_WAIT_SEC}s for this wave (memory settle)..."
    sleep "$WAVE_WAIT_SEC"
    free -h | awk 'NR==2'
    started_in_wave=0
  fi
done

if (( started_in_wave > 0 )); then
  echo "Waiting ${WAVE_WAIT_SEC}s for final wave..."
  sleep "$WAVE_WAIT_SEC"
fi

if [[ "$START_FRONTEND" == "1" && -n "${FRONTEND:-}" && -f "$FRONTEND/package.json" ]]; then
  if [[ ! -f "$FRONTEND/.env" ]]; then
    cat >"$FRONTEND/.env" <<'EOF'
PORT=3000
HOST=0.0.0.0
CORS="*"
REACT_APP_API_BASE_URL=http://127.0.0.1:8080/api
EOF
  fi
  if is_up "$LOG_DIR/frontend.pid"; then
    echo "Already running: frontend (pid $(cat "$LOG_DIR/frontend.pid"))"
  else
    (
      cd "$FRONTEND"
      nohup npm start >"$LOG_DIR/frontend.out.log" 2>"$LOG_DIR/frontend.err.log" &
      echo $! >"$LOG_DIR/frontend.pid"
    )
    echo "Started frontend (pid $(cat "$LOG_DIR/frontend.pid"))"
  fi
elif [[ "$START_FRONTEND" == "1" ]]; then
  echo "CCAFrontend not found next to CCABackend; skipping UI."
else
  echo "Frontend not started by this script (keeps UI stable)."
  echo "  cd ../CCAFrontend && npm start"
fi

echo ""
echo "Frontend: http://127.0.0.1:3000"
echo "API:      http://127.0.0.1:8080/api"
echo "Eureka:   http://127.0.0.1:7777"
echo "Logs:     $LOG_DIR"
echo "Stop:     ./stop-lan.sh"
