param (
    [string]$sourceFolder,
    [string]$targetFolder
)

$sourceFolder = (Resolve-Path $sourceFolder).Path
$targetFolder = (Resolve-Path $targetFolder).Path

Get-ChildItem -Path $targetFolder -File | ForEach-Object {
    Move-Item -Path $_.FullName -Destination $sourceFolder -Force
}
