package com.app.converter;

import com.app.model.Product;

import java.util.List;

public class ProductToJsonConverter extends JsonConverter<List<Product>> {
    public ProductToJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
