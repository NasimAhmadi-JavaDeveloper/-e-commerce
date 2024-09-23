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
            checkUniqueProductName(product.getName());
            productCatalog.put(product.getName(), product);
        }
    }

    private void checkUniqueProductName(String productName) {
        if (productCatalog.values()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productName))) {
            throw new DuplicateProductNameException(productName);
        }
    }

    public Product getProductByName(String productName) {
        synchronized (lock) {
            Product product = productCatalog.get(productName);
            if (product == null) {
                throw new ProductNotFoundException(productName);
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
            Product entity = productCatalog.get(product.getName());

            if (entity == null) {
                throw new ProductNotFoundException(product.getName());
            }
            checkUniqueProductName(product.getName());
            entity.setDescription(product.getDescription());
            entity.setPrice(product.getPrice());
            entity.setQuantityInStock(product.getQuantityInStock());
        }
    }

    public void reduceStock(String productName, int quantity) {
        synchronized (lock) {
            Product product = productCatalog.get(productName);
            if (product != null && product.getQuantityInStock() >= quantity) {
                product.setQuantityInStock(product.getQuantityInStock() - quantity);
            }
        }
    }

    public void deleteProduct(String productName) {
        synchronized (lock) {
            if (!productCatalog.containsKey(productName)) {
                throw new ProductNotFoundException(productName);
            }
            productCatalog.remove(productName);
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
