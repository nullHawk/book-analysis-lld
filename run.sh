#!/bin/bash

# Amazon Bestseller Book Analysis - Run Script
# This script compiles and runs the Book Analysis application

echo "=================================================="
echo "Amazon Bestseller Book Analysis"
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

# Check if Java is installed
if ! command -v javac &> /dev/null; then
    print_error "Java compiler (javac) not found. Please install Java 8 or higher."
    exit 1
fi

if ! command -v java &> /dev/null; then
    print_error "Java runtime (java) not found. Please install Java 8 or higher."
    exit 1
fi

# Print Java version
print_status "Using Java version:"
java -version

echo

# Create build directory if it doesn't exist
if [ ! -d "build" ]; then
    print_status "Creating build directory..."
    mkdir -p build
fi

# Clean previous build
print_status "Cleaning previous build..."
rm -rf build/*

# Compile the application
print_highlight "Compiling Java source files..."

# Find all Java files in the restructured project
JAVA_FILES=$(find src -name "*.java" | tr '\n' ' ')

if [ -z "$JAVA_FILES" ]; then
    print_error "No Java source files found in src directory"
    exit 1
fi

# Compile with explicit classpath
javac -d build -cp src $JAVA_FILES

# Check compilation result
if [ $? -eq 0 ]; then
    print_status "Compilation successful!"
else
    print_error "Compilation failed!"
    exit 1
fi

echo

# Copy resources
if [ -d "resources" ]; then
    print_status "Copying resources..."
    cp -r resources build/
else
    print_warning "No resources directory found"
fi

echo

# Run the application
print_status "Starting Book Analysis Application..."
echo "========================================"

# Change to build directory and run
cd build

# Check if the main class exists
if [ ! -f "com/nullhawk/books/Main.class" ]; then
    print_error "Main class not found. Compilation may have failed."
    exit 1
fi

# Run the application
java -cp . com.nullhawk.books.Main

echo
print_status "Application finished."
