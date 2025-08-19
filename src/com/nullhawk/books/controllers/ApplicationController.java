package com.nullhawk.books.controllers;

import com.nullhawk.books.views.BookAnalysisView;

import javax.swing.*;

/**
 * Main application controller that manages the application lifecycle.
 * Follows the controller pattern in MVC architecture.
 */
public class ApplicationController {
    
    /**
     * Starts the GUI application
     */
    public static void startGUIApplication() {
        // Set system look and feel
        try {
            String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            if (systemLookAndFeel != null) {
                UIManager.setLookAndFeel(systemLookAndFeel);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel: " + e.getMessage());
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BookAnalysisView view = new BookAnalysisView();
                view.setVisible(true);
            }
        });
    }
    
    /**
     * Starts the console application
     */
    public static void startConsoleApplication() {
        try {
            // Import and run console demo using reflection to avoid compile-time dependencies
            Class<?> demoClass = Class.forName("com.nullhawk.books.BookAnalysisDemo");
            demoClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            System.err.println("Error starting console application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
