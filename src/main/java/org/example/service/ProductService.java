package org.example.service;

import org.example.entity.Product;
import org.example.exception.DuplicateProductNameException;
import org.example.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Map<String, Product> PRODUCT_CATALOG = new HashMap<>();

    public void addProduct(Product product) {
        synchronized (PRODUCT_CATALOG) {
            checkUniqueProductName(product.getName());
            PRODUCT_CATALOG.put(product.getName(), product);
        }
    }

    private void checkUniqueProductName(String productName) {
        if (PRODUCT_CATALOG.values()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productName))) {
            throw new DuplicateProductNameException(productName);
        }
    }

    public Product getProductByName(String productName) {
        synchronized (PRODUCT_CATALOG) {
            Product product = PRODUCT_CATALOG.get(productName);
            if (product == null) {
                throw new ProductNotFoundException(productName);
            }
            return product;
        }
    }

    public Page<Product> getAllProducts(int page, int size) {
        List<Product> products = new ArrayList<>(PRODUCT_CATALOG.values());
        int total = products.size();
        int start = Math.min(page * size, total);
        int end = Math.min(start + size, total);
        List<Product> paginatedList = products.subList(start, end);
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), total);
    }

    public void updateProduct(Product product) {
        synchronized (PRODUCT_CATALOG) {
            Product entity = PRODUCT_CATALOG.get(product.getName());

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
        synchronized (PRODUCT_CATALOG) {
            Product product = PRODUCT_CATALOG.get(productName);
            if (product != null && product.getQuantityInStock() >= quantity) {
                product.setQuantityInStock(product.getQuantityInStock() - quantity);
            }
        }
    }

    public void deleteProduct(String productName) {
        synchronized (PRODUCT_CATALOG) {
            if (!PRODUCT_CATALOG.containsKey(productName)) {
                throw new ProductNotFoundException(productName);
            }
            PRODUCT_CATALOG.remove(productName);
        }
    }

    public List<Product> getAllByName(Collection<String> productNames) {
        return PRODUCT_CATALOG.values()
                .stream()
                .filter(product -> productNames.contains(product.getName()))
                .collect(Collectors.toList());
    }
}
