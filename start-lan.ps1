# Start CCAB backends (test profile) + CCAFrontend for LAN access.
# UI:  http://<LAN_IP>:3000
# API: http://<LAN_IP>:8080/api

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$frontend = Join-Path (Split-Path $root -Parent) 'CCAFrontend'
$logDir = Join-Path $root '.local-logs'
New-Item -ItemType Directory -Force -Path $logDir | Out-Null

$lanIp = (Get-NetIPAddress -AddressFamily IPv4 |
  Where-Object { $_.IPAddress -like '10.*' -or $_.IPAddress -like '192.168.*' } |
  Select-Object -First 1 -ExpandProperty IPAddress)

if (-not $lanIp) { $lanIp = '127.0.0.1' }
Write-Host "LAN IP: $lanIp"

# Point CRA at this machine's gateway
$envFile = Join-Path $frontend '.env'
@"
PORT=3000
HOST=0.0.0.0
CORS="*"
REACT_APP_API_BASE_URL=http://${lanIp}:8080/api
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

Start-Process -FilePath 'cmd.exe' -ArgumentList '/c', 'npm start' -WorkingDirectory $frontend `
  -RedirectStandardOutput (Join-Path $logDir 'frontend.log') `
  -RedirectStandardError (Join-Path $logDir 'frontend.err.log') `
  -WindowStyle Hidden | Out-Null

Write-Host ""
Write-Host "Frontend: http://${lanIp}:3000"
Write-Host "API:      http://${lanIp}:8080/api"
Write-Host "Logs:     $logDir"
