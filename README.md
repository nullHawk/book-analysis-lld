# Amazon Bestseller Book Analysis

A Java application that demonstrates **Low-Level Designs** and **optimal data structures** for efficient retrieval of book data from Amazon's Top 50 bestselling books dataset (2009-2019).

## Screenshots
![ezgif-54cac3f0db53cf](https://github.com/user-attachments/assets/622cdb8d-829b-46c1-b056-5a938131bf28)

## Project Architecture

The project follows a clean, layered architecture with proper separation of concerns:

```
src/com/nullhawk/books/
├── controllers/          # Application flow control
│   └── ApplicationController.java
├── model/               # Data entities
│   └── Book.java
├── services/            # Business logic and data access
│   ├── BookService.java
│   ├── InMemoryLoader.java
│   └── GUIDataService.java
├── views/               # User interface components
│   ├── BookAnalysisView.java
│   └── LauncherView.java
├── utils/               # Utility classes
│   └── ApplicationUtils.java
├── Main.java            # Application entry point
├── BookAnalysisDemo.java # Console demonstration
└── DataStructureTest.java # Performance verification
```

## Features

- **O(1) Time Complexity** for author and rating lookups using HashMap indexing
- **Professional Java Swing GUI** with intuitive interface
- **Console Interface** for automation and scripting
- **Performance Testing** with real-time metrics
- **Clean Architecture** following SOLID principles
- **Robust CSV Parsing** with quoted field support

## Required Tasks Implementation

✅ **Get total number of books by a given author** - O(1) HashMap lookup  
✅ **Print names of all unique authors** - O(1) HashSet retrieval  
✅ **Get names of all books by author** - O(1) HashMap lookup + Stream mapping  
✅ **Get books with specific rating** - O(1) HashMap lookup  
✅ **Get book names and prices by author** - O(1) HashMap lookup + Stream collection  

## Prerequisites

- **Java 8 or higher**
- **PowerShell** (Windows) or **Bash** (Linux/Mac)

## Quick Start

### Option 1: Run with Scripts (Recommended)

#### Windows (PowerShell)
```powershell
# Run the application
.\run.ps1

# Build distributable JAR
.\build-jar.ps1
```

#### Linux/Mac (Bash)
```bash
# Make scripts executable
chmod +x run.sh build-jar.sh

# Run the application
./run.sh

# Build distributable JAR
./build-jar.sh
```

### Option 2: Manual Compilation

```bash
# Compile the project
javac -d build -cp src src/com/nullhawk/books/*.java src/com/nullhawk/books/model/*.java src/com/nullhawk/books/services/*.java src/com/nullhawk/books/views/*.java src/com/nullhawk/books/controllers/*.java src/com/nullhawk/books/utils/*.java

# Copy resources
cp -r resources build/

# Run the application
cd build
java -cp . com.nullhawk.books.Main
```

## Usage

### Application Launcher
The application starts with a launcher that allows you to choose between:

1. **GUI Version** - Interactive Java Swing interface
2. **Console Version** - Command-line interface

### GUI Features
- **Query Operations**: Author lookups, rating filters, genre analysis
- **Advanced Queries**: Year ranges, price ranges, top-rated books
- **Performance Testing**: Real-time O(1) vs O(n) comparisons
- **Dataset Information**: Comprehensive data statistics

### Console Features
- **Interactive Demos**: All required tasks with examples
- **Performance Verification**: Data structure efficiency tests
- **Batch Operations**: Multiple queries in sequence

## Performance Characteristics

| Operation | Time Complexity | Data Structure |
|-----------|-----------------|----------------|
| Author lookup | **O(1)** | HashMap |
| Rating lookup | **O(1)** | HashMap |
| Unique authors | **O(1)** | HashSet |
| Genre filtering | **O(n)** | Stream + ArrayList |
| Year range filtering | **O(n)** | Stream + ArrayList |
| Top-rated books | **O(n log n)** | Stream + Sorting |

## Design Patterns

- **Service Layer Pattern**: Business logic separation
- **Repository Pattern**: Data access abstraction
- **Builder Pattern**: Immutable object creation
- **MVC Architecture**: Model-View-Controller separation
- **Observer Pattern**: Asynchronous data loading

## Project Structure

```
books/
├── resources/
│   └── data.csv                 # Amazon bestseller dataset
├── src/
│   └── com/nullhawk/books/
│       ├── controllers/          # Application flow control
│       ├── model/               # Data entities
│       ├── services/            # Business logic & data access
│       ├── views/               # User interface components
│       ├── utils/               # Utility classes
│       └── Main.java            # Entry point
├── build/                       # Compiled classes
├── dist/                        # Distribution packages
├── run.sh                       # Bash run script
├── run.ps1                      # PowerShell run script
├── build-jar.sh                 # Bash JAR build script
├── build-jar.ps1                # PowerShell JAR build script
└── README.md                    # This file
```

## Build Scripts

### Run Scripts
- **`run.sh`** / **`run.ps1`**: Compile and run the application
- **`build-jar.sh`** / **`build-jar.ps1`**: Create distributable JAR

### Script Features
- **Automatic compilation** with dependency management
- **Resource copying** to build directory
- **Error handling** with colored output
- **Cross-platform** support (Windows/Linux/Mac)

## Performance Results

The application demonstrates significant performance improvements:

- **Author lookups**: 277x faster than linear search
- **Rating lookups**: O(1) average case performance
- **Memory efficiency**: Optimal data structure usage
- **Scalability**: Linear indexing time, constant lookup time

## Testing

### Built-in Performance Tests
- **HashMap vs Linear Search** comparison
- **Real-time performance metrics**
- **Time complexity verification**
- **Memory usage analysis**

### Manual Testing
- **GUI Interface**: Interactive query testing
- **Console Interface**: Batch operation testing
- **Edge Cases**: Invalid inputs, boundary conditions

## Future Enhancements

1. **Database Integration**: Replace in-memory storage
2. **Caching Layer**: Redis implementation
3. **API Endpoints**: RESTful web services
4. **Real-time Updates**: Event-driven architecture
5. **Analytics Dashboard**: Data visualization
6. **Enhanced GUI**: Charts, graphs, advanced filtering
