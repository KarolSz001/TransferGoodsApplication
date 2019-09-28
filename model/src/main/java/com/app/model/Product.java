package com.app.model;

import com.app.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product implements Cloneable {


    private String name;
    private int quantity;
    private BigDecimal price;
    private Category category;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
