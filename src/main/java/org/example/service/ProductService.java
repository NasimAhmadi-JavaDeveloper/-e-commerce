package org.example.service;

import org.example.entity.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private final Map<String, Product> productCatalog = new HashMap<>();

    public Product addProduct(String name, String description, double price, int quantityInStock) {
        String id = UUID.randomUUID().toString();
        Product product = new Product(id, name, description, price, quantityInStock);
        productCatalog.put(id, product);
        return product;
    }

    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(productCatalog.get(id));
    }

    public List<Product> getAllProducts() {
        return List.copyOf(productCatalog.values());
    }

    public Product updateProduct(String id, String name, String description, double price, int quantityInStock) {
        Product product = productCatalog.get(id);
        if (product != null) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantityInStock(quantityInStock);
        }
        return product;
    }

    public void reduceStock(String productId, int quantity) {
        Product product = productCatalog.get(productId);
        if (product != null && product.getQuantityInStock() >= quantity) {
            product.setQuantityInStock(product.getQuantityInStock() - quantity);
        }
    }

    public void deleteProduct(String id) {
        if (!productCatalog.containsKey(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " does not exist.");
        }
        productCatalog.remove(id);
    }
}
