package com.app.converter;

import com.app.model.Customer;

import java.util.List;

public class CustomerToJsonConverter extends JsonConverter<List<Customer>> {
    public CustomerToJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
