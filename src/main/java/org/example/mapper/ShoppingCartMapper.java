package org.example.mapper;

import org.example.dto.ShoppingCartItemDto;
import org.example.dto.TotalPriceDto;
import org.example.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productQuantity", source = "quantity")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", source = "product.price")
    ShoppingCartItemDto toDto(Product product, int quantity);
    @Mapping(target = "totalPrice", source = "totalPrice")
    TotalPriceDto mapTotalPriceDto(BigDecimal totalPrice);
}
