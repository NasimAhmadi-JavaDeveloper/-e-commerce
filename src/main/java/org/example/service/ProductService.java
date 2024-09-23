package org.example.service;

import org.example.entity.Product;
import org.example.exception.DuplicateProductNameException;
import org.example.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ProductService {

    private final Map<String, Product> productCatalog = new HashMap<>();
    private final Object lock = new Object();

    public void addProduct(Product product) {
        synchronized (lock) {

            if (productCatalog.values()
                    .stream()
                    .anyMatch(p -> p.getName().equalsIgnoreCase(product.getName()))) {
                throw new DuplicateProductNameException("Product with the same name already exists.");
            }

            String id = UUID.randomUUID().toString();
            productCatalog.put(id, product);
        }
    }

    public Product getProductById(String id) {
        synchronized (lock) {
            Product product = productCatalog.get(id);
            if (product == null) {
                throw new ProductNotFoundException(String.format("Product with ID %s not found.", id));
            }
            return product;
        }
    }

    public Page<Product> getAllProducts(int page, int size) {
        synchronized (lock) {
            List<Product> products = new ArrayList<>(productCatalog.values());
            int total = products.size();
            int start = Math.min(page * size, total);
            int end = Math.min(start + size, total);
            List<Product> paginatedList = products.subList(start, end);
            return new PageImpl<>(paginatedList, PageRequest.of(page, size), total);
        }
    }

    public void updateProduct(Product product) {
        synchronized (lock) {
            Product entity = productCatalog.get(product.getId());

            if (entity == null) {
                throw new ProductNotFoundException((String.format("No product found with ID: %s.", product.getId())));
            }

            entity.setName(product.getName());
            entity.setDescription(product.getDescription());
            entity.setPrice(product.getPrice());
            entity.setQuantityInStock(product.getQuantityInStock());
        }
    }

    public void reduceStock(String productId, int quantity) {
        synchronized (lock) {
            Product product = productCatalog.get(productId);
            if (product != null && product.getQuantityInStock() >= quantity) {
                product.setQuantityInStock(product.getQuantityInStock() - quantity);
            }
        }
    }

    public void deleteProduct(String id) {
        synchronized (lock) {
            if (!productCatalog.containsKey(id)) {
                throw new ProductNotFoundException((String.format("No product found with ID: %s.", id)));
            }
            productCatalog.remove(id);
        }
    }

    public BigDecimal getSumOfProductPrice() {
        double sum = productCatalog.values()
                .stream()
                .mapToDouble(Product::getPrice)
                .sum();

        return BigDecimal.valueOf(sum)
                .setScale(2, RoundingMode.UP);
    }
}
