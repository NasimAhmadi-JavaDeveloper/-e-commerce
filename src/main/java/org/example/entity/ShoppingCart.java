package org.example.entity;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class ShoppingCart {

    private final Map<String, Integer> cartItems = new HashMap<>();

    public void addProduct(String productName, int quantity) {
        cartItems.merge(productName, quantity, Integer::sum);
    }

    public void removeProduct(String productName) {
        cartItems.remove(productName);
    }

    public void updateProductQuantity(String productName, int quantity) {
        if (cartItems.containsKey(productName)) {
            cartItems.put(productName, quantity);
        }
    }
}
