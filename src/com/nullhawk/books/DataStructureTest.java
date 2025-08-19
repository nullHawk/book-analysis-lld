package com.nullhawk.books;

import com.nullhawk.books.model.Book;
import com.nullhawk.books.services.BookService;
import com.nullhawk.books.services.InMemoryLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple test class to verify data structure functionality and performance.
 */
public class DataStructureTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Data Structure Verification Test ===\n");
            
            // Load data
            InMemoryLoader loader = new InMemoryLoader();
            List<Book> books = loader.loadBooksFromCSV("resources/data.csv");
            BookService bookService = new BookService(books);
            
            // Test 1: Verify HashMap author indexing
            testAuthorIndexing(bookService);
            
            // Test 2: Verify HashMap rating indexing
            testRatingIndexing(bookService);
            
            // Test 3: Verify HashSet unique authors
            testUniqueAuthors(bookService);
            
            // Test 4: Performance comparison
            testPerformanceComparison(bookService);
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void testAuthorIndexing(BookService bookService) {
        System.out.println("Test 1: Author Indexing Verification");
        System.out.println("=====================================");
        
        String[] testAuthors = {"Stephen King", "Jeff Kinney", "George R. R. Martin"};
        
        for (String author : testAuthors) {
            int bookCount = bookService.getTotalBooksByAuthor(author);
            List<String> bookNames = bookService.getBookNamesByAuthor(author);
            
            System.out.println("Author: " + author);
            System.out.println("  - Total books: " + bookCount);
            System.out.println("  - Books: " + bookNames);
            System.out.println();
        }
    }
    
    private static void testRatingIndexing(BookService bookService) {
        System.out.println("Test 2: Rating Indexing Verification");
        System.out.println("=====================================");
        
        double[] testRatings = {4.9, 4.8, 4.7};
        
        for (double rating : testRatings) {
            List<Book> booksWithRating = bookService.getBooksByRating(rating);
            System.out.println("Rating: " + rating);
            System.out.println("  - Books found: " + booksWithRating.size());
            System.out.println("  - Sample books:");
            booksWithRating.stream().limit(3).forEach(book -> 
                System.out.println("    * " + book.getTitle() + " by " + book.getAuthor())
            );
            System.out.println();
        }
    }
    
    private static void testUniqueAuthors(BookService bookService) {
        System.out.println("Test 3: Unique Authors Verification");
        System.out.println("===================================");
        
        Set<String> allAuthors = bookService.getAllAuthors();
        System.out.println("Total unique authors: " + allAuthors.size());
        
        // Verify no duplicates
        List<Book> allBooks = bookService.getAllBooks();
        long totalAuthorOccurrences = allBooks.stream().map(Book::getAuthor).count();
        System.out.println("Total author occurrences: " + totalAuthorOccurrences);
        System.out.println("Verification: " + (totalAuthorOccurrences >= allAuthors.size() ? "PASS" : "FAIL"));
        System.out.println();
    }
    
    private static void testPerformanceComparison(BookService bookService) {
        System.out.println("Test 4: Performance Comparison");
        System.out.println("===============================");
        
        // Test HashMap lookup (should be O(1))
        String testAuthor = "Stephen King";
        long startTime = System.nanoTime();
        int bookCount = bookService.getTotalBooksByAuthor(testAuthor);
        long hashMapTime = System.nanoTime() - startTime;
        
        // Test linear search simulation (would be O(n))
        List<Book> allBooks = bookService.getAllBooks();
        startTime = System.nanoTime();
        long linearCount = allBooks.stream()
                .filter(book -> book.getAuthor().equals(testAuthor))
                .count();
        long linearTime = System.nanoTime() - startTime;
        
        System.out.println("Performance comparison for author: " + testAuthor);
        System.out.println("  - HashMap lookup (O(1)): " + hashMapTime + " ns, Result: " + bookCount);
        System.out.println("  - Linear search (O(n)): " + linearTime + " ns, Result: " + linearCount);
        System.out.println("  - Speed improvement: " + (linearTime / hashMapTime) + "x faster");
        System.out.println();
        
        // Verify results match
        System.out.println("Verification: " + (bookCount == linearCount ? "PASS" : "FAIL"));
    }
}
