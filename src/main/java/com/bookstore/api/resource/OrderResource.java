package com.bookstore.api.resource;

import com.bookstore.api.datastore.BookData;
import com.bookstore.api.datastore.CartData;
import com.bookstore.api.datastore.CustomerData;
import com.bookstore.api.datastore.OrderData;
import com.bookstore.api.exception.CartNotFoundException;
import com.bookstore.api.exception.CustomerNotFoundException;
import com.bookstore.api.model.Book;
import com.bookstore.api.model.Cart;
import com.bookstore.api.model.CartItem;
import com.bookstore.api.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        LOGGER.info("Creating order for customer ID: {}", customerId);

        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Cart cart = CartData.findCartByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart is empty or not found for customer ID " + customerId);
        }

        Map<Integer, Integer> orderItems = new LinkedHashMap<>();
        for (CartItem item : cart.getItems()) {
            orderItems.put(item.getBookId(), item.getQuantity());
        }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setItems(orderItems);

        Order savedOrder = OrderData.createOrder(order);

        // Clear cart after successful order
        CartData.deleteCart(customerId);
        LOGGER.info("Order created: {}", savedOrder);

        return Response.status(Response.Status.CREATED).entity(savedOrder).build();
    }

    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        LOGGER.info("Retrieving all orders for customer ID: {}", customerId);

        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        List<Order> orders = OrderData.findOrdersByCustomerId(customerId);
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        LOGGER.info("Retrieving order ID: {} for customer ID: {}", orderId, customerId);

        if (CustomerData.findCustomerById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }

        Order order = OrderData.findOrderById(customerId, orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();
        }

        return Response.ok(order).build();
    }
}
