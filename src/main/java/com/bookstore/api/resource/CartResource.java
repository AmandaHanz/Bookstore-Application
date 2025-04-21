package com.bookstore.api.resource;

import com.bookstore.api.data.BookData;
import com.bookstore.api.data.CartData;
import com.bookstore.api.data.CustomerData;
import com.bookstore.api.exception.CartNotFoundException;
import com.bookstore.api.exception.CustomerNotFoundException;
import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Book;
import com.bookstore.api.model.Cart;
import com.bookstore.api.model.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Optional;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartResource.class);


    /**
     * Adds an item to the cart for a specific customer.
     *
     * @param customerId the ID of the customer
     * @param item       the cart item to add
     * @return the updated cart
     */
    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        LOGGER.info("Adding item to cart for customer ID: {}, item: {}", customerId, item);
        // Validate customer
        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        // Validate item
        if (item.getBookId() <= 0 || item.getQuantity() <= 0) {
            throw new InvalidInputException("Invalid cart item: bookId and quantity must be positive.");
        }
        Book book = BookData.findBookById(item.getBookId());
        if (book == null) {
            throw new InvalidInputException("Invalid cart item: book with ID " + item.getBookId() + " does not exist.");
        }
        // Get or create cart
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
        }
        // Check stock for existing and new quantity
        int existingQuantity = cart.getItems().stream()
                .filter(i -> i.getBookId() == item.getBookId())
                .mapToInt(CartItem::getQuantity)
                .sum();
        int newQuantity = existingQuantity + item.getQuantity();
        if (book.getStock() < newQuantity) {
            throw new InvalidInputException("Invalid cart item: insufficient stock for book ID " + item.getBookId() + ". Available: " + book.getStock());
        }
        // Update stock
        BookData.updateStock(item.getBookId(), book.getStock() - item.getQuantity());
        // Add or update item
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getBookId() == item.getBookId())
                .findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(newQuantity);
        } else {
//            cart.getItems().add(new CartItem(item.getBookId(), item.getQuantity()));
            cart.addOrUpdateItem(new CartItem(item.getBookId(), newQuantity));

        }
        Cart updatedCart = CartData.addCart(cart);
        LOGGER.info("Added item to cart for customer ID: {}, updated cart: {}", customerId, updatedCart);
        return Response.status(Response.Status.CREATED).entity(updatedCart).build();
    }

    /**
     * Retrieves the cart for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return the cart for the customer
     */

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        LOGGER.info("Retrieving cart for customer ID: {}", customerId);
        // Validate customer
        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(customerId, new ArrayList<>());
        }
        return Response.ok(cart).build();
    }

    /**
     * Updates an item in the cart for a specific customer.
     *
     * @param customerId the ID of the customer
     * @param bookId     the ID of the book
     * @param item       the updated cart item
     * @return the updated cart
     */
    @PUT
    @Path("/items/{bookId}")
    public Response updateItemInCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, CartItem item) {
        LOGGER.info("Updating item in cart for customer ID: {}, bookId: {}, item: {}", customerId, bookId, item);
        // Validate customer
        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        // Validate item
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Invalid cart item: quantity must be positive.");
        }
        Book book = BookData.findBookById(bookId);
        if (book == null) {
            throw new InvalidInputException("Invalid cart item: book with ID " + bookId + " does not exist.");
        }
        // Get cart
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }
        // Find existing item
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getBookId() == bookId)
                .findFirst();
        if (existingItem.isEmpty()) {
            throw new InvalidInputException("Cart item with book ID " + bookId + " does not exist in the cart.");
        }
        // Calculate stock difference
        int currentQuantity = existingItem.get().getQuantity();
        int quantityDifference = item.getQuantity() - currentQuantity;
        if (book.getStock() < quantityDifference) {
            throw new InvalidInputException("Invalid cart item: insufficient stock for book ID " + bookId + ". Available: " + book.getStock());
        }
        // Update stock
        if (quantityDifference != 0) {
            BookData.updateStock(bookId, book.getStock() - quantityDifference);
        }
        // Update item
        existingItem.get().setQuantity(item.getQuantity());
        Cart updatedCart = CartData.addCart(cart);
        LOGGER.info("Updated item in cart for customer ID: {}, updated cart: {}", customerId, updatedCart);
        return Response.ok(updatedCart).build();
    }

    /**
     * Removes an item from the cart for a specific customer.
     *
     * @param customerId the ID of the customer
     * @param bookId     the ID of the book
     * @return a response indicating the result of the removal
     */

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        LOGGER.info("Removing item from cart for customer ID: {}, bookId: {}", customerId, bookId);
        // Validate customer
        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        // Get cart
        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist.");
        }
        // Find item
        Optional<CartItem> itemToRemove = cart.getItems().stream()
                .filter(i -> i.getBookId() == bookId)
                .findFirst();
        if (itemToRemove.isEmpty()) {
            throw new InvalidInputException("Cart item with book ID " + bookId + " does not exist in the cart.");
        }
        // Update stock
        Book book = BookData.findBookById(bookId);
        if (book != null) {
            BookData.updateStock(bookId, book.getStock() + itemToRemove.get().getQuantity());
        }
        // Remove item
        cart.getItems().removeIf(i -> i.getBookId() == bookId);
        if (cart.getItems().isEmpty()) {
            CartData.deleteCart(customerId);
        } else {
            CartData.addCart(cart);
        }
        LOGGER.info("Removed item from cart for customer ID: {}, bookId: {}", customerId, bookId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}