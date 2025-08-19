#!/bin/bash

# Amazon Bestseller Book Analysis - JAR Build Script
# This script creates a distributable JAR file

echo "=================================================="
echo "Amazon Bestseller Book Analysis - JAR Builder"
echo "LLD Best Practices + Optimal Data Structures"
echo "=================================================="
echo

# Set colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_highlight() {
    echo -e "${BLUE}[BUILD]${NC} $1"
}

# Configuration
JAR_NAME="book-analysis"
VERSION="1.0.0"
MAIN_CLASS="com.nullhawk.books.Main"
DIST_DIR="dist"
BUILD_DIR="build"

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    print_error "Java compiler (javac) not found. Please install Java 8 or higher."
    exit 1
fi

if ! command -v jar &> /dev/null; then
    print_error "JAR tool not found. Please install Java Development Kit (JDK)."
    exit 1
fi

# Print Java version
print_status "Using Java version:"
java -version
echo

# Create directories
print_status "Creating build directories..."
mkdir -p "$DIST_DIR"
mkdir -p "$BUILD_DIR"

# Clean previous builds
print_status "Cleaning previous builds..."
rm -rf "$BUILD_DIR"/*
rm -f "$DIST_DIR"/*.jar

# Compile the application
print_highlight "Compiling Java source files..."

# Find all Java files in the restructured project
JAVA_FILES=$(find src -name "*.java" | tr '\n' ' ')

if [ -z "$JAVA_FILES" ]; then
    print_error "No Java source files found in src directory"
    exit 1
fi

# Compile with explicit classpath
javac -d "$BUILD_DIR" -cp src $JAVA_FILES

# Check compilation result
if [ $? -eq 0 ]; then
    print_status "Compilation successful!"
else
    print_error "Compilation failed!"
    exit 1
fi

# Copy resources to build directory
if [ -d "resources" ]; then
    print_status "Copying resources to build directory..."
    cp -r resources "$BUILD_DIR"/
else
    print_warning "No resources directory found"
fi

# Create manifest file
print_status "Creating manifest file..."
MANIFEST_FILE="$BUILD_DIR/META-INF/MANIFEST.MF"
mkdir -p "$BUILD_DIR/META-INF"

cat > "$MANIFEST_FILE" << EOF
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS
Implementation-Title: Amazon Bestseller Book Analysis
Implementation-Version: $VERSION
Implementation-Vendor: NullHawk
Created-By: Book Analysis Build Script
Description: LLD Best Practices + Optimal Data Structures Implementation
Class-Path: .
EOF

# Create the JAR file
print_highlight "Creating JAR file..."
JAR_FILE="$DIST_DIR/$JAR_NAME-$VERSION.jar"

cd "$BUILD_DIR"
jar cfm "../$JAR_FILE" META-INF/MANIFEST.MF .

# Check JAR creation result
if [ $? -eq 0 ]; then
    print_status "JAR file created successfully!"
else
    print_error "JAR file creation failed!"
    exit 1
fi

cd ..

# Verify JAR file
print_status "Verifying JAR file..."
if [ -f "$JAR_FILE" ]; then
    FILE_SIZE=$(ls -lh "$JAR_FILE" | awk '{print $5}')
    print_status "JAR file size: $FILE_SIZE"
    
    # Test the JAR file
    print_status "Testing JAR file..."
    jar tf "$JAR_FILE" | head -10
    echo "..."
    
    # Check if main class is present
    if jar tf "$JAR_FILE" | grep -q "com/nullhawk/books/Main.class"; then
        print_status "Main class found in JAR"
    else
        print_warning "Main class not found in JAR"
    fi
else
    print_error "JAR file was not created"
    exit 1
fi

echo
print_highlight "Build completed successfully!"
echo "=================================================="
echo "JAR file created: $JAR_FILE"
echo "Main class: $MAIN_CLASS"
echo "Version: $VERSION"
echo "=================================================="
echo
print_status "To run the application:"
print_status "  java -jar $JAR_FILE"
echo
print_status "To view JAR contents:"
print_status "  jar tf $JAR_FILE"
echo

# Create a simple run script for the JAR
RUN_JAR_SCRIPT="$DIST_DIR/run-jar.sh"
cat > "$RUN_JAR_SCRIPT" << EOF
#!/bin/bash

# Simple script to run the Book Analysis JAR file
echo "Starting Amazon Bestseller Book Analysis..."
java -jar "$JAR_NAME-$VERSION.jar"
EOF

chmod +x "$RUN_JAR_SCRIPT"
print_status "Created run script: $RUN_JAR_SCRIPT"

echo
print_highlight "Distribution package ready in '$DIST_DIR' directory!"
echo "Files created:"
echo "  - $JAR_FILE"
echo "  - $RUN_JAR_SCRIPT"
