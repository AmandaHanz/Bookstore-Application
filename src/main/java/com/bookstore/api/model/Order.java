package com.bookstore.api.model;

import java.util.Map;


public class Order {

    private int id;
    private int customerId;
    private Map<Integer, Integer> items;

    public Order() {
    }

    public Order(int id, int customerId, Map<Integer, Integer> items) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the items
     */
    public Map<Integer, Integer> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }



}
