package com.nullhawk.books.utils;

import javax.swing.*;

/**
 * Utility class providing common application functionality.
 * Contains static helper methods used across the application.
 */
public class ApplicationUtils {
    
    // Private constructor to prevent instantiation
    private ApplicationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Sets the system look and feel for Swing applications
     * @return true if successful, false otherwise
     */
    public static boolean setSystemLookAndFeel() {
        try {
            String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            if (systemLookAndFeel != null) {
                UIManager.setLookAndFeel(systemLookAndFeel);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Validates a string input
     * @param input the string to validate
     * @return true if valid (not null and not empty), false otherwise
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Validates a numeric string
     * @param input the string to validate
     * @return true if valid number, false otherwise
     */
    public static boolean isValidNumber(String input) {
        if (!isValidString(input)) {
            return false;
        }
        try {
            Double.parseDouble(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates an integer string
     * @param input the string to validate
     * @return true if valid integer, false otherwise
     */
    public static boolean isValidInteger(String input) {
        if (!isValidString(input)) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Formats a performance time in nanoseconds to a readable string
     * @param nanoseconds the time in nanoseconds
     * @return formatted time string
     */
    public static String formatPerformanceTime(long nanoseconds) {
        if (nanoseconds < 1_000) {
            return nanoseconds + " ns";
        } else if (nanoseconds < 1_000_000) {
            return String.format("%.2f μs", nanoseconds / 1_000.0);
        } else if (nanoseconds < 1_000_000_000) {
            return String.format("%.2f ms", nanoseconds / 1_000_000.0);
        } else {
            return String.format("%.2f s", nanoseconds / 1_000_000_000.0);
        }
    }
    
    /**
     * Creates a standard error dialog
     * @param parent the parent component
     * @param message the error message
     * @param title the dialog title
     */
    public static void showErrorDialog(java.awt.Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Creates a standard info dialog
     * @param parent the parent component
     * @param message the info message
     * @param title the dialog title
     */
    public static void showInfoDialog(java.awt.Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
