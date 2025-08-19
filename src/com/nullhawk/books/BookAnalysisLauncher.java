package com.nullhawk.books;

import javax.swing.*;
import java.awt.*;

/**
 * Launcher class that allows users to choose between console and GUI versions
 * of the Book Analysis application.
 */
public class BookAnalysisLauncher extends JFrame {
    
    public BookAnalysisLauncher() {
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
        JLabel titleLabel = new JLabel("Amazon Bestseller Book Analysis");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("LLD Best Practices + Optimal Data Structures");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
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
        consoleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                runConsoleVersion();
            }
        });
        
        JButton guiButton = new JButton("Run GUI Version");
        guiButton.setFont(new Font("Arial", Font.BOLD, 14));
        guiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                runGUIVersion();
            }
        });
        
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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Run the main console application
                    BookAnalysisDemo.main(new String[0]);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "Error running console version: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void runGUIVersion() {
        // Close launcher
        dispose();
        
        // Run GUI version
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Import and run the GUI
                    Class<?> guiClass = Class.forName("com.nullhawk.books.gui.BookAnalysisGUI");
                    Object guiInstance = guiClass.getDeclaredConstructor().newInstance();
                    guiClass.getMethod("setVisible", boolean.class).invoke(guiInstance, true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "Error running GUI version: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static void main(String[] args) {
        // Set system look and feel (optional)
        try {
            // Try to use system look and feel if available
            String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            if (systemLookAndFeel != null) {
                UIManager.setLookAndFeel(systemLookAndFeel);
            }
        } catch (Exception e) {
            // Ignore look and feel errors, use default
            e.printStackTrace();
        }
        
        // Show launcher
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BookAnalysisLauncher launcher = new BookAnalysisLauncher();
                launcher.setVisible(true);
            }
        });
    }
}
