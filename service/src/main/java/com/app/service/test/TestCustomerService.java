package com.app.service.test;



import com.app.exception.MyUncheckedException;
import com.app.model.Customer;

import com.app.service.CustomerService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)

public class TestCustomerService {

    private String jsonCustomerFile = "TestFileCustomer.json";
    private CustomerService customerService = new CustomerService(jsonCustomerFile);


    @Test
    @DisplayName("Test -> shouldThrowExceptionForWrongJsonCustomerFile")
    public void shouldThrowExceptionForWrongJsonCustomerFileName() {
        MyUncheckedException e = Assertions.assertThrows(MyUncheckedException.class, () -> new CustomerService("wrongFileName"));
        Assertions.assertEquals("FROM JSON - JSON FILENAME EXCEPTION", e.getMessage());
    }

    @Test
    @DisplayName("Test -> shouldReturnCorrectSizeOfCustomers")
    public void shouldReturnCorrectSizeOfCustomers() {
        Assertions.assertEquals(3, customerService.findAll().size());
    }

    @Test
    @DisplayName("Test -> shouldReturnWrongSizeOfCustomers")
    public void shouldReturnWrongSizeOfCustomers() {
        Assertions.assertNotEquals(1, customerService.findAll().size());
    }

    @Test
    @DisplayName("Test -> test correct data in ProductService ")
    public void testProductDataInService() {
        Customer customer = customerService.findAll().get(0);
        System.out.println(customer);
        Assertions.assertAll(
                () -> Assertions.assertEquals(customer.getName(), "ANNA"),
                () -> Assertions.assertEquals(customer.getAge(), 34),
                () -> Assertions.assertEquals(customer.getPreferences(), 12),
                () -> Assertions.assertEquals(customer.getCash(), new BigDecimal(8607))
        );
    }

    @Mock
    private CustomerService customerServiceMock;
    @Test
    @DisplayName("Test test mock ProductService")
    public void mockProductService() {

        customerServiceMock.findNumberOfCustomers();
        Mockito.verify(customerServiceMock).findNumberOfCustomers();
        Mockito.when(customerServiceMock.findNumberOfCustomers()).thenReturn(100);
        Assertions.assertEquals(100, customerServiceMock.findNumberOfCustomers());
    }
}
