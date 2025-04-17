package com.bookstore.api.data;

import com.bookstore.api.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerData {
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    // --- Customer methods ---
    public static Customer addCustomer(Customer customer) {
        int id = idGenerator.getAndIncrement();
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    // Method to find a customer by ID
    public static Customer findCustomerById(int id) {
        return customers.get(id);
    }

    // Method to find a customer by email
    public static Customer findCustomerByEmail(String email) {
        return customers.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Method to get all customers
    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    // Method to update a customer
    public static Customer updateCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    // Method to delete a customer
    public static void deleteCustomer(int id) {
        customers.remove(id);
    }
}