package com.app.utility;

import com.app.converter.CutomerToJsonConverter;
import com.app.model.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DataCustomerGenerator {

    private final String fileName = "jsonCustomer.json";
    private final List<Customer> customers;

    public List<Customer> getCustomers() {
        return customers;
    }

    public DataCustomerGenerator() {
        customers = customersGenerator();
        saveCustomerToJsonFile();
    }

    private void saveCustomerToJsonFile() {
        CutomerToJsonConverter cutomerToJsonConverter = new CutomerToJsonConverter(fileName);
        cutomerToJsonConverter.toJson(customers);
    }

    public static Customer customerGenerator() {
        String name = nameCustomerGenerator();
        String surname = surnameGenerator();
        int age = ageCustomerGenerator();
        BigDecimal cash = cashGenerator();
        int preferences = preferencesGenerator();
        return Customer.builder().name(name).surname(surname).age(age).cash(cash).preferences(preferences).build();
    }

    private static int preferencesGenerator() {
        String[] preferences = {"12", "13", "23", "24"};
//        String[] preferences = {"12"};
        int size = preferences.length;
        return Integer.parseInt(preferences[new Random().nextInt(size)]);
    }

    private static String nameCustomerGenerator() {
        String[] customerNames = {"KAROL", "ANNA", "TOMASZ", "ZOFIA"};
        int size = customerNames.length;
        return customerNames[new Random().nextInt(size)];
    }

    private static String surnameGenerator() {
        String[] customerSurname = {"KOWAL", "ROGAL", "SUCHAR", "KONAR"};
        int size = customerSurname.length;
        return customerSurname[new Random().nextInt(size)];
    }

    private static int ageCustomerGenerator() {
        return new Random().nextInt(100 - 10) + 10;
    }

    private static BigDecimal cashGenerator() {
        return BigDecimal.valueOf(new Random().nextInt(10000 - 1000) + 1000);
    }

    public static List<Customer> customersGenerator() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            customers.add(customerGenerator());
        }
        return customers;
    }
}