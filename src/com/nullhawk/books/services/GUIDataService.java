package com.nullhawk.books.services;

import com.nullhawk.books.model.Book;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Service class responsible for managing data loading for GUI components.
 * Handles asynchronous data loading and provides callbacks for success/error handling.
 * Follows the service layer pattern for separation of concerns.
 */
public class GUIDataService {
    
    private BookService bookService;
    private boolean isDataLoaded = false;
    
    /**
     * Interface for data load success callback
     */
    @FunctionalInterface
    public interface DataLoadSuccessCallback {
        void onSuccess(BookService bookService);
    }
    
    /**
     * Interface for data load error callback
     */
    @FunctionalInterface
    public interface DataLoadErrorCallback {
        void onError(String errorMessage);
    }
    
    /**
     * Initializes data loading in background thread
     * @param successCallback callback to execute on successful data load
     * @param errorCallback callback to execute on data load error
     */
    public void initializeData(DataLoadSuccessCallback successCallback, DataLoadErrorCallback errorCallback) {
        // Load data in background using SwingWorker
        SwingWorker<BookService, Void> worker = new SwingWorker<BookService, Void>() {
            @Override
            protected BookService doInBackground() throws Exception {
                // Load data from CSV
                InMemoryLoader loader = new InMemoryLoader();
                List<Book> books = loader.loadBooksFromCSV("resources/data.csv");
                
                // Create and return BookService
                return new BookService(books);
            }
            
            @Override
            protected void done() {
                try {
                    // Get the result and set it
                    bookService = get();
                    isDataLoaded = true;
                    
                    // Call success callback
                    if (successCallback != null) {
                        successCallback.onSuccess(bookService);
                    }
                    
                } catch (Exception e) {
                    // Handle error
                    isDataLoaded = false;
                    String errorMessage = "Failed to load data: " + e.getMessage();
                    
                    if (errorCallback != null) {
                        errorCallback.onError(errorMessage);
                    }
                    
                    e.printStackTrace();
                }
            }
        };
        
        // Execute the worker
        worker.execute();
    }
    
    /**
     * Gets the BookService instance
     * @return BookService instance or null if not loaded
     */
    public BookService getBookService() {
        return bookService;
    }
    
    /**
     * Checks if data is loaded
     * @return true if data is loaded, false otherwise
     */
    public boolean isDataLoaded() {
        return isDataLoaded;
    }
    
    /**
     * Reloads data from the CSV file
     * @param successCallback callback to execute on successful data reload
     * @param errorCallback callback to execute on data reload error
     */
    public void reloadData(DataLoadSuccessCallback successCallback, DataLoadErrorCallback errorCallback) {
        isDataLoaded = false;
        bookService = null;
        initializeData(successCallback, errorCallback);
    }
    
    /**
     * Gets data loading status as a string
     * @return status string
     */
    public String getDataStatus() {
        if (!isDataLoaded) {
            return "Data not loaded";
        }
        
        if (bookService == null) {
            return "Service not available";
        }
        
        return String.format("Data loaded: %d books, %d authors", 
                           bookService.getTotalBookCount(), 
                           bookService.getAllAuthors().size());
    }
}
