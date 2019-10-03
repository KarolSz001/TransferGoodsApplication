package com.app.service;

import com.app.converter.CutomerToJsonConverter;
import com.app.converter.PreferencesToJsonConverter;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;
import com.app.model.Preference;

import java.util.List;

public class CustomerService {

    private final List<Customer> customers;
    private final List<Preference> preferences;
    private final String jsonCustomerFile = "jsonCustomer.json";
    private final String jsonPreferenceFile = "jsonFilePreferences.json";
    private final CutomerToJsonConverter cutomerToJsonConverter = new CutomerToJsonConverter(jsonCustomerFile);
    private final PreferencesToJsonConverter preferencesToJsonConverter = new PreferencesToJsonConverter(jsonPreferenceFile);

    public CustomerService() {
        customers = loadCustomersFromJsonFile();
        preferences = loadPreferencesFromJsonFile();
    }

    public List<Customer> loadCustomersFromJsonFile(){
        return cutomerToJsonConverter.fromJson().orElseThrow(()-> new MyUncheckedException("LOAD CUSTOMER ERROR"));
    }
    public List<Preference> loadPreferencesFromJsonFile(){
        return preferencesToJsonConverter.fromJson().orElseThrow(()-> new MyUncheckedException("LOAD PREFERENCES ERROR"));
    }
    public void saveCustomersToJsonFile(){
        cutomerToJsonConverter.toJson(customers);
    }

    public List<Customer> getAllCustomers(){
        return customers;
    }

    public List<Preference> getPreferences(){return preferences;}
}
