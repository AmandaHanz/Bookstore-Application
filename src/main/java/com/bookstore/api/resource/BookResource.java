package com.bookstore.api.resource;

import com.bookstore.api.data.AuthorData;
import com.bookstore.api.data.BookData;
import com.bookstore.api.exception.BookNotFoundException;
import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookResource.class);

    /**
     * Creates a new book.
     *
     * @param book the book to create
     * @return the created book
     */
    @POST
    public Response createBook(Book book) {
        LOGGER.info("Creating book: {}", book);
        // Basic validation
        if (book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getIsbn() == null || book.getIsbn().isEmpty() ||
                book.getPublicationYear() <= 0 ||
                book.getPrice() <= 0 ||
                book.getStock() < 0) {
            throw new InvalidInputException("Invalid book data: title, isbn, publicationYear, price, and stock are required.");
        }
        // Validate authorId
        if (AuthorData.findAuthorById(book.getAuthorId()) == null) {
            throw new InvalidInputException("Invalid book data: author with ID " + book.getAuthorId() + " does not exist.");
        }
        Book createdBook = BookData.addBook(book);
        LOGGER.info("Created book with ID: {}", createdBook.getId());
        return Response.status(Response.Status.CREATED).entity(createdBook).build();
    }

    /**
     * Retrieves all books.
     *
     * @return a list of books
     */

    @GET
    public Response getAllBooks() {
        LOGGER.info("Retrieving all books");
        List<Book> books = BookData.getAllBooks();
        return Response.ok(books).build();
    }

    /**
     * Retrieves a book by ID.
     *
     * @param id the ID of the book
     * @return the book with the specified ID
     */

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        LOGGER.info("Retrieving book with ID: {}", id);
        Book book = BookData.findBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.ok(book).build();
    }

    /**
     * Updates an existing book.
     *
     * @param id   the ID of the book to update
     * @param book the updated book data
     * @return the updated book
     */

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        LOGGER.info("Updating book with ID: {}", id);
        Book existingBook = BookData.findBookById(id);
        if (existingBook == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        // Update fields
        book.setId(id);
        if (book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getIsbn() == null || book.getIsbn().isEmpty() ||
                book.getPublicationYear() <= 0 ||
                book.getPrice() <= 0 ||
                book.getStock() < 0) {
            throw new InvalidInputException("Invalid book data: title, isbn, publicationYear, price, and stock are required.");
        }
        // Validate authorId
        if (AuthorData.findAuthorById(book.getAuthorId()) == null) {
            throw new InvalidInputException("Invalid book data: author with ID " + book.getAuthorId() + " does not exist.");
        }
        Book updatedBook = BookData.updateBook(book);
        LOGGER.info("Updated book with ID: {}", id);
        return Response.ok(updatedBook).build();
    }

    /**
     * Deletes a book by ID.
     *
     * @param id the ID of the book to delete
     * @return a response indicating the result of the deletion
     */

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        LOGGER.info("Deleting book with ID: {}", id);
        Book book = BookData.findBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        BookData.deleteBook(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}