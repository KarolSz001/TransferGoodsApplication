package com.app.converter;


import com.app.model.CustomerWithProducts;


import java.util.List;


public class CustomerWithProductsJsonConverter extends JsonConverter <List<CustomerWithProducts>> {
    public CustomerWithProductsJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
