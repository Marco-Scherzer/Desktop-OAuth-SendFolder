param (
    [string]$sourceFile,
    [string]$targetFolder
)

$targetFolder = (Resolve-Path $targetFolder).Path
$sourceFile = (Resolve-Path $sourceFile).Path

$fileName = Split-Path $sourceFile -Leaf
$destination = Join-Path -Path $targetFolder -ChildPath $fileName

Copy-Item -Path $sourceFile -Destination $destination -Force

