package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ShoppingCartItemDto;
import org.example.dto.TotalPriceDto;
import org.example.entity.Product;
import org.example.entity.ShoppingCart;
import org.example.exception.OutOfStockException;
import org.example.exception.ProductNotFoundException;
import org.example.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCart shoppingCart;
    private final ProductService productService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional
    public void addProductToCart(String productId, int quantity) {
        Product product = productService.getProductById(productId);

        if (product.getQuantityInStock() < quantity) {
            throw new OutOfStockException(String.format("Product '%s' is out of stock or insufficient quantity available.", product.getName()));
        }

        shoppingCart.addProduct(productId, quantity);
        productService.reduceStock(productId, quantity);
    }

    public void removeProductFromCart(String productId) {
        if (!shoppingCart.getCartItems().containsKey(productId)) {
            throw new ProductNotFoundException(String.format("Product with ID %s is not in the shopping cart.", productId));
        }
        shoppingCart.removeProduct(productId);
    }

    public void updateProductQuantityInCart(String productId, int quantity) {

        Product product = productService.getProductById(productId);

        if (product == null) {
            throw new ProductNotFoundException((String.format("No product found with ID: %s.", productId)));
        }
        shoppingCart.updateProductQuantity(productId, quantity);
    }

    public List<ShoppingCartItemDto> getCartItems() {
        return shoppingCart.getCartItems()
                .entrySet()
                .stream()
                .map(entry -> {
                    String productId = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = productService.getProductById(productId);
                    return shoppingCartMapper.toDto(product, quantity);
                })
                .collect(Collectors.toList());
    }

    public TotalPriceDto getTotalPriceOfCart() {
        return shoppingCartMapper.mapTotalPriceDto(productService.getSumOfProductPrice());
    }
}
