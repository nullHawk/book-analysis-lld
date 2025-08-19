package com.nullhawk.books;

import com.nullhawk.books.model.Book;
import com.nullhawk.books.services.BookService;
import com.nullhawk.books.services.InMemoryLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Demonstration class that showcases all the required functionality
 * and demonstrates the efficiency of the implemented data structures.
 */
public class BookAnalysisDemo {
    
    public static void main(String[] args) {
        try {
            // Load data from CSV
            System.out.println("Loading books from CSV...");
            InMemoryLoader loader = new InMemoryLoader();
            List<Book> books = loader.loadBooksFromCSV("resources/data.csv");
            
            System.out.println("Total books loaded: " + books.size());
            System.out.println("========================================\n");
            
            // Initialize BookService with loaded data
            BookService bookService = new BookService(books);
            
            // Demonstrate all required functionality
            demonstrateRequiredTasks(bookService);
            
            // Demonstrate additional utility methods
            demonstrateUtilityMethods(bookService);
            
            // Performance demonstration
            demonstratePerformance(bookService);
            
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates all the required tasks from the assignment
     */
    private static void demonstrateRequiredTasks(BookService bookService) {
        System.out.println("=== REQUIRED TASKS DEMONSTRATION ===\n");
        
        // Task 1: Total number of books by an author
        String authorName = "Stephen King";
        int totalBooks = bookService.getTotalBooksByAuthor(authorName);
        System.out.println("Task 1: Total books by " + authorName + ": " + totalBooks);
        
        // Task 2: All authors in the dataset
        Set<String> allAuthors = bookService.getAllAuthors();
        System.out.println("\nTask 2: Total unique authors: " + allAuthors.size());
        System.out.println("First 10 authors: " + allAuthors.stream().limit(10).toList());
        
        // Task 3: Names of all books by an author
        List<String> bookNames = bookService.getBookNamesByAuthor(authorName);
        System.out.println("\nTask 3: Books by " + authorName + ":");
        bookNames.forEach(name -> System.out.println("  - " + name));
        
        // Task 4: Books with specific rating
        double rating = 4.8;
        List<Book> booksWithRating = bookService.getBooksByRating(rating);
        System.out.println("\nTask 4: Books with rating " + rating + " (showing first 5):");
        booksWithRating.stream().limit(5).forEach(book -> 
            System.out.println("  - " + book.getTitle() + " by " + book.getAuthor())
        );
        
        // Task 5: Names and prices of books by an author
        Map<String, Double> bookPrices = bookService.getBookNamesAndPricesByAuthor(authorName);
        System.out.println("\nTask 5: Books and prices by " + authorName + ":");
        bookPrices.forEach((title, price) -> 
            System.out.println("  - " + title + ": $" + price)
        );
        
        System.out.println("\n========================================\n");
    }
    
    /**
     * Demonstrates additional utility methods
     */
    private static void demonstrateUtilityMethods(BookService bookService) {
        System.out.println("=== ADDITIONAL UTILITY METHODS ===\n");
        
        // Books by genre
        String genre = "Fiction";
        List<Book> fictionBooks = bookService.getBooksByGenre(genre);
        System.out.println("Books in " + genre + " genre: " + fictionBooks.size());
        
        // Books by year range
        int startYear = 2015;
        int endYear = 2019;
        List<Book> recentBooks = bookService.getBooksByYearRange(startYear, endYear);
        System.out.println("Books published between " + startYear + "-" + endYear + ": " + recentBooks.size());
        
        // Top rated books
        int topLimit = 5;
        List<Book> topBooks = bookService.getTopRatedBooks(topLimit);
        System.out.println("\nTop " + topLimit + " rated books:");
        topBooks.forEach(book -> 
            System.out.println("  - " + book.getTitle() + " (Rating: " + book.getUserRating() + ")")
        );
        
        // Books by price range
        double minPrice = 10.0;
        double maxPrice = 20.0;
        List<Book> affordableBooks = bookService.getBooksByPriceRange(minPrice, maxPrice);
        System.out.println("\nBooks priced between $" + minPrice + "-$" + maxPrice + ": " + affordableBooks.size());
        
        System.out.println("\n========================================\n");
    }
    
    /**
     * Demonstrates the performance characteristics of the data structures
     */
    private static void demonstratePerformance(BookService bookService) {
        System.out.println("=== PERFORMANCE DEMONSTRATION ===\n");
        
        // Test author lookup performance (should be O(1))
        String testAuthor = "Jeff Kinney";
        long startTime = System.nanoTime();
        int bookCount = bookService.getTotalBooksByAuthor(testAuthor);
        long endTime = System.nanoTime();
        
        System.out.println("Author lookup performance test:");
        System.out.println("  - Author: " + testAuthor);
        System.out.println("  - Books found: " + bookCount);
        System.out.println("  - Lookup time: " + (endTime - startTime) + " nanoseconds");
        
        // Test rating lookup performance (should be O(1))
        double testRating = 4.7;
        startTime = System.nanoTime();
        List<Book> booksWithRating = bookService.getBooksByRating(testRating);
        endTime = System.nanoTime();
        
        System.out.println("\nRating lookup performance test:");
        System.out.println("  - Rating: " + testRating);
        System.out.println("  - Books found: " + booksWithRating.size());
        System.out.println("  - Lookup time: " + (endTime - startTime) + " nanoseconds");
        
        // Test unique authors retrieval (should be O(1))
        startTime = System.nanoTime();
        Set<String> allAuthors = bookService.getAllAuthors();
        endTime = System.nanoTime();
        
        System.out.println("\nUnique authors retrieval performance test:");
        System.out.println("  - Total unique authors: " + allAuthors.size());
        System.out.println("  - Retrieval time: " + (endTime - startTime) + " nanoseconds");
        
        System.out.println("\n========================================\n");
    }
}
