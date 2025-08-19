package com.nullhawk.books.gui;

import com.nullhawk.books.model.Book;
import com.nullhawk.books.service.BookService;
import com.nullhawk.books.service.InMemoryLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java Swing GUI application for Book Analysis.
 * Provides interactive interface for all book query operations.
 */
public class BookAnalysisGUI extends JFrame {
    
    private BookService bookService;
    private JTextArea resultArea;
    private JComboBox<String> authorComboBox;
    private JComboBox<String> ratingComboBox;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JTextField startYearField;
    private JTextField endYearField;
    private JComboBox<String> genreComboBox;
    
    public BookAnalysisGUI() {
        initializeFrame();
        initializeComponents();
        loadData();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeFrame() {
        setTitle("Amazon Bestseller Book Analysis - LLD + DSA Implementation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initializeComponents() {
        // Initialize components
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        authorComboBox = new JComboBox<>();
        ratingComboBox = new JComboBox<>();
        minPriceField = new JTextField(10);
        maxPriceField = new JTextField(10);
        startYearField = new JTextField(10);
        endYearField = new JTextField(10);
        genreComboBox = new JComboBox<>(new String[]{"Fiction", "Non Fiction"});
        
        // Set default values
        minPriceField.setText("0.0");
        maxPriceField.setText("100.0");
        startYearField.setText("2009");
        endYearField.setText("2019");
    }
    
    private void loadData() {
        try {
            // Show loading message
            resultArea.setText("Loading books from CSV...\nPlease wait...");
            
            // Load data in background
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    InMemoryLoader loader = new InMemoryLoader();
                    List<Book> books = loader.loadBooksFromCSV("resources/data.csv");
                    bookService = new BookService(books);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get(); // Check for exceptions
                        populateComboBoxes();
                        displayWelcomeMessage();
                    } catch (Exception e) {
                        resultArea.setText("Error loading data: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();
            
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void populateComboBoxes() {
        if (bookService != null) {
            // Populate author combo box
            Set<String> authors = bookService.getAllAuthors();
            authorComboBox.removeAllItems();
            authorComboBox.addItem("Select Author");
            for (String author : authors) {
                authorComboBox.addItem(author);
            }
            
            // Populate rating combo box
            ratingComboBox.removeAllItems();
            ratingComboBox.addItem("Select Rating");
            String[] ratings = {"4.9", "4.8", "4.7", "4.6", "4.5", "4.4", "4.3", "4.2", "4.1", "4.0", "3.9", "3.8", "3.7", "3.6", "3.5", "3.4", "3.3"};
            for (String rating : ratings) {
                ratingComboBox.addItem(rating);
            }
        }
    }
    
    private void displayWelcomeMessage() {
        if (bookService != null) {
            StringBuilder welcome = new StringBuilder();
            welcome.append("=== Amazon Bestseller Book Analysis ===\n");
            welcome.append("Using LLD Best Practices + Optimal Data Structures\n");
            welcome.append("==============================================\n\n");
            welcome.append("Data Loaded Successfully!\n");
            welcome.append("Total Books: ").append(bookService.getTotalBookCount()).append("\n");
            welcome.append("Unique Authors: ").append(bookService.getAllAuthors().size()).append("\n");
            welcome.append("Years Covered: 2009-2019\n\n");
            welcome.append("Use the buttons above to query the data.\n");
            welcome.append("All author and rating lookups are O(1) time complexity!\n");
            
            resultArea.setText(welcome.toString());
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create main panels
        JPanel controlPanel = createControlPanel();
        JPanel resultPanel = createResultPanel();
        
        // Add panels to frame
        add(controlPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Book Analysis Query Interface");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // Query buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(new TitledBorder("Query Operations"));
        
        // Row 1 buttons
        buttonPanel.add(createQueryButton("Total Books by Author", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryTotalBooksByAuthor(); }
        }));
        buttonPanel.add(createQueryButton("Books by Author", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryBooksByAuthor(); }
        }));
        buttonPanel.add(createQueryButton("Books by Rating", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryBooksByRating(); }
        }));
        
        // Row 2 buttons
        buttonPanel.add(createQueryButton("Books & Prices by Author", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryBooksAndPricesByAuthor(); }
        }));
        buttonPanel.add(createQueryButton("Books by Genre", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryBooksByGenre(); }
        }));
        buttonPanel.add(createQueryButton("Books by Year Range", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryBooksByYearRange(); }
        }));
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Advanced queries panel
        JPanel advancedPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        advancedPanel.setBorder(new TitledBorder("Advanced Queries"));
        
        advancedPanel.add(createQueryButton("Top Rated Books", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryTopRatedBooks(); }
        }));
        advancedPanel.add(createQueryButton("Books by Price Range", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryBooksByPriceRange(); }
        }));
        advancedPanel.add(createQueryButton("All Authors", new ActionListener() {
            public void actionPerformed(ActionEvent e) { queryAllAuthors(); }
        }));
        
        advancedPanel.add(createQueryButton("Performance Test", new ActionListener() {
            public void actionPerformed(ActionEvent e) { runPerformanceTest(); }
        }));
        advancedPanel.add(createQueryButton("Clear Results", new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearResults(); }
        }));
        advancedPanel.add(createQueryButton("Dataset Info", new ActionListener() {
            public void actionPerformed(ActionEvent e) { showDatasetInfo(); }
        }));
        
        panel.add(advancedPanel);
        
        return panel;
    }
    
    private JButton createQueryButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.addActionListener(action);
        return button;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Query Results"));
        
        // Create scrollable text area
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Add action listeners for combo boxes if needed
        authorComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Could add real-time updates here
            }
        });
    }
    
    // Query Methods
    private void queryTotalBooksByAuthor() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        String selectedAuthor = (String) authorComboBox.getSelectedItem();
        if ("Select Author".equals(selectedAuthor)) {
            selectedAuthor = JOptionPane.showInputDialog(this, "Enter author name:", "Query Total Books by Author", JOptionPane.QUESTION_MESSAGE);
        }
        
        if (selectedAuthor != null && !selectedAuthor.trim().isEmpty()) {
            int totalBooks = bookService.getTotalBooksByAuthor(selectedAuthor.trim());
            displayResult("Total Books by Author", 
                "Author: " + selectedAuthor.trim() + "\n" +
                "Total Books: " + totalBooks + "\n\n" +
                "Time Complexity: O(1) - HashMap lookup");
        }
    }
    
    private void queryBooksByAuthor() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        String selectedAuthor = (String) authorComboBox.getSelectedItem();
        if ("Select Author".equals(selectedAuthor)) {
            selectedAuthor = JOptionPane.showInputDialog(this, "Enter author name:", "Query Books by Author", JOptionPane.QUESTION_MESSAGE);
        }
        
        if (selectedAuthor != null && !selectedAuthor.trim().isEmpty()) {
            List<String> bookNames = bookService.getBookNamesByAuthor(selectedAuthor.trim());
            StringBuilder result = new StringBuilder();
            result.append("Author: ").append(selectedAuthor.trim()).append("\n");
            result.append("Total Books: ").append(bookNames.size()).append("\n\n");
            result.append("Books:\n");
            
            for (int i = 0; i < bookNames.size(); i++) {
                result.append(i + 1).append(". ").append(bookNames.get(i)).append("\n");
            }
            
            result.append("\nTime Complexity: O(1) - HashMap lookup + Stream mapping");
            displayResult("Books by Author", result.toString());
        }
    }
    
    private void queryBooksByRating() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        String selectedRatingStr = (String) ratingComboBox.getSelectedItem();
        Double selectedRating = null;
        
        if ("Select Rating".equals(selectedRatingStr)) {
            String input = JOptionPane.showInputDialog(this, "Enter rating (3.3-4.9):", "Query Books by Rating", JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    selectedRating = Double.parseDouble(input.trim());
                } catch (NumberFormatException e) {
                    showError("Invalid rating format. Please enter a number between 3.3 and 4.9");
                    return;
                }
            }
        } else {
            try {
                selectedRating = Double.parseDouble(selectedRatingStr);
            } catch (NumberFormatException e) {
                showError("Invalid rating format in combo box");
                return;
            }
        }
        
        if (selectedRating != null) {
            List<Book> books = bookService.getBooksByRating(selectedRating);
            StringBuilder result = new StringBuilder();
            result.append("Rating: ").append(selectedRating).append("\n");
            result.append("Total Books: ").append(books.size()).append("\n\n");
            result.append("Books:\n");
            
            for (int i = 0; i < Math.min(books.size(), 20); i++) {
                Book book = books.get(i);
                result.append(i + 1).append(". ").append(book.getTitle())
                      .append(" by ").append(book.getAuthor())
                      .append(" ($").append(book.getPrice()).append(", ").append(book.getYear()).append(")\n");
            }
            
            if (books.size() > 20) {
                result.append("... and ").append(books.size() - 20).append(" more books\n");
            }
            
            result.append("\nTime Complexity: O(1) - HashMap lookup");
            displayResult("Books by Rating", result.toString());
        }
    }
    
    private void queryBooksAndPricesByAuthor() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        String selectedAuthor = (String) authorComboBox.getSelectedItem();
        if ("Select Author".equals(selectedAuthor)) {
            selectedAuthor = JOptionPane.showInputDialog(this, "Enter author name:", "Query Books and Prices by Author", JOptionPane.QUESTION_MESSAGE);
        }
        
        if (selectedAuthor != null && !selectedAuthor.trim().isEmpty()) {
            Map<String, Double> bookPrices = bookService.getBookNamesAndPricesByAuthor(selectedAuthor.trim());
            StringBuilder result = new StringBuilder();
            result.append("Author: ").append(selectedAuthor.trim()).append("\n");
            result.append("Total Books: ").append(bookPrices.size()).append("\n\n");
            result.append("Books and Prices:\n");
            
            int i = 1;
            for (Map.Entry<String, Double> entry : bookPrices.entrySet()) {
                result.append(i++).append(". ").append(entry.getKey())
                      .append(": $").append(entry.getValue()).append("\n");
            }
            
            result.append("\nTime Complexity: O(1) - HashMap lookup + Stream collection");
            displayResult("Books and Prices by Author", result.toString());
        }
    }
    
    private void queryBooksByGenre() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        String selectedGenre = (String) genreComboBox.getSelectedItem();
        List<Book> books = bookService.getBooksByGenre(selectedGenre);
        
        StringBuilder result = new StringBuilder();
        result.append("Genre: ").append(selectedGenre).append("\n");
        result.append("Total Books: ").append(books.size()).append("\n\n");
        result.append("Sample Books (first 15):\n");
        
        for (int i = 0; i < Math.min(books.size(), 15); i++) {
            Book book = books.get(i);
            result.append(i + 1).append(". ").append(book.getTitle())
                  .append(" by ").append(book.getAuthor())
                  .append(" (Rating: ").append(book.getUserRating())
                  .append(", $").append(book.getPrice()).append(")\n");
        }
        
        if (books.size() > 15) {
            result.append("... and ").append(books.size() - 15).append(" more books\n");
        }
        
        result.append("\nTime Complexity: O(n) - Stream filtering (optimal for this use case)");
        displayResult("Books by Genre", result.toString());
    }
    
    private void queryBooksByYearRange() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        try {
            int startYear = Integer.parseInt(startYearField.getText().trim());
            int endYear = Integer.parseInt(endYearField.getText().trim());
            
            if (startYear > endYear) {
                showError("Start year cannot be greater than end year");
                return;
            }
            
            List<Book> books = bookService.getBooksByYearRange(startYear, endYear);
            
            StringBuilder result = new StringBuilder();
            result.append("Year Range: ").append(startYear).append(" - ").append(endYear).append("\n");
            result.append("Total Books: ").append(books.size()).append("\n\n");
            result.append("Sample Books (first 15):\n");
            
            for (int i = 0; i < Math.min(books.size(), 15); i++) {
                Book book = books.get(i);
                result.append(i + 1).append(". ").append(book.getTitle())
                      .append(" by ").append(book.getAuthor())
                      .append(" (").append(book.getYear()).append(", Rating: ").append(book.getUserRating()).append(")\n");
            }
            
            if (books.size() > 15) {
                result.append("... and ").append(books.size() - 15).append(" more books\n");
            }
            
            result.append("\nTime Complexity: O(n) - Stream filtering (optimal for this use case)");
            displayResult("Books by Year Range", result.toString());
            
        } catch (NumberFormatException e) {
            showError("Please enter valid years (numbers only)");
        }
    }
    
    private void queryTopRatedBooks() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        String input = JOptionPane.showInputDialog(this, "Enter number of top books to show:", "Query Top Rated Books", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                int limit = Integer.parseInt(input.trim());
                if (limit <= 0) {
                    showError("Please enter a positive number");
                    return;
                }
                
                List<Book> topBooks = bookService.getTopRatedBooks(limit);
                
                StringBuilder result = new StringBuilder();
                result.append("Top ").append(limit).append(" Rated Books:\n\n");
                
                for (int i = 0; i < topBooks.size(); i++) {
                    Book book = topBooks.get(i);
                    result.append(i + 1).append(". ").append(book.getTitle())
                          .append(" by ").append(book.getAuthor())
                          .append(" (Rating: ").append(book.getUserRating())
                          .append(", $").append(book.getPrice())
                          .append(", ").append(book.getYear()).append(")\n");
                }
                
                result.append("\nTime Complexity: O(n log n) - Stream sorting + limiting");
                displayResult("Top Rated Books", result.toString());
                
            } catch (NumberFormatException e) {
                showError("Please enter a valid number");
            }
        }
    }
    
    private void queryBooksByPriceRange() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        try {
            double minPrice = Double.parseDouble(minPriceField.getText().trim());
            double maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            
            if (minPrice > maxPrice) {
                showError("Minimum price cannot be greater than maximum price");
                return;
            }
            
            List<Book> books = bookService.getBooksByPriceRange(minPrice, maxPrice);
            
            StringBuilder result = new StringBuilder();
            result.append("Price Range: $").append(minPrice).append(" - $").append(maxPrice).append("\n");
            result.append("Total Books: ").append(books.size()).append("\n\n");
            result.append("Sample Books (first 15):\n");
            
            for (int i = 0; i < Math.min(books.size(), 15); i++) {
                Book book = books.get(i);
                result.append(i + 1).append(". ").append(book.getTitle())
                      .append(" by ").append(book.getAuthor())
                      .append(" ($").append(book.getPrice())
                      .append(", Rating: ").append(book.getUserRating()).append(")\n");
            }
            
            if (books.size() > 15) {
                result.append("... and ").append(books.size() - 15).append(" more books\n");
            }
            
            result.append("\nTime Complexity: O(n) - Stream filtering (optimal for this use case)");
            displayResult("Books by Price Range", result.toString());
            
        } catch (NumberFormatException e) {
            showError("Please enter valid prices (numbers only)");
        }
    }
    
    private void queryAllAuthors() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        Set<String> allAuthors = bookService.getAllAuthors();
        
        StringBuilder result = new StringBuilder();
        result.append("All Authors in Dataset\n");
        result.append("======================\n\n");
        result.append("Total Unique Authors: ").append(allAuthors.size()).append("\n\n");
        result.append("Authors (first 50):\n");
        
        int i = 1;
        for (String author : allAuthors) {
            if (i > 50) break;
            result.append(i++).append(". ").append(author).append("\n");
        }
        
        if (allAuthors.size() > 50) {
            result.append("... and ").append(allAuthors.size() - 50).append(" more authors\n");
        }
        
        result.append("\nTime Complexity: O(1) - Pre-built HashSet");
        displayResult("All Authors", result.toString());
    }
    
    private void runPerformanceTest() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        StringBuilder result = new StringBuilder();
        result.append("Performance Test Results\n");
        result.append("========================\n\n");
        
        // Test author lookup performance
        String testAuthor = "Stephen King";
        long startTime = System.nanoTime();
        int bookCount = bookService.getTotalBooksByAuthor(testAuthor);
        long hashMapTime = System.nanoTime() - startTime;
        
        result.append("1. Author Lookup Test:\n");
        result.append("   Author: ").append(testAuthor).append("\n");
        result.append("   Books found: ").append(bookCount).append("\n");
        result.append("   HashMap lookup time: ").append(hashMapTime).append(" nanoseconds\n");
        result.append("   Time Complexity: O(1)\n\n");
        
        // Test rating lookup performance
        double testRating = 4.7;
        startTime = System.nanoTime();
        List<Book> booksWithRating = bookService.getBooksByRating(testRating);
        long ratingTime = System.nanoTime() - startTime;
        
        result.append("2. Rating Lookup Test:\n");
        result.append("   Rating: ").append(testRating).append("\n");
        result.append("   Books found: ").append(booksWithRating.size()).append("\n");
        result.append("   HashMap lookup time: ").append(ratingTime).append(" nanoseconds\n");
        result.append("   Time Complexity: O(1)\n\n");
        
        // Test unique authors retrieval
        startTime = System.nanoTime();
        Set<String> allAuthors = bookService.getAllAuthors();
        long authorsTime = System.nanoTime() - startTime;
        
        result.append("3. Unique Authors Test:\n");
        result.append("   Total unique authors: ").append(allAuthors.size()).append("\n");
        result.append("   HashSet retrieval time: ").append(authorsTime).append(" nanoseconds\n");
        result.append("   Time Complexity: O(1)\n\n");
        
        result.append("Performance Summary:\n");
        result.append("All primary operations achieve O(1) time complexity!\n");
        result.append("This demonstrates the efficiency of our HashMap-based indexing system.");
        
        displayResult("Performance Test", result.toString());
    }
    
    private void clearResults() {
        resultArea.setText("");
        displayWelcomeMessage();
    }
    
    private void showDatasetInfo() {
        if (bookService == null) {
            showError("Data not loaded yet. Please wait...");
            return;
        }
        
        StringBuilder info = new StringBuilder();
        info.append("Dataset Information\n");
        info.append("==================\n\n");
        info.append("Source: Amazon's Top 50 Bestselling Books (2009-2019)\n");
        info.append("Format: CSV file\n");
        info.append("Total Books: ").append(bookService.getTotalBookCount()).append("\n");
        info.append("Unique Authors: ").append(bookService.getAllAuthors().size()).append("\n\n");
        
        info.append("Data Columns:\n");
        info.append("1. Name - Book title\n");
        info.append("2. Author - Author name\n");
        info.append("3. User Rating - Amazon user rating (3.3 - 4.9)\n");
        info.append("4. Reviews - Number of user reviews (37 - 87,800)\n");
        info.append("5. Price - Book price ($0 - $105)\n");
        info.append("6. Year - Publication year (2009 - 2019)\n");
        info.append("7. Genre - Fiction or Non-Fiction\n\n");
        
        info.append("Technical Implementation:\n");
        info.append("- Uses HashMap for O(1) author and rating lookups\n");
        info.append("- Uses HashSet for O(1) unique author collection\n");
        info.append("- Implements LLD best practices and design patterns\n");
        info.append("- Robust CSV parsing with quoted field support\n");
        info.append("- Service layer architecture for maintainability");
        
        displayResult("Dataset Information", info.toString());
    }
    
    private void displayResult(String title, String content) {
        StringBuilder result = new StringBuilder();
        result.append("=== ").append(title).append(" ===\n");
        result.append(content).append("\n");
        result.append("========================================\n\n");
        
        resultArea.append(result.toString());
        resultArea.setCaretPosition(resultArea.getDocument().getLength());
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Set system look and feel
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
        
        // Create and show GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BookAnalysisGUI gui = new BookAnalysisGUI();
                gui.setVisible(true);
            }
        });
    }
}
