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

    public double calculateTotalPriceOfShoppingCart(Map<String, Product> productCatalog) {
//        return cartItems.entrySet()
//                .stream()
//                .mapToDouble(entry -> productCatalog.get(entry.getKey()).getPrice() * entry.getValue())
//                .sum();
        return cartItems.entrySet()
                .stream()
                .mapToDouble(entry -> {
                    String productId = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = productCatalog.get(productId); // Get the product from the catalog
                    if (product != null) {
                        return product.getPrice() * quantity;
                    } else {
                        throw new IllegalArgumentException("Product with ID " + productId + " not found in catalog");
                    }
                }).sum();
    }
}
