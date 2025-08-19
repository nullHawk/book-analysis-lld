package com.nullhawk.books.model;

/**
 * Represents a book with all its attributes from the Amazon bestseller dataset.
 * Uses builder pattern for flexible object creation.
 */
public class Book {
    private String title;
    private String author;
    private double userRating;
    private int reviews;
    private double price;
    private int year;
    private String genre;

    // Private constructor to enforce builder pattern
    private Book() {}

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getUserRating() { return userRating; }
    public int getReviews() { return reviews; }
    public double getPrice() { return price; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setUserRating(double userRating) { this.userRating = userRating; }
    public void setReviews(int reviews) { this.reviews = reviews; }
    public void setPrice(double price) { this.price = price; }
    public void setYear(int year) { this.year = year; }
    public void setGenre(String genre) { this.genre = genre; }

    /**
     * Prints formatted book details
     */
    public void printDetails() {
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("User Rating: " + userRating);
        System.out.println("Reviews: " + reviews);
        System.out.println("Price: $" + price);
        System.out.println("Year: " + year);
        System.out.println("Genre: " + genre);
        System.out.println("----------------------------------------");
    }

    /**
     * Builder class for Book objects
     */
    public static class Builder {
        private Book book = new Book();

        public Builder title(String title) {
            book.title = title;
            return this;
        }

        public Builder author(String author) {
            book.author = author;
            return this;
        }

        public Builder userRating(double userRating) {
            book.userRating = userRating;
            return this;
        }

        public Builder reviews(int reviews) {
            book.reviews = reviews;
            return this;
        }

        public Builder price(double price) {
            book.price = price;
            return this;
        }

        public Builder year(int year) {
            book.year = year;
            return this;
        }

        public Builder genre(String genre) {
            book.genre = genre;
            return this;
        }

        public Book build() {
            return book;
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", userRating=" + userRating +
                ", reviews=" + reviews +
                ", price=" + price +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                '}';
    }
}