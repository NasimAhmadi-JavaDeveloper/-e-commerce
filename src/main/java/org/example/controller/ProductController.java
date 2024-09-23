package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public void addProduct(@RequestBody @Valid Product product) {
        productService.addProduct(product);
    }

    @GetMapping
    public Page<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(page, size);
    }

    @GetMapping("/{productName}")
    public Product getProduct(@PathVariable String productName) {
        return productService.getProductByName(productName);
    }

    @PutMapping
    public void updateProduct(@RequestBody @Valid Product product) {
        productService.updateProduct(product);
    }

    @DeleteMapping("/{productName}")
    public void deleteProduct(@PathVariable String productName) {
        productService.deleteProduct(productName);
    }
}
