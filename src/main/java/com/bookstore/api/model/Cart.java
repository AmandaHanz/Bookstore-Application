package com.bookstore.api.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> items;

    // Default constructor
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Constructor with all fields
    public Cart(int customerId, List<CartItem> items) {
        this.customerId = customerId;
        // Defensive copy to prevent external modification
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    // Getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getItems() {
        // Return a defensive copy to prevent external modification
        return new ArrayList<>(items);
    }

    public void setItems(List<CartItem> items) {
        // Defensive copy to prevent external modification
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void addOrUpdateItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.getBookId() == item.getBookId()) {
                existing.setQuantity(item.getQuantity());
                return;
            }
        }
        items.add(item);
    }


    // toString for debugging
    @Override
    public String toString() {
        return "Cart{" +
                "customerId=" + customerId +
                ", items=" + items +
                '}';
    }
}