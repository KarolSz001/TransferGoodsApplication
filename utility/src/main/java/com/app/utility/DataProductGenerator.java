package com.app.utility;

import com.app.converter.ProductToJsonConverter;
import com.app.enums.Category;
import com.app.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.app.enums.Category.*;

public class DataProductGenerator {

    private final String fileName = "jsonProductFile.json";
    private List<Product> products;


    public DataProductGenerator() {
        products = createStoreProducts();
        writeStoreToJsonFile(products);
    }
    public DataProductGenerator(String fileName){

    }

    public List<Product> getProducts() {
        return products;
    }

    private Product createProduct(String name, Category category) {
        String productName = name;
        BigDecimal price = priceGenerator();
        int quantity = quantityGenerator();
        return Product.builder().category(category).name(productName).price(price).quantity(quantity).build();
    }

    private BigDecimal priceGenerator() {
        return BigDecimal.valueOf(new Random().nextInt(500 - 100));
    }

    private int quantityGenerator() {
        return new Random().nextInt(100) + 1;
    }

    private List<Product> createStoreProducts() {
        List<Product> products = new ArrayList<>();
        //BOOKS
       /* String[] names = new String[]{"BATMAN"};
        products.addAll(Arrays.stream(names).map(name -> createProduct(name, BOOKS)).collect(Collectors.toList()));*/
        //AGD:
        String[] names = new String[]{"TV"};
        products.addAll(Arrays.stream(names).map(name -> createProduct(name, AGD)).collect(Collectors.toList()));

        /*//GROCERIES
        names = new String[]{"BANANAS"};
        products.addAll(Arrays.stream(names).map(name -> createProduct(name, GROCERIES)).collect(Collectors.toList()));*/
        //ELECTRONICS
        names = new String[]{"WATCH"};
        products.addAll(Arrays.stream(names).map(name -> createProduct(name, ELECTRONICS)).collect(Collectors.toList()));

        return products;
    }

    private void writeStoreToJsonFile(List<Product> productList) {
        ProductToJsonConverter productToJsonConverter = new ProductToJsonConverter(fileName);
        productToJsonConverter.toJson(productList);
    }


}

