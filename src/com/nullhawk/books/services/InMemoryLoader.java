package com.nullhawk.books.services;

import com.nullhawk.books.model.Book;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for loading book data from CSV file.
 * Implements the repository pattern for data access.
 */
public class InMemoryLoader {
    
    /**
     * Loads all books from the CSV file
     * @param csvFilePath path to the CSV file
     * @return List of Book objects
     * @throws IOException if file cannot be read
     */
    public List<Book> loadBooksFromCSV(String csvFilePath) throws IOException {
        List<Book> books = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                Book book = parseBookFromCSVLine(line);
                if (book != null) {
                    books.add(book);
                }
            }
        }
        
        return books;
    }
    
    /**
     * Parses a single CSV line into a Book object
     * @param csvLine the CSV line to parse
     * @return Book object or null if parsing fails
     */
    private Book parseBookFromCSVLine(String csvLine) {
        try {
            // Handle quoted fields that may contain commas
            String[] fields = parseCSVLine(csvLine);
            
            if (fields.length >= 7) {
                return new Book.Builder()
                    .title(fields[0].trim())
                    .author(fields[1].trim())
                    .userRating(Double.parseDouble(fields[2].trim()))
                    .reviews(Integer.parseInt(fields[3].trim()))
                    .price(Double.parseDouble(fields[4].trim()))
                    .year(Integer.parseInt(fields[5].trim()))
                    .genre(fields[6].trim())
                    .build();
            }
        } catch (Exception e) {
            System.err.println("Error parsing line: " + csvLine + " - " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Parses CSV line handling quoted fields
     * @param csvLine the CSV line to parse
     * @return array of field values
     */
    private String[] parseCSVLine(String csvLine) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        fields.add(currentField.toString());
        
        return fields.toArray(new String[0]);
    }
}