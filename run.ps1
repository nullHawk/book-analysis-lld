# Amazon Bestseller Book Analysis - PowerShell Run Script
# This script compiles and runs the Book Analysis application on Windows

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Amazon Bestseller Book Analysis" -ForegroundColor Cyan
Write-Host "LLD Best Practices + Optimal Data Structures" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host

# Function to print colored output
function Write-Status {
    param([string]$Message)
    Write-Host "[INFO] $Message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "[WARNING] $Message" -ForegroundColor Yellow
}

function Write-Error {
    param([string]$Message)
    Write-Host "[ERROR] $Message" -ForegroundColor Red
}

function Write-Highlight {
    param([string]$Message)
    Write-Host "[BUILD] $Message" -ForegroundColor Blue
}

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    $javacVersion = javac -version 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "Java not found"
    }
} catch {
    Write-Error "Java compiler (javac) or runtime (java) not found. Please install Java 8 or higher."
    exit 1
}

# Print Java version
Write-Status "Using Java version:"
java -version
Write-Host

# Create build directory if it doesn't exist
if (-Not (Test-Path "build")) {
    Write-Status "Creating build directory..."
    New-Item -ItemType Directory -Path "build" -Force | Out-Null
}

# Clean previous build
Write-Status "Cleaning previous build..."
Remove-Item -Path "build\*" -Recurse -Force -ErrorAction SilentlyContinue

# Compile the application
Write-Highlight "Compiling Java source files..."

# Find all Java files in the restructured project
$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }

if ($javaFiles.Count -eq 0) {
    Write-Error "No Java source files found in src directory"
    exit 1
}

# Create a temporary file with all Java file paths
$tempFile = [System.IO.Path]::GetTempFileName()
$javaFiles | Out-File -FilePath $tempFile -Encoding ASCII

try {
    # Compile using the file list
    $compileCommand = "javac -d build -cp src @`"$tempFile`""
    Invoke-Expression $compileCommand
    
    if ($LASTEXITCODE -eq 0) {
        Write-Status "Compilation successful!"
    } else {
        Write-Error "Compilation failed!"
        exit 1
    }
} finally {
    # Clean up temporary file
    Remove-Item -Path $tempFile -ErrorAction SilentlyContinue
}

Write-Host

# Copy resources
if (Test-Path "resources") {
    Write-Status "Copying resources..."
    Copy-Item -Path "resources" -Destination "build" -Recurse -Force
} else {
    Write-Warning "No resources directory found"
}

Write-Host

# Run the application
Write-Status "Starting Book Analysis Application..."
Write-Host "========================================"

# Change to build directory and run
Push-Location "build"

try {
    # Check if the main class exists
    if (-Not (Test-Path "com\nullhawk\books\Main.class")) {
        Write-Error "Main class not found. Compilation may have failed."
        exit 1
    }

    # Run the application
    java -cp . com.nullhawk.books.Main
} finally {
    Pop-Location
}

Write-Host
Write-Status "Application finished."
