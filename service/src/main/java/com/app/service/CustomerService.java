package com.app.service;

import com.app.converter.CustomerToJsonConverter;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;

import java.util.List;

public class CustomerService {

    private final List<Customer> customers;
    private final CustomerToJsonConverter customerToJsonConverter;

    public CustomerService(String fileName) {
        customerToJsonConverter = new CustomerToJsonConverter(fileName);
        customers = loadCustomersFromJsonFile();
    }

    public List<Customer> loadCustomersFromJsonFile() {
        return customerToJsonConverter.fromJson().orElseThrow(() -> new MyUncheckedException("LOAD CUSTOMER ERROR"));
    }

    public void saveCustomersToJsonFile() {
        customerToJsonConverter.toJson(customers);
    }

    public List<Customer> findAll() {
        return customers;
    }

    public int findNumberOfCustomers(){
        return customers.size();
    }

}
