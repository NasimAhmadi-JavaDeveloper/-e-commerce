package org.example.exception;

public class DuplicateProductNameException extends RuntimeException {
    public DuplicateProductNameException(String productName) {
        super(String.format("Product with the same name %s already exists", productName));
    }
}

