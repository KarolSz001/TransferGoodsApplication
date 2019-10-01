package com.app.model;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Customer {

    private String name;
    private String surname;
    private int age;
    private int preferences;
    @EqualsAndHashCode.Exclude
    private BigDecimal cash;

}
