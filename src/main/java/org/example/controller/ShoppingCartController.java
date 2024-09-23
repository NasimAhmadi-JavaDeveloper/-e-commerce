package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ShoppingCartItemDto;
import org.example.dto.TotalPriceDto;
import org.example.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public void addToCart(@RequestParam String productId, @RequestParam int quantity) {
        shoppingCartService.addProductToCart(productId, quantity);
    }

    @GetMapping
    public List<ShoppingCartItemDto> getCartItems() {
        return shoppingCartService.getCartItems();
    }

    @PutMapping("/{id}/{quantity}")
    public void updateCartItem(@PathVariable String id, @PathVariable int quantity) {
        shoppingCartService.updateProductQuantityInCart(id, quantity);
    }

    @DeleteMapping("/{productId}")
    public void removeFromCart(@PathVariable String productId) {
        shoppingCartService.removeProductFromCart(productId);
    }

    @GetMapping("/total")
    public TotalPriceDto getTotalPrice() {
        return shoppingCartService.getTotalPriceOfCart();
    }
}
