<#
    (c) Marco Scherzer. All rights reserved.
    This script is part of MSendBackupMail dbgTest.
#>
param (
    [string]$sourceFile,
    [string]$targetFolder
)

$targetFolder = (Resolve-Path $targetFolder).Path
$sourceFile = (Resolve-Path $sourceFile).Path

$fileName = Split-Path $sourceFile -Leaf
$destination = Join-Path -Path $targetFolder -ChildPath $fileName

Copy-Item -Path $sourceFile -Destination $destination -Force

