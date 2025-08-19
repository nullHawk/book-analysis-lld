package com.nullhawk.books.services;

import com.nullhawk.books.model.Book;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that provides efficient book retrieval operations.
 * Uses optimal data structures for O(1) and O(log n) time complexity operations.
 */
public class BookService {
    
    // Data structures for efficient retrieval
    private final Map<String, List<Book>> authorToBooksMap;        // O(1) author lookups
    private final Map<Double, List<Book>> ratingToBooksMap;        // O(1) rating lookups
    private final Set<String> uniqueAuthors;                       // O(1) unique author collection
    private final List<Book> allBooks;                            // All books for iteration
    
    /**
     * Constructor that initializes data structures for efficient retrieval
     * @param books list of all books to index
     */
    public BookService(List<Book> books) {
        this.allBooks = new ArrayList<>(books);
        this.authorToBooksMap = new HashMap<>();
        this.ratingToBooksMap = new HashMap<>();
        this.uniqueAuthors = new HashSet<>();
        
        // Build indexes for efficient retrieval
        buildIndexes();
    }
    
    /**
     * Builds all necessary indexes for efficient data retrieval
     * Time Complexity: O(n) where n is the number of books
     */
    private void buildIndexes() {
        for (Book book : allBooks) {
            // Index by author
            authorToBooksMap.computeIfAbsent(book.getAuthor(), k -> new ArrayList<>()).add(book);
            
            // Index by rating
            ratingToBooksMap.computeIfAbsent(book.getUserRating(), k -> new ArrayList<>()).add(book);
            
            // Collect unique authors
            uniqueAuthors.add(book.getAuthor());
        }
    }
    
    /**
     * Task 1: Get total number of books by an author
     * Time Complexity: O(1) average case
     * @param authorName name of the author
     * @return total number of books by the author
     */
    public int getTotalBooksByAuthor(String authorName) {
        List<Book> authorBooks = authorToBooksMap.get(authorName);
        return authorBooks != null ? authorBooks.size() : 0;
    }
    
    /**
     * Task 2: Get all unique authors in the dataset
     * Time Complexity: O(1) - just returns the pre-built set
     * @return set of all unique author names
     */
    public Set<String> getAllAuthors() {
        return new HashSet<>(uniqueAuthors);
    }
    
    /**
     * Task 3: Get names of all books by an author
     * Time Complexity: O(1) average case
     * @param authorName name of the author
     * @return list of book titles by the author
     */
    public List<String> getBookNamesByAuthor(String authorName) {
        List<Book> authorBooks = authorToBooksMap.get(authorName);
        if (authorBooks == null) {
            return new ArrayList<>();
        }
        
        return authorBooks.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
    
    /**
     * Task 4: Get all books with a specific user rating
     * Time Complexity: O(1) average case
     * @param rating the user rating to filter by
     * @return list of books with the specified rating
     */
    public List<Book> getBooksByRating(double rating) {
        List<Book> booksWithRating = ratingToBooksMap.get(rating);
        return booksWithRating != null ? new ArrayList<>(booksWithRating) : new ArrayList<>();
    }
    
    /**
     * Task 5: Get names and prices of all books by an author
     * Time Complexity: O(1) average case
     * @param authorName name of the author
     * @return map of book titles to prices
     */
    public Map<String, Double> getBookNamesAndPricesByAuthor(String authorName) {
        List<Book> authorBooks = authorToBooksMap.get(authorName);
        if (authorBooks == null) {
            return new HashMap<>();
        }
        
        return authorBooks.stream()
                .collect(Collectors.toMap(
                    Book::getTitle,
                    Book::getPrice,
                    (existing, replacement) -> existing  // Keep first occurrence if duplicates
                ));
    }
    
    /**
     * Additional utility method: Get books by genre
     * Time Complexity: O(n) where n is the number of books
     * @param genre the genre to filter by
     * @return list of books in the specified genre
     */
    public List<Book> getBooksByGenre(String genre) {
        return allBooks.stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
    
    /**
     * Additional utility method: Get books by year range
     * Time Complexity: O(n) where n is the number of books
     * @param startYear start year (inclusive)
     * @param endYear end year (inclusive)
     * @return list of books published in the specified year range
     */
    public List<Book> getBooksByYearRange(int startYear, int endYear) {
        return allBooks.stream()
                .filter(book -> book.getYear() >= startYear && book.getYear() <= endYear)
                .collect(Collectors.toList());
    }
    
    /**
     * Additional utility method: Get top rated books
     * Time Complexity: O(n log n) due to sorting
     * @param limit maximum number of books to return
     * @return list of top rated books
     */
    public List<Book> getTopRatedBooks(int limit) {
        return allBooks.stream()
                .sorted((b1, b2) -> Double.compare(b2.getUserRating(), b1.getUserRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Additional utility method: Get books by price range
     * Time Complexity: O(n) where n is the number of books
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of books in the specified price range
     */
    public List<Book> getBooksByPriceRange(double minPrice, double maxPrice) {
        return allBooks.stream()
                .filter(book -> book.getPrice() >= minPrice && book.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    /**
     * Get total number of books in the dataset
     * @return total count of books
     */
    public int getTotalBookCount() {
        return allBooks.size();
    }
    
    /**
     * Get all books (defensive copy)
     * @return list of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(allBooks);
    }
}
