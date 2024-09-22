package org.example.entity;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantityInStock;
}
