package com.bookstore.api.data;

import com.bookstore.api.model.Author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthorData {
    private static final Map<Integer, Author> authors = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    // --- Author methods ---
    public static Author addAuthor(Author author) {
        int id = idGenerator.getAndIncrement();
        author.setId(id);
        authors.put(id, author);
        return author;
    }

    // Method to find an author by ID
    public static Author findAuthorById(int id) {
        return authors.get(id);
    }

    // Method to get all authors
    public static List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    // Method to update an author
    public static Author updateAuthor(Author author) {
        authors.put(author.getId(), author);
        return author;
    }

    // Method to delete an author
    public static void deleteAuthor(int id) {
        // Check if author has associated books
        boolean hasBooks = BookData.getAllBooks().stream()
                .anyMatch(book -> book.getAuthorId() == id);
        if (hasBooks) {
            throw new IllegalStateException("Cannot delete author with ID " + id + " because they have associated books.");
        }
        authors.remove(id);
    }
}