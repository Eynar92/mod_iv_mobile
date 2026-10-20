<#
.SYNOPSIS
Installs the configured APKM splits on the configured Android device when needed.
#>
$ErrorActionPreference = 'Stop'

$repositoryRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$configPath = Join-Path $repositoryRoot 'src/main/resources/config.properties'
$settings = @{}
foreach ($line in Get-Content -LiteralPath $configPath) {
    if ($line -match '^\s*([^#=]+?)\s*=\s*(.*)$') {
        $settings[$matches[1].Trim()] = $matches[2].Trim()
    }
}

$deviceId = $settings['device.udid']
$packageName = $settings['app.package']
$bundlePath = $settings['app.bundle']
if ([string]::IsNullOrWhiteSpace($deviceId) -or
    [string]::IsNullOrWhiteSpace($packageName) -or
    [string]::IsNullOrWhiteSpace($bundlePath)) {
    throw 'config.properties must define device.udid, app.package, and app.bundle.'
}

$adb = (Get-Command adb -ErrorAction Stop).Source
$deviceState = (& $adb -s $deviceId get-state 2>&1 | Out-String).Trim()
$stateLines = $deviceState -split '\r?\n' | ForEach-Object { $_.Trim() }
if ($LASTEXITCODE -ne 0 -or $stateLines -notcontains 'device') {
    throw "Configured device '$deviceId' is not ready in adb."
}

$packageOutput = @(& $adb -s $deviceId shell pm path $packageName 2>&1)
$packageCheckExitCode = $LASTEXITCODE
$installedPaths = @($packageOutput | Where-Object { $_.ToString().Trim() -match '^package:' })
$unexpectedPackageOutput = @($packageOutput | Where-Object {
    $_.ToString().Trim() -and $_.ToString().Trim() -notmatch '^package:'
})
if ($packageCheckExitCode -notin @(0, 1) -or $unexpectedPackageOutput.Count -gt 0 -or
    ($installedPaths.Count -gt 0 -and $packageCheckExitCode -ne 0)) {
    throw "Could not check package '$packageName' on the configured device."
}
if ($installedPaths.Count -gt 0) {
    Write-Output "Package '$packageName' is already installed; skipping APKM installation."
    return
}

$archivePath = Join-Path $repositoryRoot $bundlePath
if (-not (Test-Path -LiteralPath $archivePath -PathType Leaf)) {
    throw "Configured app.bundle was not found: $archivePath"
}

$extractRoot = Join-Path $repositoryRoot 'build/app-install'
New-Item -ItemType Directory -Path $extractRoot -Force | Out-Null
$extractPath = Join-Path $extractRoot ([Guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $extractPath | Out-Null
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory($archivePath, $extractPath)
$apkFiles = @(Get-ChildItem -LiteralPath $extractPath -Filter '*.apk' -File -Recurse |
    Sort-Object FullName | ForEach-Object { $_.FullName })
if ($apkFiles.Count -eq 0) {
    throw 'The configured APKM archive contains no APK files.'
}

$installOutput = @(& $adb -s $deviceId install-multiple -r -g @apkFiles 2>&1)
if ($LASTEXITCODE -ne 0 -or ($installOutput -join "`n") -notmatch 'Success') {
    throw "APKM split installation failed: $($installOutput -join ' ')"
}
Write-Output "Installed $($apkFiles.Count) APK files for '$packageName'."
