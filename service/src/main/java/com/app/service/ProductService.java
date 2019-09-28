package com.app.service;

import com.app.converter.ProductToJsonConverter;
import com.app.enums.Category;
import com.app.exception.MyUncheckedException;
import com.app.model.Product;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final String jsonProductFile = "jsonProductFile.json";
    private List<Product> products;

    public ProductService() {
        this.products = loadProductsFromJsonFile(jsonProductFile);
    }

    private List<Product> loadProductsFromJsonFile(String fileName) {
        ProductToJsonConverter productToJsonConverter = new ProductToJsonConverter(fileName);
        return productToJsonConverter.fromJson().orElseThrow(() -> new MyUncheckedException("LOAD PRODUCTS ERROR"));
    }

    public void addNewProduct(Product product) {

    }

    public void deleteProductFromStoreByName(String productName) {
        products.removeIf(product -> product.getName().equals(productName));
    }

    public void updateProductByName(String name, int quantity) {

    }

    public void reduceQuantityOfProduct(Product product, int quantity) {
        product.setQuantity(product.getQuantity() - quantity);
//        deleteProductFromStoreByName(name);
        addNewProduct(product);
    }

    public Product getProductByName(String productName) {
        return products.stream().filter(f -> f.getName().equals(productName)).findFirst().orElseThrow(() -> new MyUncheckedException(" NO PRODUCT WITH THIS NAME "));
    }

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> getProductByNameWithCategory(String name, Category category) {
        return products.stream().filter(f -> f.getName().equals(name) && f.getCategory().equals(category)).findFirst();
    }


}
