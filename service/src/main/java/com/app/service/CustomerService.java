package com.app.service;

import com.app.converter.CutomerToJsonConverter;
import com.app.converter.PreferencesToJsonConverter;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;
import com.app.model.Preference;

import java.util.List;

public class CustomerService {

    private final List<Customer> customers;
    private final CutomerToJsonConverter cutomerToJsonConverter;

    public CustomerService(String fileName) {
        cutomerToJsonConverter = new CutomerToJsonConverter(fileName);
        customers = loadCustomersFromJsonFile();
    }

    public List<Customer> loadCustomersFromJsonFile() {
        return cutomerToJsonConverter.fromJson().orElseThrow(() -> new MyUncheckedException("LOAD CUSTOMER ERROR"));
    }

    public void saveCustomersToJsonFile() {
        cutomerToJsonConverter.toJson(customers);
    }

    public List<Customer> findAll() {
        return customers;
    }

    public int findNumberOfCustomers(){
        return customers.size();
    }

}
