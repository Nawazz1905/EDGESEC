
$ErrorActionPreference = "Stop"

$java = "C:\Program Files\Java\jdk-26.0.2\bin\java.exe"
$project = "C:\EdgeCloudAES"

Set-Location $project

Write-Host "`n========================================"
Write-Host " SECURE EDGE-TO-CLOUD DEMONSTRATION"
Write-Host "========================================"

Write-Host "`n[1/4] AES ENCRYPTION AND iFogSim INTEGRATION"
& $java -cp ".\bin" SecureDemoLauncher
if ($LASTEXITCODE -ne 0) { throw "AES-iFogSim integration failed." }

Write-Host "`n[2/4] AES TAMPER-DETECTION TEST"
& $java -cp ".\bin" AesTest
if ($LASTEXITCODE -ne 0) { throw "AES tamper test failed." }

Write-Host "`n[3/4] AES PERFORMANCE BENCHMARK"
& $java -cp ".\bin" AesPerformanceTest
if ($LASTEXITCODE -ne 0) { throw "AES performance test failed." }

Write-Host "`n[4/4] PLAINTEXT VS AES COMPARISON"
& $java -cp ".\bin" AesComparisonTest
if ($LASTEXITCODE -ne 0) { throw "AES comparison test failed." }

Write-Host "`n========================================"
Write-Host " ALL DEMONSTRATIONS FINISHED"
Write-Host " AES timings are measured separately."
Write-Host " iFogSim metrics are simulated results."
Write-Host "========================================"
