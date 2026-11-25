# Start CCAB backend services only (test profile) on localhost.
# API:    http://localhost:8080/api
# Eureka: http://localhost:7777

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$logDir = Join-Path $root '.local-logs'
New-Item -ItemType Directory -Force -Path $logDir | Out-Null

Write-Host "Starting BACKEND services only..."

$services = @(
  'eureka-server',
  'auth-service',
  'notification-service',
  'esign-service',
  'admin-service',
  'newlicense-service',
  'license-issuance-service',
  'esign-application-service',
  'renewal-service',
  'audit-service',
  'cessation-service',
  'dashboard-service',
  'annual-audit-service',
  'api-gateway'
)

foreach ($name in $services) {
  $dir = Join-Path $root $name
  $log = Join-Path $logDir "$name.log"
  $err = Join-Path $logDir "$name.err.log"
  $args = @('/c', 'mvnw.cmd', 'spring-boot:run', '-Dspring-boot.run.profiles=test')
  if ($name -eq 'api-gateway') {
    $args += '-Dspring-boot.run.arguments=--server.port=8080'
  }
  Start-Process -FilePath 'cmd.exe' -ArgumentList $args -WorkingDirectory $dir `
    -RedirectStandardOutput $log -RedirectStandardError $err -WindowStyle Hidden | Out-Null
  Write-Host "Started $name"
}

Write-Host ""
Write-Host "API:      http://localhost:8080/api"
Write-Host "Eureka:   http://localhost:7777"
Write-Host "Logs:     $logDir"
Write-Host ""
Write-Host "Press any key to exit this window (services keep running)..."
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
