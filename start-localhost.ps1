# Start CCAB backends (test profile) + CCAFrontend on localhost.
# UI:  http://localhost:3000
# API: http://localhost:8080/api

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$frontend = Join-Path (Split-Path $root -Parent) 'CCAFrontend'
$logDir = Join-Path $root '.local-logs'
New-Item -ItemType Directory -Force -Path $logDir | Out-Null

Write-Host "Starting all CCA apps on localhost..."

# Point CRA at localhost API
$envFile = Join-Path $frontend '.env'
@"
PORT=3000
HOST=0.0.0.0
CORS="*"
REACT_APP_API_BASE_URL=http://localhost:8080/api
"@ | Set-Content -Path $envFile -Encoding UTF8

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

# Frontend (skip if node_modules missing)
$nodeModules = Join-Path $frontend 'node_modules'
if (Test-Path $nodeModules) {
  Start-Process -FilePath 'cmd.exe' -ArgumentList '/c', 'npm start' -WorkingDirectory $frontend `
    -RedirectStandardOutput (Join-Path $logDir 'frontend.log') `
    -RedirectStandardError (Join-Path $logDir 'frontend.err.log') `
    -WindowStyle Hidden | Out-Null
  Write-Host "Started frontend"
} else {
  Write-Host "SKIP frontend: node_modules missing. Run npm install in CCAFrontend first."
}

Write-Host ""
Write-Host "Frontend: http://localhost:3000"
Write-Host "API:      http://localhost:8080/api"
Write-Host "Eureka:   http://localhost:7777"
Write-Host "Logs:     $logDir"
Write-Host ""
Write-Host "Press any key to exit this window (services keep running)..."
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
