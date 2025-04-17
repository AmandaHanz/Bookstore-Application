package com.bookstore.api.exception;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message) { super(message); }
}