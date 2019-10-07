package com.app.service;

import com.app.utility.DataCustomerGenerator;
import com.app.utility.DataManager;
import com.app.utility.DataPreferencesGenerator;
import com.app.utility.DataProductGenerator;

import java.util.stream.Collectors;


public class ControlService {

    private DataProductGenerator dataProductGenerator;
    private DataCustomerGenerator dataCustomerGenerator;
    private DataPreferencesGenerator dataPreferencesGenerator;
    private final PurchaseService purchaseService;
    private final CustomerService customerService;


    public ControlService() {
        initialisationData();
        customerService = new CustomerService();
        purchaseService = new PurchaseService();
    }

    public void controlRun() {
        printDataFromJsonFiles();
        purchaseOperation();
        findResults();
    }



    private void printDataFromJsonFiles() {
        System.out.println("\n>>>>>>>>>> CUSTOMERS DATA >>>>>>>>>>");
        customerService.getAllCustomers().forEach(System.out::println);
        System.out.println("\n>>>>>>>>>> PRODUCTS DATA >>>>>>>>>>");
        purchaseService.getProductService().findAll().forEach(System.out::println);
        System.out.println("\n>>>>>>>>>> PREFERENCES DATA >>>>>>>>>>");
        customerService.getPreferences().forEach(System.out::println);
    }

    private void findResults() {
        System.out.println("View customer data that purchased the most number of products");
        System.out.println(purchaseService.findCustomerWithBiggestNumberProducts().getKey() + "::::::::::: number of products -> " + purchaseService.findCustomerWithBiggestNumberProducts().getValue() );
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("View customer data that has purchased products with the biggest Total values");
        System.out.println(purchaseService.findCustomerWithBiggestTotalValuePrice().getKey() + "::::::::::: Total values -> " + purchaseService.findCustomerWithBiggestTotalValuePrice().getValue()) ;
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("View Map with Products Name and Numbers of Them which were selected by customer");
        purchaseService.getMapWithProductsAndNumbersSelectingThem().forEach((k, v) -> System.out.println(k + "::::::::" + v));
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("Print Product Name which was most often selected by customer");
        System.out.println(purchaseService.findProductWhichWasMostOftenSelected());
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("Print Product Name which was least often selected by customer");
        System.out.println(purchaseService.findProductWhichWasLeastOftenSelected());
        DataManager.getLine("\n PRESS ANY NUMBER TO CONTINUE");
        System.out.println("View Map with Category product and count them");
        purchaseService.findMapWithCategoryAndNumberOfThem().forEach((k, v) -> System.out.println(k + "::::::" + v));
    }

    private void purchaseOperation() {
        customerService.getAllCustomers().stream().peek(purchaseService::purchaseGoodsByCustomer).collect(Collectors.toList());
    }

    private void initialisationData() {
        System.out.println("\n >>>>>>>>>> DATA INITIALIZATION OPERATION >>>>>>>>>>");
        dataProductGenerator = new DataProductGenerator();
        dataCustomerGenerator = new DataCustomerGenerator();
        dataPreferencesGenerator = new DataPreferencesGenerator();
    }


}

