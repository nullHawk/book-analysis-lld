# Amazon Bestseller Book Analysis - PowerShell JAR Build Script
# This script creates a distributable JAR file on Windows

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Amazon Bestseller Book Analysis - JAR Builder" -ForegroundColor Cyan
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

# Configuration
$JAR_NAME = "book-analysis"
$VERSION = "1.0.0"
$MAIN_CLASS = "com.nullhawk.books.Main"
$DIST_DIR = "dist"
$BUILD_DIR = "build"

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    $javacVersion = javac -version 2>&1
    $jarVersion = jar 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "Java tools not found"
    }
} catch {
    Write-Error "Java compiler (javac), runtime (java), or JAR tool not found. Please install Java Development Kit (JDK) 8 or higher."
    exit 1
}

# Print Java version
Write-Status "Using Java version:"
java -version
Write-Host

# Create directories
Write-Status "Creating build directories..."
New-Item -ItemType Directory -Path $DIST_DIR -Force | Out-Null
New-Item -ItemType Directory -Path $BUILD_DIR -Force | Out-Null

# Clean previous builds
Write-Status "Cleaning previous builds..."
Remove-Item -Path "$BUILD_DIR\*" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "$DIST_DIR\*.jar" -Force -ErrorAction SilentlyContinue

# Compile the application
Write-Highlight "Compiling Java source files..."

# Find all Java files in the restructured project
$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName }

if ($javaFiles.Count -eq 0) {
    Write-Error "No Java source files found in src directory"
    exit 1
fi

# Create a temporary file with all Java file paths
$tempFile = [System.IO.Path]::GetTempFileName()
$javaFiles | Out-File -FilePath $tempFile -Encoding ASCII

try {
    # Compile using the file list
    $compileCommand = "javac -d `"$BUILD_DIR`" -cp src @`"$tempFile`""
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

# Copy resources to build directory
if (Test-Path "resources") {
    Write-Status "Copying resources to build directory..."
    Copy-Item -Path "resources" -Destination $BUILD_DIR -Recurse -Force
} else {
    Write-Warning "No resources directory found"
}

# Create manifest file
Write-Status "Creating manifest file..."
$manifestDir = "$BUILD_DIR\META-INF"
$manifestFile = "$manifestDir\MANIFEST.MF"
New-Item -ItemType Directory -Path $manifestDir -Force | Out-Null

$manifestContent = @"
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS
Implementation-Title: Amazon Bestseller Book Analysis
Implementation-Version: $VERSION
Implementation-Vendor: NullHawk
Created-By: Book Analysis Build Script
Description: LLD Best Practices + Optimal Data Structures Implementation
Class-Path: .
"@

$manifestContent | Out-File -FilePath $manifestFile -Encoding ASCII

# Create the JAR file
Write-Highlight "Creating JAR file..."
$jarFile = "$DIST_DIR\$JAR_NAME-$VERSION.jar"

Push-Location $BUILD_DIR
try {
    jar cfm "..\$jarFile" META-INF\MANIFEST.MF .
    
    if ($LASTEXITCODE -eq 0) {
        Write-Status "JAR file created successfully!"
    } else {
        Write-Error "JAR file creation failed!"
        exit 1
    }
} finally {
    Pop-Location
}

# Verify JAR file
Write-Status "Verifying JAR file..."
if (Test-Path $jarFile) {
    $fileSize = (Get-Item $jarFile).Length
    $fileSizeFormatted = if ($fileSize -gt 1MB) { 
        "{0:N2} MB" -f ($fileSize / 1MB) 
    } elseif ($fileSize -gt 1KB) { 
        "{0:N2} KB" -f ($fileSize / 1KB) 
    } else { 
        "$fileSize bytes" 
    }
    Write-Status "JAR file size: $fileSizeFormatted"
    
    # Test the JAR file
    Write-Status "Testing JAR file..."
    $jarContents = jar tf $jarFile
    $jarContents | Select-Object -First 10
    Write-Host "..."
    
    # Check if main class is present
    if ($jarContents -contains "com/nullhawk/books/Main.class") {
        Write-Status "Main class found in JAR"
    } else {
        Write-Warning "Main class not found in JAR"
    }
} else {
    Write-Error "JAR file was not created"
    exit 1
}

Write-Host
Write-Highlight "Build completed successfully!"
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "JAR file created: $jarFile" -ForegroundColor Cyan
Write-Host "Main class: $MAIN_CLASS" -ForegroundColor Cyan
Write-Host "Version: $VERSION" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host
Write-Status "To run the application:"
Write-Status "  java -jar $jarFile"
Write-Host
Write-Status "To view JAR contents:"
Write-Status "  jar tf $jarFile"
Write-Host

# Create a simple run script for the JAR
$runJarScript = "$DIST_DIR\run-jar.ps1"
$runJarContent = @"
# Simple script to run the Book Analysis JAR file
Write-Host "Starting Amazon Bestseller Book Analysis..."
java -jar "$JAR_NAME-$VERSION.jar"
"@

$runJarContent | Out-File -FilePath $runJarScript -Encoding ASCII
Write-Status "Created run script: $runJarScript"

Write-Host
Write-Highlight "Distribution package ready in '$DIST_DIR' directory!"
Write-Host "Files created:"
Write-Host "  - $jarFile"
Write-Host "  - $runJarScript"
