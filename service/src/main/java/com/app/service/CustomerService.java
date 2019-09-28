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

    public CustomerService() {
        customers = loadCustomersFromJsonFile(jsonCustomerFile);
        preferences = loadPreferencesFromJsonFile(jsonPreferenceFile);
    }

    public List<Customer> loadCustomersFromJsonFile(String fileName){
        CutomerToJsonConverter cutomerToJsonConverter = new CutomerToJsonConverter(fileName);
        return cutomerToJsonConverter.fromJson().orElseThrow(()-> new MyUncheckedException("LOAD CUSTOMER ERROR"));
    }
    public List<Preference> loadPreferencesFromJsonFile(String filename){
        PreferencesToJsonConverter preferencesToJsonConverter = new PreferencesToJsonConverter(filename);
        return preferencesToJsonConverter.fromJson().orElseThrow(()-> new MyUncheckedException("LOAD PREFERENCES ERROR"));
    }

    public List<Customer> getAllCustomers(){
        return customers;
    }
    public List<Preference> getPreferences(){return preferences;}
}
