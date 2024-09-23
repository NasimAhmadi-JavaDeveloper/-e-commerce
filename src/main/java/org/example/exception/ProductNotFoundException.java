package org.example.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productName) {
        super(String.format("Product with Name %s not found.", productName));
    }
}
