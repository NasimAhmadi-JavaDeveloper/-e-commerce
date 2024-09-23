package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TotalPriceDto {
    private BigDecimal totalPrice;
}