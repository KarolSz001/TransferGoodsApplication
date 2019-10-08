package com.app.service.test;


import com.app.enums.Category;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;
import com.app.model.Product;
import com.app.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)

public class TestProductServices {

    String testProductFileName = "TestProductFile.json";
    String testProductWrongFileName = "test.json";
    ProductService productService = new ProductService(testProductFileName);






    @Test
    @DisplayName("Test -> shouldThrowExceptionForWrongJsonProductFileName")
    public void shouldThrowExceptionForWrongJsonProductFileName() {

        MyUncheckedException e = Assertions.assertThrows(MyUncheckedException.class, () -> new ProductService(testProductWrongFileName));
        Assertions.assertEquals("FROM JSON - JSON FILENAME EXCEPTION", e.getMessage());

    }

    @Test
    @DisplayName("Test -> shouldReturnCorrectSizeOfProducts")
    public void shouldReturnCorrectSizeOfProducts() {

        System.out.println(productService.findAll().size());
        Assertions.assertEquals(4, productService.findAll().size());

    }

    @Test
    @DisplayName("Test -> shouldReturnWrongSizeOfProducts")
    public void shouldReturnWrongSizeOfProducts() {

        System.out.println(productService.findAll().size());
        Assertions.assertNotEquals(1, productService.findAll().size());
    }

    @Test
    @DisplayName("Test -> test correct data in ProductService ")
    public void testProductDataInService() {
        Product product = productService.findAll().get(0);
        System.out.println(product);
        Assertions.assertAll(
                () -> Assertions.assertEquals(product.getName(), "BATMAN"),
                () -> Assertions.assertEquals(product.getCategory(), Category.BOOKS),
                () -> Assertions.assertEquals(product.getPrice(), new BigDecimal(128)),
                () -> Assertions.assertEquals(product.getQuantity(), 27)
        );
    }

    @Mock
    private ProductService productServiceMock;

    @Test
    @DisplayName("Test test mock ProductService")
    public void mockProductService() {

        productServiceMock.findNumbersOfProducts();
        Mockito.verify(productServiceMock).findNumbersOfProducts();
        Mockito.when(productServiceMock.findNumbersOfProducts()).thenReturn(100);
        Assertions.assertEquals(100, productServiceMock.findNumbersOfProducts());
    }


}
