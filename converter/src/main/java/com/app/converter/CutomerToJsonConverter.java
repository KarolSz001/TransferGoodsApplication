package com.app.converter;

import com.app.model.Customer;

import java.util.List;

public class CutomerToJsonConverter extends JsonConverter<List<Customer>> {
    public CutomerToJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
