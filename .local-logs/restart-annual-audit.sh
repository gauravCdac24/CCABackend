#!/bin/bash
set -e
export JAVA_HOME=/home/gaurav/.local/jdk/jdk-17.0.21+9
export PATH="$JAVA_HOME/bin:$PATH"
export HOME=/home/gaurav
export MAVEN_OPTS="-Xms64m -Xmx256m -XX:+UseSerialGC"
JVM_ARGS="-Xms64m -Xmx256m -XX:+UseSerialGC -XX:MaxMetaspaceSize=192m"
LOG_DIR=/home/gaurav/Documents/CCABackend/.local-logs
pkill -f "in.lms.cca.AnnualAuditServiceApplication" 2>/dev/null || true
sleep 2
fuser -k 7791/tcp 2>/dev/null || true
sleep 1
cd /home/gaurav/Documents/CCABackend/annual-audit-service
: > "$LOG_DIR/annual-audit-service.out.log"
: > "$LOG_DIR/annual-audit-service.err.log"
nohup ./mvnw spring-boot:run -Dspring-boot.run.profiles=test "-Dspring-boot.run.jvmArguments=${JVM_ARGS}" >"$LOG_DIR/annual-audit-service.out.log" 2>"$LOG_DIR/annual-audit-service.err.log" &
echo $! > "$LOG_DIR/annual-audit-service.pid"
echo "spawned=$(cat $LOG_DIR/annual-audit-service.pid)"
