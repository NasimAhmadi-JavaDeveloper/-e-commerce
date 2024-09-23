package org.example.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String productName) {
        super(String.format("Product '%s' is out of stock or insufficient quantity available.", productName));
    }
}
