package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ShoppingCartDto;
import org.example.dto.ShoppingCartItemDto;
import org.example.dto.TotalPriceDto;
import org.example.entity.Product;
import org.example.entity.ShoppingCart;
import org.example.exception.OutOfStockException;
import org.example.exception.ProductNotFoundException;
import org.example.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCart shoppingCart;
    private final ProductService productService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional
    public void addProductToCart(ShoppingCartDto dto) {
        Product product = productService.getProductByName(dto.getProductName());
        checkQuantityProductInStock(dto.getQuantity(), product.getQuantityInStock(), dto.getProductName());
        shoppingCart.addProduct(dto.getProductName(), dto.getQuantity());
        productService.reduceStock(dto.getProductName(), dto.getQuantity());
    }

    private static void checkQuantityProductInStock(int quantity, int quantityInStock, String productName) {
        if (quantityInStock < quantity) {
            throw new OutOfStockException(productName);
        }
    }

    public void removeProductFromCart(String productName) {
        if (!shoppingCart.getCartItems().containsKey(productName)) {
            throw new ProductNotFoundException(productName);
        }
        shoppingCart.removeProduct(productName);
    }

    public void updateProductQuantityInCart(ShoppingCartDto dto) {
        Product product = productService.getProductByName(dto.getProductName());

        if (product == null) {
            throw new ProductNotFoundException(dto.getProductName());
        }

        checkQuantityProductInStock(dto.getQuantity(), product.getQuantityInStock(), product.getName());
        shoppingCart.updateProductQuantity(dto.getProductName(), dto.getQuantity());
        productService.reduceStock(dto.getProductName(), dto.getQuantity());
    }

    public List<ShoppingCartItemDto> getCartItems() {
        return shoppingCart.getCartItems()
                .entrySet()
                .stream()
                .map(entry -> {
                    String productName = entry.getKey();
                    int quantity = entry.getValue();
                    Product product = productService.getProductByName(productName);
                    return shoppingCartMapper.toDto(product, quantity);
                })
                .collect(Collectors.toList());
    }

    public TotalPriceDto getTotalPriceOfCart() {
        Set<String> productNames = shoppingCart.getCartItems().keySet();
        List<Product> productsInCart = productService.getAllByName(productNames);

        if (productsInCart.isEmpty()) {
            return shoppingCartMapper.mapTotalPriceDto(BigDecimal.ZERO.setScale(2, RoundingMode.UP));
        }

        double sum = productsInCart.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        BigDecimal roundedTotal = BigDecimal.valueOf(sum).setScale(2, RoundingMode.UP);
        return shoppingCartMapper.mapTotalPriceDto(roundedTotal);
    }


}
