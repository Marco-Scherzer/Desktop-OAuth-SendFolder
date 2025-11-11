<#
    (c) Marco Scherzer. All rights reserved.
    This script is part of MSendBackupMail dbgTest.
#>
param (
    [string]$sourceFile,
    [string]$targetFolder,
    [string]$logPath = "$PSScriptRoot\MoveLog.txt"
)

function Write-Log {
    param([string]$message)
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Add-Content -Path $logPath -Value "$timestamp - $message"
}

try {
    if (-not (Test-Path -Path $sourceFile -PathType Leaf)) {
        Write-Log "File not found: ${sourceFile}"
        exit 1
    }

    if (-not (Test-Path -Path $targetFolder -PathType Container)) {
        Write-Log "Target folder not found: ${targetFolder}"
        exit 2
    }

    $fileName = Split-Path $sourceFile -Leaf
    $destination = Join-Path -Path $targetFolder -ChildPath $fileName

    Move-Item -Path $sourceFile -Destination $destination -Force
    Write-Log "Moved: ${fileName} to ${targetFolder}"
    exit 0
} catch {
    Write-Log "Error moving ${sourceFile}: $_"
    exit 3
}
