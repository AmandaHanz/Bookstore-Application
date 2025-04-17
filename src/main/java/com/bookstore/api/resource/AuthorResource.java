package com.bookstore.api.resource;

import com.bookstore.api.data.AuthorData;
import com.bookstore.api.data.BookData;
import com.bookstore.api.exception.AuthorNotFoundException;
import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Author;
import com.bookstore.api.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorResource.class);

    /**
     * Creates a new author.
     *
     * @param author the author to create
     * @return the created author
     */

    @POST
    public Response createAuthor(Author author) {
        LOGGER.info("Creating author: {}", author);
        // Validation
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Invalid author data: name is required.");
        }
        Author createdAuthor = AuthorData.addAuthor(author);
        LOGGER.info("Created author with ID: {}", createdAuthor.getId());
        return Response.status(Response.Status.CREATED).entity(createdAuthor).build();
    }

    /**
     * Retrieves all authors.
     *
     * @return a list of authors
     */

    @GET
    public Response getAllAuthors() {
        LOGGER.info("Retrieving all authors");
        List<Author> authors = AuthorData.getAllAuthors();
        return Response.ok(authors).build();
    }

    /**
     * Retrieves an author by ID.
     *
     * @param id the ID of the author
     * @return the author with the specified ID
     */
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        LOGGER.info("Retrieving author with ID: {}", id);
        Author author = AuthorData.findAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return Response.ok(author).build();
    }

    /**
     * Updates an existing author.
     *
     * @param id     the ID of the author to update
     * @param author the updated author data
     * @return the updated author
     */
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        LOGGER.info("Updating author with ID: {}", id);
        Author existingAuthor = AuthorData.findAuthorById(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        // Validation
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Invalid author data: name is required.");
        }
        author.setId(id);
        Author updatedAuthor = AuthorData.updateAuthor(author);
        LOGGER.info("Updated author with ID: {}", id);
        return Response.ok(updatedAuthor).build();
    }

    /**
     * Deletes an author by ID.
     *
     * @param id the ID of the author to delete
     * @return a response indicating the result of the deletion
     */

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        LOGGER.info("Deleting author with ID: {}", id);
        Author author = AuthorData.findAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        AuthorData.deleteAuthor(id); // Will throw IllegalStateException if author has books
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    /**
     * Retrieves all books by a specific author.
     *
     * @param id the ID of the author
     * @return a list of books by the specified author
     */

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
        LOGGER.info("Retrieving books for author with ID: {}", id);
        Author author = AuthorData.findAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        List<Book> books = BookData.getAllBooks().stream()
                .filter(book -> book.getAuthorId() == id)
                .collect(Collectors.toList());
        return Response.ok(books).build();
    }
}