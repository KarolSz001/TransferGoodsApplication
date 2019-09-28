package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Customer {

    private String name;
    private String surname;
    private int age;
    private BigDecimal cash;
    private int preferences;
}
