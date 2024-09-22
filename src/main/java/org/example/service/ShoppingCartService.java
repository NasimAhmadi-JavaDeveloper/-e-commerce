package org.example.service;

import org.example.entity.Product;
import org.example.entity.ShoppingCart;
import org.example.exception.OutOfStockException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingCart shoppingCart = new ShoppingCart();
    private final ProductService productService;

    public ShoppingCartService(ProductService productService) {
        this.productService = productService;
    }

    public void addProductToCart(String productId, int quantity) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getQuantityInStock() < quantity) {
            throw new OutOfStockException("Product " + product.getName() + " is out of stock or insufficient quantity available.");
        }

        shoppingCart.addProduct(productId, quantity);
        productService.reduceStock(productId, quantity);
    }

    public void removeProductFromCart(String productId) {
        if (!shoppingCart.getCartItems().containsKey(productId)) {
            throw new IllegalArgumentException("Product with ID " + productId + " is not in the shopping cart.");
        }
        shoppingCart.removeProduct(productId);
    }

    public void updateProductQuantityInCart(String productId, int quantity) {
        shoppingCart.updateProductQuantity(productId, quantity);
    }

    public Map<String, Integer> getCartItems() {
        return shoppingCart.getCartItems();
    }

    public double getTotalPriceOfCart() {
        return shoppingCart.calculateTotalPriceOfShoppingCart(productService.getAllProducts()
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product)));
    }
}
