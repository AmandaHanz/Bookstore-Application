package com.bookstore.api.datastore;

import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Cart;

import java.util.HashMap;
import java.util.Map;

public class CartData {
    private static final Map<Integer, Cart> carts = new HashMap<>();

    // --- Cart methods ---
    public static Cart addCart(Cart cart) {
        // Validate customer exists
        if (CustomerData.findCustomerById(cart.getCustomerId()) == null) {
            throw new InvalidInputException("Cannot create cart: customer with ID " + cart.getCustomerId() + " does not exist.");
        }
        carts.put(cart.getCustomerId(), cart);
        return cart;
    }

    public static Cart findCartByCustomerId(int customerId) {
        return carts.get(customerId);
    }

    public static void deleteCart(int customerId) {
        carts.remove(customerId);
    }
}