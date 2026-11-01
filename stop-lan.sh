#!/usr/bin/env bash
# Stop backends started by start-lan.sh (and optional frontend).

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="$ROOT/.local-logs"

stop_pidfile() {
  local pid_file="$1"
  local label="$2"
  if [[ ! -f "$pid_file" ]]; then
    return 0
  fi
  local pid
  pid="$(cat "$pid_file" 2>/dev/null || true)"
  if [[ -n "${pid:-}" ]] && kill -0 "$pid" 2>/dev/null; then
    # Kill process group / children of mvnw as well
    pkill -P "$pid" 2>/dev/null || true
    kill "$pid" 2>/dev/null || true
    sleep 1
    kill -9 "$pid" 2>/dev/null || true
    echo "Stopped $label (pid $pid)"
  else
    echo "Not running: $label"
  fi
  rm -f "$pid_file"
}

# Also stop leftover java apps from this repo if pid files are stale
for pid_file in "$LOG_DIR"/*.pid; do
  [[ -e "$pid_file" ]] || continue
  name="$(basename "$pid_file" .pid)"
  stop_pidfile "$pid_file" "$name"
done

# Safety net: any leftover spring-boot:run for this workspace
pkill -f "/home/gaurav/Documents/CCABackend/.*/mvnw spring-boot:run" 2>/dev/null || true

echo "Done. Frontend left alone unless frontend.pid was present."
echo "Check memory: free -h"
