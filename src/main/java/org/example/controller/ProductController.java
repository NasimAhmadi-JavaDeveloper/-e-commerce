package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityInStock());
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PutMapping
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getQuantityInStock());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
