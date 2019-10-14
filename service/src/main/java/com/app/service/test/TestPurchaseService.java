package com.app.service.test;

import com.app.converter.CustomerWithProductsJsonConverter;
import com.app.enums.Category;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;
import com.app.model.CustomerWithProducts;
import com.app.model.Product;
import com.app.service.ProductService;
import com.app.service.PurchaseService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TestPurchaseService {


    private final PurchaseService purchaseService = new PurchaseService();
    private final String testFile = "TestCustomerProductsListFile.json";
    private CustomerWithProductsJsonConverter customerWithProductsJsonConverter = new CustomerWithProductsJsonConverter(testFile);
    private List<CustomerWithProducts> customerProductList = customerWithProductsJsonConverter.fromJson().get();
    private Map<Customer, List<Product>> customers = purchaseService.convertListCustomerWithProductToMap(customerProductList);


    @Test
    @DisplayName("Test 1 -> shouldThrowExceptionForWrongJsonCustomerFile")
    public void shouldThrowExceptionForWrongJsonCustomerFileName() {
        MyUncheckedException e = Assertions.assertThrows(MyUncheckedException.class, () -> new ProductService("wrongFileName"));
        Assertions.assertEquals("FROM JSON - JSON FILENAME EXCEPTION", e.getMessage());
    }

    @Test
    @DisplayName("Test 2 -> shouldGetCorrectNumberOfRecords()")
    public void shouldGetCorrectNumberOfRecords() {

        int numbersOfRecords = customers.size();
        Assertions.assertEquals(2, numbersOfRecords);
    }

    @Test
    @DisplayName("Test 3 -> shouldGetWrongNumberOfRecords()")
    public void shouldGetWrongNumberOfRecords() {

        int numbersOfRecords = customers.size();
        Assertions.assertNotEquals(4, numbersOfRecords);
    }


    @Test
    @DisplayName("Test 4 -> findCorrectDataInFirstRecordKey()")
    public void shouldFindCorrectDataInFirstRecordKey() {
        String keyRecord = customers.entrySet().stream().findFirst().get().getKey().getName();
        Assertions.assertEquals("ANNA", keyRecord);
    }

    @Test
    @DisplayName("Test 5 -> findCorrectDataInFirstRecordValue()")
    public void shouldCorrectDataInFirstRecordValue() {
        int sizeOfList = customers.entrySet().stream().findFirst().get().getValue().size();
        Assertions.assertEquals(2, sizeOfList);
    }

    @Test
    @DisplayName("Test 6 -> test result of method whoPaidTheMost")
    public void testRestDataInFirstRecord() {
        String name = customers.entrySet().stream().findFirst().get().getKey().getName();
        int sizeOfValue = customers.entrySet().stream().findFirst().get().getValue().size();
        Product product = customers.entrySet().stream().findFirst().get().getValue().get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(name, "ANNA"),
                () -> Assertions.assertEquals(sizeOfValue, 2),
                () -> Assertions.assertEquals(product, new Product().builder().name("TV").quantity(1).price(new BigDecimal(54)).category(Category.AGD).build()),
                () -> Assertions.assertNotEquals(product, new Product().builder().name("RADIO").quantity(4).price(new BigDecimal(100)).category(Category.AGD).build())
        );
    }

    @Test
    @DisplayName("Test 7 -> test result of method findCustomerWithBiggestNumberProducts()")
    public void shouldFindCustomerWithBiggestNumberProducts() {
        purchaseService.setCustomerProductsMap(customers);
        Customer customer = purchaseService.findCustomerWithBiggestNumberProducts().getKey();
        System.out.println(customer);
        Assertions.assertEquals("TOMASZ", customer.getName());
    }

    @Test
    @DisplayName("Test 8 -> test result of method findCustomerWithBiggestTotalValuePrice()")
    public void shouldFindCustomerWithBiggestTotalValuePrice() {
        purchaseService.setCustomerProductsMap(customers);
        Customer customer = purchaseService.findCustomerWithBiggestTotalValuePrice().getKey();
        System.out.println(customer);
        Assertions.assertAll(
                () -> Assertions.assertEquals(customer.getCash(), new BigDecimal(1516)),
                () -> Assertions.assertEquals(customer.getPreferences(), 23),
                () -> Assertions.assertEquals(customer.getAge(), 15),
                () -> Assertions.assertNotEquals(customer.getSurname(), "OLA"),
                () -> Assertions.assertNotEquals(customer.getName(), "OLA")
        );
    }

    @Test
    @DisplayName("Test 9 -> test result of method getMapWithProductsAndNumbersSelectingThem()")
    public void shouldGetMapWithProductsAndNumbersSelectingThem() {
        purchaseService.setCustomerProductsMap(customers);
        String productName = purchaseService.getMapWithProductsAndNumbersSelectingThem().entrySet().stream().findFirst().get().getKey();
        System.out.println(productName);
        Assertions.assertEquals("TV", productName);
    }

    @Test
    @DisplayName("Test 10 -> test result of method findProductWhichWasMostOftenSelected()")
    public void shouldFindProductWhichWasMostOftenSelected() {
        purchaseService.setCustomerProductsMap(customers);
        String productName = purchaseService.findProductWhichWasMostOftenSelected();
        System.out.println(productName);
        Assertions.assertEquals("WATCH", productName);
    }

    @Test
    @DisplayName("Test 11 -> test result of method findProductWhichWasLeastOftenSelected()")
    public void shouldFindProductWhichWasLeastOftenSelected() {
        purchaseService.setCustomerProductsMap(customers);
        String productName = purchaseService.findProductWhichWasLeastOftenSelected();
        System.out.println(productName);
        Assertions.assertEquals("TV", productName);
    }

    @Test
    @DisplayName("Test 12 -> test result of method findMapWithCategoryAndNumberOfThem()")
    public void shouldFindMapWithCategoryAndNumberOfThem() {
        purchaseService.setCustomerProductsMap(customers);
        Map<Category, Integer> result = purchaseService.findMapWithCategoryAndNumberOfThem();
        result.forEach((k, v) -> System.out.println(k + ":::::" + v));
        Assertions.assertAll(
                () -> Assertions.assertEquals(result.isEmpty(), false),
                () -> Assertions.assertEquals(result.size(), 3),
                () -> Assertions.assertEquals(result.containsKey(Category.AGD), true),
                () -> Assertions.assertNotEquals(result.containsValue(21), true)
        );
    }

    @Test
    @DisplayName("Test 13 -> test result of method convertListCustomerWithProductToMap()")
    public void shouldThrowExceptionInConvertListCustomerWithProductToMap() {
        MyUncheckedException e = Assertions.assertThrows(MyUncheckedException.class, () -> purchaseService.convertListCustomerWithProductToMap(null));
        Assertions.assertEquals("Arg is null in purchaseGoodsByCustomer method ", e.getMessage());
    }


    @Mock
    private PurchaseService purchaseServiceMock;

    @Test
    @DisplayName("Test test1 mock purchaseService")
    public void purchaseServiceMock1() {
        purchaseServiceMock.findProductWhichWasMostOftenSelected();
        Mockito.verify(purchaseServiceMock).findProductWhichWasMostOftenSelected();
        Mockito.when(purchaseServiceMock.findProductWhichWasMostOftenSelected()).thenReturn("KAROL");
        Assertions.assertEquals("KAROL", purchaseServiceMock.findProductWhichWasMostOftenSelected());
    }

    @Mock
    private PurchaseService purchaseServiceMock2;

    @Test
    @DisplayName("Test test1 mock purchaseService")
    public void purchaseServiceMock2() {
        purchaseServiceMock2.findMapWithCategoryAndNumberOfThem();
        Mockito.verify(purchaseServiceMock2).findMapWithCategoryAndNumberOfThem();
        Mockito.when(purchaseServiceMock2.findMapWithCategoryAndNumberOfThem()).thenReturn((Map<Category, Integer>) new HashMap<>().put(Category.AGD, 22));
        Assertions.assertEquals(new HashMap<>().put(Category.AGD, 22), purchaseServiceMock2.findProductWhichWasMostOftenSelected());
    }

  /*  @Spy
    private Map<Customer, List<Product>> customersSpy ;

    @Test
    @DisplayName("Test  - spy PurchaseService  method ")
    void purchaseServiceSpy() {

        customersSpy.put(Customer.builder().name("KAROL").build(), List.of(Product.builder().build()));
        customersSpy.put(Customer.builder().name("RAFAL").build(), List.of(Product.builder().build()));
        System.out.println(customersSpy.size());
        Assertions.assertEquals(0, customersSpy.size());
        Mockito.doReturn(100).when(customersSpy.size());
        Assertions.assertEquals(100,customersSpy.size());

    }*/


}





















