package com.app.service;

import com.app.converter.CustomerAndProductsJsonConverter;
import com.app.converter.CustomerWithProductsJsonConverter;
import com.app.enums.Category;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;
import com.app.model.CustomerWithProducts;
import com.app.model.Preference;
import com.app.model.Product;
import com.app.utility.DataManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PurchaseService {

    private final Integer firstCategory = 0;
    private final Integer secondCategory = 1;
    private ProductService productService;
    private CustomerService customerService;
    private PreferenceService preferenceService;
    private Map<Customer, List<Product>> customerProductsMap = new HashMap<>();
    private int count = 0;
    private String customerAndProductsFile = "customerAndProductsFile.json";
    private String[] fileNames = {"jsonCustomer.json", "jsonProductFile.json", "jsonFilePreferences.json"};
    private CustomerAndProductsJsonConverter customerAndProductsJsonConverter = new CustomerAndProductsJsonConverter(customerAndProductsFile);
    /**
     * json format doesn't convert correctly key as object Customer, to fix this
     * I changed structure to Map<String,List<Product> from previous List<CustomerWithProducts></>
     */
    private String customerProductsListFile = "customerProductsListFile.json";
    private CustomerWithProductsJsonConverter customerProductsListConverter = new CustomerWithProductsJsonConverter(customerProductsListFile);


    public PurchaseService() {
        productService = new ProductService(fileNames[0]);
        customerService = new CustomerService(fileNames[1]);
        preferenceService = new PreferenceService(fileNames[2]);
        customerService.loadCustomersFromJsonFile();
        productService.loadProductsFromJsonFile();
    }


    public void setCustomerProductsMap(Map<Customer, List<Product>> customerProductsMap) {
        this.customerProductsMap = customerProductsMap;
    }

    public Map<Customer, List<Product>> getCustomerProductsMap() {
        return customerProductsMap;
    }

    public ProductService getProductService() {
        return productService;
    }


    private void saveDataToJsonFiles() {
        System.out.println("\n:::::::::::::::: SAVE FILES ::::::::::::::::::::::");
        saveProductsToJsonFile();
        saveCustomerDataToJsonFile();
        saveCustomerProductsRecords();
    }

    private List<CustomerWithProducts> convertCustomerProductsMapToList() {
        return customerProductsMap.entrySet().stream().map(this::convertFromEntrySetMap).collect(Collectors.toList());
    }

    public Map<Customer, List<Product>> convertListCustomerWithProductToMap(List<CustomerWithProducts> customerWithProductsList) {
        if (customerWithProductsList == null) {
            throw new MyUncheckedException("Arg is null in purchaseGoodsByCustomer method ");
        }
        return customerWithProductsList.stream().collect(Collectors.toMap(
                CustomerWithProducts::getCustomer,
                CustomerWithProducts::getProductList
        ));
    }

    private CustomerWithProducts convertFromEntrySetMap(Map.Entry<Customer, List<Product>> customerWithProduct) {
        if (customerWithProduct == null) {
            throw new MyUncheckedException("Arg is null in convertFromEntrySetMap method ");
        }
        return CustomerWithProducts.builder().customer(customerWithProduct.getKey()).productList(customerWithProduct.getValue()).build();
    }

    private void saveProductsToJsonFile() {
        productService.saveProductsToJsonFile();
    }

    private void saveCustomerDataToJsonFile() {
        customerService.saveCustomersToJsonFile();
    }

    private void saveCustomerProductsRecords() {
        customerAndProductsJsonConverter.toJson(customerProductsMap);
        List<CustomerWithProducts> customerWithProductsList = convertCustomerProductsMapToList();
        customerProductsListConverter.toJson(customerWithProductsList);
    }


    private void printAllUpData() {
        printAllProductsInStoreAfterOperation();
        printRecordsOfOperationCustomerWithProducts();
    }

    private void printAllProductsInStoreAfterOperation() {
        System.out.println("\nRecords of Products in Store After Operations");
        productService.findAll().forEach(System.out::println);
    }


    private void printRecordsOfOperationCustomerWithProducts() {
        System.out.println("\nRecords of Customers with Products after operation");
        customerProductsMap.forEach((k, v) -> System.out.println(k + "::::::::" + v));
    }

    /**
     * Main method for Purchase Operations where all miracles happens
     * contains -> methods : purchaseProduct(), selectProductFromCategory()
     */

    public void purchaseGoodsByCustomer(Customer customer) {

        if (customer == null) {
            throw new MyUncheckedException("Arg is null in purchaseGoodsByCustomer method ");
        }

        count++;
        System.out.println("------------------------------------------------------------------------------------------");
        List<Category> customerPrefs = getCustomerPrefCategories(customer);
        System.out.println("\n WELCOME CUSTOMER ----->>>>" + customer.getName() + ":::::" + customer.getSurname() + " ::::: PREFERENCES -> " + customer.getPreferences() + " ----> " + customerPrefs + "::::::::" + customer.getCash() + " PLN");
        printProductsInStoreByCustomerPreferences(customerPrefs);
        DataManager.getLine(">>>>>>>>>> PRESS ENTER TO CONTINUE >>>>>>>>>>");
        Product selectedProduct = selectProductFromCategory(customerPrefs, firstCategory);
        System.out.println(selectedProduct);
        customerProductsMap.put(customer, new ArrayList<>());
        try {
            purchaseProduct(selectedProduct, customer);
            selectedProduct = selectProductFromCategory(customerPrefs, secondCategory);
            System.out.println(selectedProduct);
            purchaseProduct(selectedProduct, customer);
        } catch (MyUncheckedException e) {
            e.getMessage();
            e.printStackTrace();
        }
        if (count == 2) {
            printAllUpData();
            saveDataToJsonFiles();
            count = 0;
        }
    }

    /**
     * Private method for main method where products are added to customer, removed from store and cash is calculated
     * contains -> methods : removeProductFromStore(product, quantityOfTheProduct);
     */

    private void purchaseProduct(Product product, Customer customer) throws MyUncheckedException  {
        if (product == null || customer == null) {
            throw new MyUncheckedException("Arg is null in purchaseProduct method ");
        }
        List<Product> products = customerProductsMap.get(customer);
        int quantityOfTheProduct = DataManager.getInt2("\n >>>>>>>>>> PRESS NUMBER OF PRODUCT YOU WANNA BUY <<<<<<<< ");
        BigDecimal totalPriceOfBuyingProduct = product.getPrice().multiply(new BigDecimal(quantityOfTheProduct));
        BigDecimal rest = calcRestOfMoney(totalPriceOfBuyingProduct, customer.getCash());
        System.out.println(" you wanna buy " + quantityOfTheProduct + " with Total price " + totalPriceOfBuyingProduct + " and you have " + customer.getCash() + " cash ");

        if (product.getQuantity() < quantityOfTheProduct) {
            System.out.println(" >>>>>>>>>> THERE IS NO ENOUGH QUANTITY OF THIS PRODUCT FOR THIS CATEGORY >>>>>>>>>> ");
            return;
        } else if (rest.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println(" >>>>>>>>>> YOU HAVE NO ENOUGH MONEY TO BUY THIS PRODUCT FOR THIS CATEGORY, WE DONE TODAY >>>>>>>>>> ");
            return;
        }
        try {
            Product copyProduct = (Product) product.clone();
            copyProduct.setQuantity(quantityOfTheProduct);
            products.add(copyProduct);
        } catch (CloneNotSupportedException e) {
            e.getMessage();
        }

        customerProductsMap.replace(customer, products);
        System.out.println(" Payment Accepted ");
        removeProductFromStore(product, quantityOfTheProduct);
        customerProductsMap.entrySet().stream().filter(f -> f.getKey().equals(customer)).findFirst().get().getKey().setCash(rest);
        customerService.findAll().stream().filter(f -> f.equals(customer)).findFirst().get().setCash(rest);// for save file with update
        System.out.println("\n UPDATE CUSTOMER DATA ---->>>>" + customerProductsMap.entrySet().stream().filter(f -> f.getKey().equals(customer)).collect(Collectors.toList()));
    }

    private void printProductsInStoreByCustomerPreferences(List<Category> categories) throws MyUncheckedException {
        if (categories == null) {
            throw new MyUncheckedException("Arg is null in printProductsInStoreByCustomerPreferences method");
        }
        System.out.println("\n >>>>>>>>>> AVAILABLE PRODUCT FOR YOU PREFERENCES >>>>>>>>>> ");
        getProductFromCustomerPreferences(categories).forEach(System.out::println);
    }

    private Product selectProductFromCategory(List<Category> categories, int categoryNumber) throws MyUncheckedException {
        if (categories == null) {
            throw new MyUncheckedException("Arg is null in printProductsInStoreByCustomerPreferences method");
        }
        if (categoryNumber < 0) {
            throw new MyUncheckedException("Arg is VALID in printProductsInStoreByCustomerPreferences method");
        }
        System.out.println("\n>>>>>>>>>>AUTO SELECTED PRODUCT BY NUMBER " + (categoryNumber + 1) + " CATEGORY -->>RATIO PRICE/QUANTITY <<<<--- " + categories.get(categoryNumber));
        return getListFromCategory(categories.get(categoryNumber)).stream().max(Comparator.comparing(c -> c.getPrice().subtract(BigDecimal.valueOf(c.getQuantity())))).get();
    }

    private BigDecimal calcRestOfMoney(BigDecimal productPrice, BigDecimal customerMoney) throws MyUncheckedException {
        if (productPrice == null || customerMoney == null) {
            throw new MyUncheckedException("Arg is null in calcRestOfMoney method");
        }
        return customerMoney.subtract(productPrice);
    }

    private void removeProductFromStore(Product product, int number) throws MyUncheckedException {
        if (product == null) {
            throw new MyUncheckedException("Arg is null in removeProductFromStore method");
        }
        if (number < 0) {
            throw new MyUncheckedException("Arg is VALID in removeProductFromStore method");
        }
        List<Product> productsInStore = productService.findAll();
        if (!productsInStore.contains(product)) {
            throw new MyUncheckedException("NO SUCH PRODUCT IN STORE");

        } else {
            int quantity = productService.findAll().stream().filter(f -> f.equals(product)).findFirst().get().getQuantity() - number;
            productService.findAll().stream().filter(f -> f.equals(product)).findFirst().get().setQuantity(quantity);
            System.out.println("");
            productService.findAll().forEach(System.out::println);
        }
    }

    private List<Product> getProductFromCustomerPreferences(List<Category> preferences) throws MyUncheckedException {
        if (preferences == null) {
            throw new MyUncheckedException("Arg is null in getProductFromCustomerPreferences method");
        }
        List<Product> filteredListByPreference = new ArrayList<>();
        for (Category cat : preferences) {
            filteredListByPreference.addAll(getListFromCategory(cat));
        }
        return filteredListByPreference;
    }

    private List<Product> getListFromCategory(Category category) throws MyUncheckedException {
        if (category == null) {
            throw new MyUncheckedException("Arg is null in getListFromCategory method");
        }
        return productService.findAll().stream().filter(f -> f.getCategory().equals(category)).collect(Collectors.toList());
    }

    private List<Category> getCustomerPrefCategories(Customer customer) throws MyUncheckedException {
        if (customer == null) {
            throw new MyUncheckedException("Arg is null in getCustomerPrefCategories method");
        }
        int prefNumbers = customer.getPreferences();
        List<Integer> prefList = getListOfIntegerFromNumber(prefNumbers);
        return prefList.stream().map(this::getCategoryFromInt).collect(Collectors.toList());
    }

    private List<Integer> getListOfIntegerFromNumber(int prefNumbers) throws MyUncheckedException {
        if (prefNumbers < 0) {
            throw new MyUncheckedException("Arg is VALID in getListOfIntegerFromNumber method");
        }
        return Arrays.stream(Integer.toString(prefNumbers).split("")).map(Integer::valueOf).collect(Collectors.toList());
    }

    private Category getCategoryFromInt(Integer number) {
        List<Preference> preferenceList = preferenceService.findAll();
        return preferenceList.stream().filter(f -> f.getPriorityNumber() == number).findFirst().get().getCategory();
    }

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * Method finds Customer in collection how purchased the biggest number of Product
     * return Customer
     */
    public Map.Entry<Customer, Integer> findCustomerWithBiggestNumberProducts() {
        return customerProductsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Product::getQuantity).reduce(0, Integer::sum)
                )).entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
    }

    /**
     * Method finds Customer in collection how purchased Products with Total price
     * return Customer
     */

    public Map.Entry<Customer, BigDecimal> findCustomerWithBiggestTotalValuePrice() {
        return customerProductsMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().map(m -> m.getPrice().multiply(BigDecimal.valueOf(m.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add)
        ))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue)).get();
    }

    /**
     * Method return Map of products name  and  numbers of them after operations
     * return Map<String, Long>
     */

    public Map<String, Integer> getMapWithProductsAndNumbersSelectingThem() {
        return customerProductsMap.entrySet().stream()
                .flatMap(f -> f.getValue().stream())
                .collect(Collectors.toMap(
                        Product::getName,
                        Product::getQuantity,
                        Integer::sum,
                        LinkedHashMap::new
                ));
    }

    /**
     * Method return Product which was most often selected by Customer
     * return Product
     */

    public String findProductWhichWasMostOftenSelected() {
        return customerProductsMap.entrySet().stream()
                .flatMap(f -> f.getValue().stream())
                .collect(Collectors.toMap(
                        Product::getName,
                        Product::getQuantity,
                        Integer::sum,
                        LinkedHashMap::new
                ))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
    }

    /**
     * Method return Product which was least often selected by Customer
     * return Product
     */

    public String findProductWhichWasLeastOftenSelected() {
        return customerProductsMap.entrySet().stream()
                .flatMap(f -> f.getValue().stream())
                .collect(Collectors.toMap(
                        Product::getName,
                        Product::getQuantity,
                        Integer::sum,
                        LinkedHashMap::new
                ))
                .entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
    }

    /**
     * Method return Map with name of category and number of product with this category sorted by this number
     * return Map<Category, Integer>
     */
    public Map<Category, Integer> findMapWithCategoryAndNumberOfThem() {
        return customerProductsMap.entrySet().stream()
                .flatMap(f -> f.getValue().stream())
                .collect(Collectors.groupingBy(Function.identity()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getCategory(),
                        e -> e.getValue().stream().filter(f -> f.getCategory().equals(e.getKey().getCategory())).map(Product::getQuantity).reduce(0, Integer::sum),
                        Integer::sum,
                        LinkedHashMap::new
                ));
    }


}
