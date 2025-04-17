package com.bookstore.api.data;

import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BookData {
    private static final Map<Integer, Book> books = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    // --- Book methods ---
    public static Book addBook(Book book) {
        int id = idGenerator.getAndIncrement();
        book.setId(id);
        books.put(id, book);
        return book;
    }

    // Method to find a book by ID
    public static Book findBookById(int id) {
        return books.get(id);
    }

    // Method to get all books
    public static List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    // Method to update a book
    public static Book updateBook(Book book) {
        books.put(book.getId(), book);
        return book;
    }

    // Method to delete a book
    public static void deleteBook(int id) {
        books.remove(id);
    }

    // Method to update the stock of a book
    public static void updateStock(int bookId, int newStock) {
        Book book = books.get(bookId);
        if (book == null) {
            throw new InvalidInputException("Cannot update stock: book with ID " + bookId + " does not exist.");
        }
        if (newStock < 0) {
            throw new InvalidInputException("Cannot update stock: stock cannot be negative for book ID " + bookId + ".");
        }
        book.setStock(newStock);
        books.put(bookId, book);
    }
}