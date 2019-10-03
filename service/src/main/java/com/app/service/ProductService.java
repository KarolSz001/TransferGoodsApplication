package com.app.service;

import com.app.converter.ProductToJsonConverter;
import com.app.exception.MyUncheckedException;
import com.app.model.Product;
import java.util.List;

public class ProductService {

    private final String jsonProductFile = "jsonProductFile.json";
    private final List<Product> products;
    private final ProductToJsonConverter productToJsonConverter = new ProductToJsonConverter(jsonProductFile);

    public ProductService() {
        products = loadProductsFromJsonFile();
    }

    public List<Product> loadProductsFromJsonFile() {
        return productToJsonConverter.fromJson().orElseThrow(() -> new MyUncheckedException("LOAD PRODUCTS ERROR"));
    }
    public void saveProductsToJsonFile(){
        productToJsonConverter.toJson(products);
    }

    public List<Product> findAll() {
        return products;
    }


}
