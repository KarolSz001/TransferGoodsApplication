package com.app.converter;

import com.app.model.Customer;
import com.app.model.Product;

import java.util.List;
import java.util.Map;

public class CustomerAndProductsJsonConverter extends JsonConverter <Map<Customer, List<Product>>> {
    public CustomerAndProductsJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
