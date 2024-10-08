package org.example.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ShoppingCartItemDto {
    private int productQuantity;
    private String productName;
    private double productPrice;
}
