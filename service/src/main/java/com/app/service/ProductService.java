package com.app.service;

import com.app.converter.ProductToJsonConverter;
import com.app.exception.MyUncheckedException;
import com.app.model.Product;

import java.util.List;

public class ProductService {

    private String jsonProductFile;
    private List<Product> products;
    private ProductToJsonConverter productToJsonConverter;

    public ProductService(String fileName) {
        jsonProductFile = fileName;
        productToJsonConverter = new ProductToJsonConverter(jsonProductFile);
        products = loadProductsFromJsonFile();
    }

    public ProductService() {
    }

    public List<Product> loadProductsFromJsonFile() {
        return productToJsonConverter.fromJson().orElseThrow(() -> new MyUncheckedException("LOAD PRODUCTS ERROR"));
    }

    public void saveProductsToJsonFile() {
        productToJsonConverter.toJson(products);
    }

    public List<Product> findAll() {
        return products;
    }

    public int findNumbersOfProducts() {
        return products.size();
    }

    public boolean clearProducts(){
        return products.removeAll(products);
    }
}
