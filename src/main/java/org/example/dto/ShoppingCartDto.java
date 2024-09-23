package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Setter
@Getter
public class ShoppingCartDto {
    @NotBlank(message = "Product name is required")
    private String productName;
    @Positive(message = "quantity must be greater than zero")
    private int quantity;
}