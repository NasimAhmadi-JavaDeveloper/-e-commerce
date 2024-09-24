package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ShoppingCartDto;
import org.example.dto.ShoppingCartItemDto;
import org.example.dto.TotalPriceDto;
import org.example.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public void addToCart(@RequestBody @Valid ShoppingCartDto dto) {
        shoppingCartService.addProductToCart(dto);
    }

    @GetMapping
    public List<ShoppingCartItemDto> getCartItems() {
        return shoppingCartService.getCartItems();
    }

    @PutMapping
    public void updateCartItem(@RequestBody @Valid ShoppingCartDto dto) {
        shoppingCartService.updateProductQuantityInCart(dto);
    }

    @DeleteMapping("/{productName}")
    public void removeFromCart(@PathVariable String productName) {
        shoppingCartService.removeProductFromCart(productName);
    }

    @GetMapping("/total")
    public TotalPriceDto getTotalPrice() {
        return shoppingCartService.getTotalPriceOfCart();
    }
}
