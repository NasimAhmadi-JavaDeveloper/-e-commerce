//package org.example.service;
//
//import org.example.entity.Product;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ProductServiceTest {
//
//    private ProductService productService;
//    private Product testProduct;
//
//    @BeforeEach
//    void setUp() {
//        productService = new ProductService();
//        testProduct = productService.addProduct("Test Product", "Test Description", 10.0, 100);
//    }
//
//    @Test
//    void addProduct_shouldAddProductToCatalog() {
//        //WHEN
//        Product product = productService.addProduct("New Product", "New Description", 20.0, 50);
//        //THEN
//        assertNotNull(product);
//        assertEquals("New Product", product.getName());
//        assertEquals(50, product.getQuantityInStock());
//    }
//
//    @Test
//    void getProductById_shouldReturnProduct_whenProductExists() {
//        //WHEN
//        Optional<Product> foundProduct = productService.getProductById(testProduct.getId());
//        //THEN
//        assertTrue(foundProduct.isPresent());
//        assertEquals(testProduct.getId(), foundProduct.get().getId());
//    }
//
//    @Test
//    void getProductById_shouldReturnEmpty_whenProductDoesNotExist() {
//        //WHEN
//        Optional<Product> foundProduct = productService.getProductById("non-existing-id");
//        //THEN
//        assertFalse(foundProduct.isPresent());
//    }
//
//    @Test
//    void getAllProducts_shouldReturnListOfProducts() {
//        //WHEN
//        productService.addProduct("Another Product", "Another Description", 15.0, 75);
//        List<Product> products = productService.getAllProducts();
//        //THEN
//        assertEquals(2, products.size());
//    }
//
//    @Test
//    void updateProduct_shouldUpdateProductDetails() {
//        //WHEN
//        Product updatedProduct = productService.updateProduct(testProduct.getId(), "Updated Product", "Updated Description", 12.0, 90);
//        //THEN
//        assertNotNull(updatedProduct);
//        assertEquals("Updated Product", updatedProduct.getName());
//    }
//
//    @Test
//    void updateProduct_shouldReturnNull_whenProductDoesNotExist() {
//        //WHEN
//        Product updatedProduct = productService.updateProduct("non-existing-id", "Updated Product", "Updated Description", 12.0, 90);
//        //THEN
//        assertNull(updatedProduct);
//    }
//
//    @Test
//    void deleteProduct_shouldRemoveProductFromCatalog() {
//        //WHEN
//        productService.deleteProduct(testProduct.getId());
//        Optional<Product> foundProduct = productService.getProductById(testProduct.getId());
//        //THEN
//        assertFalse(foundProduct.isPresent());
//    }
//
//    @Test
//    void deleteProduct_nonExistingId_shouldThrowException() {
//        //WHEN
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            productService.deleteProduct("non-existing-id");
//        });
//        //THEN
//        assertEquals("Product with ID non-existing-id does not exist.", exception.getMessage());
//    }
//
//    @Test
//    void reduceStock_shouldReduceProductStock() {
//        //WHEN
//        productService.reduceStock(testProduct.getId(), 10);
//        //THEN
//        assertEquals(90, testProduct.getQuantityInStock());
//    }
//
//    @Test
//    void reduceStock_shouldNotReduceWhenStockIsInsufficient() {
//        //WHEN
//        productService.reduceStock(testProduct.getId(), 150);
//        //THEN
//        assertEquals(100, testProduct.getQuantityInStock());
//    }
//}