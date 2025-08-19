package com.nullhawk.books;

import com.nullhawk.books.views.LauncherView;

/**
 * Main entry point for the Book Analysis application.
 * This class launches the application launcher that allows users to choose
 * between console and GUI versions.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== Amazon Bestseller Book Analysis ===");
        System.out.println("Using LLD Best Practices + Optimal Data Structures");
        System.out.println("================================================");
        System.out.println("Launching application launcher...\n");
        
        // Launch the application launcher
        LauncherView.main(args);
    }
}