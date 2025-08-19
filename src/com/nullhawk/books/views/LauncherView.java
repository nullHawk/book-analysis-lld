package com.nullhawk.books.views;

import com.nullhawk.books.controllers.ApplicationController;

import javax.swing.*;
import java.awt.*;

/**
 * Launcher view that allows users to choose between console and GUI versions
 * of the Book Analysis application.
 * Follows the view pattern in MVC architecture.
 */
public class LauncherView extends JFrame {
    
    public LauncherView() {
        initializeFrame();
        setupLayout();
    }
    
    private void initializeFrame() {
        setTitle("Book Analysis Application Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Amazon Bestseller Book Analysis");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("LLD Best Practices + Optimal Data Structures");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(subtitleLabel);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Description
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(getBackground());
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setText(
            "This application demonstrates Low-Level Design (LLD) best practices " +
            "and optimal data structures for efficient book data retrieval.\n\n" +
            "Features:\n" +
            "• O(1) time complexity for author and rating lookups\n" +
            "• HashMap-based indexing for optimal performance\n" +
            "• 550 books from Amazon's Top 50 Bestsellers (2009-2019)\n" +
            "• All required tasks implemented with optimal algorithms\n" +
            "• Clean architecture following SOLID principles"
        );
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        JButton consoleButton = new JButton("Run Console Version");
        consoleButton.setFont(new Font("Arial", Font.BOLD, 14));
        consoleButton.addActionListener(e -> runConsoleVersion());
        
        JButton guiButton = new JButton("Run GUI Version");
        guiButton.setFont(new Font("Arial", Font.BOLD, 14));
        guiButton.addActionListener(e -> runGUIVersion());
        
        buttonPanel.add(consoleButton);
        buttonPanel.add(guiButton);
        
        // Add components
        contentPanel.add(descriptionArea);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);
        
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void runConsoleVersion() {
        // Close launcher
        dispose();
        
        // Run console version
        ApplicationController.startConsoleApplication();
    }
    
    private void runGUIVersion() {
        // Close launcher
        dispose();
        
        // Run GUI version
        ApplicationController.startGUIApplication();
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            if (systemLookAndFeel != null) {
                UIManager.setLookAndFeel(systemLookAndFeel);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel: " + e.getMessage());
        }
        
        // Show launcher
        SwingUtilities.invokeLater(() -> {
            LauncherView launcher = new LauncherView();
            launcher.setVisible(true);
        });
    }
}
