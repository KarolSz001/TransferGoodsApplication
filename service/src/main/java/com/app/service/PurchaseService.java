package com.app.service;

import com.app.enums.Category;
import com.app.exception.MyUncheckedException;
import com.app.model.Customer;
import com.app.model.Preference;
import com.app.model.Product;
import com.app.utility.DataManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PurchaseService {

    private final Integer firstCategory = 0;
    private final Integer secondCategory = 1;
    private final ProductService productService;
    private final CustomerService customerService;
    private final Map<Customer, List<Product>> customerProductsMap = new HashMap<>();

    public PurchaseService() {
        customerService = new CustomerService();
        productService = new ProductService();
    }

    public ProductService getProductService() {
        return productService;
    }


    public void purchaseGoodsByCustomer(Customer customer) {

        System.out.println("------------------------------------------------------------------------------------------");
        List<Category> customerPrefs = getCustomerPrefCategories(customer);
        System.out.println("\n WELCOME CUSTOMER ----->>>>" + customer.getName() + ":::::" + customer.getSurname() + " ::::: PREFERENCES -> " + customer.getPreferences() + " ----> " + customerPrefs + "::::::::" + customer.getCash() + " PLN");
        printProductsInStoreByCustomerPreferences(customerPrefs);
        DataManager.getLine(">>>>>>>>>> PRESS ENTER TO CONTINUE >>>>>>>>>>");
        Product selectedProduct = selectProductFromCategory(customerPrefs, firstCategory);
        System.out.println(selectedProduct);
        customerProductsMap.put(customer, new ArrayList<>());
        purchaseProduct(selectedProduct, customer);
        selectedProduct = selectProductFromCategory(customerPrefs, secondCategory);
        System.out.println(selectedProduct);
        purchaseProduct(selectedProduct, customer);
    }

    private void purchaseProduct(Product product, Customer customer) {
        List<Product> products = customerProductsMap.get(customer);
//        System.out.println(products);
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
        System.out.println("\n UPDATE CUSTOMER DATA ---->>>>" + customerProductsMap.entrySet().stream().filter(f -> f.getKey().equals(customer)).collect(Collectors.toList()));
    }

    private void printProductsInStoreByCustomerPreferences(List<Category> categories) {
        System.out.println("\n >>>>>>>>>> AVAILABLE PRODUCT FOR YOU PREFERENCES >>>>>>>>>> ");
        getProductFromCustomerPreferences(categories).forEach(System.out::println);
    }

    private Product selectProductFromCategory(List<Category> categories, int categoryNumber) {
        System.out.println("\n>>>>>>>>>>AUTO SELECTED PRODUCT BY NUMBER " + (categoryNumber + 1) + " CATEGORY -->>RATIO PRICE/QUANTITY <<<<--- " + categories.get(categoryNumber));
        Product result = getListFromCategory(categories.get(categoryNumber)).stream().max(Comparator.comparing(c -> c.getPrice().subtract(BigDecimal.valueOf(c.getQuantity())))).get();
        return result;
    }

    private BigDecimal calcRestOfMoney(BigDecimal productPrice, BigDecimal customerMoney) {
        return customerMoney.subtract(productPrice);
    }

    private void removeProductFromStore(Product product, int number) {
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


    private List<Product> getProductFromCustomerPreferences(List<Category> preferences) {
        List<Product> filteredListByPreference = new ArrayList<>();
        for (Category cat : preferences) {
            filteredListByPreference.addAll(getListFromCategory(cat));
        }
        return filteredListByPreference;
    }

    private List<Product> getListFromCategory(Category category) {
        return productService.findAll().stream().filter(f -> f.getCategory().equals(category)).collect(Collectors.toList());
    }

    private List<Category> getCustomerPrefCategories(Customer customer) {
        int prefNumbers = customer.getPreferences();
        List<Integer> prefList = getListOfIntegerFromNumber(prefNumbers);
        return prefList.stream().map(this::getCategoryFromInt).collect(Collectors.toList());
    }

    private List<Integer> getListOfIntegerFromNumber(int prefNumbers) {
        return Arrays.stream(Integer.toString(prefNumbers).split("")).map(Integer::valueOf).collect(Collectors.toList());
    }

    private Category getCategoryFromInt(Integer number) {
        List<Preference> preferenceList = customerService.getPreferences();
        return preferenceList.stream().filter(f -> f.getPriorityNumber() == number).findFirst().get().getCategory();
    }

    /**
     * Method finds Customer in collection how purchased the biggest number of Product
     * return Customer
     */

    public Customer getCustomerHowPurchasedMostProducts() {

        return customerProductsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Product::getQuantity).reduce(0, Integer::sum)
                ))
                .entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    /**
     * Method finds Customer in collection how purchased Products with Total price
     * return Customer
     */

    public Customer findCustomerHowPurchasedMostProductsWithTotalValue() {
        return customerProductsMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().map(m -> m.getPrice().multiply(BigDecimal.valueOf(m.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add)
        ))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    /**
     * Method return Map of products with numbers of selecting them
     * return Map<Product, Integer>
     */

    public Map<Product, Integer> getMapWithProductAndNumbersSelectingThem() {
        return customerProductsMap.entrySet().stream().flatMap(f -> f.getValue().stream()).collect(Collectors.groupingBy(g -> g))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size()
                ));
    }

    /**
     * Method return Product which was most often selected by Customer
     * return Product
     */
    public Product findProductWhichWasMostOftenSelected() {
        return customerProductsMap.entrySet().stream().flatMap(f -> f.getValue().stream()).collect(Collectors.groupingBy(g -> g))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size()
                )).entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    /**
     * Method return Product which was least often selected by Customer
     * return Product
     */

    public Product findProductWhichWasLeastOftenSelected() {
        return customerProductsMap.entrySet().stream().flatMap(f -> f.getValue().stream()).collect(Collectors.groupingBy(g -> g))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size()
                )).entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    /**
     * Method return Map with name of category and number of product with this category sorted by this number
     * return Map<Category, Integer>
     */

    public Map<Category, Integer> findMapWithCategoryAndNumberOfThem() {
        return customerProductsMap.entrySet().stream().flatMap(f -> f.getValue().stream()).map(Product::getCategory).collect(Collectors.groupingBy(g -> g))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size()
                ))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, HashMap::new));
    }
}
