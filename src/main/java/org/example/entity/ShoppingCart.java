package org.example.entity;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class ShoppingCart {

    private final Map<String, Integer> cartItems = new HashMap<>();

    public void addProduct(String productId, int quantity) {
        cartItems.merge(productId, quantity, Integer::sum);
    }

    public void removeProduct(String productId) {
        cartItems.remove(productId);
    }

    public void updateProductQuantity(String productId, int quantity) {
        cartItems.computeIfPresent(productId, (key, oldQuantity) -> quantity);
    }
}
