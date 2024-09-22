package org.example.service;

import org.example.entity.Product;
import org.example.exception.OutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    private ShoppingCartService shoppingCartService;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        shoppingCartService = new ShoppingCartService(productService);
    }

    @Test
    void addProductToCart_productExistsAndInStock_shouldAddProductToCart() {
        Product product = new Product("1", "Test Product", "Description", 10.0, 5);
        when(productService.getProductById("1")).thenReturn(Optional.of(product));

        shoppingCartService.addProductToCart("1", 2);

        assertEquals(2, shoppingCartService.getCartItems().get("1"));
        verify(productService).reduceStock("1", 2);
    }

    @Test
    void addProductToCart_productNotFound_shouldThrowException() {
        when(productService.getProductById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addProductToCart("1", 2);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void addProductToCart_insufficientStock_shouldThrowOutOfStockException() {
        Product product = new Product("1", "Test Product", "Description", 10.0, 1);
        when(productService.getProductById("1")).thenReturn(Optional.of(product));

        Exception exception = assertThrows(OutOfStockException.class, () -> {
            shoppingCartService.addProductToCart("1", 2);
        });

        assertEquals("Product Test Product is out of stock or insufficient quantity available.", exception.getMessage());
    }

    @Test
    void removeProductFromCart_shouldRemoveProductFromCart() {
        Product product = new Product("1", "Test Product", "Description", 10.0, 5);
        when(productService.getProductById("1")).thenReturn(Optional.of(product));
        shoppingCartService.addProductToCart("1", 2);

        shoppingCartService.removeProductFromCart("1");

        assertFalse(shoppingCartService.getCartItems().containsKey("1"));
    }

    @Test
    void updateProductQuantityInCart_productExistsAndInStock_shouldUpdateQuantity() {
        Product product = new Product("1", "Test Product", "Description", 10.0, 5);
        when(productService.getProductById("1"))
                .thenReturn(Optional.of(product));

        shoppingCartService.addProductToCart("1", 2);

        shoppingCartService.updateProductQuantityInCart("1", 3);

        assertEquals(3, shoppingCartService.getCartItems().get("1"));
    }
}