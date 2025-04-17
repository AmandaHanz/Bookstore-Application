package com.bookstore.api.datastore;


import com.bookstore.api.exception.InvalidInputException;
import com.bookstore.api.model.Order;

import java.util.*;

public class OrderData {
    private static final Map<Integer, List<Order>> orderMap = new HashMap<>();
    private static int nextOrderId = 1;

    public static Order createOrder(Order order) {
        if (CustomerData.findCustomerById(order.getCustomerId()) == null) {
            throw new InvalidInputException("Cannot create order: customer does not exist.");
        }

        order.setId(nextOrderId++);
        orderMap.computeIfAbsent(order.getCustomerId(), k -> new ArrayList<>()).add(order);
        return order;
    }

    public static List<Order> findOrdersByCustomerId(int customerId) {
        return orderMap.getOrDefault(customerId, new ArrayList<>());
    }

    public static Order findOrderById(int customerId, int orderId) {
        List<Order> orders = orderMap.get(customerId);
        if (orders != null) {
            return orders.stream()
                    .filter(o -> o.getId() == orderId)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
