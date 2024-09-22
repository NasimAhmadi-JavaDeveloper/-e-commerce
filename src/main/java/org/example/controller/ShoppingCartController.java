package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.exception.OutOfStockException;
import org.example.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestParam String productId, @RequestParam int quantity) {
        try {
            shoppingCartService.addProductToCart(productId, quantity);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (OutOfStockException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public Map<String, Integer> getCartItems() {
        return shoppingCartService.getCartItems();
    }

    @PutMapping
    public ResponseEntity<String> updateCartItem(@RequestParam String productId, @RequestParam int quantity) {
        try {
            shoppingCartService.updateProductQuantityInCart(productId, quantity);
            return ResponseEntity.ok("Cart item updated successfully");
        } catch (OutOfStockException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    public void removeFromCart(@PathVariable String productId) {
        shoppingCartService.removeProductFromCart(productId);
    }

    @GetMapping("/total")
    public double getTotalPrice() {
        return shoppingCartService.getTotalPriceOfCart();
    }
}
